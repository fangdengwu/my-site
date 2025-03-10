package cn.luischen.service.content;

import cn.luischen.dto.cond.ContentCond;
import cn.luischen.model.Content;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章服务层
 * Created by winterchen on 2018/4/29.
 */
public interface ContentService {

    /**
     * 添加文章
     * @param contentDomain
     * @return
     */
    void addArticle(Content contentDomain);

    /**
     * 根据编号删除文章
     * @param cid
     * @return
     */
    void deleteArticleById(Integer cid);

    /**
     * 更新文章
     * @param contentDomain
     * @return
     */
    void updateArticleById(Content contentDomain);

    /**
     * 更新分类
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal, String newCatefory);



    /**
     * 添加文章点击量
     * @param content
     */
    void updateContentByCid(Content content);

    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    Content getArticleById(Integer cid);

    /**
     * 根据条件获取文章列表
     * @param contentCond
     * @return
     */
    Page<Content> getArticlesByCond(ContentCond contentCond, int pageNum, int pageSize);

    /**
     * 获取最近的文章（只包含id和title）
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Content> getRecentlyArticle(int pageNum, int pageSize);

    /**
     * 搜索文章
     * @param param
     * @param pageNun
     * @param pageSize
     * @return
     */
    Page<Content> searchArticle(String param, int pageNun, int pageSize);
}
