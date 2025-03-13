package com.tencent.wxcloudrun.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author Houcloud
 */
@Slf4j
@Getter
@Setter
@Component
public class IpUtil implements Serializable {
    @Serial
    private static final long serialVersionUID = -6246313701294144575L;
    private BigDecimal area;
    private String country;
    private String isp_id;
    private String queryIp;
    private String city;
    private String ip;
    private String isp;
    private String county;
    private String region_id;
    private String area_id;
    private String county_id;
    private String region;
    private String country_id;
    private String city_id;

    // 静态常量
    public static final String UNKNOWN = "unknown";
    public static final String IP = "0:0:0:0:0:0:0:1";
    public static final String BLANK_IP = "0.0.0.0";
    public static final String SP = ",";
    public static final String EP = ".";
    public static final String NGINX_CUSTOM_IP_KEY = "X-Real-IP";
    public static final String REQUEST_HEADER_KEY_CLIENT_REAL_IP = "client_real_ip";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HEADER_PORT = "Port";
    private static final String HEADER_HOST = "Host";

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "未知";
        }
    }

    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getParameter(NGINX_CUSTOM_IP_KEY);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(NGINX_CUSTOM_IP_KEY);
        } else {
            ip = BLANK_IP;
        }
        return ip;
    }

    public static void checkIpLog(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst(NGINX_CUSTOM_IP_KEY);
        log.info("NGINX_CUSTOM_IP_KEY:{}\n", ip);
        ip = request.getHeaders().getFirst(HTTP_X_FORWARDED_FOR);
        log.info("HTTP_X_FORWARDED_FOR:{}\n", ip);
        ip = request.getHeaders().getFirst(HEADER_PROXY_CLIENT_IP);
        log.info("HEADER_PROXY_CLIENT_IP:{}\n", ip);
        ip = request.getHeaders().getFirst(HTTP_X_FORWARDED_FOR);
        log.info("HTTP_X_FORWARDED_FOR:{}\n", ip);
        ip = request.getHeaders().getFirst(HEADER_WL_PROXY_CLIENT_IP);
        log.info("HEADER_WL_PROXY_CLIENT_IP:{}\n", ip);
        log.info("getRemoteAddress:{}\n", request.getRemoteAddress());
        log.info("HEADER_HOST:{}\n", request.getHeaders().getFirst(HEADER_HOST));
        log.info("HEADER_PORT:{}\n", request.getHeaders().getFirst(HEADER_PORT));
        log.info("getLocalAddress:{}\n", request.getLocalAddress());
        log.info("getHost:{}\n", request.getHeaders().getHost());

        log.info("{}\n", request.getURI().toString());

        log.info("{}\n", request.getMethod());
    }

    public static String getDomain(HttpServletRequest request) {
        String remoteHost = request.getRemoteHost();
        log.info("RemoterHost：「{}」", remoteHost);
        return remoteHost;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (checkIsIp(ip)) {
            return ip;
        }

        ip = request.getHeader("X-Real-IP");
        if (checkIsIp(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            //本地 localhost访问 ipv6
            ip = "127.0.0.1";
        }
        if (checkIsIp(ip)) {
            return ip;
        }

        return "";
    }

    /**
     * 检测是否为ip
     *
     * @param ip 参数
     * @return String
     * @author Houcloud
     * @since 2021-08-22
     */
    public static boolean checkIsIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }

        if ("unKnown".equals(ip)) {
            return false;
        }

        if ("unknown".equals(ip)) {
            return false;
        }

        return ip.split("\\.").length == 4;
    }


    /**
     * 通过 IP 获取地址 (淘宝接口)
     *
     * @param ip {@code String} 用户 IP 地址
     * @return {@code String} 用户地址
     */
    public static IpUtil getIpInfo(String ip) {
        if (LOCALHOST.equals(ip)) {
            ip = LOCALHOST;
        }
        try {
            String json = HttpUtil.get("https://ip.taobao.com/outGetIpInfo?accessKey=amap&ip=" + ip);
            System.out.println(json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            return jsonObject.getObject("data", IpUtil.class);
        } catch (Exception e) {
            log.error("通过淘宝接口查询IP信息失败", e);
            return null;
        }
    }

    /**
     * 通过 IP API 查询IP地址
     *
     * @param ip ipv4
     * @return 过滤的地区简称
     */
    public static String getIpAddress2(String ip) {
        if (LOCALHOST.equals(ip)) {
            ip = LOCALHOST;
        }
        try {
            String json = HttpUtil.get(String.format("http://ip-api.com/json/%s?lang=zh-CN", ip));
            System.out.println(json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            String regionName = jsonObject.getString("regionName");
            return filterProvince(regionName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static StringRedisTemplate stringRedisTemplate;


    @Resource
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        IpUtil.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 通过 IP API 查询IP地址，优先读取Redis缓存
     *
     * @param ip ipv4
     * @return 所有地区信息
     */
    public static IpLocation getIpLocation(String ip) {
        // 定义缓存键
        String cacheKey = "LOCATION_IP:" + ip;

        // 尝试从 Redis 缓存中读取
        String cachedLocation = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedLocation != null) {
            // 如果缓存存在，直接解析 JSON 并返回
            JSONObject cachedJson = JSONObject.parseObject(cachedLocation);
            IpLocation cachedIpLocation = parseIpLocation(cachedJson);
            cachedIpLocation.setIp(ip);
            return cachedIpLocation;
        }

        // IP 请求 API 获取数据
        IpLocation ipLocation = new IpLocation();
        ipLocation.setIp(ip);
        try {
            String json = HttpUtil.get(String.format("http://ip-api.com/json/%s?lang=zh-CN", ip));
            log.info("FETCHED IP API: {}", json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            // 解析并设置 IP 位置信息
            ipLocation = parseIpLocation(jsonObject);

            // 将结果缓存到 Redis 中，设置有效期（例如1小时）
            stringRedisTemplate.opsForValue().set(cacheKey, jsonObject.toJSONString(), Duration.ofHours(1));

        } catch (Exception e) {
            log.error("解析IP位置信息失败", e);
        }
        return ipLocation;
    }

    /**
     * 从 JSON 对象中解析 IpLocation 信息
     *
     * @param jsonObject IP位置 JSON 对象
     * @return IpLocation 实例
     */
    private static IpLocation parseIpLocation(JSONObject jsonObject) {
        IpLocation ipLocation = new IpLocation();
        ipLocation.setCountry(jsonObject.getString("country"));
        String province = jsonObject.getString("regionName");
        ipLocation.setProvince(province);
        ipLocation.setFilteredProvince(filterProvince(province));
        ipLocation.setCity(jsonObject.getString("city"));
        return ipLocation;
    }

    public static IpLocation getIpLocation(HttpServletRequest request) {
        String ip = getRealIp(request);
        return getIpLocation(ip);
    }

    public static final Map<String, String> ALL_PROVINCE_ABBREVIATIONS = new HashMap<>() {{
        put("北京市", "北京");
        put("天津市", "天津");
        put("河北省", "河北");
        put("山西省", "山西");
        put("内蒙古自治区", "内蒙古");
        put("辽宁省", "辽宁");
        put("吉林省", "吉林");
        put("黑龙江省", "黑龙江");
        put("上海市", "上海");
        put("江苏省", "江苏");
        put("浙江省", "浙江");
        put("安徽省", "安徽");
        put("福建省", "福建");
        put("江西省", "江西");
        put("山东省", "山东");
        put("河南省", "河南");
        put("湖北省", "湖北");
        put("湖南省", "湖南");
        put("广东省", "广东");
        put("广西壮族自治区", "广西");
        put("海南省", "海南");
        put("重庆市", "重庆");
        put("四川省", "四川");
        put("贵州省", "贵州");
        put("云南省", "云南");
        put("西藏自治区", "西藏");
        put("陕西省", "陕西");
        put("甘肃省", "甘肃");
        put("青海省", "青海");
        put("宁夏回族自治区", "宁夏");
        put("新疆维吾尔自治区", "新疆");
        put("香港特别行政区", "香港");
        put("澳门特别行政区", "澳门");
        put("台湾省", "台湾");
    }};

    public static String filterProvince(String province) {
        if (province == null || province.isEmpty()) {
            return "";
        }
        province = province.trim().replaceAll("\\s+", "");
        // 直接从 Map 中获取省份简称，若不存在则返回原字符串
        return ALL_PROVINCE_ABBREVIATIONS.getOrDefault(province, province);
    }


    public static void main(String[] args) {
        System.out.println(getIpAddress2("171.109.251.51"));
    }

    public static String getIpAddress(String ip) {
        IpUtil ipInfo = getIpInfo(ip);
        if (Objects.isNull(ipInfo)) {
            return "";
        }
        if (StrUtil.isNotBlank(ipInfo.getRegion()) && ipInfo.getRegion().equals("XX")) {
            return ipInfo.getRegion();
        }
        if (StrUtil.isNotBlank(ipInfo.getCounty())) {
            return ipInfo.getCountry();
        }
        return "";
    }

    /**
     * 获取真实客户端IP
     *
     * @param serverHttpRequest s
     * @return
     */
    public static String getRealIpAddress(ServerHttpRequest serverHttpRequest) {
        String ipAddress;
        try {
            ipAddress = serverHttpRequest.getHeaders().getFirst(NGINX_CUSTOM_IP_KEY);
            if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_X_FORWARDED_FOR);
            }
            if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HTTP_X_FORWARDED_FOR);
            }
            if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_PROXY_CLIENT_IP);
            }
            if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_WL_PROXY_CLIENT_IP);
            }
            if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                InetSocketAddress inetSocketAddress = serverHttpRequest.getRemoteAddress();
                if (Objects.isNull(inetSocketAddress)) {
                    log.info("获取IP地址失败");
                    return BLANK_IP;
                }
                ipAddress = inetSocketAddress.getAddress().getHostAddress();
                if (LOCALHOST.equals(ipAddress)) {
                    InetAddress localAddress = InetAddress.getLocalHost();
                    if (Objects.nonNull(localAddress.getHostAddress())) {
                        ipAddress = localAddress.getHostAddress();
                    }
                }
            }
            if (StrUtil.isNotBlank(ipAddress)) {
                ipAddress = ipAddress.split(SP)[0].trim();
            }
        } catch (Exception e) {
            log.error("解析请求IP地址失败", e);
            ipAddress = BLANK_IP;
        }
        return StrUtil.isBlank(ipAddress) ? BLANK_IP : ipAddress;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
