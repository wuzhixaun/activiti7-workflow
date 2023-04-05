package com.imooc.activitiweb.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 出差申请中的流程变量对象
 * @author: wuzhixuan
 * @date 2023/04/05 01:06
 * @Version 1.0
 */
@Data
public class Evection implements Serializable {


    /**
     *
     */
    private Long id;
    /**
     * 出差天数
     */
    private Double num;

    /**
     * 出差单名称
     */

    private String evectionName;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;


    /**
     * 目的地
     */
    private String destination;

}
