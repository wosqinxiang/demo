package com.ahdms.framework.http.interceptor;

import com.ahdms.framework.http.response.HttpResponse;
import com.ahdms.framework.http.response.ResponseSpec;
import lombok.AllArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * 重试拦截器，应对代理问题
 *
 * @author Katrel.zhou
 */

public class RetryInterceptor implements Interceptor {
	/**
	 * 重试次数，默认3
	 */
	private final int maxAttempts;
	/**
	 * 重试间隔/ms，默认1000ms
	 */
	private final long backOffPeriod;
	private final Predicate<ResponseSpec> predicate;

	public RetryInterceptor() {
		this(null);
	}

	public RetryInterceptor(int maxAttempts, long backOffPeriod) {
		this(maxAttempts, backOffPeriod, null);
	}

	public RetryInterceptor(Predicate<ResponseSpec> predicate) {
		this(SimpleRetryPolicy.DEFAULT_MAX_ATTEMPTS, 1000L, predicate);
	}

	public RetryInterceptor(int maxAttempts, long backOffPeriod, Predicate<ResponseSpec> predicate) {
		this.maxAttempts = maxAttempts;
		this.backOffPeriod = backOffPeriod;
		this.predicate = predicate;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		RetryTemplate template = createRetryTemplate();
		return template.execute(context -> {
			Response response = chain.proceed(request);
			if (predicate == null) {
				return response;
			}
			// copy 一份 body
			ResponseBody body = response.peekBody(Long.MAX_VALUE);
			try (HttpResponse httpResponse = new HttpResponse(response)) {
				if (predicate.test(httpResponse)) {
					throw new IOException("Http retry test Failure.");
				}
			}
			return response.newBuilder().body(body).build();
		});
	}

	private RetryTemplate createRetryTemplate() {
		RetryTemplate template = new RetryTemplate();
		// 重试策略
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts);
		// 设置间隔策略
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(backOffPeriod);
		template.setRetryPolicy(retryPolicy);
		template.setBackOffPolicy(backOffPolicy);
		return template;
	}
}
