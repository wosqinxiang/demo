package com.ahdms.framework.http.dom;

import com.ahdms.framework.core.commom.util.ConvertUtils;
import com.ahdms.framework.core.commom.util.ReflectUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 代理模型
 *
 * @author Katrel.zhou
 */
@RequiredArgsConstructor
public class CssQueryMethodInterceptor implements MethodInterceptor {
    private final Class<?> clazz;
    private final Element element;

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 如果是 toString eq 等方法都不准确，故直接返回死值
        if (ReflectUtils.isObjectMethod(method)) {
            return methodProxy.invokeSuper(object, args);
        }
		// 非 bean 方法
		PropertyDescriptor propertyDescriptor = BeanUtils.findPropertyForMethod(method, clazz);
		if (propertyDescriptor == null) {
			return methodProxy.invokeSuper(object, args);
		}
		// 非 read 的方法，只处理 get 方法 is
		if (!method.equals(propertyDescriptor.getReadMethod())) {
			return methodProxy.invokeSuper(object, args);
		}
		// 兼容 lombok bug 强制首字母小写： https://github.com/rzwitserloot/lombok/issues/1861
		String fieldName = StringUtils.firstCharToLower(propertyDescriptor.getDisplayName());
		Field field = clazz.getDeclaredField(fieldName);
		if (field == null) {
			return methodProxy.invokeSuper(object, args);
		}
        CssQuery cssQuery = field.getAnnotation(CssQuery.class);
        // 没有注解，不代理
        if (cssQuery == null) {
            return methodProxy.invokeSuper(object, args);
        }
        Class<?> returnType = method.getReturnType();
        boolean isColl = Collection.class.isAssignableFrom(returnType);
        String cssQueryValue = cssQuery.value();
        // 是否为 bean 中 bean
        boolean isInner = cssQuery.inner();
        if (isInner) {
            return proxyInner(cssQueryValue, method, returnType, isColl);
        }
        Object proxyValue = proxyValue(cssQueryValue, cssQuery, returnType, isColl);
        if (String.class.isAssignableFrom(returnType)) {
            return proxyValue;
        }
        TypeDescriptor typeDescriptor = new TypeDescriptor(field);
        return ConvertUtils.convert(proxyValue, typeDescriptor);
    }

    private Object proxyInner(String cssQueryValue, Method method, Class<?> returnType, boolean isColl) {
        if (isColl) {
            Elements elements = Selector.select(cssQueryValue, element);
            Collection<Object> valueList = newColl(returnType);
            ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
            Class<?> innerType = resolvableType.getGeneric(0).resolve();
            for (Element select : elements) {
                valueList.add(DomMapper.readValue(select, innerType));
            }
            return valueList;
        }
        Element select = Selector.selectFirst(cssQueryValue, element);
        return DomMapper.readValue(select, returnType);
    }

    private Object proxyValue(String cssQueryValue, CssQuery cssQuery, Class<?> returnType, boolean isColl) {
        String attrName = cssQuery.attr();
        String regex = cssQuery.regex();
        int regexGroup = cssQuery.regexGroup();
        if (isColl) {
            Elements elements = Selector.select(cssQueryValue, element);
            Collection<Object> valueList = newColl(returnType);
            // 判断空
            if (elements.isEmpty()) {
                return valueList;
            }
            for (Element select : elements) {
                Object value = getValue(select, attrName, regex, regexGroup);
                if (value != null) {
                    valueList.add(value);
                }
            }
            return valueList;
        }
        Element select = Selector.selectFirst(cssQueryValue, element);
        return getValue(select, attrName, regex, regexGroup);
    }

    private String getValue(Element element, String attrName, String regex, int regexGroup) {
        if (element == null) {
            return null;
        }
        String attrValue;
        if (StringUtils.isBlank(attrName)) {
            attrValue = element.outerHtml();
        } else if ("html".equalsIgnoreCase(attrName)) {
            attrValue = element.html();
        } else if ("text".equalsIgnoreCase(attrName)) {
            attrValue = getText(element);
        } else if ("allText".equalsIgnoreCase(attrName)) {
            attrValue = element.text();
        } else {
            attrValue = element.attr(attrName);
        }
        if (StringUtils.isBlank(attrValue) || StringUtils.isBlank(regex)) {
            return attrValue;
        }
        return getRegexValue(regex, regexGroup, attrValue);
    }

    private String getRegexValue(String regex, int regexGroup, String value) {
        // 处理正则表达式
        Matcher matcher = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(value);
        if (!matcher.find()) {
            return null;
        }
        // 正则 group
        if (regexGroup > CssQuery.DEFAULT_REGEX_GROUP) {
            return matcher.group(regexGroup);
        }
        return matcher.group();
    }

    private String getText(Element element) {
        return element.childNodes().stream()
            .filter(node -> node instanceof TextNode)
            .map(node -> (TextNode) node)
            .map(TextNode::text)
            .collect(Collectors.joining());
    }

    private Collection<Object> newColl(Class<?> returnType) {
        return Set.class.isAssignableFrom(returnType) ? new HashSet<>() : new ArrayList<>();
    }

}
