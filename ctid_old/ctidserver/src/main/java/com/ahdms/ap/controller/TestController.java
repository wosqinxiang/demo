package com.ahdms.ap.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.common.HttpResponseBody;
import com.ahdms.ap.utils.HttpRequestUtils;
import com.ahdms.ap.utils.JsonUtils;
import com.ahdms.ap.vo.AuthResponseVo;
import com.ahdms.ap.vo.OfflineParamVO;
import com.ahdms.ap.vo.WxMiniProgramVo;
import com.ahdms.ctidservice.util.WxFaceApiUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

//允许跨域访问
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Configuration
@RequestMapping(value = "/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Value("${wx.mini.program.appId}")
    private String appid;
    @Value("${wx.mini.program.secretKey}")
    private String secretKey;
    @Value("${transfer.service.signUrl}")
    private String signUrl;

	/*private String appid = "wx894c4d9d019dbd9a";
	private String secretKey = "7c44229b55bedd0596f097618614dd2d";*/
    
    @Autowired
    private WxFaceApiUtils wx;

    @RequestMapping(value = "/wxLoginAuth", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "wxLoginAuth", notes = "wxLoginAuth")
    public WxMiniProgramVo wxSmallRoutineLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WxMiniProgramVo ret = new WxMiniProgramVo();
        //System.out.println("appid: "+appid);
        //System.out.println("secretKey: "+secretKey);
        Map<String, Object> map = new HashMap<>();

        //拿到微信小程序传过来的code
        String code = request.getParameter("code");
        logger.debug("code: {}", code);
        if (code == null || "".equals(code)) {
            return null;
        } else {
            logger.debug("appid: {}", appid);
            logger.debug("secretKey: {}", secretKey);
            // 发送http请求
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secretKey
                    + "&js_code=" + code + "&grant_type=authorization_code";   //接口地址
            logger.debug("url: {}", url);
            String results = HttpRequestUtils.sendGetReq(url);
            logger.debug("results: {}", results);
            if (results == null || "".equals(results)) {
                logger.info("网络超时");
                return null;
            } else {
                JSONObject res = JSONObject.fromObject(results);
                //把信息封装到map
                map = JsonUtils.parseJSON2Map(res);
                logger.debug("map: {}", map);
                if (map.containsKey("errcode")) {
                    String errcode = map.get("errcode").toString();
                    logger.info("微信返回的错误码 {}", errcode);
                    return null;
                } else if (map.containsKey("session_key")) {
                    logger.info("调用微信成功");
                    ret.setOpenid(map.get("openid").toString());
                    ret.setSessionKey(map.get("session_key").toString());
                }
            }
        }
        return ret;
    }

    /**
     * 获取用户信息的地址
     * 传递参数：access_token, openid
     * 返回参数：openid, nickname, sex, province, city, country, headimgurl, privilege, unionid
     */
    private static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    @RequestMapping(value = "/getUserInfoByOpenId", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "getUserInfoByOpenId", notes = "getUserInfoByOpenId")
    public HttpResponseBody getUserInfoByOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {


       /* try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("appid", ShareConstants.WX_ID);
            params.put("secret", ShareConstants.WX_SECRET);
            params.put("code", code);
            params.put("grant_type", "authorization_code");

            String jsonResult = getJsonResultByUrlPath(context, TOKEN_URL, params);
            JSONObject jsonObject = new JSONObject(jsonResult);
            String accessToken = jsonObject.optString("access_token", "");
            String openid = jsonObject.optString("openid", "");

            if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(openid)) {
                params.clear();
                params.put("access_token", accessToken);
                params.put("openid", openid);
                String userInfo = getJsonResultByUrlPath(context, USERINFO_URL, params);
                if (listener != null && !TextUtils.isEmpty(userInfo)) {
                    listener.onComplete(ParseToMap.parse(userInfo, ParseToMap.TYPE_WECHAT));
                }
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return null;
    }

    /**
          * 网络请求 使用了第三方jar包datadroid
          */
    /*private String getJsonResultByUrlPath(Context context, String urlPath,
                                          HashMap<String, String> params) throws ConnectionException {

        String jsonResult = "";

        NetworkConnection networkConnection = new NetworkConnection(context,
                urlPath);
        if (params != null) {
            networkConnection.setParameters(params);
        }

        ConnectionResult connectionResult = null;
        connectionResult = networkConnection.execute();
        jsonResult = connectionResult.body;
        Log.i(TAG, "urlPath " + urlPath + " params " + params
                + " jsonResult " + jsonResult);
        return jsonResult;
    }*/

    @RequestMapping(value = "/getSerialNum", method = {RequestMethod.GET})
    @ApiOperation(value = "getSerialNum", notes = "在线认证，后台产生流水号")
    public HttpResponseBody getSerialNum() {
        // 流水号长度：24字节，48个字符   规则：1个字节设备类型+12个字节设备号+11字节位随机数
        // 将流水号返回给前端

        HttpResponseBody ret = new HttpResponseBody();
        StringBuilder stringBuilder = new StringBuilder();
        String result = stringBuilder.append("01").append(getRandomString(30)).toString();
        ret.setData(result);
        ret.setMessage("在线认证，后台产生流水号");
        return ret;
    }

    /**
     * 返回长度为【strLength】的随机数，在前面补0
     */
    /*private static String getFixLenthString(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }*/

    private String getRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    /*public static void main(String[] args) throws Exception{
            System.out.println(getRandomString(30));
    }*/

    /*@RequestMapping(value = "/checkUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "checkUserInfo", notes = "checkUserInfo")
    public HttpResponseBody checkUserInfo() {
        // 接收前端（小程序）传参：openID，根据openID查询对应的用户信息（姓名，身份证号码）
        // 将查询结果返回给前端

        return null;
    }*/

   /* @RequestMapping(value = "/offlineVerify", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "offlineVerify", notes = "offlineVerify")
    public HttpResponseBody offlineVerify() {
        // 接收前端（小程序）传参：姓名，身份证，人像信息，openID，业务流水号，回调地址，设备账号，设备密码

        // 查询对应openID的用户信息
        // 判断用户是否存在。存在，则进行下一步；不存在，则保存至数据库，再进行下一步

        // 判断设备权限？ 查询调用次数，超过次数，则终止验证

        // 调用网证接口验证个人信息+人像，保存业务信息？

        // 将用户姓名，身份证号码，业务流水号，签名值返回给前端

        return null;
    }*/

    @RequestMapping(value = "/onlineVerify", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "onlineVerify", notes = "onlineVerify")
    public HttpResponseBody onlineVerify() {
        HttpResponseBody hb = new HttpResponseBody<>();
        try {
            onlineVerify("刘彻", "430581198711243511",
                    "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",
                    1, "", "");
        } catch (Exception e) {
            hb.setCode(Contents.RETURN_CODE_ERROR);
            hb.setMessage(e.getMessage());
            return hb;
        }
        hb.setCode(Contents.RETURN_CODE_SUCCESS);
        return hb;
    }

    public void onlineVerify(String name, String iDcard, String facePic, int type, String openID, String serialNum)
            throws Exception {
        // t1:查询对应序列号的服务账号信息
        /*AuthBiz ab = abdao.queryBySerial(serialNum);
        if (null == ab) {
            throw new Exception("账户信息有误。");
        }
        ServerAccount account = serverDao.selectByName(ab.getServerAccount());*/
        // t2:查询二项信息+openID是否存在，不存在则保存
        /*PersonInfo person = personDao.selectByOpenID(openID);
        if (null == person) {
            person = new PersonInfo(UUIDGenerator.getSerialId(), type, serialNum, new Date(), iDcard, null, name, null,
                    1, facePic);
            personDao.insertSelective(person);
        }*/
        // t3:调取网证接口验证
        if (1 == 1) {
            // 网证服务返回该用户有网证信息
            /*person.setIsCtid(0);
            personDao.updateByPrimaryKeySelective(person);*/
        }

        // t4:保存业务信息
        /*AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, iDcard,
                Contents.AUTH_TYPE_ONLINE, 1, Contents.AUTH_RESULT_TRUE, account.getServerDesc(), new Date(), serialNum,
                facePic, null, serialNum);*/
        //recordDao.insertSelective(ar);
        // t5:将结果通过回调地址返回
        /*OnlineVO vo = new OnlineVO();
        vo.setAuthResult(0);
        vo.setSerialNum(ar.getSerialNum());
        vo.setIdcard(iDcard);
        vo.setName(name);
        vo.setPic(facePic);
        vo.setVerifyDate(DateUtils.format(ar.getCreateTime()));
        String result = JSONObject.fromObject(vo).toString();*/
        //httpRequest(ab.getUrl(), "POST", result);
    }

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping(value = "/signByTs", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "signByTs", notes = "signByTs")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "Name", value = "姓名", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "IDcard", value = "身份证", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "serialNum", value = "业务流水号", required = true, dataType = "string")})
    public HttpResponseBody signByTs(@RequestParam("Name") String name,
                        @RequestParam("IDcard") String iDcard, @RequestParam("serialNum") String serialNum) {
        //HttpResponseBody ret = new HttpResponseBody();
        //Map map = new HashMap();
        //String url = "http://localhost:8687/signBySKID";
        /*String email = "test@hhui.top";
        String nick = "一灰灰Blog";*/
        OfflineParamVO opVo = new OfflineParamVO();
        opVo.setIdNum(iDcard);
        opVo.setName(name);
        opVo.setSerialNum(serialNum);

        /*MultiValueMap<String, OfflineParamVO> request = new LinkedMultiValueMap<>();
        request.add("opVo", opVo);

        // 使用方法三
        URI uri = restTemplate.postForLocation(url, request);
        System.out.println(uri);*/

        /**
         * exchange( )方法
         *
         * public <T> ResponseEntity<T>
         *
         *            exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) 
         *
         *     url:请求地址，如果需要在url中带参数，使用String.format()，用%s拼接即可;
         *     method:请求方式，HttpMethod是一个枚举类型，有GET、POST、DELETE等方法;
         *     requestEntity:请求参数，这个主要是POST等有请求体的方法才需要，GET、DELETE等方法可以为null;
         *     responseType:返回数据类型。
         *SignResponseVo
         */
        HttpEntity<OfflineParamVO> entity = new HttpEntity<>(opVo);
        ResponseEntity<HttpResponseBody> responseEntity = restTemplate.exchange(signUrl, HttpMethod.POST, entity, HttpResponseBody.class);

        if (responseEntity.getStatusCode()==HttpStatus.OK){
            /*logger.debug("Data {}",responseEntity.getBody().getData());
            logger.debug("Data {}",responseEntity.getBody().getMessage());
            logger.debug("Data {}",responseEntity.getBody().getCode());*/

            HttpResponseBody<LinkedHashMap> responseBody = responseEntity.getBody();

            LinkedHashMap data = responseBody.getData();

            //System.out.println("======="+data.get("authResult"));
            System.out.println(data.get("twoBarCodeData").toString());

            System.out.println("Data {}"+responseEntity.getBody().getData());
            System.out.println("Message {}"+responseEntity.getBody().getMessage());
            System.out.println("Code {}"+responseEntity.getBody().getCode());
            return responseEntity.getBody();
        }
        return null;
    }

    /*private String postByDefault()
    {
        *//*ExpressionDomain expressionDomain=new ExpressionDomain("hello","hasaki","win");
        JSONObject jsonObj = (JSONObject) JSONObject.toJSON(expressionDomain);*//*

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        //请求体
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);

        //发起请求
        String jsonResult = restTemplate.postForObject("http://localhost:8081/findDataByReflection" , formEntity, String.class);
return jsonResult;
        //将Json字符串解析成对象
        //Response resp = JSON.parseObject(jsonResult, new TypeReference<Response>() {});
    }*/

    @RequestMapping(value = "/testAuthByCtid", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "testAuthByCtid", notes = "testAuthByCtid")
    public HttpResponseBody testAuthByCtid() {

        /*try {
            JsonUtils.json2Bean("{}",AuthResponseVo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //ResponseEntity<HttpResponseBody> responseEntity = restTemplate.postForEntity("http://172.16.0.116:9096/api/auth", aVo, HttpResponseBody.class);
        String cardName = "刘彻";
        String cartNum = "430581198711243511";
        String photoData = "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";
        String authUrl = "http://172.16.0.116:9096/api/auth";
        ResponseEntity<HttpResponseBody> responseEntity = authByCtid(cardName, cartNum, photoData, authUrl);


        HttpResponseBody<LinkedHashMap> responseBodyAuth = responseEntity.getBody();
        int authCode = responseBodyAuth.getCode();
        System.out.println(authCode);
        LinkedHashMap authData = responseBodyAuth.getData();
        System.out.println(authData);
        String authMode = authData.get("authMode").toString();
        System.out.println(authMode);
        String authResult = authData.get("authResult").toString();
        System.out.println(authResult);
        String authDesc = authData.get("authDesc").toString();
        System.out.println(authDesc);
        return responseBodyAuth;
    }

    private ResponseEntity<HttpResponseBody> authByCtid(String cardName, String cartNum, String photoData, String authUrl) {
        AuthResponseVo aVo = new AuthResponseVo();
        HttpEntity<AuthResponseVo> entity = new HttpEntity<>(aVo);
        aVo.setCardName(cardName);
        aVo.setCardNum(cartNum);
        aVo.setPhotoData(photoData);

        return restTemplate.exchange(authUrl, HttpMethod.POST, entity, HttpResponseBody.class);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        //连接redis服务器，localhost:6379
        Jedis redis = new Jedis("172.16.1.105", 7002);
        // 获取所有key
        Set<byte[]> keySet = redis.keys("*".getBytes());
        byte[][] keys = keySet.toArray(new byte[keySet.size()][]);
        // 获取所有value
        byte[][] values = redis.mget(keys).toArray(new byte[keySet.size()][]);

        // 打印key-value对
        for (int i = 0; i < keySet.size(); ++i) {
            System.out.println(byte2hex(keys[i]) + " --- " + byte2hex(values[i]));
        }

        long end = System.currentTimeMillis();
        // 计算耗时
        System.out.println("Query " + values.length + " pairs takes " + (end - start) + " millis");
        redis.close();
    }

    private static String byte2hex(byte[] buffer) {
        String h = "0x";

        for (byte aBuffer : buffer) {
            String temp = Integer.toHexString(aBuffer & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }

        return h;

    }
    
    @RequestMapping(value = "/getVideo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "onlineVerify", notes = "onlineVerify")
    public HttpResponseBody<String> getVideo(@RequestParam String verifyResult) {
        HttpResponseBody<String> hb = new HttpResponseBody<>(); 
        String s = wx.getVideo(verifyResult);
        hb.setData(s);
        hb.setCode(Contents.RETURN_CODE_SUCCESS);
        return hb;
    }

//    @RequestMapping(value = "/exportExcel",headers="Accept=application/vnd.ms-excel", method = {RequestMethod.GET, RequestMethod.POST})
////    @PostMapping(value="/downLoadExcel",headers="Accept=application/vnd.ms-excel")
//    @ApiOperation(value = "exportExcel", notes = "exportExcel")
//    public HttpResponseBody<String> exportExcel(HttpServletRequest req, HttpServletResponse resp) {
//        HttpResponseBody<String> hb = new HttpResponseBody<>(); 
//        hb.setCode(Contents.RETURN_CODE_SUCCESS);
//        
//    List<Product> list = new ArrayList<Product>();
//
//    for (int i = 0 ; i<10 ; i++) {
//        //组装测试数据
//        Product product = new Product();
//        product.setName("爱奇艺会员"+i);
//        product.setPrice(9.99);
//        product.setDate(new Date());
//        list.add(product);
//    }
//    OutputStream out = null;
//    try {
//        out = resp.getOutputStream();
//    } catch (IOException e1) {
//        // TODO Auto-generated catch block
//        e1.printStackTrace();
//    }
//    
//    try {
//        ReportExcel reportExcel = new ReportExcel();
//        ExcelUtil excel = new ExcelUtil();
//        String title = "包头市工业企业疫情防控信息统计表";
////        reportExcel.excelExport(list,"测试",Product.class,1,resp,req);
//        HSSFWorkbook hss = new HSSFWorkbook();
//        hss = excel.exportExcel(list,title,Product.class,0);
//        
//        String fileName = URLEncoder.encode(title,"UTF-8");
//          resp.reset();
//          String disposition = "attachment;filename=";
//          if(req!=null&&req.getHeader("USER-AGENT")!=null&& StringUtils.contains(req.getHeader("USER-AGENT"), "Firefox")){
//              disposition += new String((title+".xls").getBytes(),"ISO8859-1");
//          }else{
//              disposition += URLEncoder.encode(title+".xls", "UTF-8");
//          }
//
//          resp.setContentType("application/vnd.ms-excel;charset=UTF-8");
//          resp.setHeader("Content-disposition", disposition);
//      hss.write(out);
//      
//    } catch (IOException e) {
//        e.printStackTrace();
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        try {
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    return hb;
//    }
    
    
}
