package com.worldkey.mapper;

import com.worldkey.entity.BaseShow;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.InformationExample;
import com.worldkey.entity.Show;
import com.worldkey.entity.WindowShow;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface InformationAllMapper {
    /**
     * @param ids information ID集合
     * @return InformationAll 集合
     */
    List<InformationAll> selectByPermaryKeys(List<Long> ids);


    int deleteByPrimaryKey(Long id);

    int insert(InformationAll record);

    int insertSelective(InformationAll record);

    int insertSelectives(InformationAll record);
    //置顶
    int zhiding(@Param("id") Long id);

    InformationAll selectByPrimaryKey(Long id);
    
    @Select("select  i.id, i.create_date, i.title, i.title_img, i.type, i.weburl, i.abstracte, i.auther, i.point_number, i.checked, i.users,i.classify,i.competitive,i.user_brand,i.info,users.pet_name from information_all AS i left join users on users.id = i.users where i.id=#{id}")
    InformationAll selectByPrimaryKey1(Long id);

    InformationExample selectById(Long id);

    int updateByPrimaryKeySelective(InformationAll record);

    int updateByPrimaryKey(InformationAll record);

    List<InformationAll> findAll();

    List<InformationAll> selectByType(Integer type);

    List<InformationAll> selectByOneType(InformationAll vo);

    List<InformationAll> selectBySelective(InformationAll vo);

    @Update("UPDATE information_all "
            + " SET point_number=(SELECT point_number+1) "
            + " WHERE id=#{id}")
    void addPointNumber(Long id);

    @Update("UPDATE information_all "
            + "SET checked=#{checked} ,create_date=#{createDate}"
            + " WHERE id=#{id}")
    int checked(InformationAll vo);

    List<InformationAll> usersSelectBySelective(InformationAll vo);

    @Select("SELECT " +
            "i.id," +
            "i.create_date as createDate," +
            "i.title," +
            "i.title_img AS titleImg," +
            "i.info," +
            "i.type," +
            "i.weburl," +
            "i.abstracte," +
            "i.auther," +
            "i.point_number AS pointNumber," +
            "i.users," +
            "i.checked," +
            "i.classify " +
            "FROM " +
            "information_all AS i " +
            "ORDER BY " +
            "i.point_number DESC")
    List<InformationAll> selectOrderByPointNumber();

    @Select("SELECT " +
            "i.id, " +
            "i.title, " +
            "i.title_img as titleImg, " +
            "i.type, " +
            "i.classify " +
            "FROM " +
            "information_all AS i " +
            "WHERE  " +
            "i.users=#{id} and i.checked=2  " +
            "ORDER BY id DESC")
    List<InformationAll> selectUsersDraft(Long id);

    /**
     * 交换记录的类型属性
     *
     * @param type replace 要交换的两个类型  类型排序之后的将记录对应的类型修改为移动之后的类型ID
     */
    @Update("UPDATE  " +
            " information_all " +
            " AS t1 JOIN  " +
            " information_all " +
            "  AS t2 ON t1.type=#{type} AND t2.type=#{replace} " +
            " SET t1.type=#{replace},t2.type=#{type}")
    int replaceType(@Param("type") Integer type, @Param("replace") Integer replace);

    /**
     * 修改二级分类
     *
     * @param changeId      修改的文章ID
     * @param changeTwoType 修改为的二级分类
     */
    @Update("UPDATE information_all SET type=#{changeTwoType} WHERE id=#{changeId}")
    void changeTwoType(@Param("changeId") Integer changeId, @Param("changeTwoType") Integer changeTwoType);

    /*
     * 修改三级分类4.19
     */
    @Update("UPDATE information_all SET type=#{changeThreeType} WHERE id=#{changeId}")
    void changeThreeType(@Param("changeId") Integer changeId, @Param("changeThreeType") Integer changeThreeType);
    
    
    
    
    
    /**
     * 通过一级分类的分类名查找用户的展示内容
     * 展示板块使用  可以和selectShowByOneTypeAll()整合为一个方法，只是不想改
     */
    List<BaseShow> selectByOneTypeName(@Param("oneTypeName") String oneTypeName, @Param("userId") Long userId);

    /**
     * 以一级分类查询全部数据,可查全部包括information的所有分类
     */
    @Select(" SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title,\n" +
            "            i.title_img   AS titleImg,\n" +
            "			 i.classify    AS classify,\n"	+	
            "            pm.praise_num AS praiseNum,\n" +
            "            i.weburl      AS webUrl,\n" +
            "            i.create_date AS createDate,\n" +
            "            i.auther AS author\n" +
            "        FROM\n" +
            "            information_all AS i\n" +
            "            LEFT JOIN praise_num AS pm ON i.id = pm.information\n" +
            "        WHERE\n" +
            "            i.type IN (\n" +
            "                SELECT t.id\n" +
            "                FROM\n" +
            "                    two_type t\n" +
            "                WHERE\n" +
            "                     t.one_type=#{oneType}\n" +
            "            ) AND (i.checked=1 or i.checked=4) \n" +
            "        ORDER BY  i.create_date DESC")
   // @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectShowByOneType(@Param("oneType") Integer oneType);

    @Select("SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title,     \n" +
            "            i.title_img   AS titleImg,  \n" +
            "			 i.classify    AS classify,\n"	+
            "            pm.praise_num AS praiseNum, \n" +
            "            i.weburl      AS webUrl,    \n" +
            "            i.create_date AS createDate,\n" +
            "            i.auther      AS author,    \n" +
            "			 i.users       AS usersId,   \n" +  
            "            u.login_name  AS loginName, \n" +
            "			 u.head_img    AS headImg    \n" +
            "        FROM\n" +
            "            users AS u,\n" +
            "            information_all AS i\n" +
            "            LEFT JOIN praise_num AS pm ON i.id = pm.information\n" +
            "        WHERE\n" +
            "            i.type =#{twoType} AND (i.checked=1 or i.checked=4) AND i.users=u.id  "+
            "        ORDER BY  i.create_date DESC")
   // @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectShowByTwoType(@Param("twoType") Integer twoType);
    
    /*
     *--------------------------以下三级分类 2018.4.17更新--------------------------
     */
    //通过一级分类id获取所有详情
    @Select(" SELECT\n" +
    		"            i.id,\n" +
            "            i.title       AS title      ,\n" +
            "            i.title_img   AS titleImg   ,\n" +
            "			 i.classify    AS classify   ,\n"	+
            "            pm.praise_num AS praiseNum  ,\n" +
            "            i.weburl      AS webUrl     ,\n" +
            "            i.create_date AS createDate ,\n" +
            "            i.auther AS author          ,\n" +
            "			 i.users  AS usersId         ,\n" +  
            "            u.login_name AS loginName   ,\n" +
            "			 u.head_img    AS headImg    ,\n" +
            "            u.pet_name AS petName       \n" +
            "        FROM                             \n" +
            "            users AS u                  ,\n" +
            "            information_all AS i\n" +
            "            LEFT JOIN praise_num AS pm ON i.id = pm.information\n" +
            "        WHERE\n" +
            "            i.users=u.id AND (i.checked=1 or i.checked=4)  \n" +
            "          AND  i.type IN (SELECT ttt.id FROM three_type AS ttt WHERE ttt.two_type IN  \n"+
            "                         (SELECT tt.id FROM two_type AS tt WHERE tt.one_type IN    \n"+
            "                            (SELECT ot.id FROM one_type AS ot WHERE ot.id=#{oneType})))   \n"+
            "        ORDER BY  i.create_date DESC")
   // @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectShowAllByOneType(@Param("oneType") Integer oneType);
 
    
    //通过二级id获取三级详情页列表
    @Select("SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title      ,\n" +
            "            i.title_img   AS titleImg   ,\n" +
            "			 i.classify    AS classify    ,\n"	+
            "            pm.praise_num AS praiseNum  ,\n" +
            "            i.weburl      AS webUrl     ,\n" +
            "            i.create_date AS createDate ,\n" +
            "            i.auther AS author          ,\n" +
            "			 i.users  AS usersId         ,\n" +  
            "            u.login_name AS loginName   ,\n" +
            "            a.apply_record_id AS applyRecordId    ,\n" +
            "			 u.head_img    AS headImg     ,\n" +
            "			u.pet_name AS petName          \n"+
            "        FROM                             \n" +
            "            users AS u                                  ,\n"+
            "            three_type AS tt                             \n"+
            "            LEFT JOIN two_type AS t ON t.id=tt.two_type ,\n"+
            "            information_all AS i                         \n"+
            "            LEFT JOIN praise_num AS pm ON i.id = pm.information\n" +
            "            LEFT JOIN apply_record AS a  ON a.information =i.id  \n" +
            "        WHERE\n" +
            "            i.type =tt.id AND (i.checked=1 or i.checked=4) AND i.users=u.id AND t.id=#{twoType}  "+
            "        ORDER BY  i.create_date DESC")
  //  @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectShowThreeTypeAllByTwoType(@Param("twoType") Integer twoType);
    
    //通过三级id获取三级详情页列表
    @Select("SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title,\n" +
            "            i.title_img   AS titleImg,\n" +
            "			 i.classify    AS classify,\n"	+
            "            pm.praise_num AS praiseNum,\n" +
            "            i.weburl      AS webUrl,\n" +
            "            i.create_date AS createDate,\n" +
            "            i.auther AS author,\n" +
            "			 i.users  AS usersId,\n"+  
            "            u.login_name AS loginName,\n"+
            "            a.apply_record_id AS applyRecordId    ,\n" +
            "			 u.head_img    AS headImg    ,\n" +
            "			u.pet_name AS  petName       \n" +
            "        FROM\n" +
            "            users AS u,\n" +
            "            information_all AS i\n" +
            "            LEFT JOIN praise_num AS pm ON i.id = pm.information\n" +
            "            LEFT JOIN apply_record AS a  ON a.information =i.id  \n" +
            "        WHERE\n" +
            "            i.type =#{threeType} AND (i.checked=1 or i.checked=4) AND i.users=u.id  "+
            "        ORDER BY  i.create_date DESC")
   // @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectShowByThreeType(@Param("threeType") Integer threeType);
    
    
    
    
    
    //通过三级id获取三级详情页列表
    @Select("SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title,\n" +
            "            i.title_img   AS titleImg,\n" +
            "			 i.classify    AS classify,\n"	+
            "            i.weburl      AS webUrl,\n" +
            "            i.create_date AS createDate,\n" +
            "			 users.head_img AS headImg,\n" +
            "            users.login_name AS loginName,\n"	+
            "            pm.praise_num AS praiseNum,\n" +
            "            i.auther AS author,\n" +
            "			 i.users  AS usersId,\n"+  
            " 				users.pet_name AS petName  \n"+
            "        FROM\n" +
            "            information_all AS i\n" +
            "			inner join users on users.id=i.users "+
            "            left join praise_num AS pm ON i.id = pm.information\n" +
            "        WHERE\n" +
            "            i.type =#{threeType}   "+
            "        ORDER BY  i.create_date DESC")
//    @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectShowByThreeType1(@Param("threeType") Integer threeType);
    
  //说说
    @Select(" SELECT "
    		+ "information_all.id,"
    		+ "information_all.title,"
    		+ "information_all.create_date,"
    		+ "information_all.title_img,"
    		+ "information_all.info,"
    		+ "information_all.type,"
    		+ "information_all.show_push,"
    		+ "information_all.weburl," +
    		  "users.id as usersId," +
    		  "users.pet_name as petName,"+
    		  "users.head_img,users.login_name ,"+
    		  "pm.praise_num "+
            " from information_all "+
            "inner join users on users.id=information_all.users " +
              "left join praise_num AS pm ON pm.information = information_all.id " +
              "where classify=0 "+
            " and users.id=#{userId} order by information_all.id desc")
    List<BaseShow> selectByclassify(Long userId);
    
    
    /*
     * ---------------------------------以上方法修改与2018.4.17------------------------------------------
     */

    /**
     * 通过二级分类的分类名查找
     * 展示板块使用  selectShowByTwoTypeAll()整合为一个方法，只是不想改
     */
    List<BaseShow> selectByTwoTypeName(@Param("twoTypeName") String twoTypeName, @Param("userId") Long userId);
    //<!-- 4.21修改   -->
    @Select(" SELECT " +
            "        i.id, " +
            "        i.title, " +
            "		 i.classify, " +
            "        i.type, " +
            "        i.show_push as showPush, " +
            "        i.weburl as webUrl " +
            "        FROM " +
            "        information_all AS i " +
            "		 INNER JOIN three_type AS r ON i.type = r.id "	+		
            "        INNER JOIN two_type AS t ON r.two_type = t.id " +
            "        INNER JOIN one_type AS o ON t.one_type = o.id " +
            "             WHERE o.id = #{oneType} " +
            "                AND (i.title LIKE concat('%',#{word},'%') OR  i.info LIKE  concat('%', #{word}, '%')) " +
            "        order by i.id desc")
    List<Show> selectShowByOneTypeAll(@Param("oneType") Integer oneType, @Param("word") String word);

    @Select(" SELECT " +
            "            i.id, " +
            "            i.title       AS title, " +
            "		 i.classify	AS classify, " +
            "            i.type, " +		
            "        i.show_push as showPush, " +
            "            i.weburl      AS webUrl " +
            "        FROM " +
            "            information_all AS i  " +
            "        WHERE " +
            "            i.type =#{twoType}" +
            "        ORDER BY  i.create_date DESC")
    List<Show> selectShowByTwoTypeAll(@Param("twoType") Integer twoType);

    @Update("update information_all set show_push=#{push} where id=#{itemID}")
    void updateShowPush(@Param("itemID") Long itemID, @Param("push") Integer push);

    List<BaseShow> SelectByIds(List<Long> items);
    
    //推荐功能暂停，暂时采用发布时间排序，2018.4.13
    @Select("SELECT " +
            "i.id, " +
            "i.title AS title, " +
            "i.title_img AS titleImg," +
            "i.classify	AS classify," +
            "pm.praise_num AS praiseNum," +
            "i.weburl AS webUrl," +
            "i.create_date AS createDate," +
            "users.login_name AS loginName,"+
            "users.head_img AS headImg , "+
            "users.id AS usersId, " +
            "i.auther AS author, " +
            "users.pet_name AS petName "+
            "FROM " +
            "information_all AS i " +
            "LEFT JOIN praise_num AS pm ON i.id = pm.information " +
            "LEFT JOIN users on users.id=i.users "+
            "WHERE type is not null " +
            "AND i.checked=1 "+
            "AND (i.classify=1 or (i.classify=0 AND i.show_push!=0)) "+
            "ORDER BY " +
            "i.create_date DESC")
//    @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectOrderByPointNumberShowBean();
    @Select("SELECT " +
            "COUNT(*) " +
            "FROM " +
            " information_all AS i " +
            "WHERE " +
            "i.create_date >STR_TO_DATE('${date} 00:00:00','%Y-%m-%d %H:%i:%s')  AND i.users=#{usersId} and (i.checked=1 or i.checked=4 )")
    Integer selectBetweenCreateDate(@Param(value ="date") String date,@Param(value ="usersId")Long usersId);

   
    //4.18
    List<BaseShow> selectByThreeTypeName(@Param("threeTypeName") String threeTypeName, @Param("userId") Long userId);
    
    @Select(" SELECT " +
            "            i.id, " +
            "            i.title       AS title, " +
            "			 i.classify	AS classify," +
            "            i.type, " +
            "            i.show_push as showPush, " +
            "		     users.login_name as loginName, " +
            "			 users.head_img as headImg ," +
            "            users.pet_name as petName ," +
            "            i.weburl      AS webUrl " +
            "        FROM " +
            "            information_all AS i  " +
            "		 INNER JOIN users on users.id = i.users "+	
            "        WHERE " +
            "            i.type =#{threeType}" +
            "        ORDER BY  i.create_date DESC")
    List<Show> selectShowByThreeTypeAll(@Param("threeType") Integer threeType);
    
    @Select("select MAX(id) from information_all")
    Long seleceMAXId();
    /**
     * 如果需要更改置顶数量,Num修改成数量-1,
     * @return
     */                                                        //Num
    @Select("SELECT * FROM information_all ORDER BY id desc limit 2,1")
    InformationAll selectLastButOne();
    
    Integer changeId();
    

    
    
    @Select("select title,title_img,weburl from information_all where id = #{id} ")
    InformationAll selectinfo(Long id);
    
    @Select("select id from information_all order by id desc limit 0,1")
    Long getNewId();
    
    
    
    
    @Select("SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title,\n" +
            "            i.title_img   AS titleImg,\n" +
            "			 i.classify    AS classify,\n"	+
            "            i.weburl      AS webUrl,\n" +
            "            i.create_date AS createDate,\n" +
            "  			 users.login_name AS loginName,\n"+
            "			 users.head_img AS headImg,\n"+
            "            i.auther AS author,\n" +
            "			 users.pet_name AS petName,\n "+
            "			 i.users  AS usersId\n"+  
            "        FROM\n" +
            "            information_all AS i\n" +
            "        inner join users on users.id=i.users "+
            "        WHERE\n" +
            "            i.type =#{threeType}   "+
            "            and i.classify=1 " +
            "            and users.id=#{userId}    " +
            "        ORDER BY  i.create_date DESC")
   // @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
    List<BaseShow> selectype(@Param("threeType") Integer threeType,@Param("userId") Long userId);
    
    
    @Select("select T.count As commentNum,T.information As id from information_all inner join "+
    		" (select count(*) as count,information from comment group by information) as T "+
    		" on information_all.id=T.information")
    List<BaseShow> selectCommentNum();
    
    @Select("SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title,\n" +
            "            i.title_img   AS titleImg,\n" +
            "            i.weburl      AS webUrl,\n" +
            "			 i.users  AS usersId,\n"+ 
            "			 u.login_name AS loginName,\n"+
            "			 u.pet_name AS author,\n"+
            "			 u.head_img AS headImg,\n"+
            "			 u.pet_name AS petName\n"+
            "        FROM\n" +
            "            information_all AS i\n" +
            "			 inner join users AS u on u.id=i.users\n"+
            "        WHERE\n" +
            "            i.classify=1 " +
            "            and users=#{userId}    " +
            "            and i.competitive!=0    " +
            "        ORDER BY i.competitive")
    List<WindowShow> selectWindow(@Param("userId") Long userId);
    
    @Update("update information_all set competitive=#{competitive} where id=#{id}")
    Integer putElegant1(@Param("competitive")Integer competitive,@Param("id")Long id);
    
    @Update("update information_all set competitive=0 where id=#{id}")
    Integer putElegant0(Long id);
    
    @Select("select count(competitive) from information_all where users=#{userId} and competitive!=0")
    Integer MaxNum(Long userId);

    @Select("select competitive from information_all where id=#{id}")
    Integer selectCompetitive(Long id);
    
    @Select("SELECT " +
            "i.id," +
            "i.create_date as createDate," +
            "i.title," +
            "i.title_img AS titleImg," +
            "i.info," +
            "i.type," +
            "i.weburl," +
            "i.abstracte," +
            "i.auther," +
            "i.point_number AS pointNumber," +
            "i.users," +
            "i.checked," +
            "i.classify " +
            "FROM " +
            "information_all AS i " +
            "where users=#{id} and user_brand=1")
    InformationAll selectBrandArticle(Long id);
    
    @Select("select MAX(competitive) from information_all where users=#{id}")
    Integer selectMaxCompetitive(Long id);

}