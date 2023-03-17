package cn.luischen.service.attach.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.AttachDao;
import cn.luischen.dto.AttachDto;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Attach;
import cn.luischen.service.attach.AttachService;
import cn.luischen.utils.DateKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 附件服务实现层
 * Created by winterchen on 2018/4/29.
 */
@Service
public class AttachServiceImpl implements AttachService {

    @Autowired
    private AttachDao attachDao;

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void addAttach(Attach attach) {
        if (null == attach) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attach.setCreated(DateKit.getCurrentUnixTime());
        attachDao.insert(attach);

    }

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void batchAddAttach(List<Attach> list) {
        if (null == list || list.size() == 0) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attachDao.batchAddAttach(list);

    }

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void deleteAttach(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attachDao.deleteById(id);

    }

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void updateAttach(Attach attach) {
        if (null == attach || null == attach.getId()) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attach.setCreated(DateKit.getCurrentUnixTime());
        attachDao.updateById(attach);

    }

    @Override
    @Cacheable(value = "attCache", key = "'attachById' + #p0")
    public AttachDto getAttachById(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return attachDao.getAttachById(id);
    }

    @Override
    @Cacheable(value = "attCaches", key = "'atts' + #p0")
    public Page<AttachDto> getAtts(int pageNum, int pageSize) {
        Page<AttachDto> page = new Page<>(pageNum, pageSize);
        List<AttachDto> atts = attachDao.getAtts();
        page.setRecords(atts);
        return page;
    }


}
