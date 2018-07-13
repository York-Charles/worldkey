package com.worldkey.mapper;
import com.worldkey.entity.InformationAll;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wu
 * 简单搜索功能
 */
@Repository
public interface SearchMapper {
    /**
     *  根据标题中的关键字，从文章表中模糊查询
     * @param keyWords    关键字
     * @return            返回文章对象
     */
   @Select("SELECT " +
           "i.id," +
           "i.create_date as createDate," +
           "i.title," +
           "i.title_img AS titleImg," +
           "i.type," +
           "i.weburl," +
           "i.abstracte," +
           "i.auther," +
           "i.point_number AS pointNumber," +
           "i.users," +
           "i.checked " +
           "FROM " +
           "information_all AS i " +
           "WHERE i.title like concat('%',#{keyWords},'%')")
    List<InformationAll> searchInfo(@Param("keyWords") String keyWords);
}
