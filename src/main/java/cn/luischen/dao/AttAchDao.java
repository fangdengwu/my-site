package cn.luischen.dao;

import cn.luischen.dto.AttAchDto;
import cn.luischen.model.AttAch;
import cn.luischen.model.Content;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by winterchen on 2018/4/29.
 */
@Mapper
public interface AttAchDao extends BaseMapper<AttAch> {


    /**
     * 添加单个附件信息
     * @param attAchDomain
     * @return
     */
    int addAttAch(AttAch attAchDomain);

    /**
     * 批量添加附件信息
     * @param list
     * @return
     */
    int batchAddAttAch(List<AttAch> list);

    /**
     * 根据主键编号删除附件信息
     * @param id
     * @return
     */
    int deleteAttAch(int id);

    /**
     * 更新附件信息
     * @param attAchDomain
     * @return
     */
    int updateAttAch(AttAch attAchDomain);

    /**
     * 根据主键获取附件信息
     * @param id
     * @return
     */
    AttAchDto getAttAchById(@Param("id") int id);

    /**
     * 获取所有的附件信息
     * @return
     */
    List<AttAchDto> getAtts();

    /**
     * 查找附件的数量
     * @return
     */
    Long getAttsCount();
}
