package org.example.filter;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.controller.ServiceMain;
import org.example.entity.StandardResponse;
import org.example.enums.StatusResponse;
import org.example.utils.JwtUtil;
import org.example.utils.LogbackWrapper;
import org.slf4j.Logger;
import spark.Filter;

import static java.net.HttpURLConnection.HTTP_OK;
import static spark.Spark.halt;

public class AuthFilter {
    private static final AuthFilter INSTANCE = new AuthFilter();
    protected static Logger log = LogbackWrapper.getLogger(ServiceMain.class);

    public Filter setFilter = (request, response) -> {

        if ("OPTIONS".equals(request.requestMethod())) {
            response.status(HTTP_OK);
            return;
        }

        response.type("application/json");
        String ret;
        String authorizationHeader = request.headers("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.status(401);
            ret = StandardResponse.createStandardResponse(StatusResponse.ERROR, "Bearer token is invalid");
            response.body(ret);
            halt(401, ret);
        }

        String token = authorizationHeader.substring("Bearer ".length());

        try {
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            String userId = decodedJWT.getSubject();
            log.info(userId);
            response.status(200);
            ret = StandardResponse.createStandardResponse(StatusResponse.SUCCESS, "Welcome");
            response.body(ret);
        } catch (Exception e) {
            response.status(401);
            ret = StandardResponse.createExceptionResponse(StatusResponse.ERROR, e);
            response.body(ret);
            halt(401, ret);
        }
    };

    public static AuthFilter getInstance() {
        return INSTANCE;
    }
}
