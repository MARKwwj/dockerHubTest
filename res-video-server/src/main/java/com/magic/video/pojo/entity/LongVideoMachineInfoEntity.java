package com.magic.video.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_machine_info")
public class LongVideoMachineInfoEntity {

    private Integer machineId;
    private String machineName;
    private String machineIp;
    private Integer machinePort;
    private Integer machineProxyPort;
    private String machineAccount;
    private String machinePasswd;
    private Integer machineFlag;
    private String machineSummary;

}
