package com.magic.longvideo.controller.system;

import cn.hutool.json.JSONObject;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.pojo.vo.CommitFeedbackVo;
import com.magic.longvideo.service.system.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/long_video/feedback")
@Api(tags = "[我的反馈，意见反馈的问题标签，常见问题，提交反馈]")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @ApiOperation("我的反馈")
    public JsonResult myFeedBack() {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.successMap("myFeedBack", feedbackService.myFeedBack(userId));
    }

    @PostMapping("/problem_tags")
    @ApiOperation("意见反馈的问题标签")
    public JsonResult feedbackTags() {
        return JsonResult.successMap("problemTags", feedbackService.feedbackTags());
    }

    @PostMapping("/common_problem")
    @ApiOperation("常见问题")
    public JsonResult commonProblem() {
        return JsonResult.successMap("commonProblem", feedbackService.commonProblem());
    }

    @PostMapping("/commit")
    @ApiOperation("提交反馈")
    public JsonResult commit(@RequestBody CommitFeedbackVo commitFeedbackVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        boolean isCommitSuccess = feedbackService.commit(commitFeedbackVo, userId);
        if (isCommitSuccess) {
            return JsonResult.success("提交成功", new JSONObject().set("isSuccess", true));
        }
        return JsonResult.success("提交失败", new JSONObject().set("isSuccess", false));
    }
}
