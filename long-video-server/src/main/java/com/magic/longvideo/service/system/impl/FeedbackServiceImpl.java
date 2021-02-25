package com.magic.longvideo.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoCommonProblemMapper;
import com.magic.longvideo.mapper.LongVideoFeedbackProblemTagMapper;
import com.magic.longvideo.mapper.LongVideoFeedbackRecordMapper;
import com.magic.longvideo.pojo.dto.LongVideoCommonProblemDto;
import com.magic.longvideo.pojo.dto.LongVideoFeedbackProblemTagDto;
import com.magic.longvideo.pojo.dto.LongVideoFeedbackRecordDto;
import com.magic.longvideo.pojo.entity.LongVideoCommonProblemEntity;
import com.magic.longvideo.pojo.entity.LongVideoFeedbackProblemTagEntity;
import com.magic.longvideo.pojo.entity.LongVideoFeedbackRecordEntity;
import com.magic.longvideo.pojo.vo.CommitFeedbackVo;
import com.magic.longvideo.service.system.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final LongVideoCommonProblemMapper longVideoCommonProblemMapper;
    private final LongVideoFeedbackRecordMapper longVideoFeedbackRecordMapper;
    private final LongVideoFeedbackProblemTagMapper longVideoFeedbackProblemTagMapper;

    public FeedbackServiceImpl(
            LongVideoCommonProblemMapper longVideoCommonProblemMapper,
            LongVideoFeedbackRecordMapper longVideoFeedbackRecordMapper,
            LongVideoFeedbackProblemTagMapper longVideoFeedbackProblemTagMapper
    ) {
        this.longVideoCommonProblemMapper = longVideoCommonProblemMapper;
        this.longVideoFeedbackRecordMapper = longVideoFeedbackRecordMapper;
        this.longVideoFeedbackProblemTagMapper = longVideoFeedbackProblemTagMapper;
    }


    @Override
    public List<LongVideoCommonProblemDto> commonProblem() {
        QueryWrapper<LongVideoCommonProblemEntity> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort");
        List<LongVideoCommonProblemEntity> longVideoCommonProblemEntityList = longVideoCommonProblemMapper.selectList(queryWrapper);
        return MagicBeanUtil.copyListProperties(longVideoCommonProblemEntityList, LongVideoCommonProblemDto::new);
    }

    @Override
    public boolean commit(CommitFeedbackVo commitFeedbackVo, Long userId) {
        LongVideoFeedbackRecordEntity longVideoFeedbackRecordEntity = new LongVideoFeedbackRecordEntity();
        MagicBeanUtil.copyProperties(commitFeedbackVo, longVideoFeedbackRecordEntity);
        longVideoFeedbackRecordEntity.setStatusFlag(0);
        longVideoFeedbackRecordEntity.setDelFlag(0);
        longVideoFeedbackRecordEntity.setCreateTime(new Date());
        longVideoFeedbackRecordEntity.setUserId(userId);
        int row = longVideoFeedbackRecordMapper.insert(longVideoFeedbackRecordEntity);
        if (row == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<LongVideoFeedbackProblemTagDto> feedbackTags() {
        QueryWrapper<LongVideoFeedbackProblemTagEntity> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort");
        List<LongVideoFeedbackProblemTagEntity> longVideoFeedbackProblemTagEntityList = longVideoFeedbackProblemTagMapper.selectList(queryWrapper);
        return MagicBeanUtil.copyListProperties(longVideoFeedbackProblemTagEntityList, LongVideoFeedbackProblemTagDto::new);
    }

    @Override
    public List<LongVideoFeedbackRecordDto> myFeedBack(Long userId) {
        QueryWrapper<LongVideoFeedbackRecordEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        List<LongVideoFeedbackRecordEntity> longVideoFeedbackRecordEntityList = longVideoFeedbackRecordMapper.selectList(queryWrapper);
        return MagicBeanUtil.copyListProperties(longVideoFeedbackRecordEntityList, LongVideoFeedbackRecordDto::new);
    }
}
