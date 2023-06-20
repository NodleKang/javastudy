/*
 * 클래스 사용
 *
 * File file = new file("파일 경로");
 * MyFileWatcher watcher = new MyFileWatcher(file);
 * Thread thread = new Thread(watcher);
 * thread.setDeamon(true);
 * thread.start();
 *
 * 사용 종료
 * watcher.stop();
 */
package test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MyFileWatcher implements Runnable {

    private static final int DELAY_MILLIS = 100; // 0.1초

    private boolean isRun;

    private final File file;

    public MyFileWatcher(File file) {
        this.file = file;
    }

    // 스레드에서 할 일
    @Override
    public void run() {
        System.out.println("Start to tail a file -" + file.getName());

        isRun = true;
        if (!file.exists()) {
            System.out.println("Failed to find a file - " + file.getAbsolutePath());
        }

        // try 문에서 stream을 열면 블록이 끝날 때 자동으로 close 해 줌
        try (BufferedReader br =
                     new BufferedReader(
                             new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
                     )
        ) {
            while (isRun) { // 무한반복
                final String line = br.readLine(); // 파일에서 한 라인 읽어오기
                if (line != null) { // 파일에서 읽어온 라인이 null이 아니면 출력
                    System.out.println("New line added - " + line);
                } else {
                    Thread.sleep(DELAY_MILLIS);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to tail a file - " + file.getAbsolutePath());
        }

        System.out.println("Stop to tail a file - " + file.getAbsolutePath());
    }

    // 스레드 중지 기능
    public void stop() {
        isRun = false;
    }

}
