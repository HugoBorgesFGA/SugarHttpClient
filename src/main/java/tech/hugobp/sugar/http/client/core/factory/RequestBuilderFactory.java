package tech.hugobp.sugar.http.client.core.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.hugobp.sugar.http.client.core.RequestBuilder;
import tech.hugobp.sugar.http.client.entities.HttpProperties;
import tech.hugobp.sugar.http.client.entities.HttpRequest;

@Component
public class RequestBuilderFactory {

    private final AbstractBeanFactory<HttpRequest> internal;

    @Autowired
    public RequestBuilderFactory(HttpProperties httpProperties) {
        internal = new AbstractBeanFactory<HttpRequest>(httpProperties.getRequests(), HttpRequest::getId) {
            @Override
            public HttpRequest create(String id) {
                return super.create(id);
            }
        };
    }

    public RequestBuilder create(String id) {
        return new RequestBuilder(internal.create(id));
    }
}
