package cn.luischen.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 网站配置项
 * Created by winterchen on 2018/4/28.
 */
@Data
public class Option {

    /** 名称 */
    @TableId(value = "name")
    private String name;
    /** 内容 */
    private String value;
    /** 备注 */
    private String description;
}
