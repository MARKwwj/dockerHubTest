package com.magic.jump.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class JumpController {

//      @ApiModelProperty("用户来源")
//      private Integer userOrigin;
//      @ApiModelProperty("代理Id")
//      private Integer agentId;
//      @ApiModelProperty("上级Id")
//      private Long parentId;
//      @ApiModelProperty("用户类型")
//      private Integer userType;
//      @ApiModelProperty("用户分级")
//      private Integer userLevel;

    @GetMapping("/{param}")
    public String redirect(@PathVariable String param) {
        //TODO 解密字符串
        //TODO 解析参数
        //TODO 封装参数
        return "redirect:https://www.baidu.com/";
    }
}
