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

    /**
     * @Author: winterchen
     * @Description: 更改用户信息
     * @Date: 2018/4/20
     * @param user
     */
    int updateUserInfo(User user);

    /**
     * @Author: winterchen
     * @Description: 根据主键编号获取用户信息
     * @Date: 2018/4/20
     * @param uId 主键
     */
    User getUserInfoById(@Param("uid") Integer uId);

    /**
     * 根据用户名和密码获取用户信息
     * @param username
     * @param password
     * @return
     */
    User getUserInfoByCond(@Param("username") String username, @Param("password") String password);

}
