package project.dto.returns;

import org.springframework.core.io.Resource;

public class AvatarGetReturnDto {
    private Resource avatar;

    public AvatarGetReturnDto(Resource avatar) {
        this.avatar = avatar;
    }

    public Resource getAvatar() {
        return this.avatar;
    }
}
