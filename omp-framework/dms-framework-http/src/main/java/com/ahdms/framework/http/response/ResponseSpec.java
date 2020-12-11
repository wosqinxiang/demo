package com.ahdms.framework.http.response;

import com.ahdms.framework.core.commom.util.Try;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * 相应接口
 *
 * @author Katrel.zhou
 */
public interface ResponseSpec {

    /**
     * Returns the HTTP code.
     *
     * @return code
     */
    int code();

    /**
     * Returns the HTTP status message.
     *
     * @return message
     */
    String message();

    /**
     * Returns the HTTP exception.
     *
     * @return message
     */
    Exception exception();

    /**
     * Returns the HTTP Successful.
     *
     * @return boolean
     */
    boolean isOk();

    /**
     * Returns the is Redirect.
     *
     * @return is Redirect
     */
    boolean isRedirect();

    /**
     * Returns the Headers.
     *
     * @return Headers
     */
    Headers headers();

    /**
     * Returns the Cookies.
     *
     * @return Cookie List
     */
    List<Cookie> cookies();

    /**
     * Returns body String.
     *
     * @return body String
     */
    String asString();

    /**
     * Returns body to byte arrays.
     *
     * @return byte arrays
     */
    byte[] asBytes();

    /**
     * Returns body to InputStream.
     *
     * @return InputStream
     */
    <T> T stream(Try.UncheckedFunction<InputStream, T> function);

    /**
     * Returns body to JsonNode.
     *
     * @return JsonNode
     */
    JsonNode asJsonNode();

    /**
     * Returns body to Object.
     *
     * @param valueType value value type
     * @return Object
     */
    <T> T asValue(Class<T> valueType);

    /**
     * Returns body to Object.
     *
     * @param typeReference value Type Reference
     * @return Object
     */
    <T> T asValue(TypeReference<T> typeReference);

    /**
     * Returns body to List.
     *
     * @param valueType value type
     * @return List
     */
    <T> List<T> asList(Class<T> valueType);

    /**
     * Returns body to Map.
     *
     * @param keyClass  key type
     * @param valueType value type
     * @return Map
     */
    <K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueType);

    /**
     * Returns body to Map.
     *
     * @param valueType value 类型
     * @return Map
     */
    <V> Map<String, V> asMap(Class<?> valueType);

    /**
     * Returns body to jsoup Document.
     *
     * @return Document
     */
    Document asDocument();

    /**
     * 将 xml、heml 转成对象
     *
     * @param valueType 对象类
     * @param <T>       泛型
     * @return 对象
     */
    <T> T asDomValue(Class<T> valueType);

    /**
     * 将 xml、heml 转成对象
     *
     * @param valueType 对象类
     * @param <T>       泛型
     * @return 对象集合
     */
    <T> List<T> asDomList(Class<T> valueType);

    /**
     * toFile.
     *
     * @param file File
     */
    void toFile(File file);

    /**
     * toFile.
     *
     * @param path Path
     */
    void toFile(Path path);

    /**
     * Returns contentType.
     *
     * @return contentType
     */
    MediaType contentType();

    /**
     * Returns contentLength.
     *
     * @return contentLength
     */
    long contentLength();

    /**
     * Returns rawResponse.
     *
     * @return Response
     */
    Response rawResponse();

    /**
     * Returns rawBody.
     *
     * @return ResponseBody
     */
    ResponseBody rawBody();
}
