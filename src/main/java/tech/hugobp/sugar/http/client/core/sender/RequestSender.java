package tech.hugobp.sugar.http.client.core.sender;

import io.reactivex.Single;
import tech.hugobp.sugar.http.client.entities.HttpRequest;
import tech.hugobp.sugar.http.client.entities.HttpResponse;

public interface RequestSender {
    Single<HttpResponse> send(HttpRequest request);
}
