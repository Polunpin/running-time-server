package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.entity.Track;
import com.tencent.wxcloudrun.model.response.RunningCalendarResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024-12-19
 */
public interface TrackMapper extends BaseMapper<Track> {

    List<RunningCalendarResponse> selectRunningCalendar(@Param("userId") Long userId,
                                                        @Param("year") Integer year,
                                                        @Param("month") Integer month);


    @Select("""
            SELECT SUM(distance) AS total_distance
                  FROM t_track
                  WHERE user_id = #{userId} AND deleted_at IS NULL
                  GROUP BY user_id
            """)
    Double getUserTotalDistance(Long userId);

    @Select("""
            SELECT COUNT(DISTINCT DATE(created_at)) AS total_days
                FROM t_track
                WHERE user_id = #{userId} AND deleted_at IS NULL
            """)
    Long getUserTotalDays(Long userId);

    @Select("""
        SELECT SUM(distance) AS total_distance
        FROM t_track
        WHERE user_id = #{userId} 
          AND deleted_at IS NULL 
          AND DATE(created_at) = CURDATE()
        GROUP BY user_id
        """)
    Double getUserTodayDistance(Long userId);

}
