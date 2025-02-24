package org.example.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;
import org.example.adapter.LocalDateTimeAdapter;
import org.example.dto.UserDto;
import org.example.enums.StatusResponse;
import org.example.exception.ValidationException;
import org.example.utils.ConverterFromObjectToJson;

import java.time.LocalDateTime;

@Data
@Builder
public class StandardResponse {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    StatusResponse status;
    String message;
    JsonElement data;

     public StandardResponse(StatusResponse status, String message) {
        this.status = status;
        this.message = message;
    }
    public StandardResponse(StatusResponse status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    public StandardResponse(StatusResponse status, String message, JsonElement data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static String createStandardResponse (StatusResponse status, String message) {
        return gson.toJson(
                StandardResponse
                        .builder()
                        .status(status)
                        .message(message)
                        .build());
    }


    public static String createExceptionResponse (StatusResponse status, Exception exception) {
        return gson.toJson(
                StandardResponse
                        .builder()
                        .status(status)
                        .message(exception.getMessage())
                        .build());
    }


    public static String createUserSuccessResponse (StatusResponse status, String message,
                                             UserDto data ) {
        return gson.toJson(
                StandardResponse
                        .builder()
                        .status(status)
                        .message(message)
                        .data(ConverterFromObjectToJson.convert(data))
                        .build());
    }

    public static String createUserErrorResponse (StatusResponse status, String message,
                                                  ValidationException data ) {
        return gson.toJson(
                StandardResponse
                        .builder()
                        .status(status)
                        .message(message)
                        .data(gson.toJsonTree(data.getMessages()))
                        .build());
    }

    public static String createUserEntityResponse (StatusResponse status, String message,
                                                   JsonObject data ) {
        return gson.toJson(
                StandardResponse
                        .builder()
                        .status(status)
                        .message(message)
                        .data(gson.toJsonTree(data))
                        .build());
    }

}
