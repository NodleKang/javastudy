package test;

public class RunManager {

    public static void main(String[] args) {

        /////////////////////////////////
        // 파일 관련 유틸 사용법 시작
        /////////////////////////////////
        MyFileUtil myFileUtil = new MyFileUtil();

        // 특정 경로에서 시작 키워드와 종료 키워드에 맞는 파일의 절대 경로 목록을 찾아서 String 배열로 반환 (하위 디렉토리 포함 여부 선택 가능)
        String[] files = myFileUtil.findFiles(".", "", "", true);
        for (String file : files) {
            System.out.println(file);
        }

        // 파일 내용을 모두 읽어서 단일 String 객체로 반환
        // 파일을 못 찾으면 빈 문자열 반환
        String context = myFileUtil.getStringFromFile("./javastudy.iml");

        // 파일 내용을 모두 읽어서 String 배열로 반환
        // 파일을 못 찾으면 빈 배열 반환
        String lines[] = myFileUtil.getStringArrayFromFile("./javastudy.iml");

        //
        boolean checkDirectoryExists = myFileUtil.checkDirectoryExists("./FILES");
        myFileUtil.createDirectory("./FILES", true);
        myFileUtil.createFile("./FILES/TEST.TXT");
        myFileUtil.writeStringToFile("./FILES/TEST.TXT", "Hello World!", "append");

        /////////////////////////////////
        // 파일 관련 유틸 사용법 끝
        /////////////////////////////////

        System.out.println("Hello World!");
    }
}
