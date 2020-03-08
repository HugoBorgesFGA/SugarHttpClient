package tech.hugobp.sugar.http.client.core.sender.apache;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.hugobp.sugar.http.client.core.factory.RequestBuilderFactory;
import tech.hugobp.sugar.http.client.entities.HttpRequest;
import tech.hugobp.sugar.http.client.entities.HttpResponse;
import tech.hugobp.sugar.http.client.entities.HttpStatusCode;

@SpringBootTest
class RequestSenderApacheImplTest {

    @Autowired
    private RequestBuilderFactory requestBuilderFactory;

    @Autowired
    private RequestSenderApacheImpl requestSender;

    @Test
    void Test_ApacheRequestSender_performGetRequest() {

        final HttpRequest requestBuilder = requestBuilderFactory.create("bitcointrade-ticker")
                                                                   .using("pair", "BRLBTC")
                                                                   .using("securityKey", "fake-key")
                                                                   .build();

        final TestSubscriber<HttpResponse> testSubscriber = new TestSubscriber<>();

        Single<HttpResponse> sendRequest = requestSender.send(requestBuilder);
        HttpResponse httpResponse = sendRequest.blockingGet();

        Assertions.assertNotNull(httpResponse);
        Assertions.assertEquals(httpResponse.getStatusCode(), HttpStatusCode.OK);
        Assertions.assertNotNull(httpResponse.getBody());
    }
}