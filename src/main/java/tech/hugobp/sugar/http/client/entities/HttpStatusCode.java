package tech.hugobp.sugar.http.client.entities;

import java.util.Arrays;

public enum HttpStatusCode {
    OK(200),
    BAD_REQUEST(400),
    NOT_AUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404);

    private final int code;
    HttpStatusCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static HttpStatusCode fromCode(int code) {
        return Arrays.stream(values())
                     .filter(httpStatusCode -> httpStatusCode.getCode() == code)
                     .findFirst()
                     .orElseThrow(() -> new RuntimeException("Unknown http status code: " + code));
    }
}
