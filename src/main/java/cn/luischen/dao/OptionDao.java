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

    /**
     * 删除网站配置
     * @param name
     * @return
     */
    int deleteOptionByName(@Param("name") String name);

    /**
     * 更新网站配置
     * @param options
     * @return
     */
    int updateOptionByName(Option options);

    /***
     * 根据名称获取网站配置
     * @param name
     * @return
     */
    Option getOptionByName(@Param("name") String name);

    /**
     * 获取全部网站配置
     * @return
     */
    List<Option> getOptions();
}
