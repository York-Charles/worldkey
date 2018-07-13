package com.worldkey.mapper;
import com.worldkey.entity.Praise;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author wu
 */
@Repository
public interface PraiseMapper {
    /**
     *
     * @param praiseId
     * @return
     */
    int deleteByPrimaryKey(Long praiseId);
    int insert(Praise record);
    int insertSelective(Praise record);
    Praise selectByPrimaryKey(Long praiseId);
    int updateByPrimaryKeySelective(Praise record);
    int updateByPrimaryKey(Praise record);
    //以上方法为自动生成部分


    /**
     * 更新赞数+1
     * @param information a
     * @return a
     */
    @Update("UPDATE `praise_num` SET " +
            "praise_num=praise_num+1 " +
            "WHERE information=#{information}")
    int  addPraiseNum(@Param("information")
                              Integer information);

    /**
     * 更新赞数-1
     * @param information a
     * @return a
     */
    @Update("UPDATE `praise_num` SET " +
            "praise_num=praise_num-1 " +
            "WHERE information=#{information}")
    int  lessenPraiseNum(@Param("information") Integer information);

    /**
     * 查询赞表主键ID
     * @param users 用户
     * @param information 文章
     */
    @Select ( "SELECT "
            + "praise_id as praiseId "
            + "FROM "
            + "praise "
            + "WHERE "
            + "(users = #{users} ANd information = #{information})")
    Long selectByUserAndInfo(@Param("users") Long users, @Param("information") Integer information);

  //5.18
    @Select("SELECT praise.information,praise.create_time "+
    		"from praise "+
    		"INNER JOIN information_all on praise.information = information_all.id  "+
    		"INNER JOIN users ON users.id = information_all.users "+
    		"WHERE users.id=#{userId} order by praise.create_time desc")
    List<Praise> Praise(Long userId);
    
    @Select("select praise.users from praise "+
    		"INNER JOIN information_all on praise.information = information_all.id "+
    		"where information_all.users=#{userId} order by praise.create_time desc")
    List<Long> chooseUsers(Long userId);
    

    @Select("select * from praise where users=#{users.id} and information=#{information}")
    List<Praise> selectExist(Praise p);
}