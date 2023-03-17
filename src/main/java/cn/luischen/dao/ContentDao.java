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
     * 获取归档数据
     * @param contentCond 查询条件（只包含开始时间和结束时间）
     * @return
     */
    List<ArchiveDto> getArchive(ContentCond contentCond);

}
