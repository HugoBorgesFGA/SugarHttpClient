package tech.hugobp.sugar.http.client.entities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "http")
public class HttpProperties {

    private List<HttpRequest> requests;
    private List<HttpClient> clients;

    public HttpProperties() {
    }

    public void setRequests(List<HttpRequest> requests) {
        this.requests = requests;
    }

    public List<HttpRequest> getRequests() {
        return requests;
    }

    public List<HttpClient> getClients() {
        return clients;
    }

    public void setClients(List<HttpClient> clients) {
        this.clients = clients;
    }
}
