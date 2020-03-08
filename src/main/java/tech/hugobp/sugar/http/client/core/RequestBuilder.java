package tech.hugobp.sugar.http.client.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.hugobp.sugar.http.client.entities.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpRequest request;

    private Object body = null;
    private Map<String, String> valueByPlaceholder = new HashMap<>();

    public RequestBuilder() {
        this.request = new HttpRequest();
    }

    public RequestBuilder(HttpRequest request) {
        this.request = request;
    }

    public RequestBuilder using(String placeHolder, String value){
        this.valueByPlaceholder.put(placeHolder, value);
        return this;
    }

    public RequestBuilder withBody(Object body) {
        this.body = body;
        return this;
    }

    public HttpRequest build() {

        // Serialize request into Json
        String jsonString = null;
        try {
            jsonString = MAPPER.writeValueAsString(this.request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Perform all placeholder replacements
        for(String placeholderName : valueByPlaceholder.keySet()) {
            final String placeholder = String.format("\\{%s\\}", placeholderName);
            jsonString = jsonString.replaceAll(placeholder, valueByPlaceholder.get(placeholderName));
        }

        // Deserialize
        try {
            HttpRequest result = MAPPER.readValue(jsonString, HttpRequest.class);
            result.setBody(body);

            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
