package cn.luischen.service.log.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.LogDao;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Log;
import cn.luischen.service.log.LogService;
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
        Log logDomain = new Log();
        logDomain.setAuthorId(authorId);
        logDomain.setIp(ip);
        logDomain.setData(data);
        logDomain.setAction(action);
        logDao.addLog(logDomain);
    }

    @Override
    public void deleteLogById(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        logDao.deleteLogById(id);
    }

    @Override
    public Page<Log> getLogs(int pageNum, int pageSize) {
        Page page = new Page(pageNum, pageSize);
        logDao.selectPage(page, null);
        return page;
    }
}
