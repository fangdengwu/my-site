package cn.luischen.dao;

import cn.luischen.model.RelationShip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 中间表
 * Created by winterchen on 2018/4/30.
 */
@Mapper
@Component
public interface RelationShipDao extends BaseMapper<RelationShip> {
}
