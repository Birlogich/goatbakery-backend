package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConverterFromObjectToJson {

    private final Gson gson = new Gson();

    public static JsonObject convert (Object object) {
        String jsonString = gson.toJson(object);
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

}
