package tech.hugobp.sugar.http.client.core.sender.apache;

import io.reactivex.Single;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.hugobp.sugar.http.client.core.factory.ClientFactory;
import tech.hugobp.sugar.http.client.core.sender.RequestSender;
import tech.hugobp.sugar.http.client.entities.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class RequestSenderApacheImpl implements RequestSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestSenderApacheImpl.class);
    private Map<String, CloseableHttpAsyncClient> cachedApacheClients;
    private final ClientFactory clientFactory;

    private final Map<HttpMethod, Supplier<RequestBuilder>> requestBuilderByHttpMethod;

    @Autowired
    public RequestSenderApacheImpl(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
        this.cachedApacheClients = new HashMap<>();

        // Auxiliary map to build requests more easily (:
        this.requestBuilderByHttpMethod = new HashMap<>();
        this.requestBuilderByHttpMethod.put(HttpMethod.GET, RequestBuilder::get);
        this.requestBuilderByHttpMethod.put(HttpMethod.POST, RequestBuilder::post);
        this.requestBuilderByHttpMethod.put(HttpMethod.DELETE, RequestBuilder::delete);
        this.requestBuilderByHttpMethod.put(HttpMethod.PUT, RequestBuilder::put);
    }

    @Override
    public Single<HttpResponse> send(HttpRequest request) {

        final CloseableHttpAsyncClient apacheClient = toApacheClient(request);
        final HttpUriRequest apacheRequest = toApacheRequest(request);

        return Single.fromFuture(apacheClient.execute(apacheRequest, null))
                     .map(this::fromApacheResponse);
    }

    private HttpUriRequest toApacheRequest(HttpRequest request) {

        RequestBuilder requestBuilder = requestBuilderByHttpMethod.get(request.getMethod()).get();
        request.getHeaders().forEach(requestBuilder::addHeader);
        requestBuilder.setUri(request.getHost() + request.getPath());

        Optional.ofNullable(request.getBody())
                .ifPresent(body -> {
                    try {
                        requestBuilder.setEntity(new StringEntity(request.getBody().toString()));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                });

        return requestBuilder.build();
    }

    private HttpResponse fromApacheResponse(org.apache.http.HttpResponse apacheResponse) {

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHeaders(
                Arrays.stream(apacheResponse.getAllHeaders())
                      .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue))
        );
        httpResponse.setStatusCode(HttpStatusCode.fromCode(apacheResponse.getStatusLine().getStatusCode()));
        try {
            httpResponse.setBody(EntityUtils.toString(apacheResponse.getEntity()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return httpResponse;
    }

    private CloseableHttpAsyncClient toApacheClient(HttpRequest request) {

        final String clientId = request.getId();
        CloseableHttpAsyncClient client = cachedApacheClients.get(clientId);
        if (client == null) {
            client = createApacheClient(request);

            LOGGER.info("Adding http client '{}' to cache...", clientId);
            cachedApacheClients.put(clientId, client);
        }

        return client;
    }

    private CloseableHttpAsyncClient createApacheClient(HttpRequest request) {

        final String clientId = request.getClient();
        final HttpClient httpClientSettings = clientFactory.create(clientId);

        // Create according to the settings provided by the httpClient
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();

        if (!client.isRunning()) {
            LOGGER.info("Client '{}' was not running yet. Starting client...", clientId);
            client.start();
        }

        return client;
    }
}
