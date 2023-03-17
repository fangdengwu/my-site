package cn.luischen.dao;

import cn.luischen.dto.MetaDto;
import cn.luischen.dto.cond.MetaCond;
import cn.luischen.model.Meta;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 项目dao
 * Created by winterchen on 2018/4/29.
 */
@Mapper
@Component
public interface MetaDao extends BaseMapper<Meta> {

    /**
     * 根据sql查询
     * @param paraMap
     * @return
     */
    List<MetaDto> selectFromSql(Map<String, Object> paraMap);

}
