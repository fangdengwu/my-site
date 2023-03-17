package cn.luischen.dto;

import cn.luischen.model.Content;
import lombok.Data;

import java.util.List;

/**
 * 文章归档类
 * Created by winterchen on 2018/4/30.
 */
@Data
public class ArchiveDto {

    private String date;
    private String count;
    private List<Content> articles;
}
