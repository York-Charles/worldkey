package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.*;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.enumpackage.KbObtainType;
import com.worldkey.enumpackage.ZsObtainType;
import com.worldkey.jdpush.Jdpush;
import com.worldkey.mapper.GiftMapper;
import com.worldkey.mapper.GiftUsersMapper;
import com.worldkey.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class GiftServiceImpl extends ServiceImpl<GiftMapper, Gift> implements GiftService {
	@Resource
	private InformationAllService informationAllService;
	@Resource
	private UsersService usersService;
	@Resource
	private GiftRecordService giftRecordService;
	@Resource
	private SystemProperties systemProperties;
	@Resource
	private JdDetailService jdDetailService;
	@Resource
	private ZsDetailService zsDetailService;
	@Resource
	private KbDetailService kbDetailService;
	@Resource
	private GiftUsersMapper giftUsersMapper;
	/**
	 *
	 * @param users
	 *            当前登录的用户
	 * @param giftId
	 *            赠送礼物的ID
	 * @param toInformation
	 *            受赠的文章的ID
	 * @return 礼物ID不存在返回0 文章不存在返回0 余额不足返回2 成功返回3，sql错误返回4
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer giveGift(Users users, Integer giftId, Long toInformation) {
		log.error("giftId:" + giftId);
		Gift gift = this.baseMapper.selectById(giftId);
		// 礼物ID不存在返回0
		if (gift == null) {
			return 0;
		}
		// 文章不存在返回0
		InformationAll info = informationAllService.info(toInformation);
		// 4.29文章所对应的用户id
		Long a1 = info.getUsers().getId();
		String s = info.getUsers().getLoginName();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("img", users.getHeadImg());
		map.put("Mid", Long.toString(users.getId()));
		map.put("name", users.getPetName());
		map.put("infoImg", info.getTitleImg());
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");
		String date = df.format(day);
		map.put("date", date);
		map.put("webUrl", info.getWeburl());
		map.put("id", Long.toString(info.getId()));
		map.put("fenlei", "2");
		map.put("title", info.getTitle());
		map.put("abstracte", info.getAbstracte());
		map.put("giftImg", gift.getGiftImg());
		if (info == null) {
			return 0;
		}
		if(!(a1.equals(users.getId()))){
		Users jdAndZsAndKb = usersService.getJdAndZsAndKb(users.getId());
		// 判断是否是金豆礼物
		boolean jdPrice = gift.getJdPrice() != null && gift.getJdPrice() > 0;
		// 判断余额是否充足
		if (jdPrice && jdAndZsAndKb.getJd() >= gift.getJdPrice()) {
			// 赠送礼物操作
			// 1.用户的金豆数量减少
			usersService.addJd(gift.getJdPrice() * (-1), jdAndZsAndKb.getId());
			// 2.被赠送的用户的金豆增加
			usersService.addJd(gift.getJdPrice(), info.getUsers().getId());
			// 记录两人的金豆收支记录
			// 赠送人的支出记录
			JdDetail jdDetail = new JdDetail(new Date(), gift.getJdPrice() * -1, JdRewardType.giveGift.getIndex(),
					users.getId(), info.getUsers().getId(), info.getUsers().getPetName(),
					"送给" + info.getUsers().getPetName() + "一个" + gift.getName());
			// 受赠人的收入记录
			JdDetail jdDetail1 = new JdDetail(new Date(), gift.getJdPrice(), JdRewardType.giveGift.getIndex(),
					info.getUsers().getId(), users.getId(), users.getPetName(),
					"收到" + users.getPetName() + "送的一个" + gift.getName());
			List<JdDetail> jdDetails = new ArrayList<>(2);
			jdDetails.add(jdDetail);
			jdDetails.add(jdDetail1);
			// 存入数据库
			jdDetailService.insertBatch(jdDetails);
			// 记录礼物赠送记录
			GiftRecord giftRecord = new GiftRecord(new Date(), users.getId(), toInformation, giftId);
			boolean insert = giftRecordService.insert(giftRecord);
			// 4.28 极光推送
			if (!(users.getId().equals(a1))) {
				Jdpush.jpushAndriod2(users.getPetName(), s, gift.getName(), map);
			}
			// 成功返回3，插入失败返回4
			return insert ? 3 : 4;
		}
		// 判断是否是钻石礼物
		boolean zsPrice = gift.getZsPrice() != null && gift.getZsPrice() > 0;
		// 判断余额是否充足
		if (zsPrice && jdAndZsAndKb.getZs() >= gift.getZsPrice()) {
			// 赠送礼物操作
			// 赠送放的钻石减少
			usersService.addOrSubZs(gift.getZsPrice() * (-1), users.getId());
			// 记录赠送方的钻石支出记录
			ZsDetail zsDetail = new ZsDetail(new Date(), gift.getZsPrice() * -1, ZsObtainType.giveGift.getIndex(),
					users.getId(), info.getUsers().getId(), info.getUsers().getPetName());
			zsDetail.setMsg("赠送给" + info.getUsers().getPetName() + "一个" + gift.getName());
			zsDetailService.insert(zsDetail);
			// 受赠方的K币增加
			BigDecimal zs2kb = new BigDecimal(systemProperties.getZs2kb() * 0.01);
			BigDecimal giftPrice = new BigDecimal(gift.getZsPrice());
			BigDecimal kb = (zs2kb.multiply(giftPrice)).setScale(2, BigDecimal.ROUND_HALF_DOWN);

			// 此处使用BigDecimal.ROUND_HALF_DOWN方法，得到两位且四舍五入的精度
			usersService.addOrSubKb(kb.setScale(2, BigDecimal.ROUND_HALF_DOWN), info.getUsers().getId());
			// //4.28 极光推送 5.19暂时关闭礼物推送功能
			 if(!(users.getId().equals(a1))){
			 Jdpush.jpushAndriod2(users.getPetName(),s,gift.getName(),map);
			 }
			// 记录受赠方的K币收入记录
			String msg = "收到" + users.getPetName() + "赠送的一个" + gift.getName();
			KbDetail kbDetail = new KbDetail(new Date(), kb.setScale(2, BigDecimal.ROUND_HALF_DOWN),
					KbObtainType.getGift.getIndex(), info.getUsers().getId(), users.getId(), users.getPetName(), msg);
			kbDetailService.insert(kbDetail);
			// 记录礼物赠送记录
			GiftRecord giftRecord = new GiftRecord(new Date(), users.getId(), toInformation, giftId);
			boolean insert = giftRecordService.insert(giftRecord);
			return insert ? 3 : 4;
		}
		}else if(a1.equals(users.getId())){
			return 6;
		}
		// 余额不足返回2
		return 2;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer giftUsers(String token, Integer giftId, Long userId) {
			Gift gift = this.baseMapper.selectById(giftId);
			// 礼物ID不存在返回0
			if (gift == null) {
				return 0;
			}
			Users users = usersService.findByToken(token);
			Users users1 = usersService.selectByPrimaryKey(userId);
			Users jdAndZsAndKb = usersService.getJdAndZsAndKb(users.getId());
			// 判断是否是金豆礼物
			boolean jdPrice = gift.getJdPrice() != null && gift.getJdPrice() > 0;
			// 判断余额是否充足
			if (jdPrice && jdAndZsAndKb.getJd() >= gift.getJdPrice()) {
				// 赠送礼物操作
				// 1.用户的金豆数量减少
				usersService.addJd(gift.getJdPrice() * (-1), jdAndZsAndKb.getId());
				// 2.被赠送的用户的金豆增加
				usersService.addJd(gift.getJdPrice(),userId);
				// 记录两人的金豆收支记录
				// 赠送人的支出记录
				JdDetail jdDetail = new JdDetail(new Date(), gift.getJdPrice() * -1, JdRewardType.giveGift.getIndex(),
						users.getId(),userId, users1.getPetName(),
						"送给" + users1.getPetName() + "一个" + gift.getName());
				// 受赠人的收入记录
				JdDetail jdDetail1 = new JdDetail(new Date(), gift.getJdPrice(), JdRewardType.giveGift.getIndex(),
						userId, users.getId(), users.getPetName(),
						"收到" + users.getPetName() + "送的一个" + gift.getName());
				List<JdDetail> jdDetails = new ArrayList<>(2);
				jdDetails.add(jdDetail);
				jdDetails.add(jdDetail1);
				// 存入数据库
				jdDetailService.insertBatch(jdDetails);
				// 记录礼物赠送记录
				GiftUsers giftUsers = new GiftUsers(new Date(), users.getId(), userId, giftId);
				boolean insert = giftUsersMapper.insert(giftUsers);
				// 成功返回3，插入失败返回4
				return insert ? 3 : 4;
			}
			// 判断是否是钻石礼物
			boolean zsPrice = gift.getZsPrice() != null && gift.getZsPrice() > 0;
			// 判断余额是否充足
			if (zsPrice && jdAndZsAndKb.getZs() >= gift.getZsPrice()) {
				// 赠送礼物操作
				// 赠送放的钻石减少
				usersService.addOrSubZs(gift.getZsPrice() * (-1), users.getId());
				// 记录赠送方的钻石支出记录
				ZsDetail zsDetail = new ZsDetail(new Date(), gift.getZsPrice() * -1, ZsObtainType.giveGift.getIndex(),
						users.getId(), userId, users1.getPetName());
				zsDetail.setMsg("赠送给" + users1.getPetName() + "一个" + gift.getName());
				zsDetailService.insert(zsDetail);
				// 受赠方的K币增加
				BigDecimal zs2kb = new BigDecimal(systemProperties.getZs2kb() * 0.01);
				BigDecimal giftPrice = new BigDecimal(gift.getZsPrice());
				BigDecimal kb = (zs2kb.multiply(giftPrice)).setScale(2, BigDecimal.ROUND_HALF_DOWN);

				// 此处使用BigDecimal.ROUND_HALF_DOWN方法，得到两位且四舍五入的精度
				usersService.addOrSubKb(kb.setScale(2, BigDecimal.ROUND_HALF_DOWN), userId);
				
				// 记录受赠方的K币收入记录
				String msg = "收到" + users.getPetName() + "赠送的一个" + gift.getName();
				KbDetail kbDetail = new KbDetail(new Date(), kb.setScale(2, BigDecimal.ROUND_HALF_DOWN),
						KbObtainType.getGift.getIndex(), users1.getId(), users.getId(), users.getPetName(), msg);
				kbDetailService.insert(kbDetail);
				// 记录礼物赠送记录
				GiftUsers giftUsers = new GiftUsers(new Date(), users.getId(), userId, giftId);
				boolean insert = giftUsersMapper.insert(giftUsers);
				// 成功返回3，插入失败返回4
				return insert ? 3 : 4;
			}
			// 余额不足返回2
			return 2;
	}

}
