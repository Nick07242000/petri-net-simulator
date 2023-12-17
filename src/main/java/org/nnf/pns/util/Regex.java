package org.nnf.pns.util;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Regex {
    private static final Logger log = Logger.getLogger(Regex.class);

    public static void callRegexAnalyzer() {
        log.debug("Executing python script...");
        ProcessBuilder processBuilder = new ProcessBuilder("python", "./src/main/resources/regex.py");
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8))
                    .lines()
                    .forEach(log::debug);

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.debug("Error executing python script: ", e);
            currentThread().interrupt();
        }
    }
}
