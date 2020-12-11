package com.ahdms.framework.http.request;

import com.ahdms.framework.http.response.HttpResponse;
import com.ahdms.framework.http.async.AsyncCall;
import okhttp3.FormBody;

import java.util.Map;

/**
 * 表单构造器
 *
 * @author Katrel.zhou
 */
public class FormBuilder {
    private final HttpRequest request;
    private final FormBody.Builder formBuilder;

    FormBuilder(HttpRequest request) {
        this.request = request;
        this.formBuilder = new FormBody.Builder();
    }

    public FormBuilder add(String name, Object value) {
        this.formBuilder.add(name, HttpRequest.handleValue(value));
        return this;
    }

    public FormBuilder addEncoded(String name, Object encodedValue) {
        this.formBuilder.addEncoded(name, HttpRequest.handleValue(encodedValue));
        return this;
    }

    public FormBuilder addMap(Map<String, Object> formMap) {
        if (formMap != null && !formMap.isEmpty()) {
            formMap.forEach(this::add);
        }
        return this;
    }

    public HttpRequest build() {
        FormBody formBody = formBuilder.build();
        this.request.form(formBody);
        return this.request;
    }

    public HttpResponse execute() {
        return this.build().execute();
    }

    public AsyncCall async() {
        return this.build().async();
    }
}
