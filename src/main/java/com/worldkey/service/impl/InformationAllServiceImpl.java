package com.worldkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.*;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.JdDetailMapper;
import com.worldkey.mapper.PraiseMapper;
import com.worldkey.service.BrowsingHistoryService;
import com.worldkey.service.InformationAllService;
import com.worldkey.service.ThreeTypeService;
import com.worldkey.service.TwoTypeService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import com.worldkey.worldfilter.WordFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author HP
 */
@Service
public class InformationAllServiceImpl implements InformationAllService {
	@Resource
	private InformationAllMapper allMapper;
	/*
	 * @Resource private ImageService imageService;
	 */
	@Resource
	private WordFilter wordFilter;
	@Resource
	private BrowsingHistoryService browsingHistoryService;
	@Resource
	private UsersService usersService;
	@Resource
	private TwoTypeService twoTypeService;
	@Resource
	private ThreeTypeService threeTypeService;
	@Resource
	private SystemProperties systemProperties;
	@Resource
	private InformationAllMapper informationAllMapper;
	@Resource
	private PraiseMapper praiseMapper;

	private Logger log = LoggerFactory.getLogger(InformationAllServiceImpl.class);

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 通过ID的集合查询
	 *
	 * @param ids
	 *            ID的集合
	 * @return Information的集合
	 */
	@Override
	public List<InformationAll> findByIds(List<Long> ids) {
		return this.allMapper.selectByPermaryKeys(ids);
	}

	/**
	 * 查询，无查询参数，返回所有记录的信息
	 */
	@Override
	public PageInfo<InformationAll> findAll(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return new PageInfo<>(this.allMapper.findAll());
	}

	/**
	 * 通过二级分类查询
	 */
	@Override
	public PageInfo<InformationAll> findByType(Integer pageNum, Integer pageSize, Integer type,String host) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<InformationAll> list = this.allMapper.selectByType(type);
        for(InformationAll a:list){
			a.setWeburl("http://" + host + "/informationall/info1/" + a.getId());
		}
		return new PageInfo<>(list);
	}

	/**
	 * 添加记录
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer add(InformationAll vo, String host) {
		// 判定是否草稿
		boolean isDraftBox = Objects.equals(vo.getChecked(), 2);
		// 判定是否展示板块内容
		boolean isDraftBox4 = Objects.equals(vo.getChecked(), 4);
		if (isDraftBox4) {
			// 添加发布奖励金豆部分
			String format = simpleDateFormat.format(new Date());
			Integer integer = this.allMapper.selectBetweenCreateDate(format, vo.getUsers().getId());
			if (integer < systemProperties.getReleaseAwardsFrequency()) {
				// 添加用户的发布奖励
				this.usersService.addJd(systemProperties.getReleaseAwardsJd(), vo.getUsers().getId());
				// 记录用户的发布奖励金豆明细
				JdDetail jdDetail = new JdDetail(new Date(), systemProperties.getReleaseAwardsJd(),
						JdRewardType.releaseReward.getIndex(), vo.getUsers().getId(), null, null);
				jdDetail.setMsg("发布获得" + systemProperties.getReleaseAwardsJd() + "金豆");
				this.jdDetailMapper.insert(jdDetail);
			}
		}
		// 判定是否是展示推送的内容
		boolean isDraftBox1 = Objects.equals(vo.getChecked(), 1);
		// 不是上三类就一数据库默认值为初始值
		if (!isDraftBox && !isDraftBox4 && !isDraftBox1) {
			vo.setChecked(null);
		}
		// 敏感词检测
		vo = this.worldFilter(vo);
		// 将包含的图片标记为以用---改为发布时设置
		/* = this.imageService.imageHandle(vo.getInfo(), host); */
		vo.setCreateDate(new Date());
		vo.setClassify(1);
		vo.setStick(0);
		vo.setState(0);
		this.allMapper.insertSelective(vo);
		Long id = this.allMapper.seleceMAXId();
		InformationAll info = this.allMapper.selectLastButOne();

		InformationAll up = new InformationAll();
		up.setId(vo.getId());

		up.setWeburl("http://" + host + "/informationall/info/" + vo.getId());
		return this.allMapper.updateByPrimaryKeySelective(up);
	}

	/**
	 * 添加记录
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String add1(InformationAll vo, String host) {
		boolean isDraftBox = Objects.equals(vo.getChecked(), 2);
		boolean isDraftBox4 = Objects.equals(vo.getChecked(), 4);
		if (isDraftBox4) {
			Integer integer = this.allMapper.selectBetweenCreateDate(simpleDateFormat.format(new Date()),
					vo.getUsers().getId());
			if (systemProperties.getReleaseAwardsFrequency() > integer) {
				this.usersService.addJd(systemProperties.getReleaseAwardsJd(), vo.getUsers().getId());
				JdDetail jdDetail = new JdDetail(new Date(), systemProperties.getReleaseAwardsJd(),
						JdRewardType.releaseReward.getIndex(), vo.getUsers().getId(), null, null);
				jdDetail.setMsg("发布获得" + systemProperties.getReleaseAwardsJd() + "金豆");
				this.jdDetailMapper.insert(jdDetail);
			}
		}
		boolean isDraftBox1 = Objects.equals(vo.getChecked(), 1);
		if (!isDraftBox && !isDraftBox4 && !isDraftBox1) {
			vo.setChecked(null);
		}
		vo = this.worldFilter(vo);
		vo.setCreateDate(new Date());
		vo.setClassify(1);
		vo.setStick(0);
		vo.setState(0);
		this.allMapper.insertSelective(vo);
		InformationAll up = new InformationAll();
		up.setId(vo.getId());
		up.setWeburl("http://" + host + "/informationall/info/" + vo.getId());
		this.allMapper.updateByPrimaryKeySelective(up);
		return "http://" + host + "/informationall/info/" + vo.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer showPush(Long itemID, Integer type, String host) {
		InformationAll info = this.allMapper.selectByPrimaryKey(itemID);
		info.setType(type);
		info.setId(null);
		info.setChecked(1);
		info.setShowPush(1);
		this.allMapper.updateShowPush(itemID, 1);
		return this.add(info, host);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer showPush1(Long itemID, Integer type, String host) {
		InformationAll info = this.allMapper.selectByPrimaryKey(itemID);
		info.setType(type);
		info.setId(null);
		info.setChecked(1);
		info.setShowPush(1);
		this.allMapper.updateShowPush(itemID, 1);
		return this.add(info, host);
	}

	@Override
	public List<BaseShow> findByIds1(List<Long> items) {
		return this.allMapper.SelectByIds(items);
	}

	@Override
	public List<BaseShow> findOrderByPointNumber1() {
		List<BaseShow> list = this.allMapper.selectOrderByPointNumberShowBean();
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
		 for(int i = 0;i<list.size();i++){
  			for(int j =0;j<commentNum.size();j++){
  				if(list.get(i).getId().equals((commentNum.get(j).getId()))){
  					list.get(i).setCommentNum(commentNum.get(j).getCommentNum());
  					break;
  				}
  			}
  		}
		return list;
	}

	/**
	 * 更新记录， weburl不可变，CreateDate不可变
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@CachePut(value = "info", key = "'InformationAll'+#vo.id")
	public InformationAll update(InformationAll vo, String host, MultipartFile file, String oldTitleImg) {
		if (vo.getTitleImg() != null) {
			List<String> titleImgList = Arrays.asList(vo.getTitleImg().split(","));
			Arrays.stream(oldTitleImg.split(",")).filter(item -> !titleImgList.contains(item)).forEach(item -> {
				String localImgSrc = item.replace("http://" + host, "/mnt");
				boolean delete = new File(localImgSrc).delete();
				log.debug("删除图片成功 " + delete + " :" + localImgSrc);
			});
		}
		vo = this.worldFilter(vo);
		this.allMapper.updateByPrimaryKeySelective(vo);
		return info(vo.getId());
	}

	/**
	 * 点击量加一
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@CachePut(value = "info", key = "'InformationAll'+#id")
	public InformationAll update(Long id) {
		this.allMapper.addPointNumber(id);
		return info(id);
	}

	/**
	 * 获取详情
	 *
	 * @return InformationAll对象
	 */
	@Override
//	@Cacheable(value = "info", key = "'InformationAll'+#id")
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InformationAll info(Long id) {
		this.allMapper.addPointNumber(id);
		return this.allMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
//	@Cacheable(value = "info", key = "'InformationAll'+#itemID")
	public InformationAll info(Long itemID, Long userID) {
		// 查看是否浏览过本条记录
		BrowsingHistory bh = this.browsingHistoryService.findByUserAndItem(itemID, userID);
		if (bh == null) {
			// 保存浏览记录
			this.browsingHistoryService.insert(new BrowsingHistory(itemID, userID, 1.0F, new Date()));
		}
		return info(itemID);
	}
	
	@Override
	public PageInfo<InformationAll> findBySelective(Integer pageNum, Integer pageSize, InformationAll vo,String host) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<InformationAll> list = this.allMapper.selectBySelective(vo);
		 for(InformationAll a:list){
				a.setWeburl("http://" + host + "/informationall/info1/" + a.getId());
			}
		return new PageInfo<>(list);
	}

	/**
	 * 以InformationAll属性为区别查询，type，title模糊查询
	 */
	@Override
	public PageInfo<BaseShow> findBySelective1(Integer pageNum, Integer pageSize, InformationAll vo,String token) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<BaseShow> list = this.allMapper.selectBySelective1(vo);
		if(token!=null){
			Users user = usersService.findByToken(token);
	        for(BaseShow l:list){
	        	
	        	Integer i = this.praiseMapper.i(user.getId(),l.getId());
	        	l.setStatus(i);
	        }
        }
		return new PageInfo<>(list);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@CacheEvict(value = "info", key = "'InformationAll'+#id")
	@Override
	public int delById(Long id) {
		return this.allMapper.deleteByPrimaryKey(id);
	}

	@Override
	public PageInfo<InformationAll> findByOneType(Integer pageNum, Integer pageSize, InformationAll vo) {
		PageHelper.startPage(pageNum, pageSize, true);
		return new PageInfo<>(this.allMapper.selectByOneType(vo));
	}
	


	/**
	 * 查询用户的记录
	 *
	 * @return PageInfo
	 */
	@Override
	public PageInfo<InformationAll> usersFindBySelective(Integer pageNum, Integer pageSize, InformationAll vo) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<InformationAll> list = this.allMapper.usersSelectBySelective(vo);
		return new PageInfo<>(list);
	}

	@Override
	public InformationAllMapper getAllMapper() {
		return allMapper;
	}

	@Override
	public List<InformationAll> findOrderByPointNumber() {
		return this.allMapper.selectOrderByPointNumber();
	}

	@Override
	public List<InformationAll> userFindDraft(Users users) {
		return this.allMapper.selectUsersDraft(users.getId());
	}

	/**
	 * 替换记录的类型ID
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int replaceType(Integer type, Integer replace) {
		return this.allMapper.replaceType(type, replace);
	}

	/**
	 * 修改记录的分类
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String changeTwoType(Integer changeId, Integer changeTwoType) {
		allMapper.changeTwoType(changeId, changeTwoType);
		return twoTypeService.findById(changeTwoType).getTypeName();
	}

	/*
	 * 修改三级记录分类4.19
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String changeThreeType(Integer changeId, Integer changeThreeType) {
		allMapper.changeThreeType(changeId, changeThreeType);
		return threeTypeService.findById(changeThreeType).getTypeName();
	}

	/**
	 * 展示一级分类查找
	 *
	 * @param oneTypeName
	 *            一级分类的名称
	 */
	@Override
	public List<BaseShow> findShowByOneType(String oneTypeName, Long userId) {
		return this.allMapper.selectByOneTypeName(oneTypeName, userId);
	}

	@Override
	public List<BaseShow> findShowByTwoType(String twoTypeName, Long userId) {
		return this.allMapper.selectByTwoTypeName(twoTypeName, userId);
	}

	@Override
	public List<Show> findShowByOneTypeAll(Integer oneType, String word,String host) {
		
				List<Show> list=this.allMapper.selectShowByOneTypeAll(oneType, word);
				 for(Show a:list){
						a.setWebUrl("http://" + host + "/informationall/info1/" + a.getId());
					}
				
				return list;
	}

	@Override
	public List<Show> findShowByTwoTypeAll(Integer twoType) {
		return this.allMapper.selectShowByTwoTypeAll(twoType);
	}

	@Resource
	private JdDetailMapper jdDetailMapper;

	/**
	 * 修改文章的状态 值与对应的状态 0 ->未审核 1 ->已审核 2 ->草稿 3 ->已下架
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	@CacheEvict(value = "info", key = "'InformationAll'+#vo.id")
	public int checked(InformationAll vo) {
		Date date = new Date();
		vo.setCreateDate(date);
		// 发布时的操作
		/*
		 * 后台发布文章，审核通过时候添加金豆，vo对象无法获取users属性，会报空，暂不使用 if (vo.getChecked() == 1)
		 * { //发布奖励金豆 String format = simpleDateFormat.format(new
		 * Date(System.currentTimeMillis())); Integer integer =
		 * this.allMapper.selectBetweenCreateDate(format,
		 * vo.getUsers().getId()); if (integer <
		 * systemProperties.getReleaseAwardsFrequency()) { //为用户添加奖励的金豆 Users
		 * byLoginName =
		 * this.usersService.findByLoginName(vo.getUsers().getLoginName());
		 * Users users = new Users(); users.setId(byLoginName.getId());
		 * users.setJd(byLoginName.getJd() +
		 * systemProperties.getReleaseAwardsJd());
		 * this.usersService.updateByPrimaryKeySelective(users); //记录用户的金豆收支明细
		 * JdDetail jdDetail = new JdDetail(new Date(),
		 * systemProperties.getReleaseAwardsJd(),
		 * JdRewardType.releaseReward.getIndex(), users.getId(), null, null);
		 * jdDetail.setMsg("发布获得" + systemProperties.getReleaseAwardsJd() +
		 * "金豆"); jdDetailMapper.insert(jdDetail); } return
		 * allMapper.updateByPrimaryKeySelective(vo); }
		 */
		return this.allMapper.checked(vo);
	}
	
	/**
	 * 修改bug状态solve
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	@CacheEvict(value = "info", key = "'InformationAll'+#vo.id")
	public int solve(InformationAll vo) {
		Date date = new Date();
		vo.setCreateDate(date);
		return this.allMapper.solve(vo);
	}

	private InformationAll worldFilter(InformationAll vo) {
		String abstracte = vo.getAbstracte();
		String title = vo.getTitle();
		String info = vo.getInfo();
		if (abstracte != null) {
			vo.setAbstracte(wordFilter.doFilter(vo.getAbstracte()));
		}
		if (title != null) {
			vo.setTitle(wordFilter.doFilter(vo.getTitle()));
		}
		if (info != null) {
			vo.setInfo(wordFilter.doFilter(vo.getInfo()));
		}
		return vo;
	}

	// 4.18
	@Override
	public List<BaseShow> findShowByThreeType(String threeTypeName, Long userId) {
		return this.allMapper.selectByThreeTypeName(threeTypeName, userId);
	}

	// 4.18
	@Override
	public List<Show> findShowByThreeTypeAll(Integer threeType,String host) {
		 List<Show> list=this.allMapper.selectShowByThreeTypeAll(threeType);
		 for(Show a:list){
				a.setWebUrl("http://" + host + "/informationall/info1/" + a.getId());
			}
		 
		 return list;
	}

	@Override
	public Integer updateType(InformationAll vo) {
		return this.allMapper.updateByPrimaryKey(vo);
	}

	@Override
	public PageInfo<BaseShow> findShuoshuo(Long userId, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<BaseShow> baseShows = this.allMapper.selectByclassify(userId);
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
		for (int i = 0; i < baseShows.size(); i++) {
			baseShows.get(i).setImgNum(this.getImgNum(baseShows.get(i).getTitleImg()));
			for (int j = 0; j < commentNum.size(); j++) {
				if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
					baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
					break;
				}
			}
		}
		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return baseShowPageInfo;
	}
	
	@Override
	public PageInfo<BaseShow> findShuoshuoDraft(Long userId, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<BaseShow> baseShows = this.allMapper.selectByclassifyDraft(userId);
		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return baseShowPageInfo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer zhiding(Long id) {
		// TODO Auto-generated method stub
		return this.allMapper.updateStick(id);
	}

	@Override
	public Integer addShuoContent(InformationAll info) {
		return this.allMapper.insertSelective(info);
	}

	@Override
	public Integer updateImg(InformationAll info) {
		// TODO Auto-generated method stub
		return this.allMapper.updateByPrimaryKeySelective(info);
	}

	@Override
	public String distribution(Long id, String host) {
		// TODO Auto-generated method stub
		InformationAll info = new InformationAll();
		info.setId(id);
		info.setChecked(4);
		info.setCreateDate(new Date());
		info.setClassify(0);
		info.setStick(0);
		info.setWeburl("http://" + host + "/informationall/info/" + id);
		this.allMapper.updateByPrimaryKey(info);
		return info.getWeburl();
	}

	@Override
	public InformationAll getById(Long id) {
		// TODO Auto-generated method stub
		return this.allMapper.selectByPrimaryKey(id);
	}

	@Override
	public String addShuo(InformationAll vo, String host) {
		/*
		 * //判定是否草稿 boolean isDraftBox = Objects.equals(vo.getChecked(), 2);
		 * //判定是否展示板块内容 boolean isDraftBox4 = Objects.equals(vo.getChecked(),
		 * 4); if (isDraftBox4) { //添加发布奖励金豆部分 String format =
		 * simpleDateFormat.format(new Date()); Integer integer =
		 * this.allMapper.selectBetweenCreateDate(format,
		 * vo.getUsers().getId()); if (integer <
		 * systemProperties.getReleaseAwardsFrequency()) { //添加用户的发布奖励
		 * this.usersService.addJd(systemProperties.getReleaseAwardsJd(),
		 * vo.getUsers().getId()); //记录用户的发布奖励金豆明细 JdDetail jdDetail = new
		 * JdDetail(new Date(), systemProperties.getReleaseAwardsJd(),
		 * JdRewardType.releaseReward.getIndex(), vo.getUsers().getId(), null,
		 * null); jdDetail.setMsg("发布获得" + systemProperties.getReleaseAwardsJd()
		 * + "金豆"); this.jdDetailMapper.insert(jdDetail); } } //判定是否是展示推送的内容
		 * boolean isDraftBox1 = Objects.equals(vo.getChecked(), 1);
		 * //不是上三类就一数据库默认值为初始值 if (!isDraftBox && !isDraftBox4 && !isDraftBox1)
		 * { vo.setChecked(null); }
		 */
		// 敏感词检测
		vo = this.worldFilter(vo);
		vo.setClassify(0);
		vo.setStick(0);
		vo.setState(0);;
		vo.setDraft(1);
		if(vo.getType()==10468||vo.getType()==10463){
			vo.setSolve(1);
		}
		// 将包含的图片标记为以用---改为发布时设置
		/* = this.imageService.imageHandle(vo.getInfo(), host); */
		vo.setCreateDate(new Date());
		this.allMapper.insertSelective(vo);
		InformationAll up = new InformationAll();
		up.setId(vo.getId());
		up.setWeburl("http://" + host + "/informationall/info/" + vo.getId());
		this.allMapper.updateByPrimaryKeySelective(up);
		log.info(vo+"vo");
		return up.getWeburl();
	}
	
	
	
	@Override
	public String addShuoDraft(InformationAll vo, String host) {
		/*
		 * //判定是否草稿 boolean isDraftBox = Objects.equals(vo.getChecked(), 2);
		 * //判定是否展示板块内容 boolean isDraftBox4 = Objects.equals(vo.getChecked(),
		 * 4); if (isDraftBox4) { //添加发布奖励金豆部分 String format =
		 * simpleDateFormat.format(new Date()); Integer integer =
		 * this.allMapper.selectBetweenCreateDate(format,
		 * vo.getUsers().getId()); if (integer <
		 * systemProperties.getReleaseAwardsFrequency()) { //添加用户的发布奖励
		 * this.usersService.addJd(systemProperties.getReleaseAwardsJd(),
		 * vo.getUsers().getId()); //记录用户的发布奖励金豆明细 JdDetail jdDetail = new
		 * JdDetail(new Date(), systemProperties.getReleaseAwardsJd(),
		 * JdRewardType.releaseReward.getIndex(), vo.getUsers().getId(), null,
		 * null); jdDetail.setMsg("发布获得" + systemProperties.getReleaseAwardsJd()
		 * + "金豆"); this.jdDetailMapper.insert(jdDetail); } } //判定是否是展示推送的内容
		 * boolean isDraftBox1 = Objects.equals(vo.getChecked(), 1);
		 * //不是上三类就一数据库默认值为初始值 if (!isDraftBox && !isDraftBox4 && !isDraftBox1)
		 * { vo.setChecked(null); }
		 */
		// 敏感词检测
		vo = this.worldFilter(vo);
		vo.setClassify(0);
		vo.setStick(0);
		vo.setState(0);
		vo.setDraft(0);
		if(vo.getType()==10468||vo.getType()==10463){
			vo.setSolve(1);
		}
		// 将包含的图片标记为以用---改为发布时设置
		/* = this.imageService.imageHandle(vo.getInfo(), host); */
		vo.setCreateDate(new Date());
		this.allMapper.insertSelective(vo);
		InformationAll up = new InformationAll();
		up.setId(vo.getId());
		up.setWeburl("http://" + host + "/informationall/info/" + vo.getId());
		this.allMapper.updateByPrimaryKeySelective(up);
		log.info(vo+"vo");
		return up.getWeburl();
	}
	
	
	
	

	@Override
	public PageInfo<BaseShow> getShuoComponent(Integer type, Integer pageNum, Integer pageSize, String token) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<BaseShow> baseShows = informationAllMapper.selectShowByThreeType1(type);
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();

		if (token != null) {
			for (int i = 0; i < baseShows.size(); i++) {
				Users user = usersService.findByToken(token);
				Praise p = new Praise();
				p.setUsers(user);
				p.setInformation(Integer.parseInt(baseShows.get(i).getId() + ""));
				List<Praise> isPraise = praiseMapper.selectExist(p);
				if (isPraise.size() == 0) {
					baseShows.get(i).setIsPraise(0);
				} else {
					baseShows.get(i).setIsPraise(1);
				}
				baseShows.get(i).setImgNum(this.getImgNum(baseShows.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < baseShows.size(); i++) {
				baseShows.get(i).setImgNum(this.getImgNum(baseShows.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		}

		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return baseShowPageInfo;
	}

	private Integer getImgNum(String s) {
		String[] arr = s.split(",");
		return arr.length;
	}

	@Override
	public PageInfo<BaseShow> selectByOneType(Integer type, Integer pageNum, Integer pageSize, String token) {
		PageHelper.startPage(pageNum, pageSize);
		List<BaseShow> baseShows = informationAllMapper.selectShowAllByOneType(type);
		if(token!=null){
			Users user = usersService.findByToken(token);
	        for(BaseShow l:baseShows){
	        	
	        	Integer i = this.praiseMapper.i(user.getId(),l.getId());
	        	l.setStatus(i);
	        }
        }
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
		if (token != null) {
			for (int i = 0; i < baseShows.size(); i++) {
				Users user = usersService.findByToken(token);
				Praise p = new Praise();
				p.setUsers(user);
				p.setInformation(Integer.parseInt(baseShows.get(i).getId() + ""));
				List<Praise> isPraise = praiseMapper.selectExist(p);
				if (isPraise.size() == 0) {
					baseShows.get(i).setIsPraise(0);
				} else {
					baseShows.get(i).setIsPraise(1);
				}
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < baseShows.size(); i++) {
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		}

		PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		return baseShowPageInfo;
	}
	@Override
	public Integer putElegant(Long id,Long userId) {
		// TODO Auto-generated method stu
		InformationAll info = this.allMapper.selectByPrimaryKey(id);
		if(info.getCompetitive()==0){
			if(this.informationAllMapper.MaxNum(userId)>=6){
				return 2;
			}
			Integer val = this.informationAllMapper.selectMaxCompetitive(userId);
			this.allMapper.putElegant1(val+1,id);
			return 1;
		}else{
			this.allMapper.putElegant0(id);
			return 0;
		}
	}

	@Override
	public List<WindowShow> putWindow(Long userId) {
		// TODO Auto-generated method stub
		return this.informationAllMapper.selectWindow(userId);
	}

	@Override
	public Integer windowState(Long id) {
		// TODO Auto-generated method stub
		return this.informationAllMapper.selectCompetitive(id);
	}

	@Override
	public InformationAll brandArticle(Long id) {
		// TODO Auto-generated method stub
		return this.informationAllMapper.selectBrandArticle(id);
	}

	@Override
	public String editBrandArticle(InformationAll vo, String host) {
		// TODO Auto-generated method stub
		this.informationAllMapper.updateByPrimaryKeySelective(vo);
		return "http://" + host + "/informationall/info/" + vo.getId();
	}

	@Override
	public InformationAll BrandExist(Long id) {
		// TODO Auto-generated method stub
		return this.informationAllMapper.selectBrandArticle(id);
	}

	@Override
	public Integer zhidingup(Long id) {
		// TODO Auto-generated method stub
		return this.informationAllMapper.updateStickup(id);
	}

	@Override
	public PageInfo<BaseShow> findStick(Integer pageNum, Integer pageSize, Integer type,String token) {
		PageHelper.startPage(pageNum, pageSize);
		List<BaseShow> baseShows= this.allMapper.findStick(type);
		List<BaseShow> commentNum = informationAllMapper.selectCommentNum();

		if (token != null) {
			for (int i = 0; i < baseShows.size(); i++) {
				Users user = usersService.findByToken(token);
				Praise p = new Praise();
				p.setUsers(user);
				p.setInformation(Integer.parseInt(baseShows.get(i).getId() + ""));
				List<Praise> isPraise = praiseMapper.selectExist(p);
				if (isPraise.size() == 0) {
					baseShows.get(i).setIsPraise(0);
				} else {
					baseShows.get(i).setIsPraise(1);
				}
				baseShows.get(i).setImgNum(this.getImgNum(baseShows.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < baseShows.size(); i++) {
				baseShows.get(i).setImgNum(this.getImgNum(baseShows.get(i).getTitleImg()));
				for (int j = 0; j < commentNum.size(); j++) {
					if (baseShows.get(i).getId().equals((commentNum.get(j).getId()))) {
						baseShows.get(i).setCommentNum(commentNum.get(j).getCommentNum());
						break;
					}
				}
			}
		}
		 PageInfo<BaseShow> baseShowPageInfo = new PageInfo<>(baseShows);
		 return baseShowPageInfo;
	}
	
	@Override
	public  PageInfo<BaseShow> getshequ(Integer pageNum, Integer pageSize){
		return new PageInfo<>(this.allMapper.getshequ());
		
	}

	@Override
	public Integer deleteInformation(Long id) {
		
		return this.informationAllMapper.deleteInformation(id);
	}
	
	@Override
	public String findTitle(Long id) {
		return this.informationAllMapper.findTitle(id);
	}
	
	@Override
	public String findInfo(Long id) {		
		return this.informationAllMapper.findInfo(id);
	}
	
	@Override
	public Integer compileto(Long id,String title,String titleImgs,String info) {		
		return this.informationAllMapper.compileto(id, title,titleImgs, info);
	}

	@Override
	public List<BaseShow> myCreate(Long id) {
		return this.informationAllMapper.myCreate(id);
	}
	

}
