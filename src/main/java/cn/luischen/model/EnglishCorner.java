package cn.luischen.model;

import lombok.Data;

/**
 * 英语角实体
 */
@Data
public class EnglishCorner {

    /**
     * 英语句子id
     */
    private Integer eid;

    /**
     * 英语句子内容
     */
    private String sentence;

    /**
     * 英语句子翻译
     */
    private String translation;
}
