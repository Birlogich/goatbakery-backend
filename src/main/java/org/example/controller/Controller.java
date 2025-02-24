package org.example.controller;
import com.google.gson.*;
import org.example.adapter.LocalDateTimeAdapter;
import org.example.dao.ItemDao;
import org.example.dao.UserDao;
import org.example.dto.*;
import org.example.entity.*;
import org.example.enums.StatusResponse;
import org.example.exception.ValidationException;
import org.example.filter.AuthFilter;
import org.example.service.CommentService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.example.utils.*;
import org.example.validator.CommentValidator;
import org.example.validator.OrderValidator;
import org.example.validator.UserValidator;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.net.HttpURLConnection.*;
import static org.example.entity.StandardResponse.createUserEntityResponse;
import static spark.Spark.*;



public class Controller {
    private final UserService userService = UserService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final CommentService commentService = CommentService.getInstance();
    private final ItemDao itemDao = ItemDao.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    protected static Logger log = LogbackWrapper.getLogger(ServiceMain.class);


    public Controller() {
        setupCORS();
        initializeRoutes();
    }

    private void setupCORS() {
        options("/*", (request, response) -> {
            String origin = request.headers("Origin");
            if (origin != null) {
                response.header("Access-Control-Allow-Origin", origin);
            }
            response.header("Access-Control-Allow-Credentials", "true");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.status(HTTP_OK);
            return "OK";
        });

        before((request, response) -> {
            if (!"OPTIONS".equalsIgnoreCase(request.requestMethod())) {
                String origin = request.headers("Origin");
                if (origin != null && response.raw().getHeader("Access-Control-Allow-Origin") == null) {
                    response.header("Access-Control-Allow-Origin", origin);
                }
                response.header("Access-Control-Allow-Credentials", "true");
                response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
                response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            }
        });
    }

    private void initializeRoutes() {
        AuthFilter authFilter = AuthFilter.getInstance();
        before("/users/*", authFilter.setFilter);
        before("/items/*", authFilter.setFilter);

        // Users
        post("/registration", this::processRegisterRequest);
        post("/login", this::processLoginRequest);
        get("/users/:id", this::processGetUserRequest);
        put("/users/:id", this::processEditUserRequest);
        delete("/users/:id", this::processDeleteUserRequest);

        // Orders
        post("/orders", this::processCreateOrderRequest);

        // Comments
        post("/items/:itemID/comments", this::processCreateCommentRequest);
        get("/comments", this::processGetItemCommentsRequest);

        after((req, res) -> res.type("application/json"));
    }


    public String processRegisterRequest (Request request, Response response) {
            String ret;
            response.type("application/json");
            try {
                UserDto user = new Gson().fromJson(request.body(), UserDto.class);
                UserValidator.validateUser(user);

                String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
                user.setPassword(hashedPassword);


                if(userDao.isPresent(user.getEmail())) {
                    ret = StandardResponse.createStandardResponse(StatusResponse.ERROR, "Use another email");
                    log.error("Email already in use: " + user.getEmail());
                } else {
                    userService.create(user);
                    response.status(HTTP_OK);
                    ret =  StandardResponse.createStandardResponse(StatusResponse.SUCCESS, "User created");
                    log.info("User with email: " + user.getEmail() + " created");
                }
            } catch (ValidationException ve) {
                response.status(HTTP_BAD_REQUEST);
                ret = StandardResponse.createUserErrorResponse(StatusResponse.ERROR, "Validation failed", ve);
                log.error("Validation error: " + ve.getMessage());
            } catch (Exception e) {
                response.status(HTTP_INTERNAL_ERROR);
                ret = StandardResponse.createExceptionResponse(StatusResponse.ERROR, e);
                log.error("Unexpected error: " + e.getMessage());
            }
            return ret;
        }

    public String processLoginRequest (Request request, Response response) {
        String ret;

        try{
            JsonObject jsonObject = gson.fromJson(request.body(), JsonObject.class);
            String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;
            String password = jsonObject.has("password") ? jsonObject.get("password").getAsString() : null;

            if (email == null || password == null) {
                response.status(HTTP_BAD_REQUEST);
                log.error("Email or Password was not provided");
                return StandardResponse.createStandardResponse(StatusResponse.ERROR, "Email or Password was not provided");
            }

            Optional<User> existingUser = userService.findByEmail(jsonObject.get("email").getAsString());

            if (existingUser.isPresent() && PasswordUtil.checkPassword(jsonObject.get("password").getAsString(), existingUser.get().getPassword())) {
                response.status(HTTP_OK);
                String token = JwtUtil.generateToken(String.valueOf(existingUser.get().getId()));

                //Set cookie
                response.cookie("token", token, 360000, true, false);
                ret = StandardResponse.createStandardResponse(StatusResponse.SUCCESS, token);

            } else {
                response.status(HTTP_UNAUTHORIZED);
                ret = StandardResponse.createStandardResponse(StatusResponse.ERROR, "The user is not present in the DB or password is incorrect");
                log.error("Unauthorized login attempt for email: {}", email);
            }
        }  catch (Exception e) {
            response.status(HTTP_INTERNAL_ERROR);
            ret = StandardResponse.createExceptionResponse(StatusResponse.ERROR, e);
            log.error("Unknown error happened: {}", e.getMessage());
        }
        return ret;
    }

    public String processGetUserRequest (Request request, Response response) {
        String ret;

        try {
            Optional<User> user = userService.getUserById(request.params(":id"));
            ret = user.map(value -> gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(value))))
                    .orElseGet(() -> {
                        response.status(HTTP_UNAUTHORIZED);
                        return StandardResponse.createStandardResponse(StatusResponse.ERROR, "The user is not present in the DB");
                    });
        }  catch (Exception e) {
            response.status(HTTP_INTERNAL_ERROR);
            ret = StandardResponse.createExceptionResponse(StatusResponse.ERROR, e);
            log.error("Unknown error happened: {}", e.getMessage());
        }

        return ret;
    }

    public String processDeleteUserRequest (Request request, Response response) {
        String ret;

        try {
            boolean isUserDeleted = userService.deleteUserById(request.params(":id"));
            if (isUserDeleted) {
                response.status(HTTP_OK);
                ret = gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
            } else {
                response.status(HTTP_UNAUTHORIZED);
                ret = StandardResponse.createStandardResponse(StatusResponse.ERROR, "The user is not present in the DB");
                log.error("The user with ID: " + request.params(":id") + " is not present in the DB" );
            }
        }  catch (Exception e) {
            response.status(HTTP_INTERNAL_ERROR);
            ret = StandardResponse.createExceptionResponse(StatusResponse.ERROR, e);
            log.error("Unknown error happened: {}", e.getMessage());
        }
        return ret;
    }

    public String processEditUserRequest (Request request, Response response) {
        String ret;

        try {
            int userId = Integer.parseInt(request.params(":id"));
            UserDto userToEdit = gson.fromJson(request.body(), UserDto.class);
            User editedUser = userService.editUser(userToEdit, userId);

            if(editedUser != null) {
                response.status(HTTP_OK);
                ret = gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(editedUser)));
            } else {
                response.status(HTTP_INTERNAL_ERROR);
                ret = StandardResponse.createStandardResponse(StatusResponse.ERROR, "User not found or error in edit");
                log.error("The user with ID: " + request.params(":id") + " is not present in the DB" );
            }

        } catch (Exception e) {
            response.status(HTTP_INTERNAL_ERROR);
            ret = StandardResponse.createExceptionResponse(StatusResponse.ERROR, e);
            log.error("Unknown error happened: {}", e.getMessage());
        }

        return ret;
    }

    //ORDERS

    private String processCreateOrderRequest(Request request, Response response) {
        String ret;

        try {
            String userIdCookie = request.cookie("userId");
            if (userIdCookie == null || !userIdCookie.matches("\\d+")) {
                response.status(HTTP_BAD_REQUEST);
                return StandardResponse.createStandardResponse(StatusResponse.ERROR, "Invalid userId");
            }

            OrderDto orderDto = gson.fromJson(request.body(), OrderDto.class);
            OrderValidator.validateOrder(orderDto);

            Integer totalSum = orderService.createOrder(orderDto);

            if (totalSum == null || totalSum < 0) {
                response.status(HTTP_INTERNAL_ERROR);
                return StandardResponse.createStandardResponse(StatusResponse.ERROR, "Invalid totalSum");
            }

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("totalSum", totalSum);
            ret = createUserEntityResponse(StatusResponse.SUCCESS, "Order Created", itemJson);

        } catch (ValidationException ve) {
            response.status(HTTP_BAD_REQUEST);
            ret = StandardResponse.createUserErrorResponse(StatusResponse.ERROR, "Validation failed", ve);
            log.error("Validation error: " + ve.getMessage());
        } catch (Exception e) {
            response.status(HTTP_INTERNAL_ERROR);
            ret = StandardResponse.createExceptionResponse(StatusResponse.ERROR, e);
            log.error("Unknown error happened: {}", e.getMessage());
        }

        return ret;
    }

    //  COMMENTS

    private String processCreateCommentRequest(Request request, Response response) {
        String ret = "";
        String userId = null;
        try {
            String token = request.cookie("token");
            if (token == null) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "You are not authorized"));
            } else {
                userId = JwtUtil.getJWTUserId(token);
            }
            String itemID = request.params(":itemId");
            JsonObject jsonObject = gson.fromJson(request.body(), JsonObject.class);
            String comment = jsonObject.get("comment").getAsString();

            if (userId == null) {
                return gson.toJson(new StandardResponse(StatusResponse.ERROR, "Invalid token"));
            }

            String finalUserId = userId;
            BasicUserDto user = userDao.findBasicById(userId).orElseThrow(() -> new RuntimeException("User with ID " + finalUserId + " not Found"));
            Item item = itemDao.findById(itemID).orElseThrow(() -> new RuntimeException("Item with ID " + itemID + " not Found"));

            CommentDto createCommentDto = new CommentDto(user, item, comment);

            log.info(String.valueOf(createCommentDto));

            CommentValidator.validateOrder(createCommentDto);

            commentService.createComment(createCommentDto);

            response.status(HTTP_OK);
            ret = gson.toJson(new StandardResponse(StatusResponse.SUCCESS, "comment added"));


        } catch (ValidationException ve) {
            response.status(HTTP_BAD_REQUEST);
            ret = StandardResponse.createUserErrorResponse(StatusResponse.ERROR, "Validation failed", ve);
            log.error("Validation error: " + ve.getMessage());
        } catch (Exception e) {
            response.status(HTTP_INTERNAL_ERROR);
            ret = gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
            log.error("An error occurred {}", e.getMessage());
        }

        return ret;
    }

    private String processGetItemCommentsRequest(Request request, Response response) {
        String ret = "";

        try {
           String comments = commentService.getAllComments();
           ret = gson.toJson(new StandardResponse(StatusResponse.SUCCESS, gson.toJsonTree(comments)));
        }catch (Exception e) {
            response.status(HTTP_INTERNAL_ERROR);
            ret = gson.toJson(new StandardResponse(StatusResponse.ERROR, e.getMessage()));
            log.error("An error occurred {}", e.getMessage());
        }

        return ret;
    }


}
