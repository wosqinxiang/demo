package com.ahdms.framework.http.dom;

import com.ahdms.framework.core.constant.SystemError;
import com.ahdms.framework.core.exception.FrameworkException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.cglib.proxy.Enhancer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * xml 转 bean 基于 jsoup
 *
 * @author Katrel.zhou
 */
@SuppressWarnings("unchecked")
public class DomMapper {

    /**
     * 读取 xml 信息为 java Bean
     *
     * @param inputStream InputStream
     * @param clazz       bean Class
     * @param <T>         泛型
     * @return 对象
     */
    public static <T> T readValue(InputStream inputStream, final Class<T> clazz) {
        try {
            Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
            return readValue(document, clazz);
        } catch (IOException e) {
            throw new FrameworkException(SystemError.INTERNAL_ERROR,e);
        }
    }

    /**
     * 读取 xml 信息为 java Bean
     *
     * @param html  html String
     * @param clazz bean Class
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T readValue(String html, final Class<T> clazz) {
        Document document = Parser.parse(html, "");
        return readValue(document, clazz);
    }

    /**
     * 读取 xml 信息为 java Bean
     *
     * @param doc   xml element
     * @param clazz bean Class
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T readValue(final Element doc, final Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setUseCache(true);
        enhancer.setCallback(new CssQueryMethodInterceptor(clazz, doc));
        return (T) enhancer.create();
    }

    /**
     * 读取 xml 信息为 java Bean
     *
     * @param <T>         泛型
     * @param inputStream InputStream
     * @param clazz       bean Class
     * @return 对象
     */
    public static <T> List<T> readList(InputStream inputStream, final Class<T> clazz) {
        try {
            Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
            return readList(document, clazz);
        } catch (IOException e) {
            throw new FrameworkException(SystemError.INTERNAL_ERROR, e);
        }
    }

    /**
     * 读取 xml 信息为 java Bean
     *
     * @param <T>   泛型
     * @param html  html String
     * @param clazz bean Class
     * @return 对象
     */
    public static <T> List<T> readList(String html, final Class<T> clazz) {
        Document document = Parser.parse(html, "");
        return readList(document, clazz);
    }

    /**
     * 读取 xml 信息为 java Bean
     *
     * @param doc   xml element
     * @param clazz bean Class
     * @param <T>   泛型
     * @return 对象列表
     */
    public static <T> List<T> readList(Element doc, Class<T> clazz) {
        CssQuery annotation = clazz.getAnnotation(CssQuery.class);
        if (annotation == null) {
            throw new IllegalArgumentException("DomMapper readList " + clazz + " mast has annotation @CssQuery.");
        }
        String cssQueryValue = annotation.value();
        Elements elements = doc.select(cssQueryValue);
        List<T> valueList = new ArrayList<>();
        for (Element element : elements) {
            valueList.add(readValue(element, clazz));
        }
        return valueList;
    }

}
