package com.worldkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.worldkey.entity.Apply;
import com.worldkey.entity.ApplyRecord;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Users;

/**
 * 乐赠模块
 */
@Repository
public interface ApplyMapper extends BaseMapper<Apply> {

	// 根据物品ID和用户(购买者)ID，查询申请表
	@Select("SELECT * FROM apply WHERE (users=#{users} AND information=#{information})")
	Apply findByuIdAndinfoId(@Param("users") Long users, @Param("information") Long information);

	// 更新申请状态 status为1
	@Update("UPDATE apply SET status='1'  WHERE (users=#{users} AND information=#{information})")
	Integer updateStatus1(@Param("users") Long users, @Param("information") Long information);

	// 更新申请状态 status为0
	@Update("UPDATE apply SET status='0'  WHERE (users=#{users} AND information=#{information})")
	Integer updateStatus0(@Param("users") Long users, @Param("information") Long information);

	// 根据物品ID查询申请数，集合
	@Select("SELECT apply_id,users,information,status,head_img AS headImg ,create_time FROM apply,users WHERE (information=#{information} AND status='1' AND apply.users=users.id)")
	List<Apply> findByinfoId(@Param("information") Long information);

	// 查询物品是否被选中，生成了订单
	@Select("SELECT * FROM apply_record WHERE information=#{information} ")
	ApplyRecord findByinfo(@Param("information") Long information);

	// 乐赠闲置形成的订单表
	@Select("SELECT apply_record_id,users_seller,information,users_buyer,head_img AS headImg,create_time,msg FROM apply_record,users WHERE (information=#{information} AND apply_record.users_buyer=users.id)")
	List<ApplyRecord> findByinfooo(@Param("information") Long information);

	// 查乐享闲置(twotype=101) 个人总分享物品数量 threetype BETWEEN 10034 AND 10043
	@Select(  "SELECT  \n"
			+ "COUNT(1)\n"
			+ "FROM    \n"
			+ "information_all AS i, \n"
			+ "three_type AS ttt     \n"
			+ "LEFT JOIN two_type AS tt ON tt.id=ttt.two_type \n"
			+ "WHERE   \n"
			+ "i.type=ttt.id AND ttt.two_type=101 AND i.users=#{users}")
	//@Select("SELECT COUNT(1) FROM information_all WHERE type BETWEEN 10034 AND 10044 AND users=#{users}")
	Integer findByinfo101(@Param("users") Long users);

	// 查分享中的物品 用查询的数量做减法(总分享数-分享成功数)
	@Select( "SELECT \n"
			+ " (SELECT COUNT(1)  \n"
			+ "  FROM             \n"
			+ "  information_all AS i, \n "
			+ "  three_type AS ttt     \n"
			+ "  LEFT JOIN two_type AS tt ON tt.id=ttt.two_type \n"
			+ "  WHERE   \n"
			+ "  i.type=ttt.id AND ttt.two_type=101 AND i.users=#{users}) - \n"
			+ "  (SELECT COUNT(1) FROM apply_record AS a WHERE a.users_seller=#{users})")
	//@Select("SELECT (SELECT COUNT(1) FROM information_all AS i  WHERE i.users=#{users} AND i.type BETWEEN 10034 AND 10044)-(SELECT COUNT(1) FROM apply_record AS a WHERE a.users_seller=#{users})")
	Integer findByinfoNotin(@Param("users") Long users);

	// 分享成功
	@Select("SELECT COUNT(1) FROM apply_record AS a WHERE a.users_seller=#{users}")
	Integer findByinfoOver(@Param("users") Long users);

	
	@Select( "SELECT  \n"
			+ "*      \n"
			+ "FROM    \n"
			+ "information_all AS i, \n"
			+ "three_type AS ttt     \n"
			+ "LEFT JOIN two_type AS tt ON tt.id=ttt.two_type \n"
			+ "WHERE   \n"
			+ "i.type=ttt.id AND ttt.two_type=101 AND i.users=#{users}\n"
			+ "ORDER BY i.create_date DESC")
	// 获取个人总分分享详情
	//@Select("SELECT * FROM information_all AS i WHERE i.type BETWEEN 10034 AND 10044 AND i.users=#{users} ORDER BY i.create_date DESC")
	List<InformationAll> findInfoList(@Param("users") Long users);
//	@Select(  "SELECT    \n"
//			+ "DISTINCT *\n"
//			+ "FROM      \n"
//			+ "information_all AS i, \n"
//			+ "three_type AS ttt     \n"
//			+ "LEFT JOIN two_type AS tt ON tt.id=ttt.two_type \n"
//			+ "WHERE   \n"
//			+ "i.type=ttt.id AND ttt.two_type=101 AND i.users=#{users} AND\n"
//			+ "(NOT EXISTS (SELECT * FROM apply_record AS a WHERE i.id=a.information AND i.users=#{users} )) \n"
//			+ "ORDER BY i.create_date DESC")
	// 分享详情，进行中
	@Select("SELECT  DISTINCT * FROM information_all AS i WHERE i.type BETWEEN 10034 AND 10044 AND i.users=#{users} AND ( not  EXISTS(SELECT * FROM apply_record AS a WHERE i.id=a.information AND i.users=#{users} )) ORDER BY i.create_date DESC")
	List<InformationAll> findInfoListIng(@Param("users") Long users);
//	@Select(  "SELECT    \n"
//			+ "DISTINCT *\n"
//			+ "FROM      \n"
//			+ "information_all AS i, \n"
//			+ "three_type AS ttt     \n"
//			+ "LEFT JOIN two_type AS tt ON tt.id=ttt.two_type \n"
//			+ "WHERE   \n"
//			+ "i.type=ttt.id AND ttt.two_type=101 AND i.users=#{users} AND \n"
//			+ "(EXISTS (SELECT * FROM apply AS a WHERE  a.information=i.id AND a.checked=1 AND a.status=1)) \n"
//			+ "ORDER BY i.create_date DESC")
	// 分享详情，分享成功后的订单
	@Select("SELECT * FROM information_all AS i WHERE i.users=#{users} ANd i.type BETWEEN 10034 AND 10044 AND  (EXISTS (SELECT * FROM apply AS a WHERE  a.information=i.id AND a.checked=1 AND a.status=1))ORDER BY i.create_date DESC")
	List<InformationAll> findInfoListEnd(@Param("users") Long users);

	@Select("SELECT * FROM apply AS a WHERE a.information=#{information} ANd a.users=#{users} AND  a.status=1")
	Apply findApplyStatus(@Param("information") Long information, @Param("users") Long users);

	/*
	 * 未选中页面 人员展示
	 */
	@Select("SELECT u.id,u.login_name,u.pet_name,u.head_img FROM users AS u,apply AS a WHERE a.information=#{information} AND a.users=u.id AND a.checked=0 AND a.status=1 ORDER BY  a.create_time DESC")
	List<Users> showUsers0(@Param("information") Long information);

	/*
	 * 已选中用户展示
	 */
	@Select("SELECT u.id,u.login_name,u.pet_name,u.head_img FROM users AS u,apply AS a WHERE a.information=#{information} AND a.users=u.id AND a.checked=1 AND a.status=1 ORDER BY  a.create_time DESC")
	List<Users> showUsers1(@Param("information") Long information);

	/*
	 * 未选中数量
	 */
	@Select("select COUNT(1) from apply AS a WHERE a.information=#{information} AND a.checked=0 ANd a.status=1")
	Integer showApplyNum0(@Param("information") Long information);

	/*
	 * 已选中数量
	 */
	@Select("select COUNT(1) from apply AS a WHERE a.information=#{information} AND a.checked=1 ANd a.status=1")
	Integer showApplyNum1(@Param("information") Long information);

	/*
	 * 获取当前文章的总报名数
	 */
	@Select("select COUNT(1) from apply AS a WHERE a.information=#{information} AND  a.status=1")
	Integer showApplyNum2(@Param("information") Long information);

	/*
	 * 通过个人id 获得申请表实体
	 */
	
	  @Select("SELECT * FROM apply AS a  WHERE a.users=#{users} ")
	  List<Apply> findApplyObject(@Param("users") Long users );
	  
	  //我的申请，进行中查询
	  @Select("SELECT\n" +
            "            i.id,\n" +
            "            i.title       AS title,\n" +
            "            i.title_img   AS titleImg,\n" +
            "            pm.praise_num AS praiseNum,\n" +
            "            i.weburl      AS webUrl,\n" +
            "            i.create_date AS createDate,\n" +
            "            i.auther AS author,\n" +
            "			 i.users  AS usersId,\n"+  
            "            u.login_name AS loginName,\n"+
            "            u.head_img AS headImg\n"+
            "        FROM\n" +
            "            users AS u,\n" +
            "            apply AS a \n" +
            "            INNER JOIN information_all AS i ON i.id = a.information   \n"+
            "            LEFT  JOIN praise_num     AS pm ON i.id = pm.information  \n"+
            "        WHERE\n" +
            "            a.users=#{users} AND i.users=u.id AND a.status=1 AND a.checked=0 \n"+
            "        ORDER BY  i.create_date DESC")
	    @ResultMap(value = "com.worldkey.mapper.InformationAllMapper.BaseShowResultMap")
	    List<BaseShow> findBaseShow(@Param("users") Long users);
	 
	    //删除成交记录
	    @Delete("DELETE FROM apply_record  WHERE apply_record.users_buyer=#{users} AND apply_record.information=#{information}")
	    void deleteApplyRecord(@Param("users") Long users,@Param("information") Long information);
	  	// 更新申请状态checked为0
		@Update("UPDATE apply SET checked='0'  WHERE (users=#{users} AND information=#{information})")
		Integer updateChecked0(@Param("users") Long users, @Param("information") Long information);
		
		//薛秉臣   查看物品选中状态 1已选中 0未选中
		@Select("SELECT chedked FROM apply WHERE information=#{information} ")
		Integer Checked(@Param("information") Long information);
		
		
		
		

}
