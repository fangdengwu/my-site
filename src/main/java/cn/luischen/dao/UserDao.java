package cn.luischen.dao;

import cn.luischen.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by winterchen on 2018/4/20.
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}
