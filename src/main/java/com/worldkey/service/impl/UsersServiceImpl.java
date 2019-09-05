package com.worldkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.Gift;
import com.worldkey.entity.JdDetail;
import com.worldkey.entity.KbDetail;
import com.worldkey.entity.Presentor;
import com.worldkey.entity.ThreeType;
import com.worldkey.entity.Users;
import com.worldkey.entity.ZsDetail;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.enumpackage.KbObtainType;
import com.worldkey.enumpackage.ZsObtainType;
import com.worldkey.mapper.GiftMapper;
import com.worldkey.mapper.GiftRecordMapper;
import com.worldkey.mapper.GiftUsersMapper;
import com.worldkey.mapper.JdDetailMapper;
import com.worldkey.mapper.ThreeTypeMapper;
import com.worldkey.mapper.UserRelationMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.KbDetailService;
import com.worldkey.service.SystemConfigService;
import com.worldkey.service.UsersService;
import com.worldkey.service.ZsDetailService;
import com.worldkey.util.Pager;
import com.worldkey.worldfilter.WordFilter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author HP
 */
@Service
public class UsersServiceImpl implements UsersService {
    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);
    @Resource
    private WordFilter wordFilter;
    @Resource
    private UsersMapper uMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SystemConfigService systemConfigService;
    @Resource
    private JdDetailMapper jdDetailMapper;
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private ThreeTypeMapper threeTypeMapper;
    @Resource
    private UserRelationMapper userRelationMapper;
    @Resource
    private GiftRecordMapper  giftRecordMapper; 
    @Resource 
    private GiftMapper giftMapper;
    @Resource
    private GiftUsersMapper giftUserMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public synchronized int updateByPrimaryKeySelective(Users usersVo) {
        return this.uMapper.updateByPrimaryKeySelective(usersVo);
    }

    /**
     * @param recommendedCode 若推荐码不为空则推荐码可定存在，在C层完成判断
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int reg(Users usersVo, Users recommendedCode) {
        usersVo.setHeadImg(systemConfigService.find().getDefaultHeadimg());
        //存在敏感词
        if (wordFilter.isContains(usersVo.getLoginName())) {
            return 3;
        }
        //用户名已存在
        if (checkName(usersVo.getLoginName()) == 1) {
            return 2;
        }
        //注册奖励20金豆
        usersVo.setJd(systemProperties.getRegRewardJd());
        usersVo.setZs(0);
        usersVo.setKb(new BigDecimal(0.00));
        usersVo.setCreateDate(new Date());
        usersVo.setPassword(DigestUtils.md5Hex(usersVo.getPassword()));
        int i = this.uMapper.insertSelective(usersVo);
        Users users = uMapper.selectByLoginName(usersVo.getLoginName());
        //记录注册奖励金豆收支明细
        jdDetailMapper.insert(new JdDetail(new Date(), systemProperties.getRegRewardJd(), JdRewardType.regReward.getIndex(),"注册奖励"+systemProperties.getRegRewardJd()+"金豆", users.getId()));
        //有邀请人的操作，邀请人奖励金豆，并记录邀请人的金豆收支明细
        if (recommendedCode != null) {
            Integer jd = recommendedCode.getJd() == null ? 0 : recommendedCode.getJd();
            recommendedCode.setJd(jd + systemProperties.getInvitedRewardJd());
            uMapper.updateByPrimaryKeySelective(recommendedCode);
            //记录邀请好友奖励金豆收支明细
            jdDetailMapper.insert(new JdDetail(new Date(), systemProperties.getInvitedRewardJd(), JdRewardType.invitedReward.getIndex(), recommendedCode.getId(), users.getId(), users.getPetName(),"邀请好友奖励"+systemProperties.getInvitedRewardJd()+"金豆"));
        }
        return i;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String,Object> reg1(Users usersVo) {
    	Map<String,Object> map = new HashMap<String,Object>();
        usersVo.setHeadImg(systemConfigService.find().getDefaultHeadimg());
        //存在敏感词
        if (wordFilter.isContains(usersVo.getLoginName())) {
        	map.put("result", 3);
            return map;
        }else{
        	map.put("result", 0);
        }
        //用户名已存在
        if (checkName(usersVo.getLoginName()) == 1) {
        	map.put("result", 2);
            return map;
        }else{
        	map.put("result", 0);
        }
        //名称已存在
        if (checkPetName(usersVo.getPetName()) == 1) {
        	map.put("result", 4);
            return map;
        }else{
        	map.put("result", 0);
        }

        //注册奖励20金豆
        usersVo.setJd(systemProperties.getRegRewardJd());
        usersVo.setZs(0);
        usersVo.setKb(new BigDecimal(0.00));
        usersVo.setCreateDate(new Date());
        usersVo.setPassword(DigestUtils.md5Hex(usersVo.getPassword()));
         this.uMapper.updateByPrimaryKeySelective(usersVo);
        //记录注册奖励金豆收支明细
        jdDetailMapper.insert(new JdDetail(new Date(), systemProperties.getRegRewardJd(), JdRewardType.regReward.getIndex(),"注册奖励"+systemProperties.getRegRewardJd()+"金豆", usersVo.getId()));
        //有邀请人的操作，邀请人奖励金豆，并记录邀请人的金豆收支明细
        map.put("users", usersVo);
        log.info(usersVo.toString());
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
    public int checkName(String loginName) {
        Users uCo = this.uMapper.selectByLoginName(loginName);
        if (uCo == null) {
            return 0;
        } else {
            return 1;
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
    public int checkPetName(String petName) {
        Users uCo = this.uMapper.selectByPetName(petName);
        if (uCo == null) {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    @Cacheable(value = "token", key = "#token")
    public Users findByToken(String token) {
        return null;
    }


    /**
     * 判断是否登录成功
     * 1.用户名为空，返回空
     * 2.用户名不为空，密码不匹配，返回空
     * 3.用户名，密码匹配正确，返回token
     */
    @Override
    @SuppressWarnings("unchecked")
    public String login(String loginName, String password) {
        Users vo = new Users();
        vo.setLoginName(loginName);
        vo.setPassword(DigestUtils.md5Hex(password));

        ValueOperations<String, Users> operations = redisTemplate.opsForValue();
        //*代表占位符，表示任意
        Set<String> set = redisTemplate.keys(loginName + "-*");
        if (set.size() != 0) {
            redisTemplate.delete(set);
        }
        Users users = this.uMapper.selectByLoginName(loginName);
        Users usersTel = this.uMapper.selectBytelNums(loginName);
        if (users != null && (users.getPassword().equals(password)||users.getPassword().equals(DigestUtils.md5Hex(password)))) {
            String token = loginName + "-" + DigestUtils.md5Hex(loginName + password + System.currentTimeMillis());
            users.setToken(token);
            operations.set(token, users);
            log.debug("login success: " + loginName);
            return token;
        }else if(usersTel != null && (usersTel.getPassword().equals(password)||usersTel.getPassword().equals(DigestUtils.md5Hex(password)))){
        	 String token = usersTel.getLoginName() + "-" + DigestUtils.md5Hex(usersTel.getLoginName() + password + System.currentTimeMillis());
        	 usersTel.setToken(token);
             operations.set(token, usersTel);
             return token;
        }
        return null;
    }


    @Override
    @CacheEvict(value = "token", key = "#token")
    public int logout(String token) {
        log.info("logout success：" + token);
        return 0;
    }

    /**
     * 用户后台登录
     *
     * @param user 用户账号密码
     * @return 登陆的用户对象
     */
    @Override
    public Users login(Users user) {
        Users users = this.uMapper.selectByLoginName(user.getLoginName());
        Users usersTel = this.uMapper.selectBytelNums(user.getLoginName());
        log.debug("userCO:" + users);
        log.debug("userVO:" + user);
        if (users != null && users.getPassword().equals(DigestUtils.md5Hex(user.getPassword()))) {
            return users;
        }
        if (usersTel != null && usersTel.getPassword().equals(DigestUtils.md5Hex(user.getPassword()))) {
            return usersTel;
        }
        return null;
    }

    /**
     * 通过用户名查找用户
     *
     * @param loginName 用户名
     * @return 若存在返回Users对象，不存在返回null
     */
    @Override
    public Users findByLoginName(String loginName) {
        Users u = this.uMapper.selectByLoginName(loginName);
        //若用户不存在返回null
        if (u == null) {
            return null;
        }
        u.setPassword(null);
        return u;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @CachePut(value = "token", key = "#token")
    public Users changeInfo(Users usersVo, String host, String token) {
        this.uMapper.updateByPrimaryKeySelective(usersVo);
        return this.uMapper.selectByPrimaryKey(usersVo.getId());
    }

    /**
     * 添加balance数值，可以为负值，为减少
     *
     * @return 受影响的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addBalance(Users users) {
        return this.uMapper.addBalance(users);
    }

    @Override
    public BigDecimal findBalanceByID(Long id) {
        return this.uMapper.selectBalanceById(id);
    }

    @Override
    public PageInfo<Users> findAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<Users> users = this.uMapper.selectAll();
        return new PageInfo<>(users);
    }

    @Override
    public void getUsersExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename="
                + URLEncoder.encode("用户列表.xls", "UTF-8"));

        List<Users> users = this.uMapper.selectAll();
        List<String[]> list = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        list.add(new String[]{"#", "loginName", "petName", "sex", "birthday", "telNum", "balance", "email",
                "createDate"});
        Integer rowiD = 1;
        for (Users e : users) {
            list.add(new String[]{
                    (rowiD++).toString(),
                    e.getLoginName() == null ? "NAN" : e.getLoginName(),
                    e.getPetName() == null ? "NAN" : e.getPetName(),
                    e.getSex() == null ? "NAN" : (e.getSex() == 0 ? "男" : "女"),
                    e.getBirthday() == null ? "NAN" : simpleDateFormat.format(e.getBirthday()),
                    e.getTelNum() == null ? "NAN" : e.getTelNum(),
                    e.getBalance().toString(),
                    e.getEmail() == null ? "NAN" : e.getEmail(),
                    simpleDateFormat.format(e.getCreateDate())});
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet testExcel = workbook.createSheet("用户列表");
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillBackgroundColor((short) 1);
        testExcel.setColumnWidth(1, 20 * 256);
        testExcel.setColumnWidth(2, 30 * 256);
        testExcel.setColumnWidth(5, 20 * 256);
        testExcel.setColumnWidth(8, 20 * 256);
        XSSFRow row;
        int rowId = 0;
        for (String[] strings : list) {
            row = testExcel.createRow(rowId++);
            int cellID = 0;
            for (String string : strings) {
                XSSFCell cell = row.createCell(cellID++);

                cell.setCellStyle(cellStyle);
                cell.setCellValue(string);
            }
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public Integer getPraiseNum(Long id) {
        return this.uMapper.selectPraiseNum(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer addJd(Integer releaseAwardsJd, Long id) {
        return this.uMapper.addJd(releaseAwardsJd, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer addOrSubZs(Integer zsNum, Long id) {
        return uMapper.addOrSubZs(zsNum, id);
    }

    @Override
    public Users getJdAndZsAndKb(Long id) {
        return this.uMapper.selectJdAndZsAndKbByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer addOrSubKb(BigDecimal kbNum, Long id) {
        return this.uMapper.addOrSubKb(kbNum.setScale(2, BigDecimal.ROUND_HALF_DOWN), id);
    }

    @Resource
    private ZsDetailService zsDetailService;
    @Resource
    private KbDetailService kbDetailService;

    /**
     * 用户使用K币购买钻石的记录，
     * 其中K币购买钻石的比例在配置文件中定义
     * @param kbNum 要购买的K币的数量
     * @param id  发起购买时间的用户的ID
     * @return Integer
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer kb2Zs(Integer kbNum, Long id){
        Users jdAndZsAndKb = this.getJdAndZsAndKb(id);
        //boolean b = jdAndZsAndKb.getKb().compareTo(new BigDecimal(kbNum))>=0 ;
        if (jdAndZsAndKb.getKb().compareTo(new BigDecimal(kbNum)) < 0) {
            return 0;
        }
        jdAndZsAndKb.setKb(jdAndZsAndKb.getKb().subtract(new BigDecimal(kbNum)));
        jdAndZsAndKb.setZs(new BigDecimal(kbNum).multiply(new BigDecimal(10)).intValue());
        //记录用户的钻石添加记录
        ZsDetail zsDetail = new ZsDetail(new Date(), kbNum * systemProperties.getKb2zs(), ZsObtainType.KbRecharge.getIndex(), id);
        zsDetail.setMsg("购买"+kbNum * systemProperties.getKb2zs()+"个钻石");
        zsDetailService.insert(zsDetail);
        //记录用户的Kb购买钻石的记录
        
        //KbDetail kbDetail = new KbDetail(new Date(), new BigDecimal(kbNum*-1), KbObtainType.PurchaseZs.getIndex(), id);
        KbDetail kbDetail = new KbDetail(new Date(), (new BigDecimal(kbNum*-1)).setScale(2, BigDecimal.ROUND_HALF_DOWN), KbObtainType.PurchaseZs.getIndex(), id);
        kbDetail.setMsg("购买"+kbNum * systemProperties.getKb2zs()+"个钻石");
        kbDetailService.insert(kbDetail);
        return this.uMapper.kb2Zs(kbNum, id, systemProperties.getKb2zs());
    }
    
    
    //5.11
	@Override
	public PageInfo<Users> findUserByGroup(Integer groupId,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize, true);
		List<Users> users = this.uMapper.selectByGroup(groupId);
		ThreeType group = this.threeTypeMapper.selectGroupById(groupId);
		for(int i = 0;i<users.size();i++){
			if(users.get(i).getId().equals(group.getUserId())){
				users.remove(i);
				break;
			}
		}
		return new PageInfo<>(users);
	}

	@Override
	public Users selectGroupLeader(Integer groupId) {
		return this.uMapper.selectGroupLeader(groupId);
	}

	@Override
	public PageInfo<Users> findUserByGroup1(Integer groupId, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize, true); 
		return new PageInfo<>(this.uMapper.selectByGroup(groupId));
	}

	@Override
	public Integer updateBackground(Users user) {
		return this.uMapper.updateBackground(user);
	}

	@Override
	public Map<String, Object> userDetails(Long userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		Users user = this.uMapper.selectByPrimaryKey(userId);
		Integer fans = this.userRelationMapper.selectFans(Integer.parseInt(userId+""));
		Integer star = this.userRelationMapper.selectStars(Integer.parseInt(userId+""));
		Integer giftNum = this.giftRecordMapper.selectGiftNum(Integer.parseInt(userId+""));
		Integer giftn = this.giftUserMapper.getUserGiftNum(userId);
		giftNum = (giftNum==null?0:giftNum)+(giftn==null?0:giftn);
		map.put("user", user);
		map.put("fans", fans==null?0:fans);
		map.put("star", star==null?0:star);
		map.put("giftNum", giftNum==null?0:giftNum);
		return map;
	}

	@Override
	public Users getUserDetail(Long id) {
		return this.uMapper.getUserDetail(id);
	}

	@Override
	public Integer editFond(Users user) {
		return this.uMapper.updateFond(user);
	}
	
	public Integer insertModule(Users record){
		return this.uMapper.insertSelective(record);
	}

	@Override
	public Users findByTelNum(String phone) {
		// TODO Auto-generated method stub
		return this.uMapper.selectBytelNum(phone);
	}

	@Override
	public PageInfo<Users> getFans(Integer userId,Integer pageNum,Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize, true);
		List<Users> fans = this.uMapper.getFans(userId);
		return new PageInfo<>(fans);
	}

	@Override
	public PageInfo<Users> getStars(Integer userId,Integer pageNum,Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize, true);
		List<Users> stars = this.uMapper.getStars(userId);
		return new PageInfo<>(stars);
	}
	
	@Override
	public Pager<Presentor> getPresentors(Long id, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		List<Long> userPresentor = this.giftUserMapper.getPresentorsByUsers(id);
		List<Presentor> allPresentor = this.uMapper.getAllPresentors(id);
		if (allPresentor != null) {
			for (Presentor p : allPresentor) {
				Long userId = p.getPid();
				List<Integer> giftByPresentors = this.giftRecordMapper.getAllGiftsByPresentors(id, userId);
				Users user = this.uMapper.selectByPrimaryKey(userId);
				Map<String, Integer> map = this.getPresentorsZS(giftByPresentors);
				p.setZs(map.get("zs"));
				p.setJd(map.get("jd"));
				p.setHeadImg(user.getHeadImg());
				p.setPetName(user.getPetName());
				if (userPresentor.contains(p.getPid())) {
					List<Integer> giftByUserPresentors = this.giftUserMapper.giftByUserPressentors(p.getPid());
					Map<String, Integer> map1 = this.getPresentorsZS(giftByUserPresentors);
					p.setZs(p.getZs() + map1.get("zs"));
					p.setJd(p.getJd() + map1.get("jd"));
					userPresentor.remove(userPresentor.indexOf(p.getPid()));
				}
			}
		}
		for (Long l : userPresentor) {
			List<Integer> giftByUserPresentors = this.giftUserMapper.giftByUserPressentors(l);
			Map<String, Integer> map = this.getPresentorsZS(giftByUserPresentors);
			Users user = this.uMapper.selectByPrimaryKey(l);
			Presentor p = new Presentor();
			p.setPid(l);
			p.setZs(map.get("zs"));
			p.setJd(map.get("jd"));
			p.setHeadImg(user.getHeadImg());
			p.setPetName(user.getPetName());
			allPresentor.add(p);
		}
		allPresentor.sort(new Comparator<Presentor>() {

			@Override
			public int compare(Presentor o1, Presentor o2) {
				// TODO Auto-generated method stub
				int sort = 0;
				int a = o1.getZs() - o2.getZs();
				if (a != 0) {
					sort = (a < 0) ? 1 : -1;
				} else {
					a = o1.getJd() - o2.getJd();
					if (a != 0) {
						sort = (a < 0) ? 1 : -1;
					}
				}
				return sort;
			}

		});
		Pager<Presentor> pager = new Pager<Presentor>(pageNum,pageSize,allPresentor);
		List<Presentor> s = pager.getEveryPage();
		for (int i = pageSize*(pageNum-1); i < pageSize*(pageNum-1)+s.size(); i++) {
			s.get(i%pageSize).setOm(i + 1);
		}
//		if (s.size() < pageSize){
//			for (int i = pageSize*(pageNum-1); i < s.size(); i++) {
//				s.get(i).setOm(i + 1);
//			}
//		}else{
//			for(int i = pageSize*(pageNum-1);i<pageSize*pageNum;i++){
//				s.get(i).setOm(i+1);
//			}
//		}
//		if (pageNum == 1) {
//			if (allPresentor.size() < 5) {
//				for (int i = 0; i < allPresentor.size(); i++) {
//					allPresentor.get(i).setOm(i + 1);
//				}
//			} else {
//				for (int i = 0; i < 5; i++) {
//					allPresentor.get(i).setOm(i + 1);
//				}
//			}
//		}
		
		return pager;
	}
	
	private Map<String,Integer> getPresentorsZS(List<Integer> gp){
		Integer zs = 0;
		Integer jd = 0;
		Map<String,Integer> map = new HashMap<String,Integer>();
		List<Gift> allGifts = this.giftMapper.getAllGifts();
		for(int i = 0;i<gp.size();i++){
			for(int j = 0;j<allGifts.size();j++){
				if(gp.get(i)==allGifts.get(j).getGiftId()){
					zs = zs+allGifts.get(j).getZsPrice();
					jd = jd+allGifts.get(j).getJdPrice();
					map.put("zs", zs);
					map.put("jd", jd);
				}
			}
		}
		return map;
	}
	

	@Override
	public Users selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return this.uMapper.selectByPrimaryKey(id);
	}

	@Override
	public Map<String, Object> getRelationsAndComments(Long userId) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		Integer fans = this.userRelationMapper.selectFans(Integer.parseInt(userId+""));
		Integer star = this.userRelationMapper.selectStars(Integer.parseInt(userId+""));
		Integer giftNum = this.giftRecordMapper.selectGiftNum(Integer.parseInt(userId+""));
		Integer giftn = this.giftUserMapper.getUserGiftNum(userId);
		giftNum = (giftNum==null?0:giftNum)+(giftn==null?0:giftn);
		map.put("fans", fans==null?0:fans);
		map.put("star", star==null?0:star);
		map.put("giftNum", giftNum==null?0:giftNum);
		return map;
	}

	@Override
	public Integer updatePersonalBrand(Long userId) {
		return this.uMapper.updatePersonalBrand(userId);	
	}

	@Override
	public Users userTelNum(String telNum) {
		return this.uMapper.userTelNum(telNum);
	}

}
