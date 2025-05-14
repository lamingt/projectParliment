package project.users.storage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    public String store(MultipartFile file, UUID userId) throws IOException;

    public Resource load(String filename) throws IOException;

    public void delete(String filename) throws IOException;
}
