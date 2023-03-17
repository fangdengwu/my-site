package cn.luischen.service.user.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.UserDao;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.User;
import cn.luischen.service.user.UserService;
import cn.luischen.utils.TaleUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by winterchen on 2018/4/20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;//这里会报错，但是并不会影响


    @Transactional
    @Override
    public int updateUserInfo(User user) {
        if (null == user.getUid()) {
            throw BusinessException.withErrorCode("用户编号不可能为空");
        }
        return userDao.updateById(user);
    }

    @Override
    public User getUserInfoById(Integer uId) {
        return userDao.selectById(uId);
    }

    @Override
    public User login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);
        }

        String pwd = TaleUtils.MD5encode(username + password);

        LambdaQueryWrapper<User> lwq = new LambdaQueryWrapper<>();
        lwq.eq(User::getScreenName, username);
        lwq.eq(User::getPassword, pwd);
        User user = userDao.selectOne(lwq);

        if (null == user) {
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);
        }

        return user;
    }


}
