package project.users.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public class LocalStorageService implements StorageService {
    private final Path upload_dir;

    public LocalStorageService() throws IOException {
        upload_dir = Files.createTempDirectory("avatars");
        Files.createDirectories(upload_dir);
    }

    @Override
    public String store(MultipartFile file, UUID userId) throws IOException {
        String filename = userId.toString() + "_" + file.getOriginalFilename();
        Path target = upload_dir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    @Override
    public Resource load(String filename) throws IOException {
        Path file = upload_dir.resolve(filename);
        return new UrlResource(file.toUri());
    }

    @Override
    public void delete(String filename) throws IOException {
        Path file = upload_dir.resolve(filename);
        Files.delete(file);
    }
    
}
