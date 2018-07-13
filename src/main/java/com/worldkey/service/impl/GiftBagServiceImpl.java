package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.entity.*;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.enumpackage.ZsObtainType;
import com.worldkey.mapper.GiftBagMapper;
import com.worldkey.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wu
 * 礼包的实现
 */
@Service
@Slf4j
public class GiftBagServiceImpl extends ServiceImpl<GiftBagMapper, GiftBag> implements GiftBagService {

    @Resource
    private UsersService usersService;
    @Resource
    private JdDetailService jdDetailService;
    @Resource
    private ZsDetailService zsDetailService;
    @Resource
    private GiftBagRecordService giftBagRecordService;

    /**
     * 购买新手礼包
     * 1 已经购买   0 未购买
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer buyGiftBag(String token, Integer giftBagId) {

        GiftBag giftBag = this.baseMapper.selectById(giftBagId);
        Users users = this.usersService.findByToken(token);
        Users jdAndZsAndKb = this.usersService.getJdAndZsAndKb(users.getId());

        //判断钻石余额是否充足
        if (jdAndZsAndKb.getZs() >= giftBag.getZsPrice()) {

            //执行购买礼包模式
            //1.用户钻石数量减少
            usersService.addOrSubZs(giftBag.getZsPrice() * (-1), users.getId());

            //2.用户购买礼包后  执行 钻石和金豆数量增加
            usersService.addJd(giftBag.getJdNum(), users.getId());
            usersService.addOrSubZs(giftBag.getZsNum(), users.getId());

            //3.记录用户的钻石和金豆的收支记录
            //  钻石支出记录
            ZsDetail zsDetailOut = new ZsDetail(new Date(), giftBag.getZsPrice() * (-1), ZsObtainType.buyGiftBag.getIndex(), users.getId());
            zsDetailOut.setMsg("购买" + giftBag.getName() + "支出" + giftBag.getZsPrice() + "钻石");
            // 钻石收入记录
            ZsDetail zsDetailIn = new ZsDetail(new Date(), giftBag.getZsNum(), ZsObtainType.getGiftBag.getIndex(), users.getId());
            zsDetailIn.setMsg("从" + giftBag.getName() + "获得" + giftBag.getZsNum() + "钻石");
            // 金豆收入记录
            JdDetail jdDetailIn = new JdDetail(new Date(), giftBag.getJdNum(), JdRewardType.giftBag.getIndex(), users.getId());
            jdDetailIn.setMsg("从" + giftBag.getName() + "获得" + giftBag.getJdNum() + "金豆");
            //打包，存入数据库
            List<ZsDetail> zsDetails = new ArrayList<>(2);
            zsDetails.add(zsDetailOut);
            zsDetails.add(zsDetailIn);
            zsDetailService.insertBatch(zsDetails);
            jdDetailService.insert(jdDetailIn);

            //记录礼包的购买记录
            GiftBagRecord giftBagRecord = new GiftBagRecord(users.getId(), giftBagId, new Date());
            boolean insert = giftBagRecordService.insert(giftBagRecord);
            //插入成功返回3.  失败返回4
            return insert ? 3 : 4;
        }
        //余额不足返回2
        return 2;
    }
}

