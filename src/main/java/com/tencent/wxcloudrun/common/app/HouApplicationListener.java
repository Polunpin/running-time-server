/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.common.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 应用事件监听器
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Component
public class HouApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Value("${spring.profiles.active:none}")
    private String env;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 在这里可以监听到Spring Boot的生命周期
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            log.info("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) {
            log.info("初始化完成");
        } else if (event instanceof ContextRefreshedEvent) {
            log.info("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {
            log.info("启动完成");
        } else if (event instanceof ContextStartedEvent) {
            log.info("应用启动，需要在代码动态添加监听器才可捕获 ");
        } else if (event instanceof ContextStoppedEvent) {
            log.info("应用停止");
        } else if (event instanceof ContextClosedEvent) {
            log.info("应用关闭");
        } else if (event instanceof ServletWebServerInitializedEvent) {
            log.info("Web服务初始化完毕");
        } else if (event instanceof ApplicationStartedEvent) {
            log.info("应用启动");
        } else if (event instanceof AvailabilityChangeEvent) {
            log.info("可用性变更事件");
        } else {
            if (env.equalsIgnoreCase("dev")) {
                log.info("其他事件{}", event);
            }
        }
    }

}
