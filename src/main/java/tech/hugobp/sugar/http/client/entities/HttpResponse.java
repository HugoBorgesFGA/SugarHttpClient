package tech.hugobp.sugar.http.client.entities;

import java.util.Map;

public class HttpResponse {

    private Map<String, String> headers;
    private HttpStatusCode statusCode;
    private Object body;

    public HttpResponse() {
    }

    public HttpResponse(HttpStatusCode statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpResponse(Map<String, String> headers, HttpStatusCode statusCode, Object body) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
