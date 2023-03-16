package cn.luischen.service.attach.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.AttAchDao;
import cn.luischen.dto.AttAchDto;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.AttAch;
import cn.luischen.service.attach.AttAchService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 附件服务实现层
 * Created by winterchen on 2018/4/29.
 */
@Service
public class AttAchServiceImpl implements AttAchService {

    @Autowired
    private AttAchDao attAchDao;

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void addAttAch(AttAch attAchDomain) {
        if (null == attAchDomain) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attAchDao.addAttAch(attAchDomain);

    }

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void batchAddAttAch(List<AttAch> list) {
        if (null == list || list.size() == 0) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attAchDao.batchAddAttAch(list);

    }

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void deleteAttAch(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attAchDao.deleteAttAch(id);

    }

    @Override
    @CacheEvict(value={"attCaches","attCache"},allEntries=true,beforeInvocation=true)
    public void updateAttAch(AttAch attAchDomain) {
        if (null == attAchDomain || null == attAchDomain.getId()) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attAchDao.updateAttAch(attAchDomain);

    }

    @Override
    @Cacheable(value = "attCache", key = "'attAchById' + #p0")
    public AttAchDto getAttAchById(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return attAchDao.getAttAchById(id);
    }

    @Override
    @Cacheable(value = "attCaches", key = "'atts' + #p0")
    public Page<AttAchDto> getAtts(int pageNum, int pageSize) {
        Page<AttAchDto> page = new Page<>(pageNum, pageSize);
        List<AttAchDto> atts = attAchDao.getAtts();
        page.setRecords(atts);
        return page;
    }


}
