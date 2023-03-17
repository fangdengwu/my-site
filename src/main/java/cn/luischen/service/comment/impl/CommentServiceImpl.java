package cn.luischen.service.comment.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.CommentDao;
import cn.luischen.dto.cond.CommentCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Comment;
import cn.luischen.model.Content;
import cn.luischen.service.comment.CommentService;
import cn.luischen.service.content.ContentService;
import cn.luischen.utils.DateKit;
import cn.luischen.utils.TaleUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 评论实现类
 * Created by winterchen on 2018/4/29.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ContentService contentService;



    private static final Map<String,String> STATUS_MAP = new ConcurrentHashMap<>();

    /**
     * 评论状态：正常
     */
    private static final String STATUS_NORMAL = "approved";
    /**
     * 评论状态：不显示
     */
    private static final String STATUS_BLANK = "not_audit";

    static {
        STATUS_MAP.put("approved",STATUS_NORMAL);
        STATUS_MAP.put("not_audit",STATUS_BLANK);
    }

    @Override
    @Transactional
    @CacheEvict(value={"commentCache","siteCache"},allEntries=true)
    public void addComment(Comment comment) {
        String msg = null;
        if (null == comment) {
            msg = "评论对象为空";
        }
        if(comment != null) {
            if (StringUtils.isBlank(comment.getAuthor())) {
                comment.setAuthor("热心网友");
            }
            if (StringUtils.isNotBlank(comment.getMail()) && !TaleUtils.isEmail(comment.getMail())) {
                msg = "请输入正确的邮箱格式";
            }
            if (StringUtils.isBlank(comment.getContent())) {
                msg = "评论内容不能为空";
            }
            if (comment.getContent().length() < 5 || comment.getContent().length() > 2000) {
                msg = "评论字数在5-2000个字符";
            }
            if (null == comment.getCid()) {
                msg = "评论文章不能为空";
            }
            if (msg != null) {
                throw BusinessException.withErrorCode(msg);
            }
            Content article = contentService.getArticleById(comment.getCid());
            if (null == article) {
                throw BusinessException.withErrorCode("该文章不存在");
            }
            comment.setOwnerId(article.getAuthorId());
            comment.setStatus(STATUS_MAP.get(STATUS_BLANK));
            comment.setCreated(DateKit.getCurrentUnixTime());
            commentDao.insert(comment);

            Content temp = new Content();
            temp.setCid(article.getCid());
            Integer count = article.getCommentsNum();
            if (null == count) {
                count = 0;
            }
            temp.setCommentsNum(count + 1);
            contentService.updateContentByCid(temp);
        }

    }

    @Transactional
    @Override
    @CacheEvict(value={"commentCache","siteCache"},allEntries=true)
    public void deleteComment(Integer coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        // 如果删除的评论存在子评论，一并删除
        //查找当前评论是否有子评论
        CommentCond commentCond = new CommentCond();
        commentCond.setParent(coid);
        Comment comment = commentDao.selectById(coid);

        List<Comment> childComments = commentDao.selectList(getQueryWrapperByCond(commentCond));
        Integer count = 0;

        //删除子评论
        if (null != childComments && childComments.size() > 0){
            for (Comment childComment : childComments) {
                commentDao.deleteById(childComment.getCoid());
                count++;
            }
        }
        //删除当前评论
        commentDao.deleteById(coid);
        count++;

        //更新当前文章的评论数
        Content content = contentService.getArticleById(comment.getCid());
        if (null != content
                && null != content.getCommentsNum()
                && content.getCommentsNum() != 0){
            content.setCommentsNum(content.getCommentsNum() - count);
            contentService.updateContentByCid(content);
        }
    }

    @Override
    @CacheEvict(value={"commentCache","siteCache"},allEntries=true)
    public void updateCommentStatus(Integer coid, String status) {
        if (null == coid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        Comment comment = Comment.builder()
                .coid(coid)
                .status(status)
                .build();
        commentDao.updateById(comment);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentById_' + #p0")
    public Comment getCommentById(Integer coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        return commentDao.selectById(coid);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCId_' + #p0")
    public List<Comment> getCommentsByCId(Integer cid) {
        if (null == cid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getCid, cid);
        lqw.eq(Comment::getStatus, "approved");
        lqw.orderByAsc(Comment::getCreated);

        return commentDao.selectList(lqw);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCond_' + #p1")
    public Page<Comment> getCommentsByCond(CommentCond commentCond, int pageNum, int pageSize) {
        if (null == commentCond) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        Page<Comment> page = new Page<>(pageNum, pageSize);
        commentDao.selectPage(page, getQueryWrapperByCond(commentCond));

        return page;
    }

    private LambdaQueryWrapper<Comment> getQueryWrapperByCond(CommentCond commentCond) {

        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.isNotEmpty(commentCond.getStatus()), Comment::getStatus, commentCond.getStatus());
        lqw.ge(commentCond.getStartTime() != null, Comment::getCreated, commentCond.getStartTime());
        lqw.le(commentCond.getEndTime() != null, Comment::getCreated, commentCond.getEndTime());
        lqw.eq(commentCond.getParent() != null, Comment::getParent, commentCond.getParent());
        lqw.orderByAsc(Comment::getCreated);

        return lqw;
    }
}
