package com.tencent.wxcloudrun.common.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p> 直接响应工具 </p>
 *
 * @author Houcloud
 */
@Slf4j
@Component
public class HttpResultUtils {

    private static ObjectMapper objectMapper;

    /**
     * 使用response输出JSON
     * 注意 swagger 调试收不到响应
     *
     * @param response response
     * @param result   result
     */
    public static void out(Object result, ServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.append(objectMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            log.info("数据转换异常", e);
        } catch (IOException e) {
            log.info("IO异常请检查后台", e);
        }
    }

    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        HttpResultUtils.objectMapper = objectMapper;
    }

}
