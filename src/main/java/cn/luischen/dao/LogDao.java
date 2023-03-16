package cn.luischen.dao;

import cn.luischen.model.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by winterchen on 2018/4/29.
 */
@Mapper
public interface LogDao extends BaseMapper<Log> {

    /**
     * 添加日志
     * @param logDomain
     * @return
     */
    int addLog(Log logDomain);

    /**
     * 删除日志
     * @param id
     * @return
     */
    int deleteLogById(@Param("id") Integer id);

    /**
     * 获取日志
     * @return
     */
    List<Log> getLogs();
}
