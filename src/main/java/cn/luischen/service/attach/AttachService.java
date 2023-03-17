package cn.luischen.service.attach;

import cn.luischen.dto.AttachDto;
import cn.luischen.model.Attach;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 附件服务层
 * Created by winterchen on 2018/4/29.
 */
public interface AttachService {

    /**
     * 添加单个附件信息
     * @param attachDomain
     * @return
     */
    void addAttach(Attach attachDomain);

    /**
     * 批量添加附件信息
     * @param list
     * @return
     */
    void batchAddAttach(List<Attach> list);

    /**
     * 根据主键编号删除附件信息
     * @param id
     * @return
     */
    void deleteAttach(Integer id);

    /**
     * 更新附件信息
     * @param attachDomain
     * @return
     */
    void updateAttach(Attach attachDomain);

    /**
     * 根据主键获取附件信息
     * @param id
     * @return
     */
    AttachDto getAttachById(Integer id);

    /**
     * 获取所有的附件信息
     * @return
     */
    Page<AttachDto> getAtts(int pageNum, int pageSize);
}
