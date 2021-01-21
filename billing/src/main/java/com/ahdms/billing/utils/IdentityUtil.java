/**
 * Created on 2018年7月14日 by luotao
 */
package com.ahdms.billing.utils;

import java.util.Calendar;
import java.util.Date;


/**
 * @Title
 * @Description
 * @Copyright
 *            <p>
 *            Copyright (c) 2017
 *            </p>
 * @Company
 *          <p>
 *          迪曼森信息科技有限公司 Co., Ltd.
 *          </p>
 * @author luotao
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class IdentityUtil {


    /**
     * 获取截止日最大时间
     * 
     * @创建人 luotao
     * @创建时间 2018年9月7日 @创建目的【】 @修改目的【修改人：，修改时间：】
     * @param notBefore日期
     * @param effectMonth生效月份
     * @return
     */
    public static Date getMaxDateByMonth(Date notBefore, int effectMonth) {
        Calendar cal = Calendar.getInstance();// 下面的就是把当前日期加一个月
        cal.setTime(notBefore);
        if (effectMonth > 0)
            cal.add(Calendar.MONTH, effectMonth); // **expireMonth
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return cal.getTime();
    }

    /**
     * 获取截止日最大时间
     * 
     * @创建人 luotao
     * @创建时间 2018年9月7日 @创建目的【】 @修改目的【修改人：，修改时间：】
     * @param notBefore日期
     * @param effectYear生效年份
     * @return
     */
    public static Date getMaxDateByYear(Date notBefore, int effectYear) {
        Calendar cal = Calendar.getInstance();// 下面的就是把当前日期加一个月
        cal.setTime(notBefore);
        if (effectYear > 0)
            cal.add(Calendar.YEAR, effectYear); // **expireMonth
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return cal.getTime();
    }

    /**
     * 获取截止日最大时间
     * 
     * @创建人 luotao
     * @创建时间 2018年9月7日 @创建目的【】 @修改目的【修改人：，修改时间：】
     * @param notBefore日期
     * @param effectDay生效天数
     * @return
     */
    public static Date getMaxDateByDay(Date notBefore, int effectDay) {
        Calendar cal = Calendar.getInstance();// 下面的就是把当前日期加一个月
        cal.setTime(notBefore);
        if (effectDay > 0)
            cal.add(Calendar.DATE, effectDay); // **expireMonth
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return cal.getTime();
    }
}
