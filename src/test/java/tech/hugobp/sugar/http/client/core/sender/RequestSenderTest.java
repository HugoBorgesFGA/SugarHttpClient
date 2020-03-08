package tech.hugobp.sugar.http.client.core.sender;

import io.reactivex.Single;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.hugobp.sugar.http.client.core.factory.RequestBuilderFactory;
import tech.hugobp.sugar.http.client.entities.HttpRequest;
import tech.hugobp.sugar.http.client.entities.HttpResponse;
import tech.hugobp.sugar.http.client.entities.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RequestSenderTest {
    
    @Autowired
    private RequestBuilderFactory requestBuilderFactory;

    private static Single<HttpResponse> mockSuccessfulResponse() {
        return Single.just(new HttpResponse(HttpStatusCode.OK, "Mocked response body"));
    }

    private static Single<HttpResponse> mockFailedResponse() {
        return Single.just(new HttpResponse(HttpStatusCode.NOT_FOUND, null));
    }

    private static Single<HttpResponse> mockErrorResponse() {
        return Single.error(() -> new RuntimeException("Any unchecked http exception"));
    }

    private HttpRequest buildRequest() {
        return requestBuilderFactory.create("bitcointrade-ticker")
                                    .using("securityKey", "XXXX")
                                    .using("pair", "BRLBTC")
                                    .build();
    }

    @Test
    public void Test_Sender_SuccessfulResponse(){

        RequestSender mockedSender = Mockito.mock(RequestSender.class);
        when(mockedSender.send(any())).thenReturn(mockSuccessfulResponse());

        HttpResponse response = mockedSender.send(buildRequest()).blockingGet();

        assertEquals(response.getBody(), "Mocked response body");
        assertEquals(response.getStatusCode(), HttpStatusCode.OK);
    }

    @Test
    public void Test_Sender_FailedResponse(){

        RequestSender mockedSender = Mockito.mock(RequestSender.class);
        when(mockedSender.send(any())).thenReturn(mockFailedResponse());

        HttpResponse response = mockedSender.send(buildRequest()).blockingGet();

        assertTrue(response.getStatusCode() != HttpStatusCode.OK);
    }

    @Test
    public void Test_Sender_Error(){

        RequestSender mockedSender = Mockito.mock(RequestSender.class);
        when(mockedSender.send(any())).thenReturn(mockErrorResponse());

        assertThrows(RuntimeException.class, () -> {
            mockedSender.send(buildRequest()).blockingGet();
        });
    }

}