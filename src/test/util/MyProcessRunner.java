package test.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class MyProcessRunner {

    public void process(String jarFileName, String imageDir) throws IOException {

        ProcessBuilder builderNoArgs = new ProcessBuilder("java", "-jar", jarFileName);
        ProcessBuilder builderWithArgs = new ProcessBuilder("java", "-jar", jarFileName, imageDir);

        builderNoArgs.directory(new File(Paths.get("").toAbsolutePath().toString()));
        builderNoArgs.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builderNoArgs.redirectError(ProcessBuilder.Redirect.INHERIT);
        builderNoArgs.start();
    }
}

