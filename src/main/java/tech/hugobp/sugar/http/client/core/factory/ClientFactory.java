package tech.hugobp.sugar.http.client.core.factory;

import org.springframework.stereotype.Component;
import tech.hugobp.sugar.http.client.entities.HttpClient;
import tech.hugobp.sugar.http.client.entities.HttpProperties;

@Component
public class ClientFactory extends AbstractBeanFactory<HttpClient> {
    public ClientFactory(HttpProperties httpProperties) {
        super(httpProperties.getClients(), HttpClient::getId);
    }
}
