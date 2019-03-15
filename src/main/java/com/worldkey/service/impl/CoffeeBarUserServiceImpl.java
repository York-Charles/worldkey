package com.worldkey.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.worldkey.service.FriendService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.worldkey.entity.CoffeeBarUser;
import com.worldkey.entity.CountAndSex;
import com.worldkey.entity.ReqRecord;
import com.worldkey.entity.Users;
import com.worldkey.mapper.CoffeeBarMapper;
import com.worldkey.mapper.CoffeeBarUserMapper;
import com.worldkey.mapper.DissRecordMapper;
import com.worldkey.mapper.ReqRecordMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.CoffeeBarUserService;
import com.worldkey.util.PrivateRequest;
import com.worldkey.util.ResultUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoffeeBarUserServiceImpl implements CoffeeBarUserService {

	@Resource
	CoffeeBarUserMapper cbum;
	@Resource
	CoffeeBarMapper cbm;
	@Resource
	UsersMapper um;
	@Resource
	ReqRecordMapper rrm;
	@Resource
    FriendService fs;


	@SuppressWarnings("serial")
	private Map<String, Object> havenUsed(List<Integer> havenSeated, List<String> havenUsed, Integer sex) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> seat = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
				add(6);
			}
		};
		for (Integer i : havenSeated) {
			if (seat.contains(i))
				seat.remove(i);
		}
		List<String> appellation = new ArrayList<String>() {
			{
				add("IT男");
				add("大叔");
				add("潮男");
				add("轮滑少年");
				add("高富帅");
				add("邻家女孩");
				add("白骨精");
				add("女神");
				add("前台小姐姐");
				add("时尚女");
			}
		};
		List<String> appell = null;
		if (sex == 1) {
			appell = appellation.subList(0, 5);
		} else {
			appell = appellation.subList(5, 10);
		}
		for (String i : havenUsed) {
			if (appell.contains(i))
				appell.remove(i);
		}
		map.put("appell", appell);
		map.put("seat", seat);
		return map;
	}

	private void eligibleBar(List<Integer> Cid, Integer sex, Integer userId, Integer state) {
		for (Integer cid : Cid) {
			// 咖啡厅里的人id（按性别分类）
			List<CountAndSex> users = this.cbm.getBarUsers(cid);
			if (users.size() == 0) {
				continue;
			} else {
				if (users.size() == 1) {
					// 已有五个同性，从咖啡厅集合中删除该房间
					if (users.get(0).getCount() == 5 && users.get(0).getSex() == sex) {
						Cid.remove(cid);
					}
				} else {
					// 已满删除
					if (users.get(0).getCount() + users.get(1).getCount() == 6) {
						Cid.remove(cid);
					}
				}
			}
		}
		if (state.equals(1)) {
			Integer ss = this.cbum.selectBarIdByUserId(userId);
			if (Cid.contains(ss)) {
				Cid.remove(ss);
			}
		}
	}

	private void initBar(List<Integer> Cid) {
		List<CoffeeBarUser> cbu = this.cbum.getAll();
		// 人数与咖啡厅比率大于4时，自动创建一个咖啡厅房间
		if (cbu.size() / Cid.size() > 4) {
            Integer scene = this.cbm.truncScene();
            List<Integer> list = new ArrayList<Integer>(){{add(1);add(2);add(3);}};
            list.remove(scene);
            this.cbm.addCoffeeHome(list.get(new Random().nextInt(list.size())));
		}
	}

	private void delBar(List<Integer> Cid) {
		List<CoffeeBarUser> cbu = this.cbum.getAll();
		if (cbu.size() / Cid.size() <= 5) {
                if(Cid.size()>1) {
                    for (Integer i : Cid) {
                        if (this.cbum.selectUsers(i).equals(0)) {
                            this.cbm.removeBar(i);
                        }
                    }
                }
            log.info("我已经走过删除方法了！！！");
			}
	}

	@Override
	@CachePut(value = "seatId", key = "#userId+'in'")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> leaving(Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> Cid = this.cbm.getCid();
		CoffeeBarUser s = this.cbum.selectByUserId(userId);
		map.put("seat", s.getSeat());
		delBar(Cid);
		List<ReqRecord> record = this.rrm.selectByBarId(s.getBarId());
		map.put("requestList", record);
		map.put("label",this.cbum.selectLabelByUserId1(userId));
		map.put("channelName",this.cbm.getBarName(this.cbum.selectBarIdByUserId1(userId)));
		if (this.rrm.selectByIsReqId(Long.parseLong(userId + "")) != null) {
			this.cbum.leaving(userId);
			this.rrm.delRecord1(userId);
			return map;
		}
		if (this.rrm.selectByIsReqId1(userId) != null) {
			this.rrm.delRecord1(userId);
		}
		this.cbum.leaving(userId);
		return map;
	}

	@Override
	public CoffeeBarUser getByUserId(Integer userId) {
		return this.cbum.selectByUserId(userId);
	}

	@Override
	@Cacheable(value = "seatId", key = "#userId+'in'")
	public Map<String, Object> getUserSeatAfterLeaving(Integer userId) {
		return null;
	}

	@Override
	public Integer getUserSeat(Long userId) {
		return this.cbum.selectSeatByUserId(userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultUtil matchingState(Integer userId, Integer state) {

		Users user = this.um.selectByPrimaryKey(Long.parseLong(userId + ""));
		// 所有咖啡厅
		List<Integer> Cid = this.cbm.getCid();
		eligibleBar(Cid, user.getSex(), userId, state);
		if (Cid.size() == 0) {
            Integer scene = this.cbm.truncScene();
            List<Integer> list = new ArrayList<Integer>(){{add(1);add(2);add(3);}};
            list.remove(scene);
			this.cbm.addCoffeeHome(list.get(new Random().nextInt(list.size())));
            Integer i = this.cbm.newCoffeeBar();
            Cid.add(i);
        }
		Random r = new Random();
		Integer coffeeId = Cid.get(r.nextInt(Cid.size()));
		Map<String, Object> map = havenUsed(this.cbum.selectSeated(coffeeId), this.cbum.selectedUsed(coffeeId),
				user.getSex());
		List<String> havenUsed = (List<String>) map.get("appell");
		List<Integer> havenSeated = (List<Integer>) map.get("seat");
		String label = havenUsed.get(r.nextInt(havenUsed.size()));
		Integer s = havenSeated.get(r.nextInt(havenSeated.size()));
		List<String> allUsers = this.cbum.selectUserIdByBarId(coffeeId);
		if (state == 0) {
            if (this.cbum.selectExist(userId) != null) {
                this.cbum.Changing(userId, coffeeId, s, label);
            }else{
                this.cbum.matchingState(userId, coffeeId, s, label);
            }
			initBar(Cid);
		} else {
			this.cbum.changingRoom(userId, coffeeId, s, label);
			delBar(Cid);
		}
        String[] ss = new String[allUsers.size()];
        String[] array = allUsers.toArray(ss);
		this.fs.barrage(user.getLoginName(),this.cbum.selectLabelByUserId(user.getId().toString())+"进入了"
                        +this.cbm.getBarName(coffeeId), null,FriendServiceImpl.NOTICE,array);
		List<CoffeeBarUser> c = this.cbum.selectByBarId(coffeeId);
		for (CoffeeBarUser cb : c) {
			if (cb.getUserId().equals(userId)) {
				cb.setIsOwn(1);
			}
		}
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("list", c);
		map1.put("scene", this.cbm.selectScene(coffeeId));
		map1.put("bar", "bar" + coffeeId);
		return new ResultUtil(200, "ok", map1);
	}

	@Override
	@CachePut(value = "seatId", key = "#userId+'in'")
	public Map<String, Object> putSeat(Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		CoffeeBarUser s = this.cbum.selectByUserId(userId);
		map.put("seat", s.getSeat());
		List<ReqRecord> record = this.rrm.selectByBarId(s.getBarId());
		map.put("requestList", record);
        map.put("label",this.cbum.selectLabelByUserId1(userId));
        map.put("channelName",this.cbm.getBarName(this.cbum.selectBarIdByUserId1(userId)));
		return map;
	}

	@Override
	@CachePut(value = "seatId", key = "#userId+'in'")
	public Map<String, Object> accepted(Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		CoffeeBarUser s = this.cbum.selectByUserId2(userId);
		map.put("seat", s.getSeat());
		List<ReqRecord> record = this.rrm.selectByBarId1(s.getBarId());
		map.put("requestList", record);
        map.put("label",this.cbum.selectLabelByUserId1(userId));
		return map;
	}

	@Override
	public String getHeadIcon(Integer seatId, Integer barId) {
		return this.cbum.getHeadIcon(seatId, barId);
	}

	@Override
	public PrivateRequest infoFromBoth(Users user,Users user1) {
		PrivateRequest pr = new PrivateRequest(user.getLoginName(),user.getHeadImg(),user.getId(),
				user1.getLoginName(),user1.getHeadImg(),user1.getId());
		pr.setMessage(2);
		return pr;
	}

    @Override
    @Cacheable(value = "channel", key = "#userId+'inchannel'")
    public Map<String, Object> WeggenNachrichten(Integer userId) {
        return null;
    }
}
