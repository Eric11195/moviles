package com.example.game.testing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class ps1Runner {
    public static void runScript(boolean isPowerShell, String scriptFileName) throws Exception {
        // Resolve the Android Studio project root directory
        File projectRoot = new File(System.getProperty("user.dir"));
        if (projectRoot.getName().equals("app")) {
            projectRoot = projectRoot.getParentFile(); // Move up if working dir is the :app module
        }

        List<String> command;
        if (isPowerShell) {
            command = List.of(
                    "powershell.exe",
                    "-NoProfile",
                    "-ExecutionPolicy", "Bypass",
                    "-File", scriptFileName
            );
        } else {
            command = List.of("cmd.exe", "/c", scriptFileName);
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(projectRoot);
        pb.redirectErrorStream(true); // Merge stderr into stdout so the buffer never blocks

        Process process = pb.start();

        // Read output (required so the process doesn't hang on full output buffers)
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Script failed with exit code: " + exitCode);
        }
    }
}
