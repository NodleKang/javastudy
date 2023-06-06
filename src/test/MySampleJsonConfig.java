package test;

import java.util.LinkedList;
import com.google.gson.Gson;

// JSON 데이터를 담을 수 있는 Java 클래스의 예시
// JSON 데이터를 담을 수 있는 Java 클래스는 필드명과 JSON 데이터의 키가 일치해야 한다.
// 필드명과 JSON 데이터의 키가 일치하지 않아도 에러가 발생하지 않으며, null로 채워진다.
// 필드명과 JSON 데이터의 키가 일치하지 않는 경우에는 @SerializedName 어노테이션을 사용하여 매핑할 수 있다.

// JSON 데이터 예시
// {
//    "SampleJsonConfig": [
//        {
//            "host": "127.0.0.1",
//            "port": 8090,
//            "path": "/auth"
//        },
//        {
//            "host": "localhost",
//            "port": 5001,
//            "path": "/service",
//            "forwardPath": "/newService"
//        }
//    ]
//}
public class MySampleJsonConfig {

    private LinkedList<ConfigItem> SampleJsonConfig;

    public LinkedList<ConfigItem> getSampleJsonConfig() {
        return SampleJsonConfig;
    }

    public void setSampleJsonConfig(LinkedList<ConfigItem> sampleJsonConfig) {
        SampleJsonConfig = sampleJsonConfig;
    }

    public static MySampleJsonConfig fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MySampleJsonConfig.class);
    }

    public static String toJson(MySampleJsonConfig config) {
        Gson gson = new Gson();
        return gson.toJson(config);
    }

    public static class ConfigItem {
        private String hostname;
        private int port;
        private String path;
        private String forwardPath;

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getForwardPath() {
            return forwardPath;
        }

        public void setForwardPath(String forwardPath) {
            this.forwardPath = forwardPath;
        }
    }
}
