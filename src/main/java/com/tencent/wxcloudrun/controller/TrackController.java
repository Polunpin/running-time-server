package com.tencent.wxcloudrun.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.result.Result;
import com.tencent.wxcloudrun.common.security.token.store.AuthContext;
import com.tencent.wxcloudrun.dao.TrackMapper;
import com.tencent.wxcloudrun.model.entity.Track;
import com.tencent.wxcloudrun.model.entity.User;
import com.tencent.wxcloudrun.model.request.IdBody;
import com.tencent.wxcloudrun.model.request.PageListParams;
import com.tencent.wxcloudrun.model.request.TrackPoint;
import com.tencent.wxcloudrun.model.request.UploadTrackBody;
import com.tencent.wxcloudrun.model.response.RunningCalendarResponse;
import com.tencent.wxcloudrun.model.response.RunningStatsResponse;
import com.tencent.wxcloudrun.model.response.TrackResponse;
import com.tencent.wxcloudrun.model.response.UserResponse;
import com.tencent.wxcloudrun.service.ITrackService;
import com.tencent.wxcloudrun.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 运动记录接口
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024-12-19
 */
@Tag(name = "运动记录接口")
@RestController
@RequestMapping("/api/front/track")
public class TrackController {

    @Resource
    private ITrackService trackService;
    @Resource
    private IUserService userService;
    @Resource
    private TrackMapper trackMapper;

    @Operation(summary = "获取运动记录详情")
    @GetMapping
    public Result<TrackResponse> getTrack(@RequestParam Long id) {
        // 获取当前用户ID
        Long userId = AuthContext.getUserId();
        Track track = trackService.lambdaQuery().eq(Track::getId, id).eq(Track::getUserId, userId).one();
        if (Objects.isNull(track)) {
            return Result.notfound("运动记录未找到");
        }
        TrackResponse response = BeanUtil.toBean(track, TrackResponse.class);
        User user = userService.getById(track.getUserId());
        response.setUser(BeanUtil.toBean(user, UserResponse.class));
        response.setPoints(JSON.parseArray(track.getPointsJson(), TrackPoint.class));
        return Result.success(response);
    }


    @Operation(summary = "获取运动日历")
    @GetMapping("/calendar")
    public Result<List<RunningCalendarResponse>> getRunningCalendar() {
        Long userId = AuthContext.getUserId();
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        List<RunningCalendarResponse> list = trackMapper.selectRunningCalendar(userId, year, month);
        List<RunningCalendarResponse> calendarDataList = list.stream().map(r -> {
            RunningCalendarResponse cyclingCalendarData = new RunningCalendarResponse();
            cyclingCalendarData.setDistance(r.getDistance());
            cyclingCalendarData.setChecked(r.getDistance() > 0);
            cyclingCalendarData.setDate(r.getDate());
            return cyclingCalendarData;
        }).toList();
        return Result.success(calendarDataList);

    }

    @Operation(summary = "获取运动统计")
    @GetMapping("/stats")
    public Result<RunningStatsResponse> getRunningStats() {
        // 获取当前用户ID
        Long userId = AuthContext.getUserId();
        RunningStatsResponse response = new RunningStatsResponse();
        Double userTotalDistance = trackMapper.getUserTotalDistance(userId);
        Long userTotalDays = trackMapper.getUserTotalDays(userId);
        Double userTodayDistance = trackMapper.getUserTodayDistance(userId);
        response.setTotalDistance(Optional.ofNullable(userTotalDistance).orElse(0.0));
        response.setTotalDays(Optional.ofNullable(userTotalDays).orElse(0L));
        response.setTodayDistance(Optional.ofNullable(userTodayDistance).orElse(0D));
        return Result.success(response);
    }


    @Operation(summary = "获取运动记录列表")
    @GetMapping("/list")
    public Result<IPage<Track>> getTrackList(PageListParams params) {
        Long userId = AuthContext.getUserId();
        LambdaQueryWrapper<Track> wrapper = Wrappers.<Track>lambdaQuery().orderByDesc(Track::getCreatedAt);
        wrapper.eq(Track::getUserId, userId);
        IPage<Track> trackPage = trackService.page(Page.of(params.getPage(), params.getLimit()), wrapper);
        return Result.success(trackPage);
    }


    @Operation(summary = "添加运动记录")
    @PostMapping("/upload")
    public Result<Track> addTrack(@Valid @RequestBody UploadTrackBody body) {
        Double maxAltitude = body.getPoints().get(0).getAltitude();
        for (TrackPoint point : body.getPoints()) {
            if (maxAltitude < point.getAltitude()) {
                maxAltitude = point.getAltitude();
            }
        }
        Long userId = AuthContext.getUserId();
        Track track = new Track();
        track.setUserId(userId);
        track.setDistance(body.getDistance());
        track.setDuration(body.getDuration());
        track.setMaxAltitude(maxAltitude);
        track.setWerunSharedAt(body.getWerunSharedAt());
        track.setPointsJson(JSONObject.toJSONString(body.getPoints()));
        boolean save = trackService.save(track);
        return save ? Result.success(track) : Result.fail();
    }

    @Operation(summary = "删除运动记录")
    @DeleteMapping
    public Result<Void> deleteTrack(@Valid @RequestBody IdBody<Long> idBody) {
        Track track = trackService.getById(idBody.getId());
        if (Objects.isNull(track)) {
            return Result.fail("运动记录不存在");
        }
        boolean deleted = trackService.removeById(track);
        return deleted ? Result.success() : Result.fail();
    }
}
