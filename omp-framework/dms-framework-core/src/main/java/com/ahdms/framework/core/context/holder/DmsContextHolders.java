package com.ahdms.framework.core.context.holder;

import com.ahdms.framework.core.context.DmsContext;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
public class DmsContextHolders {

	private static final ThreadLocal<DmsContext> contextLocal = new ThreadLocal<>();

	public static DmsContext getContext() {
		return contextLocal.get();
	}

	/**
	 * 为什么暴露给外界：让外界能在自启动线程中正确设定callcontext
	 */
	public static void setContext(DmsContext dmsContext) {
		if (dmsContext == null) {
			contextLocal.remove();
		} else {
			contextLocal.set(dmsContext);
		}
	}

	public static void removeContext() {
		contextLocal.remove();
	}

	public static <T> T getAttrByFunc(Function<DmsContext, T> supplier) {
		return Optional.ofNullable(getContext())
			.map(supplier)
			.orElse(null);
	}

}
