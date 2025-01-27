package project.utils;

public class ResponseDto {
    private final String message;
    private final Object data;

    public ResponseDto(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}