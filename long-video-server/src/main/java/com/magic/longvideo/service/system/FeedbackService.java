package com.magic.longvideo.service.system;

import com.magic.longvideo.pojo.dto.LongVideoCommonProblemDto;
import com.magic.longvideo.pojo.dto.LongVideoFeedbackProblemTagDto;
import com.magic.longvideo.pojo.dto.LongVideoFeedbackRecordDto;
import com.magic.longvideo.pojo.vo.CommitFeedbackVo;

import java.util.List;

public interface FeedbackService {
    List<LongVideoCommonProblemDto> commonProblem();

    boolean commit(CommitFeedbackVo commitFeedbackVo, Long userId);

    List<LongVideoFeedbackProblemTagDto> feedbackTags();

    List<LongVideoFeedbackRecordDto> myFeedBack(Long userId);
}
