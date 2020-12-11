package com.ahdms.bean;

import com.ahdms.framework.core.commom.util.JsonUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-10 16:10
 */
@Data
public class StandardExtMenu {

    private Integer id;
    private String extKey;
    private String name;
    private String oid;
    private Integer hasChild;
    private Integer parentId;
    private Integer isCritical;
    private Integer criticalValue;

    private Integer KeyValue;
    private List<StandardExtMenu> childMenu;


//    public static void main(String[] args) {
//        List<StandardExtMenu> defaultMenu = new ArrayList<>();
//
//        StandardExtMenu authorityKeyIdentifier = new StandardExtMenu();
//        authorityKeyIdentifier.setKey("authorityKeyIdentifier");
//        authorityKeyIdentifier.setName("颁发机构密钥标识符");
//        authorityKeyIdentifier.setCritical(true);
//        defaultMenu.add(authorityKeyIdentifier);
//
//        StandardExtMenu subjectKeyIdentifier = new StandardExtMenu();
//        subjectKeyIdentifier.setKey("subjectKeyIdentifier");
//        subjectKeyIdentifier.setName("主体密钥标识符");
//        subjectKeyIdentifier.setCritical(true);
//        defaultMenu.add(subjectKeyIdentifier);
//
//        StandardExtMenu authorityInfoAccess = new StandardExtMenu();
//        authorityInfoAccess.setKey("authorityInfoAccess");
//        authorityInfoAccess.setName("颁发机构信息访问");
//        authorityInfoAccess.setCritical(true);
//        defaultMenu.add(authorityInfoAccess);
//
//        StandardExtMenu cRLDistributionPoints = new StandardExtMenu();
//        cRLDistributionPoints.setKey("cRLDistributionPoints");
//        cRLDistributionPoints.setName("IRL发布点");
//        cRLDistributionPoints.setCritical(true);
//        defaultMenu.add(cRLDistributionPoints);
//
//        StandardExtMenu basicConstraints = new StandardExtMenu();
//        basicConstraints.setKey("basicConstraints");
//        basicConstraints.setName("基本限制");
//        basicConstraints.setCritical(true);
//        defaultMenu.add(basicConstraints);
//
//        StandardExtMenu keyUsage = new StandardExtMenu();
//        keyUsage.setKey("keyUsage");
//        keyUsage.setName("密钥用法");
//        List<StandardExtMenu> keyUsageChildMenu = getKeyUsageChildMenu();
//        keyUsage.setChildMenu(keyUsageChildMenu);
//        keyUsage.setHasChild(true);
//        keyUsage.setCritical(true);
//        defaultMenu.add(keyUsage);
//
//        StandardExtMenu extendedKeyUsage = new StandardExtMenu();
//        extendedKeyUsage.setKey("extendedKeyUsage");
//        extendedKeyUsage.setName("增强型密钥用法");
//        List<StandardExtMenu> extendedKeyUsageChildMenu = getExtendedKeyUsageChildMenu();
//        extendedKeyUsage.setHasChild(true);
//        extendedKeyUsage.setChildMenu(extendedKeyUsageChildMenu);
//        extendedKeyUsage.setCritical(true);
//        defaultMenu.add(extendedKeyUsage);
//
//        StandardExtMenu IdentifyCode = new StandardExtMenu();
//        IdentifyCode.setKey("IdentifyCode");
//        IdentifyCode.setName("个人身份标识码");
//        List<StandardExtMenu> identifyCodeChildMenu = getIdentifyCodeChildMenu();
//        IdentifyCode.setChildMenu(identifyCodeChildMenu);
//        IdentifyCode.setHasChild(true);
//        IdentifyCode.setCritical(true);
//        defaultMenu.add(IdentifyCode);
//
//        StandardExtMenu insuranceNumber = new StandardExtMenu();
//        insuranceNumber.setKey("insuranceNumber");
//        insuranceNumber.setName("个人社会保险号");
//        insuranceNumber.setCritical(true);
//        defaultMenu.add(insuranceNumber);
//
//        StandardExtMenu ICRegistrationNumber = new StandardExtMenu();
//        ICRegistrationNumber.setKey("ICRegistrationNumber");
//        ICRegistrationNumber.setName("企业组织机构代码");
//        ICRegistrationNumber.setCritical(true);
//        defaultMenu.add(ICRegistrationNumber);
//
//        StandardExtMenu OrganizationCode = new StandardExtMenu();
//        OrganizationCode.setKey("OrganizationCode");
//        OrganizationCode.setName("企业工商注册号");
//        OrganizationCode.setCritical(true);
//        defaultMenu.add(OrganizationCode);
//
//        StandardExtMenu TaxationNumeber = new StandardExtMenu();
//        TaxationNumeber.setKey("TaxationNumeber");
//        TaxationNumeber.setName("企业税号");
//        TaxationNumeber.setCritical(true);
//        defaultMenu.add(TaxationNumeber);
//
//        String s = JsonUtils.toJson(defaultMenu);
//        System.out.println(s);
//
//    }
//
//    public static List<StandardExtMenu> getKeyUsageChildMenu(){
//        List<StandardExtMenu> list = new ArrayList<>();
//        StandardExtMenu s1 = new StandardExtMenu();
//        s1.setKey("digitalSignature");
//        s1.setName("数字签名");
//        StandardExtMenu s2 = new StandardExtMenu();
//        s2.setKey("nonRepudiation");
//        s2.setName("不可否认");
//        StandardExtMenu s3 = new StandardExtMenu();
//        s3.setKey("keyEncipherment");
//        s3.setName("密钥加密");
//        StandardExtMenu s4 = new StandardExtMenu();
//        s4.setKey("dataEncipherment");
//        s4.setName("数据加密");
//
//        list.add(s1);
//        list.add(s2);
//        list.add(s3);
//        list.add(s4);
//        return list;
//    }
//
//    public static List<StandardExtMenu> getExtendedKeyUsageChildMenu(){
//        List<StandardExtMenu> list = new ArrayList<>();
//        StandardExtMenu s1 = new StandardExtMenu();
//        s1.setKey("serverAuth");
//        s1.setName("服务器验证");
//        StandardExtMenu s2 = new StandardExtMenu();
//        s2.setKey("clientAuth");
//        s2.setName("客户身份验证");
//        StandardExtMenu s3 = new StandardExtMenu();
//        s3.setKey("codeSigning");
//        s3.setName("代码签名");
//        StandardExtMenu s4 = new StandardExtMenu();
//        s4.setKey("emailProtection");
//        s4.setName("Email保护");
//
//        list.add(s1);
//        list.add(s2);
//        list.add(s3);
//        list.add(s4);
//        return list;
//    }
//
//    public static List<StandardExtMenu> getIdentifyCodeChildMenu(){
//        List<StandardExtMenu> list = new ArrayList<>();
//        StandardExtMenu s1 = new StandardExtMenu();
//        s1.setKey("residenterCardNumber");
//        s1.setName("居民身份证号码");
//        s1.setCritical(false);
//
//        StandardExtMenu s2 = new StandardExtMenu();
//        s2.setKey("militaryOfficerCardNumber");
//        s2.setName("军官证号码");
//        s2.setCritical(false);
//
//        StandardExtMenu s3 = new StandardExtMenu();
//        s3.setKey("passportNumber");
//        s3.setName("护照号码");
//        s3.setCritical(false);
//
//
//        list.add(s1);
//        list.add(s2);
//        list.add(s3);
//        return list;
//    }

}
