package project.dto.returns;

public class AvatarUploadReturnDto {
    private String filename;

    public AvatarUploadReturnDto(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }
}
