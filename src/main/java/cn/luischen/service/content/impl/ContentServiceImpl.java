package cn.luischen.service.content.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.dao.CommentDao;
import cn.luischen.dao.ContentDao;
import cn.luischen.dao.RelationShipDao;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Comment;
import cn.luischen.model.Content;
import cn.luischen.model.RelationShip;
import cn.luischen.service.content.ContentService;
import cn.luischen.service.meta.MetaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by winterchen on 2018/4/29.
 */
@Service
public class ContentServiceImpl  implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MetaService metaService;

    @Autowired
    private RelationShipDao relationShipDao;


    @Transactional
    @Override
    @CacheEvict(value={"articleCache","articleCaches","siteCache"},allEntries=true,beforeInvocation=true)
    public void addArticle(Content contentDomain) {
        if (null == contentDomain) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        if (StringUtils.isBlank(contentDomain.getTitle())) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY);
        }
        if (contentDomain.getTitle().length() > WebConst.MAX_TITLE_COUNT) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_IS_TOO_LONG);
        }
        if (StringUtils.isBlank(contentDomain.getContent())) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY);
        }
        if (contentDomain.getContent().length() > WebConst.MAX_TEXT_COUNT) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_IS_TOO_LONG);
        }

        //标签和分类
        String tags = contentDomain.getTags();
        String categories = contentDomain.getCategories();

        contentDao.insert(contentDomain);

        int cid = contentDomain.getCid();
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    @Transactional
    @CacheEvict(value={"articleCache","articleCaches","siteCache"},allEntries=true,beforeInvocation=true)
    public void deleteArticleById(Integer cid) {
        if (null == cid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        contentDao.deleteById(cid);
        //同时也要删除该文章下的所有评论
        List<Comment> comments = commentDao.getCommentsByCId(cid);
        if (null != comments && comments.size() > 0){
            comments.forEach(comment ->{
                commentDao.deleteComment(comment.getCoid());
            });
        }
        //删除标签和分类关联
        List<RelationShip> relationShips = relationShipDao.getRelationShipByCid(cid);
        if (null != relationShips && relationShips.size() > 0){
            relationShipDao.deleteRelationShipByCid(cid);
        }

    }

    @Override
    @Transactional
    @CacheEvict(value={"articleCache","articleCaches","siteCache"},allEntries=true,beforeInvocation=true)
    public void updateArticleById(Content contentDomain) {
        //标签和分类
        String tags = contentDomain.getTags();
        String categories = contentDomain.getCategories();

        contentDao.updateArticleById(contentDomain);
        int cid = contentDomain.getCid();
        relationShipDao.deleteRelationShipByCid(cid);
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());

    }

    @Override
    @Transactional
    @CacheEvict(value={"articleCache","articleCaches","siteCache"},allEntries=true,beforeInvocation=true)
    public void updateCategory(String ordinal, String newCatefory) {
        ContentCond cond = new ContentCond();
        cond.setCategory(ordinal);
        List<Content> articles = contentDao.getArticlesByCond(cond);
        articles.forEach(article -> {
            article.setCategories(article.getCategories().replace(ordinal, newCatefory));
            contentDao.updateById(article);
        });
    }



    @Override
    @CacheEvict(value={"articleCache","articleCaches","siteCache"},allEntries=true,beforeInvocation=true)
    public void updateContentByCid(Content content) {
        if (null != content && null != content.getCid()) {
            contentDao.updateById(content);
        }
    }

    @Override
    @Cacheable(value = "articleCache", key = "'articleById_' + #p0")
    public Content getArticleById(Integer cid) {
        if (null == cid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return contentDao.selectById(cid);
    }

    @Override
    @Cacheable(value = "articleCaches", key = "'articlesByCond_' + #p1 + 'type_' + #p0.type")
    public Page<Content> getArticlesByCond(ContentCond contentCond, int pageNum, int pageSize) {
        if (null == contentCond) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        Page<Content> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Content> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(contentCond.getTag()), Content::getTags, contentCond.getTag());
        lqw.like(StringUtils.isNotEmpty(contentCond.getCategory()), Content::getCategories, contentCond.getCategory());
        lqw.eq(StringUtils.isNotEmpty(contentCond.getStatus()), Content::getStatus, contentCond.getStatus());
        lqw.like(StringUtils.isNotEmpty(contentCond.getTitle()), Content::getTitle, contentCond.getTitle());
        lqw.like(StringUtils.isNotEmpty(contentCond.getContent()), Content::getContent, contentCond.getContent());
        lqw.eq(StringUtils.isNotEmpty(contentCond.getType()), Content::getType, contentCond.getType());
        lqw.ge(contentCond.getStartTime() != null, Content::getCreated, contentCond.getStartTime());
        lqw.le(contentCond.getEndTime() != null, Content::getCreated, contentCond.getEndTime());
        lqw.orderByDesc(Content::getCreated);

        contentDao.selectPage(page, lqw);
        return page;
    }

    @Override
    @Cacheable(value = "articleCaches", key = "'recentlyArticle_' + #p0")
    public Page<Content> getRecentlyArticle(int pageNum, int pageSize) {
        Page<Content> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Content> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Content::getCreated);

        contentDao.selectPage(page, lqw);
        return page;
    }

    @Override
    public Page<Content> searchArticle(String param, int pageNun, int pageSize) {
        Page<Content> page = new Page<>(pageNun,pageSize);

        LambdaQueryWrapper<Content> lqw = new LambdaQueryWrapper<>();
        lqw.like(Content::getTitle, param).or()
                .like(Content::getContent, param);


        contentDao.selectPage(page, lqw);
        return page;
    }
}
