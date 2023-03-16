package cn.luischen.dao;

import cn.luischen.dto.ArchiveDto;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.model.Content;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章持久层
 * Created by winterchen on 2018/4/29.
 */
@Mapper
public interface ContentDao extends BaseMapper<Content> {

    /**
     * 添加文章
     * @param contentDomain
     * @return
     */
    int addArticle(Content contentDomain);

    /**
     * 根据编号删除文章
     * @param cid
     * @return
     */
    int deleteArticleById(@Param("cid") Integer cid);

    /**
     * 更新文章
     * @param contentDomain
     * @return
     */
    int updateArticleById(Content contentDomain);

    /**
     * 更新文章的评论数
     * @param cid
     * @param commentsNum
     * @return
     */
    int updateArticleCommentCountById(@Param("cid") Integer cid, @Param("commentsNum") Integer commentsNum);



    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    Content getArticleById(@Param("cid") Integer cid);

    /**
     * 根据条件获取文章列表
     * @param contentCond
     * @return
     */
    List<Content> getArticlesByCond(ContentCond contentCond);

    /**
     * 获取文章总数量
     * @return
     */
    Long getArticleCount();

    /**
     * 获取归档数据
     * @param contentCond 查询条件（只包含开始时间和结束时间）
     * @return
     */
    List<ArchiveDto> getArchive(ContentCond contentCond);

    /**
     * 获取最近的文章（只包含id和title）
     * @return
     */
    List<Content> getRecentlyArticle();

    /**
     * 搜索文章-根据标题 或 内容匹配
     * @param param
     * @return
     */
    List<Content> searchArticle(@Param("param") String param);

}
