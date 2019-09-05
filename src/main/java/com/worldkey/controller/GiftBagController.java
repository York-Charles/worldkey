package com.worldkey.controller;

import com.worldkey.entity.GiftBag;
import com.worldkey.entity.GiftBagRecord;
import com.worldkey.entity.Users;
import com.worldkey.mapper.GiftBagRecordMapper;
import com.worldkey.mapper.UsersMapper;
import com.worldkey.service.GiftBagService;
import com.worldkey.service.UsersService;
import com.worldkey.util.ResultUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wu 礼包系统接口
 */

@RestController
@RequestMapping("giftBag")
public class GiftBagController {
	@Resource
	private GiftBagService giftBagService;
	@Resource
	private UsersService usersService;
	@Resource
	private UsersMapper usersMapper;
	@Resource
	private GiftBagRecordMapper giftBagRecordMapper;
	public static boolean festivalGB = false;

	/**
	 * 礼包
	 */
	@RequestMapping("all/{token}")
	public ResultUtil noviceGiftBag(@PathVariable String token, Integer giftBagId) {
		// 判断用户登录
		Users users = this.usersService.findByToken(token);
		if (users == null || token == null) {
			return new ResultUtil(200, "ok", "user null");
		}
		// 判断礼包是否存在
		GiftBag giftBag = this.giftBagService.selectById(giftBagId);
		if (giftBag == null) {
			return new ResultUtil(200, "ok", "giftBagId error");
		}

		// 礼物类型为1 新手礼包 礼物类型为2 周礼包
		if (giftBagId == 1 || giftBagId == 2) {
			// 获取购买记录实体
			Integer gBRId = giftBagRecordMapper.findByUidAndGBid(users.getId(), giftBagId);
			GiftBagRecord giftBagRecord = this.giftBagRecordMapper.selectById(gBRId);
			if (giftBagRecord == null) {
				Integer buyGiftBag = this.giftBagService.buyGiftBag(token, giftBagId);
				if (buyGiftBag == 2) {
					return new ResultUtil(200, "no", "money less");
				} else if (buyGiftBag == 3) {
					this.usersMapper.updateNoviceGiftBag(users.getId());
					return new ResultUtil(200, "ok", "buy success");
				} else if (buyGiftBag == 4) {
					return new ResultUtil(200, "no", "buy fail");
				}
			}

		}
		// 活动礼包3 礼物类型3 活动礼包4 类型为4
		if (giftBagId == 3 || giftBagId == 4) {
			if (!festivalGB) {
				return new ResultUtil(200, "ok", "activity over");
			}
			Integer buyGiftBag = this.giftBagService.buyGiftBag(token, giftBagId);
			if (buyGiftBag == 2) {
				return new ResultUtil(200, "no", "money less");
			} else if (buyGiftBag == 3) {
				return new ResultUtil(200, "ok", "buy success");
			} else if (buyGiftBag == 4) {
				return new ResultUtil(200, "no", "buy fail");
			}
		}
		return new ResultUtil(200, "no", "had bought");
	}

	/**
	 * 获取全部礼包状态 user null 用户错误 activity over 活动结束 not buy 未曾购买 had bought 已经购买
	 */
	@RequestMapping("gbStatus/{token}")
	public ResultUtil chaXun(@PathVariable String token) {
		// 判断用户登录
		Users users = this.usersService.findByToken(token);
		if (users == null || token == null) {
			return new ResultUtil(200, "ok", "user null");
		}
		List<String> list = new ArrayList<String>();
		// 根据礼包id
		for (int i = 1; i < 5; i++) {

			// 1、2号礼包 和3、4 区别在于 活动结束条件
			if (i == 1 || i == 2) {
				Integer gBRId = giftBagRecordMapper.findByUidAndGBid(users.getId(), i);
				GiftBagRecord giftBagRecord = this.giftBagRecordMapper.selectById(gBRId);
				if (giftBagRecord == null) {
					list.add((i - 1), "not buy");
				} else {
					list.add((i - 1), "had bought");
				}
			}
			// 3,4号礼包
			if (i == 3 || i == 4) {
				if (!festivalGB) {
					list.add((i - 1), "activity over");
				} else {
					list.add((i - 1), "not buy");
				}
			}
		}
		return new ResultUtil(200, "ok", list);
	}

}
