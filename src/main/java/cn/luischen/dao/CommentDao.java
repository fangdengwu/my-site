package cn.luischen.dao;

import cn.luischen.dto.cond.CommentCond;
import cn.luischen.model.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论实体类
 * Created by winterchen on 2018/4/29.
 */
@Mapper
@Component
public interface CommentDao extends BaseMapper<Comment> {
}
