package com.magic.longvideo.service.system;

import com.magic.longvideo.pojo.dto.LoopNoticeDto;
import com.magic.longvideo.pojo.dto.PopNoticeDto;
import com.magic.longvideo.pojo.dto.SystemNoticeDto;

import java.util.List;

public interface NoticeService {
    LoopNoticeDto loopNotice(Integer type);
    List<SystemNoticeDto> systemNotice(Long userId);
    PopNoticeDto popNotice();
    List<String> giftNotice(Long userId);
    boolean readSystemNotice(Long userId, Integer noticeId);
}
