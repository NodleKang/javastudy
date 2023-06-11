package test.a2021a;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import test.util.MessageQueue;
import test.util.MyString;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

public class MyServlet extends HttpServlet {

    private ConcurrentHashMap<String, MessageQueue> mqMap = new ConcurrentHashMap<String, MessageQueue>();

    @Override
    public void init() throws ServletException {
        // 서블릿 초기화 시 필요한 작업이 있으면 여기서 처리합니다.
        // 이 메소드는 서블릿이 최초로 실행될 때 한 번만 실행됩니다.
        // 서블릿이 실행되는 동안 필요한 작업이 없으면 이 메소드는 비워둡니다.
        System.out.println("init");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] paths = MyString.splitToStringArray(uri, "/", true);
        if (paths.length < 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String queueName = paths[1];
        JsonObject reqBodyAsJson = readBodyAsJson(req);
        JsonObject resBodyAsJson = null;
        if (uri.startsWith("/CREATE/")) {
            resBodyAsJson = handleCreate(queueName, reqBodyAsJson);
        } else if (uri.startsWith("/SEND")) {
            resBodyAsJson = handleSend(queueName, reqBodyAsJson);
        } else if (uri.startsWith("/RECEIVE")) {
            resBodyAsJson = handleReceive(queueName);
        } else if (uri.startsWith("/ACK")) {
            String messageId = paths[2];
            resBodyAsJson = handleAck(queueName, messageId);
        }else if (uri.startsWith("/FAIL")) {
            String messageId = paths[2];
            resBodyAsJson = handleFail(queueName, messageId);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (resBodyAsJson != null) {
            writeJson(resp, resBodyAsJson);
        }
    }

    private JsonObject handleCreate(String queueName, JsonObject reqBodyAsJson) {
        JsonObject resBodyAsJson = new JsonObject();

        int capacity = reqBodyAsJson.get("QueueSize").getAsInt();
        if (mqMap.containsKey(queueName)) {
            resBodyAsJson.addProperty("Result", "Queue Exist");
        } else {
            MessageQueue<String> mq = new MessageQueue(capacity);
            mqMap.put(queueName, mq);
            resBodyAsJson.addProperty("Result", "OK");
        }

        return resBodyAsJson;
    }

    private JsonObject handleSend(String queueName, JsonObject reqBodyAsJson) {
        JsonObject resBodyAsJson = new JsonObject();

        MessageQueue<String> mq = mqMap.get(queueName);
        if (mq == null) {
            resBodyAsJson.addProperty("Result", "Queue does not exist");
        } else if (mq.size() >= mq.getCapacity()) {
            resBodyAsJson.addProperty("Result", "Queue Full");
        } else {
            mq.addMessage(reqBodyAsJson.get("Message").getAsString());
            resBodyAsJson.addProperty("Result", "OK");
        }

        return resBodyAsJson;
    }

    private JsonObject handleReceive(String queueName) {
        JsonObject resBodyAsJson = new JsonObject();

        MessageQueue<String> mq = mqMap.get(queueName);
        if (mq == null) {
            resBodyAsJson.addProperty("Result", "No Message");
        } else if (mq.size() == 0) {
            resBodyAsJson.addProperty("Result", "No Message");
        } else {
            String message = mq.getMessage();
            resBodyAsJson.addProperty("Result", "OK");
            resBodyAsJson.addProperty("MessageId", mq.getMessageId(message));
            resBodyAsJson.addProperty("Message", message);
        }

        return resBodyAsJson;
    }

    private JsonObject handleAck(String queueName, String messageId) {
        JsonObject resBodyAsJson = new JsonObject();

        MessageQueue<String> mq = mqMap.get(queueName);
        if (mq != null) {
            mq.handleMessageSuccessById(messageId);
            resBodyAsJson.addProperty("Result", "OK");
        }

        return resBodyAsJson;
    }

    private JsonObject handleFail(String queueName, String messageId) {
        JsonObject resBodyAsJson = new JsonObject();

        MessageQueue<String> mq = mqMap.get(queueName);
        if (mq != null) {
            mq.handleMessageFailureById(messageId);
            resBodyAsJson.addProperty("Result", "OK");
        }

        return resBodyAsJson;
    }

    // 요청 본문을 JSON으로 읽어서 객체로 변환합니다.
    // 요청 본문이 JSON 형식이 아니거나 요청 본문이 없으면 null을 반환합니다.
    private JsonObject readBodyAsJson(HttpServletRequest req) {

        try {
            // HTTP 요청 본문을 읽기 위한 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            StringBuilder requestBody = new StringBuilder();
            String line;

            // BufferedReader를 사용하여 HTTP 요청 본문을 읽어옴
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            reader.close();

            // GSON을 사용하여 JSON을 객체로 변환
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(requestBody.toString(), JsonObject.class);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 객체를 JSON으로 변환해서 응답 본문에 씁니다.
    // 객체를 JSON으로 변환하는 데 실패하면 IOException이 발생합니다.
    private void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.setStatus(200);
        resp.setHeader("Content-Type", "application/json");
        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(obj));
    }

    @Override
    public void destroy() {
        // 서블릿 종료 시 필요한 작업이 있으면 여기서 처리합니다.
        // 이 메소드는 서블릿이 종료될 때 한 번만 실행됩니다.
        // 서블릿이 실행되는 동안 필요한 작업이 없으면 이 메소드는 비워둡니다.
        System.out.println("destroy");
    }

}
