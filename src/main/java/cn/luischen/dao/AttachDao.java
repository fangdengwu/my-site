package cn.luischen.dao;

import cn.luischen.dto.AttachDto;
import cn.luischen.model.Attach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by winterchen on 2018/4/29.
 */
@Mapper
public interface AttachDao extends BaseMapper<Attach> {


    /**
     * 批量添加附件信息
     * @param list
     * @return
     */
    int batchAddAttach(List<Attach> list);

    /**
     * 根据主键获取附件信息
     * @param id
     * @return
     */
    AttachDto getAttachById(@Param("id") int id);

    /**
     * 获取所有的附件信息
     * @return
     */
    List<AttachDto> getAtts();

}
