package project;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Component
@Profile("!test")
public class PythonScriptRunner {

    @PostConstruct
    public void runPythonScript() {
        try {
            // Command to activate venv and run the script
            String[] command = { "/bin/bash", "-c", "source .venv/bin/activate && python scraping/scraper.py" };

            System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
            String userDir = System.getProperty("user.dir");
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(userDir));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read output
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
