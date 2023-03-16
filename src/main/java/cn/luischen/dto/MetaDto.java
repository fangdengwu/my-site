package cn.luischen.dto;

import cn.luischen.model.Meta;

/**
 * 标签、分类列表
 * Created by winterchen on 2018/4/30.
 */
public class MetaDto extends Meta {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
