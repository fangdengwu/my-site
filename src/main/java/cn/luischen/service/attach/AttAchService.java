package cn.luischen.service.attach;

import cn.luischen.dto.AttAchDto;
import cn.luischen.model.AttAch;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 附件服务层
 * Created by winterchen on 2018/4/29.
 */
public interface AttAchService {

    /**
     * 添加单个附件信息
     * @param attAchDomain
     * @return
     */
    void addAttAch(AttAch attAchDomain);

    /**
     * 批量添加附件信息
     * @param list
     * @return
     */
    void batchAddAttAch(List<AttAch> list);

    /**
     * 根据主键编号删除附件信息
     * @param id
     * @return
     */
    void deleteAttAch(Integer id);

    /**
     * 更新附件信息
     * @param attAchDomain
     * @return
     */
    void updateAttAch(AttAch attAchDomain);

    /**
     * 根据主键获取附件信息
     * @param id
     * @return
     */
    AttAchDto getAttAchById(Integer id);

    /**
     * 获取所有的附件信息
     * @return
     */
    Page<AttAchDto> getAtts(int pageNum, int pageSize);
}
