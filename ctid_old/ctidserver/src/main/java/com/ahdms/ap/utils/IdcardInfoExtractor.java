package com.ahdms.ap.utils;

import java.text.SimpleDateFormat;   
import java.util.Calendar;   
import java.util.Date;   
import java.util.GregorianCalendar;   
import java.util.Set;

  
/**  
 *         <p>  
 *         类说明:提取身份证相关信息  
 *         </p>  
 */  
public class IdcardInfoExtractor {   
    // 省份   
    private String province;   
    // 城市   
    private String city;   
    // 区县   
    private String region;   
    // 年份   
    private int year;   
    // 月份   
    private int month;   
    // 日期   
    private int day;   
    // 性别   
    private String gender;   
    // 出生日期   
    private Date birthday;
    // 出生日期字符串
    private String birthdayStr;
    
       
    private IdcardValidator validator = null;   
       
    /**  
     * 通过构造方法初始化各个成员属性  
     */  
    public IdcardInfoExtractor(String idcard) {   
        try {   
            validator = new IdcardValidator();
            if(!validator.isValidatedAllIdcard(idcard)) {
            	throw new RuntimeException("身份证校验不通过！");
            }
            if (idcard.length() == 15) {   
                idcard = validator.convertIdcarBy15bit(idcard);   
            }   
            // 获取省份   
            String provinceId = idcard.substring(0, 2);   
            Set<String> key = AreaData.provinceCodeMap.keySet();   
            for (String id : key) {   
                if (id.equals(provinceId)) {   
                    this.province = AreaData.provinceCodeMap.get(id);   
                    break;   
                }   
            }  
            
            String cityId = idcard.substring(0, 4);   
            key = AreaData.cityCodeMap.keySet();   
            for (String id : key) {   
                if (id.equals(cityId)) {   
                    this.city = AreaData.cityCodeMap.get(id);   
                    break;   
                }   
            }
            
            String regionId = idcard.substring(0, 6);   
            key = AreaData.regionCodeMap.keySet();   
            for (String id : key) {   
                if (id.equals(regionId)) {   
                    this.region = AreaData.regionCodeMap.get(id);   
                    break;   
                }   
            }

            // 获取性别   
            String id17 = idcard.substring(16, 17);   
            if (Integer.parseInt(id17) % 2 != 0) {   
                this.gender = "男";   
            } else {   
                this.gender = "女";   
            }   

            // 获取出生日期   
            String birthday = idcard.substring(6, 14);   
            Date birthdate = new SimpleDateFormat("yyyyMMdd")   
                    .parse(birthday);
            this.birthday = birthdate; 
            this.birthdayStr = new SimpleDateFormat("yyyy-MM-dd").format(birthdate);
            GregorianCalendar currentDay = new GregorianCalendar();   
            currentDay.setTime(birthdate);   
            this.year = currentDay.get(Calendar.YEAR);   
            this.month = currentDay.get(Calendar.MONTH) + 1;   
            this.day = currentDay.get(Calendar.DAY_OF_MONTH);     
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * @return the province  
     */  
    public String getProvince() {   
        return province;   
    }   
  
    /**  
     * @return the city  
     */  
    public String getCity() {   
        return city;   
    }   
  
    /**  
     * @return the region  
     */  
    public String getRegion() {   
        return region;   
    }   
  
    /**  
     * @return the year  
     */  
    public int getYear() {   
        return year;   
    }   
  
    /**  
     * @return the month  
     */  
    public int getMonth() {   
        return month;   
    }   
  
    /**  
     * @return the day  
     */  
    public int getDay() {   
        return day;   
    }   
  
    /**  
     * @return the gender  
     */  
    public String getGender() {   
        return gender;   
    }   
  
    /**  
     * @return the birthday  
     */  
    public Date getBirthday() {   
        return birthday;   
    }  
    
    
  
    public String getBirthdayStr() {
		return birthdayStr;
	}

	@Override  
    public String toString() {   
        return "省份：" + this.province + "|市：" + this.city + "|区县：" + this.region + ",|性别：" + this.gender + ",|出生日期："  
                + this.birthdayStr;   
    }   
  
    public static void main(String[] args) {   
        String idcard = "420521198805301841";   
        IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);   
        System.out.println(ie.toString());   
    }   
}  