package com.ahdms.framework.http.async;

import com.ahdms.framework.http.response.HttpResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

/**
 * 异步处理
 *
 * @author Katrel.zhou
 */
public class AsyncCallback implements Callback {
    private final AsyncCall asyncCall;

    AsyncCallback(AsyncCall asyncCall) {
        this.asyncCall = asyncCall;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        asyncCall.onFailure(call.request(), e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        HttpResponse httpResponse = new HttpResponse(response);
        asyncCall.onResponse(httpResponse);
        if (response.isSuccessful()) {
            asyncCall.onSuccess(httpResponse);
        }
    }

}
