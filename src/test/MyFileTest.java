package test;

import test.util.MyDate;
import test.util.MyFile;

public class MyFileTest {

    public static void main(String[] args) {

        // 특정 경로에서 시작 키워드와 종료 키워드에 맞는 파일의 절대 경로 목록을 찾아서 String 배열로 반환 (하위 디렉토리 포함 여부 선택 가능)
        String[] files = MyFile.findFiles(".", "", "", true);

        // 파일 내용을 모두 읽어서 단일 String 객체로 반환
        // 파일을 못 찾으면 빈 문자열 반환
        String content = MyFile.readFileToString("./FILES/TEST.TXT");

        // 파일 내용을 모두 읽어서 String 배열로 반환
        // 파일을 못 찾으면 빈 배열 반환
        String lines[] = MyFile.readFileToArray("./javastudy.iml", "UTF-8");

        // 디렉토리가 존재하는지 확인
        boolean checkDirectoryExists = MyFile.checkDirectoryExists("./FILES");
        // 디렉토리 생성 (하위 디렉토리까지 생성할지 여부 선택 가능)
        // 없으면 생성하고 있으면 아무것도 안 함
        MyFile.createDirectory("./FILES", true);
        // 파일 생성 (디렉토리까지 생성할지 여부 선택 가능)
        // 없으면 생성하고 있으면 아무것도 안 함
        MyFile.createFile("./FILES/TEST.TXT");

        // 현재 시간을 문자열로 받기
        MyDate myDate = new MyDate();
        String nowStr = myDate.convertDateToString(myDate.now(), "yyyy-MM-dd HH:mm:ss.SSS");

        // 파일에 쓰기 테스트
        MyFile.writeToFile("./FILES/TEST.TXT", "overwrite:"+nowStr, "overwrite");
        MyFile.writeToFile("./FILES/TEST.TXT", "append:"+nowStr, "append");
        MyFile.writeToFile("./FILES/TEST.TXT", "prepend:"+nowStr, "prepend");

    }
}
