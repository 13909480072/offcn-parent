package com.offcn.project.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

//项目回收利益
@Data
@ApiModel
public class ProjectReturnVo {

    private String projectToken;

    @ApiModelProperty(value = "回报类型：0-虚拟回报 1-实物回报",required = true)
    private String type;

    @ApiModelProperty(value = "支持金额",required = true,example = "0")
    private Integer supportmoney;

    @ApiModelProperty(value = "回报内容",required = true)
    private String content;

    @ApiModelProperty(value = "单笔限购",required = true,example = "0")
    private Integer signalpurchase;

    @ApiModelProperty(value = "限购数量",required = true,example = "0")
    private Integer purchase;

    @ApiModelProperty(value = "运费",required = true,example = "0")
    private Integer freight;

    @ApiModelProperty(value = "发票 0-不开发票 1-开发票",required = true)
    private String invoice;

    @ApiModelProperty(value = "回报时间，天数",required = true,example = "0")
    private Integer rtndate;
}
