package com.worldkey.mapper;

import com.worldkey.entity.Presentor;
import com.worldkey.entity.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UsersMapper {
	int deleteByPrimaryKey(Long id);

	/**
	 * @param users
	 *            包含ID 和balance
	 * @return 受影响的记录数
	 */
	@Update("UPDATE users " + "SET balance = balance + #{balance} " + "WHERE " + " id = #{id}")
	int addBalance(Users users);

	int insert(Users record);

	int insertSelective(Users record);

	Users selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Users record);

	int updateByPrimaryKey(Users record);

	@Select("SELECT "
			+ "        id, login_name as loginName, password, pet_name as petName, sex, birthday, head_img as headImg, tel_num as telNum,  "
			+ "        email, create_date as createDate,jd,zs,kb,novice_gift_bag as noviceGiftBag,age, signature, emotional, height, weight, constellation, occupation, fond, personal_brand"
			+ "        FROM users " + "        WHERE login_name = #{loginName}")
	Users selectByLoginName(String loginName);
	
	@Select("SELECT "
			+ "        id, login_name as loginName, password, pet_name as petName, sex, birthday, head_img as headImg, tel_num as telNum,  "
			+ "        email, create_date as createDate,jd,zs,kb,novice_gift_bag as noviceGiftBag"
			+ "        FROM users " + "        WHERE pet_name = #{petName}")
	Users selectByPetName(String petName);

	
	@Select("SELECT "
			+ "        id, login_name as loginName, password, pet_name as petName, sex, birthday, head_img as headImg, tel_num as telNum,  "
			+ "        email, create_date as createDate,jd,zs,kb,novice_gift_bag as noviceGiftBag"
			+ "        FROM users " + "        WHERE tel_num = #{loginName}")
	Users selectBytelNums(String loginName);

	Users selectByToken(String token);
	
	@Select("select " + "id,tel_num as telNum," + "login_name as loginName," + "password,pet_name as petName,"
			+ "head_img as headImg " + " from users where tel_num=#{telNum}")
	Users selectBytelNum(String telNum);


	@Select("select balance from users where id=#{id}")
	BigDecimal selectBalanceById(Long id);

	int updateToken(Users u);

	@Select("select * from users")
	@ResultMap("com.worldkey.mapper.UsersMapper.BaseResultMap")
	List<Users> selectAll();

	@Select("SELECT COUNT(*) " + "FROM praise AS p " + "WHERE p.information IN ( " + "SELECT id "
			+ "FROM information_all AS i " + "WHERE i.users=#{id} AND p.`status`=1" + ")")
	Integer selectPraiseNum(@Param(value = "id") Long id);

	@Update("UPDATE users SET jd=jd+${releaseAwardsJd} WHERE id=${id}")
	Integer addJd(@Param(value = "releaseAwardsJd") Integer releaseAwardsJd, @Param(value = "id") Long id);

	@Select("SELECT  u.id,  u.jd,  u.zs, u.kb FROM  users AS u WHERE  u.id = #{id}")
	Users selectJdAndZsAndKbByPrimaryKey(@Param(value = "id") Long id);

	@Update("UPDATE users SET zs=zs+${zsNum} WHERE id=${id}")
	Integer addOrSubZs(@Param(value = "zsNum") Integer zsNum, @Param(value = "id") Long id);

	/*
	 * 2018.4.12此处改动 kb=Round ((kb+${kbNum}),2)
	 */
	@Update("UPDATE users SET kb=Round ((kb+${kbNum}),2) WHERE id=${id}")
	Integer addOrSubKb(@Param(value = "kbNum") BigDecimal kbNum, @Param(value = "id") Long id);

	/**
	 * 此处有改动 ROUND ((users.kb-${kbNum}),2) 保证K币数精度保留两位，且四舍五入
	 */
	@Update("UPDATE users SET users.zs=users.zs+${kbNum}*${kb2Zs},users.kb= ROUND ((users.kb-${kbNum}),2) "
			+ "WHERE users.id=#{id}")
	Integer kb2Zs(@Param("kbNum") Integer kbNum, @Param("id") Long id, @Param("kb2Zs") Integer kb2Zs);

	/**
	 * 更新用户表中新手礼包的状态，默认是 0，已经购买的话改为 1.
	 */
	@Update("UPDATE users SET novice_gift_bag  = 1 WHERE id = #{id}")
	Integer updateNoviceGiftBag(@Param("id") Long id);

	// 5,18
	@Select("select pet_name,login_name,head_img from users where id=#{id}")
	Users selectPetNameById(Long id);
	
	  //5.11
    @Select(
    		"select " +
    		"        users.id, users.login_name, users.password, users.pet_name, users.sex, users.birthday, users.head_img, users.tel_num,  " +
            "        users.email, users.create_date,users.jd,users.zs,users.kb,users.novice_gift_bag" +
            "        FROM users INNER JOIN user_group on users.id=user_group.user_id inner join three_type on three_type.id=user_group.group_id"+ 
    		"		 where three_type.id=#{groupId}" )
    List<Users> selectByGroup(Integer groupId);
    
    @Select("select " +
    		"        users.id, users.login_name, users.password, users.pet_name, users.sex, users.birthday, users.head_img, users.tel_num,  "+
            "		 users.email, users.create_date,users.jd,users.zs,users.kb,users.novice_gift_bag" +
    		"		 from users inner join three_type on three_type.user_id=users.id where three_type.id=#{groupId}")
    Users selectGroupLeader(Integer groupId);
    
    
    	@Select("select id,login_name,pet_name from users where id=#{id} ")
    	Users uuu(Long id);
    
    //6.7个人信息详细页面
    	@Update("update users set bg_img=#{bgImg},bg_content=#{bgContent} where id=#{id}")
    	Integer updateBackground(Users user);
    
		@Select("select id,emotional,height,weight,constellation,occupation,fond from "
			+ " users where id=#{id}")
		Users getUserDetail(Long id);

		@Update("update users set fond=#{fond} where id=#{id}")
		Integer updateFond(Users user);
		
		//6.25 粉丝与关注
		@Select("select * from users where id in (select follower as id from user_relation where leader=#{userId})")
		List<Users> getFans(Integer userId);
		
		@Select("select * from users where id in (select leader as id from user_relation where follower=#{userId})")
		List<Users> getStars(Integer userId);
		
		//6.26 根据id差一个对象
		@Select("select * from users where id = #{userId}")
		Users user(Integer userId);
		
		@Select("select DISTINCT g.users as pid from users as u "+
				"inner join information_all as i on i.users=u.id "+
				"inner join gift_record as g on g.to_information=i.id "+
				"where u.id=#{id}")
		List<Presentor> getAllPresentors(@Param("id")Long id);
		
		
		@Update("update users set personal_brand=1 where id=#{userId}")
		Integer updatePersonalBrand(@Param("userId")Long userId);

		@Select("select users.id from users,coffee_bar,coffee_scene,coffee_bar_user "
					+"where coffee_bar.id=coffee_scene.bar_id and users.id=coffee_bar_user.user_id "
					+"and coffee_bar.id=coffee_bar_user.coffee_bar_id "
					+"and coffee_bar.id=#{barId} and coffee_scene.scene=#{sceneId}")
		List<Integer> getUsersByBarIdAndRoomId(@Param("barId")Integer barId,@Param("sceneId")Integer sceneId);
}