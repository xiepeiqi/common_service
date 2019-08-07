package com.xpq.cs.entity;

import java.time.LocalDateTime;
import java.sql.Blob;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人员表
 * </p>
 *
 * @author x
 * @since 2019-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TblPerson implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 人员名称
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 人员类型
     */
    private Integer type;

    /**
     * 一年薪水
     */
    private Double salary;

    /**
     * 人员注册照（不能超过65k,业务设成不能超过30k）
     */
    private Blob imageByte;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
