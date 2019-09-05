package com.worldkey.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.worldkey.config.SystemProperties;
import com.worldkey.entity.*;
import com.worldkey.enumpackage.JdRewardType;
import com.worldkey.enumpackage.ZsObtainType;
import com.worldkey.mapper.PrizeMapper;
import com.worldkey.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PrizeServiceImpl  extends ServiceImpl<PrizeMapper,Prize> implements PrizeService {
    @Resource private PrizeMapper prizeMapper;
    @Resource private SystemProperties systemProperties;
    @Resource private UsersService usersService;
    @Resource private PrizeRecordService prizeRecordService;
    @Resource private ZsDetailService zsDetailService;
    @Resource private JdDetailService jdDetailService;
    @Override
    public List<Prize> selectAll() {
        List<Prize> prizes = prizeMapper.selectAll();
        prizes.forEach(e->e.setChance(null));
        return  prizes;
    }

    @Override
    @Transactional
    public Prize prizeDraw(Users users){
        //获取一百以内随机数
        Random random = new Random();
        Integer MIN = 1;
        Integer MAX = 100;
        int randomNum = Math.abs(random.nextInt()) % (MAX - MIN + 1) + MIN;
        //获取全部奖品信息
        List<Prize> prizes = prizeMapper.selectAll();
        //抽奖
        Integer a=0;
        for (Prize prize : prizes) {
            a+=prize.getChance();
            if (randomNum<=a){
                //修改用户账户钻石余额
                usersService.addOrSubZs(prize.getZs()-systemProperties.getPrizeDraw(),users.getId());
               List<ZsDetail>zsDetails=new ArrayList<>(2);
                ZsDetail zsDetail = new ZsDetail(new Date(),-systemProperties.getPrizeDraw() , ZsObtainType.UsePrizeDraw.getIndex(), users.getId(),"抽奖使用"+systemProperties.getPrizeDraw()+"钻石");
                zsDetails.add(zsDetail);
                if (prize.getZs()>0){
                    ZsDetail zsDetail1 = new ZsDetail(new Date(), prize.getZs(), ZsObtainType.GetPrizeDraw.getIndex(), users.getId(),"抽奖获得"+prize.getZs()+"钻石");
                    zsDetails.add(zsDetail1);
                }
                zsDetailService.insertBatch(zsDetails);
                //修改用户金豆余额
                if (prize.getJd()>0){
                    usersService.addJd(prize.getJd(),users.getId());
                    //记录用户金豆明细
                    JdDetail jdDetail = new JdDetail(new Date(), prize.getJd(), JdRewardType.PrizeDraw.getIndex(),"抽奖获得"+prize.getJd()+"金豆", users.getId());
                    jdDetailService.insert(jdDetail);
                }
                //添加用户抽奖记录
                PrizeRecord prizeRecord = new PrizeRecord(users.getId(), prize.getPrizeId(), new Date());
                prizeRecordService.insert(prizeRecord);
                return prize;
            }
        }
    return null;
    }



}
