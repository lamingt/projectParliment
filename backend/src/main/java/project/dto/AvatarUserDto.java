package project.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvatarUserDto {
    private MultipartFile file;

    public AvatarUserDto(@JsonProperty("file") MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }
}
