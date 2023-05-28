package test;

public class RunManager {

    public static void main(String[] args) {

        /////////////////////////////////
        // 파일 관련 유틸 사용법 시작
        /////////////////////////////////
        MyFileUtil myFileUtil = new MyFileUtil();

        // 특정 경로에서 시작 키워드와 종료 키워드에 맞는 파일의 절대 경로 목록을 찾아서 String 배열로 반환 (하위 디렉토리 포함 여부 선택 가능)
        String[] files = myFileUtil.findFiles(".", "", "", true);

        // 파일 내용을 모두 읽어서 단일 String 객체로 반환
        // 파일을 못 찾으면 빈 문자열 반환
        String content = myFileUtil.readFileContent("./FILES/TEST.TXT");

        // 파일 내용을 모두 읽어서 String 배열로 반환
        // 파일을 못 찾으면 빈 배열 반환
        String lines[] = myFileUtil.readFileContentToArray("./javastudy.iml");

        // 현재 시간을 문자열로 받기
        String nowStr = "";
        MyDateUtil myDateUtil = new MyDateUtil();
        nowStr = myDateUtil.convertDateToString(myDateUtil.now(), "yyyy-MM-dd HH:mm:ss.SSS");

        boolean checkDirectoryExists = myFileUtil.checkDirectoryExists("./FILES");
        myFileUtil.createDirectory("./FILES", true);
        myFileUtil.createFile("./FILES/TEST.TXT");
        myFileUtil.writeToFile("./FILES/TEST.TXT", "append:"+nowStr, "append");

        nowStr = myDateUtil.convertDateToString(myDateUtil.now(), "yyyy-MM-dd HH:mm:ss.SSS");
        myFileUtil.writeToFile("./FILES/TEST.TXT", "prepend:"+nowStr, "prepend");

        /////////////////////////////////
        // 파일 관련 유틸 사용법 끝
        /////////////////////////////////

        System.out.println("Hello World!");
    }
}
