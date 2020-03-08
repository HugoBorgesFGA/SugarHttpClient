package tech.hugobp.sugar.http.client.core.factory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractBeanFactory<T> {

    protected final Map<String, T> beanById;

    public AbstractBeanFactory(List<T> beans, Function<T, String> getBeanId) {
        this.beanById = beans.stream()
                             .collect(Collectors.toMap(bean -> getBeanId.apply(bean), Function.identity()));
    }

    public T create(String id) {
        return Optional.ofNullable(beanById.get(id)).orElseThrow(() -> new RuntimeException("Could not create object. Unknown id: " + id));
    }
}
