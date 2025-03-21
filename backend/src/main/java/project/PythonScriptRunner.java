package project;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.*;

@Component
@Profile("!test")
public class PythonScriptRunner {

    @PostConstruct
    public void runPythonScript() {
        try {
            String projectRoot = new File(System.getProperty("user.dir")).getParent(); // Go up from backend/
            File venvPython = new File(projectRoot, ".venv/bin/python");
            File scriptFile = new File(projectRoot, "scraping/scraper.py");

            if (!venvPython.exists()) {
                System.err.println("Python virtual environment not found at: " + venvPython.getAbsolutePath());
                return;
            }
            if (!scriptFile.exists()) {
                System.err.println("Python script not found at: " + scriptFile.getAbsolutePath());
                return;
            }

            String[] command = { venvPython.getAbsolutePath(), scriptFile.getAbsolutePath() };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(projectRoot)); // Run from projectParliment/
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
