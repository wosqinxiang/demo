package com.ahdms.context.holder;

import com.ahdms.context.CtidContext;

import java.util.Optional;
import java.util.function.Function;

public class CtidContextHolder {

	private static final ThreadLocal<CtidContext> contextLocal = new ThreadLocal<>();

	public static CtidContext getContext() {
		return contextLocal.get();
	}

	/**
	 * 为什么暴露给外界：让外界能在自启动线程中正确设定callcontext
	 */
	public static void setContext(CtidContext ompContext) {
		if (ompContext == null) {
			contextLocal.remove();
		} else {
			contextLocal.set(ompContext);
		}
	}

	public static void removeContext() {
		contextLocal.remove();
	}

	public static <T> T getAttrByFunc(Function<CtidContext, T> supplier) {
		return Optional.ofNullable(getContext())
			.map(supplier)
			.orElse(null);
	}

}
