package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.*;

public class RunManager {

    public static void main(String[] args) {

        // String 관련 유틸 사용
        // testStringUtil();

        // 파일 관련 유틸 사용
        // testFileUtil();

        // Json 관련 유틸 사용
        // testJsonUtil();
    }

    // String 관련 유틸 사용
    public static void testStringUtil() {
        // 문자열에 '#'이나 ':'과 같은 여러 구분 기호가 모두 포함되어 있는 경우
        // 문자열을 나누는 방법 샘플
        String input = "ghi:abc##def:jkl";
        String[] strArr = MyStringUtil.splitStringToStringArray(input, "[:#]", true);
        LinkedList<String> strList = MyStringUtil.splitStringToLinkedList(input, "[:#]", true);
        String str1 = MyStringUtil.joinString(strArr, ":");
        String str2 = MyStringUtil.joinString(strList, ":");
        System.out.println("input: " + input);
        System.out.println("strArr: " + Arrays.toString(strArr));
        System.out.println("strList: " + strList);
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        strArr = MyStringUtil.sortStringArray(strArr, true);
        strList = MyStringUtil.sortStringList(strList, true);
        System.out.println("strArr: " + Arrays.toString(strArr));
        System.out.println("strList: " + strList);
        String[] convArr = MyStringUtil.convertStringListToStringArray(strList);
        System.out.println("convArr: " + Arrays.toString(convArr));
        LinkedList<String> convList = MyStringUtil.convertStringArrayToLinkedList(strArr);
        System.out.println("convList: " + convList);
    }

    // 파일 관련 유틸 사용
    public static void testFileUtil() {

        // 특정 경로에서 시작 키워드와 종료 키워드에 맞는 파일의 절대 경로 목록을 찾아서 String 배열로 반환 (하위 디렉토리 포함 여부 선택 가능)
        String[] files = MyFileUtil.findFiles(".", "", "", true);

        // 파일 내용을 모두 읽어서 단일 String 객체로 반환
        // 파일을 못 찾으면 빈 문자열 반환
        String content = MyFileUtil.readFileContent("./FILES/TEST.TXT");

        // 파일 내용을 모두 읽어서 String 배열로 반환
        // 파일을 못 찾으면 빈 배열 반환
        String lines[] = MyFileUtil.readFileContentToArray("./javastudy.iml");

        // 디렉토리가 존재하는지 확인
        boolean checkDirectoryExists = MyFileUtil.checkDirectoryExists("./FILES");
        // 디렉토리 생성 (하위 디렉토리까지 생성할지 여부 선택 가능)
        // 없으면 생성하고 있으면 아무것도 안 함
        MyFileUtil.createDirectory("./FILES", true);
        // 파일 생성 (디렉토리까지 생성할지 여부 선택 가능)
        // 없으면 생성하고 있으면 아무것도 안 함
        MyFileUtil.createFile("./FILES/TEST.TXT");

        // 현재 시간을 문자열로 받기
        MyDateUtil myDateUtil = new MyDateUtil();
        String nowStr = myDateUtil.convertDateToString(myDateUtil.now(), "yyyy-MM-dd HH:mm:ss.SSS");

        // 파일에 쓰기 테스트
        MyFileUtil.writeToFile("./FILES/TEST.TXT", "overwrite:"+nowStr, "overwrite");
        MyFileUtil.writeToFile("./FILES/TEST.TXT", "append:"+nowStr, "append");
        MyFileUtil.writeToFile("./FILES/TEST.TXT", "prepend:"+nowStr, "prepend");

    }

    // JSON 관련 유틸 사용
    public static void testJsonUtil() {

        // JSON 문자열을 JsonObject 객체로 변환
        String jsonStr1 = "{\"name\":\"홍길동\",\"age\":20}";
        JsonObject jsonObj = MyJsonUtil.convertStringToJsonObject(jsonStr1);

        // JsonObject 객체를 JSON 문자열로 변환
        String jsonStr2 = MyJsonUtil.convertJsonObjectToString(jsonObj);

        // JSON 문자열을 JsonArray 객체로 변환
        String jsonStr3 = "[{\"name\":\"홍길동\",\"age\":20},{\"name\":\"홍길동\",\"age\":20}]";
        JsonArray jsonArray = MyJsonUtil.convertStringToJsonArray(jsonStr3);

        // JsonArray 객체를 JSON 문자열로 변환
        String jsonStr4 = MyJsonUtil.convertJsonArrayToString(jsonArray);

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

        SampleJsonConfig sampleJsonConfig = SampleJsonConfig.fromJson(json);

        LinkedList<SampleJsonConfig.ConfigItem> configItems = sampleJsonConfig.getSampleJsonConfig();
        for (SampleJsonConfig.ConfigItem item : configItems) {
            System.out.println("Hostname: " + item.getHostname());
            System.out.println("Port: " + item.getPort());
            System.out.println("Path: " + item.getPath());
            System.out.println("Forward Path: " + item.getForwardPath());
            System.out.println();
        }

        String convertedJson = SampleJsonConfig.toJson(sampleJsonConfig);
        System.out.println("Converted JSON:");
        System.out.println(convertedJson);
    }
}
