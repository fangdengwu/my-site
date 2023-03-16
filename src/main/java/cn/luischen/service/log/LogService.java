package cn.luischen.service.log;

import cn.luischen.model.Log;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 用户请求日志
 * Created by winterchen on 2018/4/29.
 */
public interface LogService {

    /**
     * 添加
     * @param action
     * @param data
     * @param ip
     * @param authorId
     */
    void addLog(String action, String data, String ip, Integer authorId);

    /**
     * 删除日志
     * @param id
     * @return
     */
    void deleteLogById(Integer id);

    /**
     * 获取日志
     * @return
     */
    Page<Log> getLogs(int pageNum, int pageSize);
}
