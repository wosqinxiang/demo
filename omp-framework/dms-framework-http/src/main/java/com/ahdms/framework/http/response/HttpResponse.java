package com.ahdms.framework.http.response;

import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.framework.core.commom.util.Try;
import com.ahdms.framework.core.constant.SystemError;
import com.ahdms.framework.core.exception.FrameworkException;
import com.ahdms.framework.http.dom.DomMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import okhttp3.internal.Util;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ok http 封装，相应结构体
 *
 * @author Katrel.zhou
 */
public class HttpResponse implements ResponseSpec, Closeable {
    private final Request request;
    private final Response response;
    private final ResponseBody body;
    private IOException exception;

    public HttpResponse(final Response response) {
        this.request = response.request();
        this.response = response;
        this.body = response.body();
    }

    public HttpResponse(Request request, IOException exception) {
        this.request = request;
        this.response = null;
        this.body = null;
        this.exception = exception;
    }

    private void checkIfException() {
        if (exception != null) {
            throw new FrameworkException(SystemError.INTERNAL_ERROR, exception);
        }
    }

    @Override
    public int code() {
//        checkIfException();
        return response.code();
    }

    @Override
    public String message() {
//        checkIfException();
        return response.message();
    }

    @Override
    public Exception exception() {
        return exception;
    }


    @Override
    public boolean isOk() {
        return response != null && response.isSuccessful();
    }

    @Override
    public boolean isRedirect() {
        checkIfException();
        return response.isRedirect();
    }

    public <T> T onResponse(Function<ResponseSpec, T> function) {
        if (this.response != null) {
            return function.apply(this);
        }
        return null;
    }

    public HttpResponse onSuccessful(Consumer<ResponseSpec> consumer) {
        if (this.isOk()) {
            consumer.accept(this);
        }
        return this;
    }

    public <T> T onSuccess(Function<ResponseSpec, T> function) {
        if (this.isOk()) {
            return function.apply(this);
        }
        return null;
    }

    public <T> Optional<T> onSuccessOpt(Function<ResponseSpec, T> function) {
        if (this.isOk()) {
            return Optional.ofNullable(function.apply(this));
        }
        return Optional.empty();
    }

    public HttpResponse onFailed(BiConsumer<Request, IOException> consumer) {
        if (exception != null) {
            consumer.accept(this.request, this.exception);
        }
        return this;
    }

    @Override
    public Headers headers() {
        checkIfException();
        return response.headers();
    }

    public HttpResponse headers(Consumer<Headers> consumer) {
        consumer.accept(this.headers());
        return this;
    }

    @Override
    public List<Cookie> cookies() {
        checkIfException();
        HttpUrl httpUrl = response.request().url();
        return Cookie.parseAll(httpUrl, this.headers());
    }

    public HttpResponse cookies(Consumer<List<Cookie>> consumer) {
        consumer.accept(this.cookies());
        return this;
    }

    @Override
    public String asString() {
        checkIfException();
        try {
            return body.string();
        } catch (IOException e) {
            throw new FrameworkException(SystemError.INTERNAL_ERROR, e);
        }
    }

    @Override
    public byte[] asBytes() {
        checkIfException();
        try {
            return body.bytes();
        } catch (IOException e) {
            throw new FrameworkException(SystemError.INTERNAL_ERROR, e);
        }
    }

    @Override
    public <T> T stream(Try.UncheckedFunction<InputStream, T> function) {
        checkIfException();
        try (InputStream stream = body.byteStream()) {
            return function.apply(stream);
        } catch (Throwable e) {
            throw new FrameworkException(SystemError.REMOTE_EXCEPTION, e);
        }
    }

    @Override
    public JsonNode asJsonNode() {
        return this.stream(JsonUtils::readTree);
    }

    @Override
    public <T> T asValue(Class<T> valueType) {
        return this.stream(input -> JsonUtils.readValue(input, valueType));
    }

    @Override
    public <T> T asValue(TypeReference<T> typeReference) {
        return this.stream(input -> JsonUtils.readValue(input, typeReference));
    }

    @Override
    public <T> List<T> asList(Class<T> valueType) {
        return this.stream(input -> JsonUtils.readList(input, valueType));
    }

    @Override
    public <K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueClass) {
        return this.stream(input -> JsonUtils.readMap(input, keyClass, valueClass));
    }

    @Override
    public <V> Map<String, V> asMap(Class<?> valueClass) {
        return this.asMap(String.class, valueClass);
    }

    @Override
    public Document asDocument() {
        return Parser.parse(this.asString(), "");
    }

    @Override
    public <T> T asDomValue(Class<T> valueType) {
        return DomMapper.readValue(this.asDocument(), valueType);
    }

    @Override
    public <T> List<T> asDomList(Class<T> valueType) {
        return DomMapper.readList(this.asDocument(), valueType);
    }

    @Override
    public void toFile(File file) {
        toFile(file.toPath());
    }

    @Override
    public void toFile(Path path) {
        this.stream(input -> Files.copy(input, path));
    }

    @Override
    public MediaType contentType() {
        checkIfException();
        return body.contentType();
    }

    @Override
    public long contentLength() {
        checkIfException();
        return body.contentLength();
    }

    public Request request() {
        checkIfException();
        return this.request;
    }

    @Override
    public Response rawResponse() {
        checkIfException();
        return this.response;
    }

    @Override
    public ResponseBody rawBody() {
        checkIfException();
        return this.body;
    }

    @Override
    public String toString() {
        checkIfException();
        return this.response.toString();
    }

    @Override
    public void close() throws IOException {
        Util.closeQuietly(this.body);
    }
}
