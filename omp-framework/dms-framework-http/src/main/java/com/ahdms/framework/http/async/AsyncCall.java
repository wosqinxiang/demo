package com.ahdms.framework.http.async;

import com.ahdms.framework.http.response.HttpResponse;
import com.ahdms.framework.http.response.ResponseSpec;
import okhttp3.Call;
import okhttp3.Request;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 异步执行器
 *
 * @author Katrel.zhou
 */
public class AsyncCall {
    private final static Consumer<ResponseSpec> DEFAULT_CONSUMER = (r) -> {};
    private final static BiConsumer<Request, IOException> DEFAULT_FAIL_CONSUMER = (r, e) -> {};
    private final Call call;
    private Consumer<ResponseSpec> successConsumer;
    private Consumer<ResponseSpec> responseConsumer;
    private BiConsumer<Request, IOException> failedBiConsumer;

    public AsyncCall(Call call) {
        this.call = call;
        this.successConsumer = DEFAULT_CONSUMER;
        this.responseConsumer = DEFAULT_CONSUMER;
        this.failedBiConsumer = DEFAULT_FAIL_CONSUMER;
    }

    public AsyncCall onSuccessful(Consumer<ResponseSpec> consumer) {
        this.successConsumer = consumer;
        return this;
    }

    public AsyncCall onResponse(Consumer<ResponseSpec> consumer) {
        this.responseConsumer = consumer;
        return this;
    }

    public AsyncCall onFailed(BiConsumer<Request, IOException> biConsumer) {
        this.failedBiConsumer = biConsumer;
        return this;
    }

    public void execute() {
        call.enqueue(new AsyncCallback(this));
    }

    void onResponse(HttpResponse httpResponse) {
        responseConsumer.accept(httpResponse);
    }

    void onSuccess(HttpResponse httpResponse) {
        successConsumer.accept(httpResponse);
    }

    void onFailure(Request request, IOException e) {
        failedBiConsumer.accept(request, e);
    }
}
