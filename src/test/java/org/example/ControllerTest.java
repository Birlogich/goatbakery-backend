package org.example;

import org.example.controller.Controller;
import org.example.dao.UserDao;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spark.Request;
import spark.Response;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class ControllerTest {


    @Mock
    private UserService userService;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private Controller controller;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

/*    @Test
    public void testProcessRegisterRequest_success() {
        // Mock request data
        String requestBody = "{\"firstName\": \"Kseniia\",\"lastName\": \"Zhigaleva\",\"address\": \"123 Main St\",\"email\": \"kseniiazigaleva@gmail.com\",\"password\": \"1234\",\"phoneNumber\": \"+14847617991\"}";

        when(request.body()).thenReturn(requestBody);

        // Mock successful service call
        when(userDao.isPresent(anyString())).thenReturn(false);
        doNothing().when(userService).create(any(CreateUserDto.class));

        // Call the method
        String result = controller.processRegisterRequest(request, response);

        // Assert response
        verify(response).status(HTTP_OK);
        assertTrue(result.contains("\"status\":\"SUCCESS\""));
    }

    @Test
    public void testProcessRegisterRequest_duplicateEmail() {
        // Mock request data
        String requestBody = "{\"firstName\": \"Kseniia\",\"lastName\": \"Zhigaleva\",\"address\": \"123 Main St\",\"email\": \"kseniiazigaleva@gmail.com\",\"password\": \"1234\",\"phoneNumber\": \"+123456789\"}";
        when(request.body()).thenReturn(requestBody);

        // Mock duplicate email check
        when(userDao.isPresent(anyString())).thenReturn(true);

        // Call the method
        String result = controller.processRegisterRequest(request, response);

        // Assert response
        verify(response).status(HTTP_OK);
        assertTrue(result.contains("\"status\":\"ERROR\""));
        assertTrue(result.contains("Use another email"));
    }

    @Test
    public void testProcessRegisterRequest_validationError() {
        // Mock invalid request data
        String requestBody = "{\"firstName\": \"Kseniia\",\"lastName\": \"Zhigaleva\",\"address\": \"123 Main St\",\"email\": \"kseniiazigaleva@gmail.com\",\"password\": \"1234\",\"phoneNumber\": \"+123456789\"}";
        when(request.body()).thenReturn(requestBody);

        // Call the method
        String result = controller.processRegisterRequest(request, response);

        // Assert validation error
        verify(response).status(HTTP_BAD_REQUEST);
        assertTrue(result.contains("\"status\":\"ERROR\""));
    }*/

}
