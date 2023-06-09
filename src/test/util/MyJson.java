package test.util;

import com.google.gson.*;

import java.util.LinkedList;

public class MyJson {

    public static JsonObject convertStringToJsonObject(String jsonStr) {
        Gson gson = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation() // @Expose 어노테이션이 붙어있지 않은 필드는 JSON을 변환 안 함
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // 날짜 타입을 json으로 담을 때의 형식
                .setPrettyPrinting() // gson 데이터를 보기 좋게 출력
                .create();
        JsonObject jsonObj = gson.fromJson(jsonStr, JsonObject.class);
        return jsonObj;
    }

    public static String convertJsonObjectToString(JsonObject jsonObj) {
        Gson gson = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation() // @Expose 어노테이션이 붙어있지 않은 필드는 JSON을 변환 안 함
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // 날짜 타입을 json으로 담을 때의 형식
                .setPrettyPrinting() // gson 데이터를 보기 좋게 출력
                .create();
        String jsonStr = gson.toJson(jsonObj);
        return jsonStr;
    }

    public static JsonArray convertStringToJsonArray(String jsonStr) {
        Gson gson = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation() // @Expose 어노테이션이 붙어있지 않은 필드는 JSON을 변환 안 함
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // 날짜 타입을 json으로 담을 때의 형식
                .setPrettyPrinting() // gson 데이터를 보기 좋게 출력
                .create();
        JsonArray jsonArray = gson.fromJson(jsonStr, JsonArray.class);
        return jsonArray;
    }

    public static String convertJsonArrayToString(JsonArray jsonArray) {
        Gson gson = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation() // @Expose 어노테이션이 붙어있지 않은 필드는 JSON을 변환 안 함
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // 날짜 타입을 json으로 담을 때의 형식
                .setPrettyPrinting() // gson 데이터를 보기 좋게 출력
                .create();
        String jsonStr = gson.toJson(jsonArray);
        return jsonStr;
    }

    public static LinkedList<String> convertJsonArrayToStringList(JsonArray jsonArray) {
        LinkedList<String> list = new LinkedList<String>();
        for (JsonElement element: jsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }

}
