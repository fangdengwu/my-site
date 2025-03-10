package cn.luischen.service.option.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.OptionDao;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Option;
import cn.luischen.service.option.OptionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 网站配置服务层
 * Created by winterchen on 2018/4/28.
 */
@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionDao optionDao;

    @Override
    @CacheEvict(value={"optionsCache","optionCache"},allEntries=true,beforeInvocation=true)
    public void deleteOptionByName(String name) {
        if(StringUtils.isBlank(name)) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        optionDao.deleteById(name);

    }

    @Override
    @Transactional
    @CacheEvict(value={"optionsCache","optionCache"},allEntries=true,beforeInvocation=true)
    public void updateOptionByName(String name, String value) {
        if(StringUtils.isBlank(name)) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        Option option = new Option();
        option.setName(name);
        option.setValue(value);
        optionDao.updateById(option);

    }

    @Override
    @Transactional
    @CacheEvict(value={"optionsCache","optionCache"},allEntries=true,beforeInvocation=true)
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::updateOptionByName);
        }
    }

    @Override
    @Cacheable(value = "optionCache", key = "'optionByName_' + #p0")
    public Option getOptionByName(String name) {
        if(StringUtils.isBlank(name)) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return optionDao.selectById(name);
    }

    @Override
    @Cacheable(value = "optionsCache", key = "'options_'")
    public List<Option> getOptions() {
        return optionDao.selectList(null);
    }
}
