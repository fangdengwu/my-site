package cn.luischen.service.site.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.dao.AttachDao;
import cn.luischen.dao.CommentDao;
import cn.luischen.dao.ContentDao;
import cn.luischen.dao.MetaDao;
import cn.luischen.dto.ArchiveDto;
import cn.luischen.dto.MetaDto;
import cn.luischen.dto.StatisticsDto;
import cn.luischen.dto.cond.CommentCond;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Comment;
import cn.luischen.model.Content;
import cn.luischen.model.Meta;
import cn.luischen.service.site.SiteService;
import cn.luischen.utils.DateKit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点服务
 * Created by winterchen on 2018/4/30.
 */
@Service
@Slf4j
public class SiteServiceImpl implements SiteService{


    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private MetaDao metaDao;

    @Autowired
    private AttachDao attachDao;

    @Override
    @Cacheable(value = "siteCache", key = "'comments_' + #p0")
    public List<Comment> getComments(int limit) {
        log.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10){
            limit = 10;
        }
        List<Comment> rs = commentDao.selectList(null);
        log.debug("Exit recentComments method");
        return rs;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'newArticles_' + #p0")
    public List<Content> getNewArticles(int limit) {
        log.debug("Enter recentArticles method:limit={}", limit);
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        List<Content> rs = contentDao.selectList(null);
        log.debug("Exit recentArticles method");
        return rs;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'comment_' + #p0")
    public Comment getComment(Integer coid) {
        log.debug("Enter recentComment method");
        if (null == coid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        Comment comment = commentDao.selectById(coid);
        log.debug("Exit recentComment method");
        return comment;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'statistics_'")
    public StatisticsDto getStatistics() {
        log.debug("Enter recentStatistics method");
        //文章总数
        Integer artices = contentDao.selectCount(null);

        Integer comments = commentDao.selectCount(null);

        LambdaQueryWrapper<Meta> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Meta::getType, Types.LINK.getType());
        Integer links = metaDao.selectCount(lqw);

        Integer atts = attachDao.selectCount(null);

        StatisticsDto rs = new StatisticsDto();
        rs.setArticles(artices);
        rs.setAttachs(atts);
        rs.setComments(comments);
        rs.setLinks(links);

        log.debug("Exit recentStatistics method");
        return rs;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'archivesSimple_' + #p0")
    public List<ArchiveDto> getArchivesSimple(ContentCond contentCond) {
        log.debug("Enter getArchives method");
        List<ArchiveDto> archives = contentDao.getArchive(contentCond);
        log.debug("Exit getArchives method");
        return archives;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'archives_' + #p0")
    public List<ArchiveDto> getArchives(ContentCond contentCond) {
        log.debug("Enter getArchives method");
        List<ArchiveDto> archives = contentDao.getArchive(contentCond);
        parseArchives(archives, contentCond);
        log.debug("Exit getArchives method");
        return archives;
    }



    private void parseArchives(List<ArchiveDto> archives, ContentCond contentCond) {
        if (null != archives){
            archives.forEach(archive -> {
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;

                LambdaQueryWrapper<Content> lqw = new LambdaQueryWrapper<>();
                lqw.ge(Content::getCreated, start);
                lqw.le(Content::getCreated, end);
                lqw.eq(StringUtils.isNotEmpty(contentCond.getType()), Content::getType, contentCond.getType());
                lqw.orderByDesc(Content::getCreated);

                List<Content> contents = contentDao.selectList(lqw);
                archive.setArticles(contents);
            });
        }
    }

    @Override
    @Cacheable(value = "siteCache", key = "'metas_' + #p0")
    public List<MetaDto> getMetas(String type, String orderBy, int limit) {
        log.debug("Enter metas method:type={},order={},limit={}", type, orderBy, limit);
        List<MetaDto> retList=null;
        if (StringUtils.isNotBlank(type)) {
            if(StringUtils.isBlank(orderBy)){
                orderBy = "count desc, a.mid desc";
            }
            if(limit < 1 || limit > WebConst.MAX_POSTS){
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderBy);
            paraMap.put("limit", limit);
            retList= metaDao.selectFromSql(paraMap);
        }
        log.debug("Exit metas method");
        return retList;
    }
}
