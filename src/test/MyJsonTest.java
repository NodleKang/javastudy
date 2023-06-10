package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import test.httpbackup.MySampleJsonConfig;
import test.util.MyJson;

import java.util.LinkedList;

public class MyJsonTest {

    public static void main(String[] args) {

        // JSON 문자열을 JsonObject 객체로 변환
        String jsonStr1 = "{\"name\":\"홍길동\",\"age\":20}";
        JsonObject jsonObj = MyJson.convertStringToJsonObject(jsonStr1);

        // JsonObject 객체를 JSON 문자열로 변환
        String jsonStr2 = MyJson.convertJsonObjectToString(jsonObj);

        // JSON 문자열을 JsonArray 객체로 변환
        String jsonStr3 = "[{\"name\":\"홍길동\",\"age\":20},{\"name\":\"홍길동\",\"age\":20}]";
        JsonArray jsonArray = MyJson.convertStringToJsonArray(jsonStr3);

        // JsonArray 객체를 JSON 문자열로 변환
        String jsonStr4 = MyJson.convertJsonArrayToString(jsonArray);

        String json = "{\n" +
                "  \"SampleJsonConfig\": [\n" +
                "    {\n" +
                "      \"hostname\": \"127.0.0.1\",\n" +
                "      \"port\": 8090,\n" +
                "      \"path\": \"/auth\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"hostname\": \"localhost\",\n" +
                "      \"port\": 5001,\n" +
                "      \"path\": \"/service\",\n" +
                "      \"forwardPath\": \"/newService\"\n" +
                "    }\n" +
                "    ]\n" +
                "}";

        MySampleJsonConfig mySampleJsonConfig = MySampleJsonConfig.fromJson(json);

        LinkedList<MySampleJsonConfig.ConfigItem> configItems = mySampleJsonConfig.getSampleJsonConfig();
        for (MySampleJsonConfig.ConfigItem item : configItems) {
            System.out.println("Hostname: " + item.getHostname());
            System.out.println("Port: " + item.getPort());
            System.out.println("Path: " + item.getPath());
            System.out.println("Forward Path: " + item.getForwardPath());
            System.out.println();
        }

        String convertedJson = MySampleJsonConfig.toJson(mySampleJsonConfig);
        System.out.println("Converted JSON:");
        System.out.println(convertedJson);
    }
}
