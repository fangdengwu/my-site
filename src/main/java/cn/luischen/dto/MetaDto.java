package cn.luischen.dto;

import cn.luischen.model.Meta;
import lombok.Data;

/**
 * 标签、分类列表
 * Created by winterchen on 2018/4/30.
 */
@Data
public class MetaDto extends Meta {

    private int count;
}
