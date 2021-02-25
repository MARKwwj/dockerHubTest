package com.magic.longvideo.pojo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RechargeRecordDto {

    private Date purchaseDate;
    private String videoTitle;
    private Integer balance;
    private Integer deducted;

}
