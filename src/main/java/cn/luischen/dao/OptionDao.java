package cn.luischen.dao;

import cn.luischen.model.Option;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网站配置dao
 * Created by winterchen on 2018/4/29.
 */
@Mapper
public interface OptionDao extends BaseMapper<Option> {
}
