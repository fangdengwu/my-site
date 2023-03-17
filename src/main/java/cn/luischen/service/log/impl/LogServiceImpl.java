package cn.luischen.service.log.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.LogDao;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Log;
import cn.luischen.service.log.LogService;
import cn.luischen.utils.DateKit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  请求日志
 * Created by winterchen on 2018/4/29.
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Override
    public void addLog(String action, String data, String ip, Integer authorId) {
        Log log = new Log();
        log.setAuthorId(authorId);
        log.setIp(ip);
        log.setData(data);
        log.setAction(action);
        log.setCreated(DateKit.getCurrentUnixTime());
        logDao.insert(log);
    }

    @Override
    public void deleteLogById(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        logDao.deleteById(id);
    }

    @Override
    public Page<Log> getLogs(int pageNum, int pageSize) {
        Page<Log> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Log> lwq = new LambdaQueryWrapper<>();
        lwq.orderByDesc(Log::getCreated);
        logDao.selectPage(page, lwq);
        return page;
    }
}
