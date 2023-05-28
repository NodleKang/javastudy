package test;

import java.io.*;
import java.util.*;

public class MyFileUtil {

    // 주어진 경로(path)에 해당하는 파일이 존재하는지 여부 반환
    public boolean checkFileExists(String fileFullPath) {
        File file = new File(fileFullPath);
        return file.exists() && file.isFile();
    }

    // 주어진 경로(path)에 해당하는 디렉토리가 존재하는지 여부 반환
    public boolean checkDirectoryExists(String directoryFullPath) {
        File directory = new File(directoryFullPath);
        return directory.exists() && directory.isDirectory();
    }

    // 주어진 경로(path)에 해당하는 파일 또는 디렉토리가 존재하는지 여부 반환
    public boolean checkFileOrDirectoryExists(String fileOrDirectoryFullPath) {
        File fileOrDirectory = new File(fileOrDirectoryFullPath);
        return fileOrDirectory.exists();
    }

    // 주어진 경로(path)가 없으면 디렉토리 생성 (하위 디렉토리 포함 여부 선택 가능)
    public void createDirectory(String directoryFullPath, boolean includeSubdirectories) {
        // 디렉토리 객체 생성
        File directory = new File(directoryFullPath);
        // 디렉토리가 존재하지 않는 경우에만 디렉토리 생성
        if (!directory.exists()) {
            // 디렉토리 생성
            directory.mkdirs();
            // 하위 디렉토리 포함 여부가 true인 경우에만 하위 디렉토리 생성
            if (includeSubdirectories) {
                // 디렉토리의 상위 디렉토리 경로를 가져옴
                String parentDirectoryPath = directory.getParent();
                // 상위 디렉토리가 존재하지 않는 경우에만 상위 디렉토리 생성
                if (!checkDirectoryExists(parentDirectoryPath)) {
                    createDirectory(parentDirectoryPath, true);
                }
            }
        }
    }

    // 주어진 경로에 파일 생성
    public void createFile(String fileFullPath) {
        // 파일 객체 생성
        File file = new File(fileFullPath);
        // 파일이 존재하지 않는 경우에만 파일 생성
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 주어진 경로에 파일 삭제
    public void deleteFile(String fileFullPath) {
        // 파일 객체 생성
        File file = new File(fileFullPath);
        // 파일이 존재하는 경우에만 파일 삭제
        if (file.exists()) {
            file.delete();
        }
    }

    // 특정 경로에서 시작 키워드와 종료 키워드에 맞는 파일의 절대 경로 목록을 찾아서 String 배열로 반환 (하위 디렉토리 포함 여부 선택 가능)
    public String[] findFiles(String path, String startKeyword, String endKeyword, boolean includeSubdirectories) {
        // ArrayList 객체를 사용해서 파일 경로 저장
        List<String> fileList = new ArrayList<>();
        // 디렉토리 객체 생성
        File directory = new File(path);
        // 디렉토리가 존재하고 디렉토리인 경우에만 파일 목록 읽기
        if (directory.exists() && directory.isDirectory()) {
            // 디렉토리의 파일 목록을 읽어서 파일 경로 목록에 추가
            File[] files = directory.listFiles();
            // 파일 목록이 null이 아닌 경우에만 파일 경로 목록에 추가
            if (files != null) {
                // 파일 목록을 순회하면서 파일 경로 목록에 추가
                for (File file : files) {
                    // 파일이고 시작 키워드와 종료 키워드에 맞는 경우에만 파일 경로 목록에 추가
                    if (file.isFile() && file.getName().startsWith(startKeyword) && file.getName().endsWith(endKeyword)) {
                        fileList.add(file.getAbsolutePath());
                        // 하위 디렉토리 포함 여부가 true인 경우에만 하위 디렉토리의 파일 경로 목록을 추가
                    } else if (includeSubdirectories && file.isDirectory()) {
                        // 재귀 호출을 사용해서 하위 디렉토리의 파일 경로 목록을 추가
                        String subdirectoryPath = file.getAbsolutePath();
                        // 하위 디렉토리의 파일 경로 목록을 재귀 호출을 사용해서 추가
                        String[] subdirectoryFiles = findFiles(subdirectoryPath, startKeyword, endKeyword, true);
                        // 하위 디렉토리의 파일 경로 목록을 파일 경로 목록에 추가
                        fileList.addAll(Arrays.asList(subdirectoryFiles));
                    }
                }
            }
        }
        // ArrayList 객체를 String 배열로 변환해서 반환
        return fileList.toArray(new String[0]);
    }

    // 특정 경로에서 시작 키워드와 종료 키워드에 맞는 파일의 절대 경로 목록을 찾아서 파일들 삭제
    public void deleteFiles(String path, String startKeyword, String endKeyword) {
        // 파일 경로 목록을 찾아서 String 배열로 반환
        String[] files = findFiles(path, startKeyword, endKeyword, true);
        // 파일 경로 목록을 순회하면서 파일 삭제
        for (String file : files) {
            deleteFile(file);
        }
    }

    // 파일 내용을 모두 읽어서 단일 String 객체로 반환
    public String getStringFromFile(String fileFullPath) {
        // StringBuilder 객체를 사용해서 파일 내용 저장
        StringBuilder content = new StringBuilder();
        // 파일 객체 생성
        File file = new File(fileFullPath);
        // 파일이 존재하고 파일인 경우에만 파일 내용 읽기
        if (file.exists() && file.isFile()) {
            // try-with-resources 구문 사용
            // try 블록이 끝나면 자동으로 close() 메소드가 호출됨
            // BufferedReader 객체를 사용해서 파일 내용 읽기
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {
                // 예외 발생 시 스택 트레이스 출력
                e.printStackTrace();
            }
        }
        // StringBuilder 객체를 String 객체로 변환해서 반환
        return content.toString();
    }

    // 파일 내용을 모두 읽어서 String 배열로 반환
    public String[] getStringArrayFromFile(String fileFullPath) {
        // ArrayList 객체를 사용해서 파일 내용 저장
        List<String> lines = new ArrayList<>();
        // 파일 객체 생성
        File file = new File(fileFullPath);
        // 파일이 존재하고 파일인 경우에만 파일 내용 읽기
        if (file.exists() && file.isFile()) {
            // try-with-resources 구문 사용 (Java 7 이상)
            // try 블록이 끝나면 자동으로 close() 메소드가 호출됨
            // BufferedReader 객체를 사용해서 파일 내용 읽기
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                // 예외 발생 시 스택 트레이스 출력
                e.printStackTrace();
            }
        }
        // ArrayList 객체를 String 배열로 변환해서 반환
        return lines.toArray(new String[0]);
    }

    // 주어진 경로(path)에 해당하는 파일에 입력할 문자열(content)을 지정된 위치(position)에 따라 쓰기 (position: prepend, append)
    public boolean writeStringToFile(String path, String content, String position) {
        // 파일 객체 생성
        File file = new File(path);

        // BufferedWriter 객체를 사용해서 파일 쓰기
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, position.equals("prepend")))) {
            // try-with-resources 구문 사용 (Java 7 이상)
            // try 블록이 끝나면 자동으로 close() 메소드가 호출됨
            // 문자열을 입력할 위치가 prepend인 경우에는 파일의 맨 앞에 문자열을 입력
            if (position.equals("prepend")) {
                // BufferedWrite 객체에 입력할 문자열 쓰기
                writer.write(content);
                // 줄바꿈 문자 쓰기
                writer.newLine();
                // 버퍼 비우기
                writer.flush();
                // BufferedWriter 객체 닫기
                writer.close();
                return true;
                // 문자열을 입력할 위치가 append인 경우에는 파일의 맨 뒤에 문자열을 입력
            } else if (position.equals("append")) {
                // BufferedWriter 객체에 입력할 문자열 덧붙이기
                writer.append(content);
                // 줄바꿈 문자 쓰기
                writer.newLine();
                // 버퍼 비우기
                writer.flush();
                // BufferedWriter 객체 닫기
                writer.close();
                return true;
            } else {
                // 파일쓰기에 실패하면 false 반환
                System.out.println("Invalid position. Please specify either 'prepend' or 'append'.");
                return false;
            }
        } catch (IOException e) {
            // 예외 발생 시 스택 트레이스 출력
            e.printStackTrace();
            // 파일쓰기에 실패하면 false 반환
            return false;
        }
    }

}
