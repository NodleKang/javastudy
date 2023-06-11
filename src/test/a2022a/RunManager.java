package test.a2022a;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import test.util.MyFile;
import test.util.MyJson;
import test.util.MyString;

import java.util.HashMap;
import java.util.Scanner;

public class RunManager {

    public static void main(String[] args) {

        //testOnConsole();

        if (args.length > 0) {
            String path = "C:/sp_workspace/javastudy/src/test/a2022a/";
            String proxyFilePath = path + args[0];
            if (!proxyFilePath.endsWith(".json")) {
                proxyFilePath = proxyFilePath + ".json";
            }
            String content = MyFile.readFileContent(proxyFilePath);
            JsonObject jsonRouteRule = MyJson.convertStringToJsonObject(content);
            testOnHttp(jsonRouteRule);
        }
    }

    // 콘솔 입력 기반으로 테스트
    public static void testOnConsole() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // read line from console util user input "exit"
            String line = scanner.nextLine();
            if (line.equals("exit")) {
                break;
            }
            String[] commands = MyString.splitToStringArray(line, " ");
            String path = "C:/sp_workspace/javastudy/src/test/a2022a/";
            String proxyFileName = commands[0] + ".txt";
            String[] routeRules = MyFile.readFileContentToArray(path + proxyFileName);
            for (String routeRule: routeRules) {
                String[] routeArr = MyString.splitToStringArray(routeRule, "#", true);
                if (routeArr[0].equals(commands[1])) {
                    if (routeArr[1].startsWith("Proxy")) {
                        String[] routeRules2 = MyFile.readFileContentToArray(path + routeArr[1]);
                        for (String routeRule2 : routeRules2) {
                            String[] routeArr2 = MyString.splitToStringArray(routeRule2, "#", true);
                            if (routeArr2[0].equals(commands[1])) {
                                String targetPath = path + routeArr2[1];
                                System.out.println(targetPath);
                                String content = MyFile.readFileContent(targetPath);
                                System.out.println(content);
                            }
                        }
                    } else {
                        String targetPath = path + proxyFileName.trim();
                        System.out.println(targetPath);
                        String content = MyFile.readFileContent(targetPath);
                        System.out.println(content);
                    }
                }
            }
        }

    }

    //
    public static void testOnHttp(JsonObject jsonRouteRule) {

        int port = jsonRouteRule.get("port").getAsInt();
        HashMap<String, String> proxyMap = new HashMap<>();
        JsonArray jsonRouteArray = jsonRouteRule.get("routes").getAsJsonArray();
        for (int i=0; i < jsonRouteArray.size(); i++) {
            JsonObject jsonRule = jsonRouteArray.get(i).getAsJsonObject();
            proxyMap.put(jsonRule.get("pathPrefix").getAsString(), jsonRule.get("url").getAsString());
        }
        MyServer server = MyServer.getInstance(port, proxyMap);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
