package test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MyJsonUtil {

    public JsonObject convertStringToJsonObject(String jsonStr) {
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(jsonStr, JsonObject.class);
        return jsonObj;
    }

    public String convertJsonObjectToString(JsonObject jsonObj) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(jsonObj);
        return jsonStr;
    }

    public JsonArray convertStringToJsonArray(String jsonStr) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonStr, JsonArray.class);
        return jsonArray;
    }

    public String convertJsonArrayToString(JsonArray jsonArray) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(jsonArray);
        return jsonStr;
    }

}
