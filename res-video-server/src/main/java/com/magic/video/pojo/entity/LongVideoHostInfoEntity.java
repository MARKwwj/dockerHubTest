package com.magic.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_host_info")
public class LongVideoHostInfoEntity {

    private Integer hostId;
    private Integer machineId;
    private String hostName;
    private String hostDns;
    private String hostCdn;
    private Integer hostFlag;

}
