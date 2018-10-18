package com.worldkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.ThreeType;



/**
 * 三级标签
 *
 */
@Repository
public interface ThreeTypeMapper extends BaseMapper<ThreeType> {
	/**
	 * 根据二级Type获取三级标签的内容（说说同样适用）
	 * @param id
	 * @return
	 */
	@Select("select id,type_name as typeName,type_img as typeImg,two_type as twoType,checked from three_type where two_type=#{id}")
	List<ThreeType> selectByTwo(@Param("id") Integer id);

	/**
	 * 查询文章全部分类
	 * @return
	 */
	@Select("select id,type_name as typeName,type_img as typeImg,two_type as twoType,checked from three_type")
	List<ThreeType> selectAll();

	/*
	 * 排序用的替换
	int replaceId(@Param("id") Integer id, @Param("replace") Integer replace);
	*/

	@Select("select  id,type_name as typeName,type_img as typeImg,two_type as twoType,checked from three_type where type_name=#{typeName}")
	ThreeType selectByTypeName(@Param("typeName") String typeName);
	
	/**
	 * 4.12薛秉臣添加  三级排序  以下两个方法
	 */
	@Update( "UPDATE three_type AS t1 SET t1.id = 0 WHERE t1.id = #{id};          "+ 
             "UPDATE three_type AS t1 SET t1.id = #{id} WHERE t1.id = #{replace}; "+
             "UPDATE three_type AS t1 SET t1.id = #{replace} WHERE t1.id = 0;")
	int replaceId(@Param("id") Integer id, @Param("replace") Integer replace);
	
	@Select("select id,type_name as typeName,type_img as typeImg,two_type as twoType,checked from three_type where id=#{id}")
	ThreeType selectById(@Param("id") Integer id);
	
	/*
	 * 通过一级id，全查一级id下面的所有三级标签
	 */
	@Select(  "SELECT \n"
			+ "ttt.*  \n"
			+ "FROM   \n"
			+ "one_type AS ot \n"
			+ "LEFT JOIN two_type AS tt ON tt.one_type = ot.id     \n"
			+ "LEFT JOIN three_type AS ttt ON ttt.two_type = tt.id \n"
			+ "WHERE  \n"
			+ "ot.id = tt.one_type AND tt.id = ttt.two_type AND ot.id =#{id}")
	List<ThreeType> selectAllByOne(@Param("id") Integer id);
	
	@Select("select three_type.type_name,three_type.checked from three_type where three_type.id=#{type}")
	ThreeType findByThreeType(Integer type);
	
	@Update("update three_type set checked = #{checked} where id=#{id} ")
	int checked(@Param("checked") Integer checked, @Param("id") Integer id);
	
	//5.11
		/**
		 * 添加小组
		 * @param group
		 * @return
		 */
		@Insert("INSERT INTO three_type (id, type_name, head_img,bg_img, two_type, content,user_id,checked) "+
	        "VALUES (null, #{typeName,jdbcType=VARCHAR}, #{headImg,jdbcType=VARCHAR}, #{bgImg,jdbcType=VARCHAR},226, "+
	        		"#{content,jdbcType=VARCHAR},#{userId,jdbcType=BIGINT},0)")
		int addGroup(ThreeType group);
		
		/**
		 * 更新小组人数(添加)
		 * @return
		 */
		@Update("update three_type set amount=amount+1 where id=#{id}")
		int updateAmount(Integer id);
		
		/**
		 * 更新小组人数（减）
		 * @param id
		 * @return
		 */
		@Update("update three_type set amount=amount-1 where id=#{id}")
		int updateAmountQuit(Integer id);
		
		/**
		 * 获取全部小组
		 * @return
		 */
		@Select("select id,type_name,head_img,bg_img,content,amount from three_type where two_type=226 and checked=1")
		List<ThreeType> findGroup();
		
		/**
		 * 根据用户获取所有已加入的小组
		 * @param id
		 * @return
		 */
		@Select("select three_type.id, type_name, two_type, three_type.head_img, three_type.bg_img,users.pet_name,three_type.content,three_type.amount from three_type inner join user_group on "+
				"three_type.id=user_group.group_id inner join users on users.id=user_group.user_id where users.id=#{id} and three_type.checked=1 order by three_type.id desc")
		List<ThreeType> findGroupByUser(Long id);
		/**
		 * 每日发现新小组功能(两个)
		 * @return
		 */
		@Select("SELECT id, type_name, two_type, head_img, bg_img, content,amount "+
				"FROM three_type "+
				"WHERE Id >= ("+
				"		(SELECT MAX(Id) FROM three_type where two_type=226)"+
				"		-(SELECT MIN(Id) FROM three_type where two_type=226)"+
				"		) * RAND() + "+
				"(SELECT MIN(Id) FROM three_type where two_type=226)  LIMIT 2")
		List<ThreeType> findRandGroup();
		
		/**
		 * 选择id最大的group
		 * @return
		 */
		@Select("select id, type_name, two_type, head_img, bg_img,content " + 
				" from three_type where id=(select id from "+
				" (select MAX(id) id from three_type) temp)")
		ThreeType selectMAX();
		
		@Select("select id, type_name, two_type, head_img, bg_img, content,user_id,amount" + 
				" from three_type where id=#{id} and checked=1")
		ThreeType selectGroupById(Integer id);
		
		@Select("select type_name from three_type where type_name=#{typeName} and two_type=226")
		String selectGroupByTypeName(@Param("typeName") String typeName);
		
		@Select("select id, type_name, two_type, head_img, bg_img, content,user_id,amount" + 
				" from three_type where checked=1 and two_type=226 ORDER BY amount desc limit 0,10")
		List<ThreeType> findHotGroup();
	
		@Select("select user_id from three_type where id=#{id} ")
		Long findUserId(Integer id);
	
		//更新小组
		@Update("update three_type set head_img=#{headImg} where id=#{id}")
		Integer updateHeadImg(ThreeType group);
				
		@Update("update three_type set bg_img=#{bgImg} where id=#{id}")
		Integer updateBgImg(ThreeType group);
				
		@Update("update three_type set content=#{content} where id=#{id}")
		Integer updateContent(ThreeType group);
		
		@Select("select id, type_name, two_type, head_img, bg_img, content,user_id,amount "+
				 "from three_type where user_id=#{userId}")
		List<ThreeType> findLeaderGroup(Integer userId);
		
		@Select("select three_type.id, type_name, two_type, three_type.head_img, three_type.bg_img, three_type.content,three_type.amount "+ 
				"from three_type "+
				"inner join user_group " +
				"on three_type.id=user_group.group_id "+
				"inner join users "+
				"on users.id=user_group.user_id "+
				"where users.id=#{userId} and three_type.checked=1 and three_type.user_id!=#{userId} order by three_type.id desc")	
		List<ThreeType> findJoinedGroup(Integer userId);
		
		@Select("SELECT * FROM three_type WHERE type_name LIKE concat('%',#{name},'%') and two_type=226")
		List<ThreeType> findXiaozu(String name);
}
