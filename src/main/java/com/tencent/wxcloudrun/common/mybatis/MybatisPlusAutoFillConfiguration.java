package com.tencent.wxcloudrun.common.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * <p> MyBatisPlus公共字段自动填充 </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Configuration
public class MybatisPlusAutoFillConfiguration implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("INSERT 自动填充创建时间和更新时间");
        LocalDateTime now = LocalDateTime.now();
        this.fillStrategy(metaObject, "createdAt", now);
        this.fillStrategy(metaObject, "updateAt", now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("UPDATE 自动更新时间");
        LocalDateTime now = LocalDateTime.now();
        this.fillStrategy(metaObject, "updatedAt", now);
    }
}
