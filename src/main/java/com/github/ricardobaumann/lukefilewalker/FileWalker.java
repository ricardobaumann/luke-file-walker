package com.github.ricardobaumann.lukefilewalker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

class FileWalker {

    static String sort(String inputFilePath) throws IOException {
        AtomicReference<File> tempFile = new AtomicReference<>(createTempFile());
        try (Stream<String> stream = Files.lines(Paths.get(inputFilePath))) {
            stream.forEach(line -> {
                try {
                    tempFile.set(sortLine(line, tempFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return tempFile.get().getAbsolutePath();
    }

    private static File createTempFile() throws IOException {
        return Files.createTempFile("sort", "ed").toFile();
    }

    private static File sortLine(String baseLine, AtomicReference<File> tempFileRef) throws IOException {

        File tempFile = tempFileRef.get();
        if (tempFile.length() == 0) {
            Files.write(Paths.get(tempFile.getAbsolutePath()), baseLine.getBytes());
            return tempFile;
        }

        int baseIndex = Integer.parseInt(baseLine.split(";")[0]);

        File newTempFile = createTempFile();
        FileWriter fw = new FileWriter(newTempFile);
        AtomicBoolean wroteBase = new AtomicBoolean(false);
        try (Stream<String> stream = Files.lines(Paths.get(tempFile.getAbsolutePath()))) {
            stream.forEach(line -> {
                int index = Integer.parseInt(line.split(";")[0]);
                try {
                    if (baseIndex == index) {
                        return;
                    }
                    if (!wroteBase.get() && baseIndex < index) {
                        fw.write(baseLine);
                        fw.write("\r\n");
                        wroteBase.set(true);
                    }
                    fw.write(line);
                    fw.write("\r\n");


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        if (!wroteBase.get()) {
            fw.write(baseLine);
            fw.write("\r\n");
        }
        fw.flush();
        fw.close();

        return newTempFile;
    }
}
