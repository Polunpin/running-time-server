/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.common.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.util.List;

/**
 * <p>
 * mark
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
public class PatternMatcher {

    public static boolean match(HttpServletRequest request, List<UrlInfo> ignoreInfos) {
        for (UrlInfo ignoreInfo : ignoreInfos) {
            if (PatternMatchUtils.simpleMatch(ignoreInfo.getUri(), request.getRequestURI())
                    &&
                    request.getMethod().equalsIgnoreCase(ignoreInfo.getMethod())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(PatternMatchUtils.simpleMatch("/api/front/carousel/**", "/api/front/carousel"));
    }
}

