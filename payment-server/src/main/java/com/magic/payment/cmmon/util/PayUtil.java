package com.magic.payment.cmmon.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayUtil {

    /**
     * 根据年月日和时间毫秒值生成订单号
     *
     * @return 订单号
     */
    public static String createOrderNo() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date) + System.currentTimeMillis();
    }

    /**
     * 根据字典序生成签名串
     *
     * @param paramMap     参数集合
     * @param ignoreParams 不参与签名的参数名
     * @return 签名
     */
    public static String createSignByLexicographicalOrder(Map<String, String> paramMap, String... ignoreParams) {
        return createSignByLexicographicalOrder(null,paramMap,  ignoreParams);
    }

    /**
     * 根据字典序生成签名串
     *
     * @param paramMap     参数集合
     * @param key          三方平台提供的key，用于生成签名，具体逻辑请参考三方平台
     * @param ignoreParams 不参与签名的参数名
     * @return 签名
     */
    public static String createSignByLexicographicalOrder( String key,Map<String, String> paramMap, String... ignoreParams) {
        paramMap = new TreeMap<>(paramMap);
        StringBuilder signBuilder = new StringBuilder();
        if (MapUtil.isNotEmpty(paramMap)) {
            if (ignoreParams != null && ignoreParams.length > 0) {
                for (String ignoreParam : ignoreParams) {
                    paramMap.remove(ignoreParam);
                }
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (StrUtil.isNotBlank(entry.getValue())) {
                    signBuilder.append(entry.getKey());
                    signBuilder.append("=");
                    signBuilder.append(entry.getValue());
                    signBuilder.append("&");
                }
            }
            if (StrUtil.isNotBlank(key)) {
                signBuilder.append("key");
                signBuilder.append("=");
                signBuilder.append(key);
            } else {
                signBuilder.delete(signBuilder.length() - 1, signBuilder.length());
            }
        }
        return SecureUtil.md5(signBuilder.toString());
    }

    /**
     * 按照指定参数顺序签名（put时需要按照第三方api要求的参数顺序进行put）
     * @param key 商户私钥
     * @param paramMap 参数Map(此处使用LinkedHashMap,linkedHashMap是按照put顺序存储的)
     * @param ignoreParams 签名忽略字段
     * @return java.lang.String MD5加密串
     */
    public static String createSignByParamMapOrder( String key,Map<String, String> paramMap, String... ignoreParams) {
        paramMap=new LinkedHashMap<>(paramMap);
        StringBuilder signBuilder = new StringBuilder();
        if (MapUtil.isNotEmpty(paramMap)) {
            if (ignoreParams != null && ignoreParams.length > 0) {
                for (String ignoreParam : ignoreParams) {
                    paramMap.remove(ignoreParam);
                }
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (StrUtil.isNotBlank(entry.getValue())) {
                    signBuilder.append(entry.getKey());
                    signBuilder.append("=");
                    signBuilder.append(entry.getValue());
                    signBuilder.append("&");
                }
            }
            if (StrUtil.isNotBlank(key)) {
                signBuilder.append("key");
                signBuilder.append("=");
                signBuilder.append(key);
            } else {
                signBuilder.delete(signBuilder.length() - 1, signBuilder.length());
            }
        }
        return SecureUtil.md5(signBuilder.toString());
    }


    /**
     * 签名函数
     * @param key 商户私钥
     * @param valueMark 参数拼接符
     * @param keyMark 参数串与 key之间的拼接符
     * @param paramMap 参数Map
     * @param ignoreParams 不加入签名的paramMap中的key
     * @return MD5签名串
     */
    public static String createSignByLexicographicalOrderV2(String key,String valueMark,String keyMark,Map<String, String> paramMap,String... ignoreParams){
        paramMap = new TreeMap<>(paramMap);
        StringBuilder signBuilder = new StringBuilder();
        valueMark=Optional.ofNullable(valueMark).orElse("");
        if (MapUtil.isNotEmpty(paramMap)) {
            if (ignoreParams != null && ignoreParams.length > 0) {
                for (String ignoreParam : ignoreParams) {
                    paramMap.remove(ignoreParam);
                }
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (StrUtil.isNotBlank(entry.getValue())) {
                    signBuilder.append(entry.getKey());
                    signBuilder.append("=");
                    signBuilder.append(entry.getValue());
                    signBuilder.append(valueMark);
                }
            }
            if(!"".equals(valueMark)){
                signBuilder.delete(signBuilder.length() - 1, signBuilder.length());
            }
            if (StrUtil.isNotBlank(key)) {
                signBuilder.append(Optional.ofNullable(keyMark).orElse("")).append(key);
            }
        }
        return SecureUtil.md5(signBuilder.toString());
    }



    /**
     * 自定义参数顺序生成签名
     *
     * @param paramMap    参数集合
     * @param key         三方平台提供的key，用于生成签名，具体逻辑请参考三方平台
     * @param paramsOrder 参与签名的参数，填写顺序为签名加密参数的顺序
     * @return 签名
     */
    public static String createSignByCustomOrder(String key, JSONObject paramMap, String... paramsOrder) {
        StringBuilder signBuilder = new StringBuilder();
        if (MapUtil.isNotEmpty(paramMap)) {
            if (paramsOrder != null && paramsOrder.length > 0) {
                for (String paramName : paramsOrder) {
                    if (StrUtil.isNotBlank(Convert.toStr(paramMap.get(paramName)))) {
                        signBuilder.append(paramName);
                        signBuilder.append("=");
                        signBuilder.append(paramMap.get(paramName));
                        signBuilder.append("&");
                    }
                }
                if (StrUtil.isNotBlank(key)) {
                    signBuilder.append("key");
                    signBuilder.append("=");
                    signBuilder.append(key);
                } else {
                    signBuilder.delete(signBuilder.length() - 1, signBuilder.length());
                }
            }
        }
        return SecureUtil.md5(signBuilder.toString());
    }

    /**
     * 自定义参数顺序生成签名
     *
     * @param paramMap    参数集合
     * @param key         三方平台提供的key，用于生成签名，具体逻辑请参考三方平台
     * @param paramsOrder 参与签名的参数，填写顺序为签名加密参数的顺序
     * @return 签名
     */
    public static String createSignByCustomOrderV2(String key,String mark,JSONObject paramMap, String... paramsOrder) {
        StringBuilder signBuilder = new StringBuilder();
        boolean markEmpty=(mark==null||"".equals(mark));
        if (MapUtil.isNotEmpty(paramMap)) {
            if (paramsOrder != null && paramsOrder.length > 0) {
                for (String paramName : paramsOrder) {
                    if (StrUtil.isNotBlank(Convert.toStr(paramMap.get(paramName)))) {
                        signBuilder.append(paramName);
                        signBuilder.append("=");
                        signBuilder.append(paramMap.get(paramName));
                        if(StringUtils.isNotBlank(mark)){
                            signBuilder.append(paramMap.get(mark));
                        }
                    }
                }
                if (StrUtil.isNotBlank(key)) {
                    signBuilder.append(key);
                } else if(!markEmpty){
                    signBuilder.delete(signBuilder.length() - 1, signBuilder.length());
                }
            }
        }
        return SecureUtil.md5(signBuilder.toString());
    }


    public static String splicePayUrlParams(JSONObject params){
        Set<Map.Entry<String,Object>> paramSet=params.entrySet();
        StringBuilder urlParamsBuilder = new StringBuilder();
        for (Map.Entry<String,Object> entry:paramSet) {
            urlParamsBuilder.append(entry.getKey());
            urlParamsBuilder.append("=");
            urlParamsBuilder.append(entry.getValue());
            urlParamsBuilder.append("&");
        }
        return urlParamsBuilder.delete(urlParamsBuilder.length()-1,urlParamsBuilder.length()).toString();
    }


    /**
     * 费率金额计算
     * @param rechargeAmount 实充金额
     * @param rate 费率
     * @return {@link java.lang.Double} 费率金额
     */
    public static Double rateAmountCompute(Double rechargeAmount,Double rate){
        if(null==rechargeAmount){
            return null;
        }
        return Optional.ofNullable(rate).map((r)->(new BigDecimal(Double.toString(rechargeAmount)).multiply(new BigDecimal(String.valueOf(r)))).doubleValue()).orElse(0d);
    }
}
