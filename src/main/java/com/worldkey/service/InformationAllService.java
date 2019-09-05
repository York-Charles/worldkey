package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.Show;
import com.worldkey.entity.Users;
import com.worldkey.entity.WindowShow;
import com.worldkey.mapper.InformationAllMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface InformationAllService {
	List<InformationAll> findByIds(List<Long> ids);

	PageInfo<InformationAll> findAll(Integer pageNum, Integer pageSize);

	PageInfo<InformationAll> findByType(Integer pageNum, Integer pageSize, Integer type, String host,Integer checked,String title);

	Integer add(InformationAll vo, String host);
	// 说说

	PageInfo<BaseShow> findShuoshuo(Long userId, Integer pageNum, Integer pageSize);

	PageInfo<BaseShow> findShuoshuoDraft(Long userId, Integer pageNum, Integer pageSize);

	String add1(InformationAll vo, String host);

	String addShuo(InformationAll vo, String host);

	String addShuoDraft(InformationAll vo, String host);

	InformationAll update(InformationAll vo, String host, MultipartFile file, String oldTitleImg);

	InformationAll update(Long id);

	InformationAll info(Long id);

	InformationAll info(Long id, Long userID);

	PageInfo<InformationAll> findBySelective(Integer pageNum, Integer pageSize, InformationAll vo, String host);

	PageInfo<BaseShow> findBySelective1(Integer pageNum, Integer pageSize, InformationAll vo, String token);

	int delById(Long id);

	PageInfo<InformationAll> findByOneType(Integer pageNum, Integer pageSize, InformationAll vo);

	PageInfo<InformationAll> usersFindBySelective(Integer pageNum, Integer pageSize, InformationAll vo);

	/**
	 * 修改文章的状态 值与对应的状态 0 ->未审核 1 ->已审核 2 ->草稿 3 ->已下架
	 */
	int checked(InformationAll vo);

	/**
	 * 修改bug反馈状态
	 */
	int solve(InformationAll vo);

	InformationAllMapper getAllMapper();

	List<InformationAll> findOrderByPointNumber();

	List<InformationAll> userFindDraft(Users users);

	/**
	 * 替换记录的type属性，实现二级分类的排序功能
	 * 
	 * @param type
	 * @param replace
	 * @return
	 */
	int replaceType(Integer type, Integer replace);

	String changeTwoType(Integer changeId, Integer changeTwoType);

	/*
	 * 2018.4.19添加改变三级的方法
	 */
	String changeThreeType(Integer changeId, Integer changeThreeType);

	List<BaseShow> findShowByOneType(String oneTypeName, Long userId);

	List<BaseShow> findShowByTwoType(String twoTypeName, Long userId);

	List<Show> findShowByOneTypeAll(Integer oneType, String word, String host);

	List<Show> findShowByTwoTypeAll(Integer twoType);

	Integer showPush(Long itemID, Integer type, String host);

	Integer showPush1(Long itemID, Integer type, String host);

	List<BaseShow> findByIds1(List<Long> items);

	List<BaseShow> findOrderByPointNumber1();

	// 4.18
	List<BaseShow> findShowByThreeType(String threeTypeName, Long userId);

	// 4.18
	List<Show> findShowByThreeTypeAll(Integer threeType, String host);

	// 5.7
	Integer updateType(InformationAll vo);

	// 5.16
	Integer zhiding(Long id);

	Integer zhidingup(Long id);

	// 5.21
	Integer addShuoContent(InformationAll info);

	Integer updateImg(InformationAll info);

	String distribution(Long id, String host);

	InformationAll getById(Long id);

	PageInfo<BaseShow> getShuoComponent(Integer type, Integer pageNum, Integer pageSize, String token);

	PageInfo<BaseShow> selectByOneType(Integer type, Integer pageNum, Integer pageSize, String token);

	Integer putElegant(Long id, Long userId);

	List<WindowShow> putWindow(Long userId);

	Integer windowState(Long id);

	InformationAll brandArticle(Long id);

	String editBrandArticle(InformationAll vo, String host);

	InformationAll BrandExist(Long id);

	PageInfo<BaseShow> findStick(Integer pageNum, Integer pageSize, Integer type, String token);

	PageInfo<BaseShow> getshequ(Integer pageNum, Integer pageSize);

	// 文章说说删除
	Integer deleteInformation(Long id);

	// 编辑
	String findTitle(Long id);

	String findInfo(Long id);

	Integer compileto(Long id, String titleImgs, String title, String info);

	// 精选
	List<BaseShow> myCreate(Long id);

	InformationAll as(Long id);

}
