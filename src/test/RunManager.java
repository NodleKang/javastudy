package test;

import com.google.gson.*;
import java.util.*;
import test.http.*;
import test.util.MyString;

public class RunManager {

    public static void main(String[] args) {

        try {
            testOnHttp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testOnHttp() throws Exception {

        // Http Client
        MyClient client = new MyClient(5000, 5000);
        String value = client.get("http://127.0.0.1:8080/");
        Gson gson = new Gson();
        JsonObject jsonObjectResp = gson.fromJson(value, JsonObject.class);

        // Http Server
        int port = 8080;
        MyServer server = MyServer.getInstance(port);
        while (true) {
            Thread.sleep(1000);
        }

    }

    public static void testOnConsole() {

        Scanner sc = new Scanner(System.in);

        while (true) {
            String line = sc.nextLine();
            if (line.equals("exit")) {
                break;
            } else {
                String[] commands = MyString.splitToStringArray(line, " ", true);
            }
        }
    }

}
