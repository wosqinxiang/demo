package com.ahdms.framework.core.commom.util;


import lombok.experimental.UtilityClass;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@UtilityClass
public class RuntimeUtils {
    private static volatile int pId = -1;
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 获得当前进程的PID
     * <p>
     * 当失败时返回-1
     *
     * @return pid
     */
    public static int getPId() {
        if (pId > 0) {
            return pId;
        }
        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf(CharPool.AT);
        if (index > 0) {
            pId = NumberUtils.toInt(jvmName.substring(0, index), -1);
            return pId;
        }
        return -1;
    }

    /**
     * 返回应用启动到现在的时间
     *
     * @return {Duration}
     */
    public static Duration getUpTime() {
        long upTime = ManagementFactory.getRuntimeMXBean().getUptime();
        return Duration.ofMillis(upTime);
    }

    /**
     * 返回输入的JVM参数列表
     *
     * @return jvm参数
     */
    public static String getJvmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, StringPool.SPACE);
    }

    /**
     * 获取CPU核数
     *
     * @return cpu count
     */
    public static int getCpuNum() {
        return CPU_NUM;
    }

}
