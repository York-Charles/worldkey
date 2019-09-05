package com.worldkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldkey.entity.BaseShow;
import com.worldkey.entity.BrowsingHistory;
import com.worldkey.entity.InformationAll;
import com.worldkey.entity.Praise;
import com.worldkey.entity.Users;
import com.worldkey.mahout.CollaborativeFiltering;
import com.worldkey.mapper.BrowsingHistoryMapper;
import com.worldkey.mapper.InformationAllMapper;
import com.worldkey.mapper.PraiseMapper;
import com.worldkey.service.BrowsingHistoryService;
import com.worldkey.service.InformationAllService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.impl.model.BooleanPreference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {
    @Resource
    private BrowsingHistoryMapper browsingHistoryMapper;
    @Resource
    private CollaborativeFiltering mahout;
    @Resource
    private InformationAllService allService;
    @Resource 
    private InformationAllMapper informationAllMapper;
    @Resource
	private PraiseMapper praiseMapper;

    @Override
    public List<BrowsingHistory> findAll() {
        return this.browsingHistoryMapper.select();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BrowsingHistory vo) {
        return this.browsingHistoryMapper.insert(vo);
    }

    @Override
    public List<BooleanPreference> findByUser(Long user) {
        return this.browsingHistoryMapper.selectByUser(user);
    }

    @Override
    public Set<Long> findUsers() {
        return  this.browsingHistoryMapper.selectUsers();
    }



    @Override
    public PageInfo<InformationAll> tuijian(Long userID, Integer pageNum, Integer pageSize) throws Exception {
        List<Long> items = mahout.baseUser(userID, pageSize * pageNum);
        int count=items.size()-(pageNum-1)*pageNum;
        log.error("推荐：count："+count);
        if (count>=10){
           // List<Long> longs = items.subList((pageNum - 1) * pageNum, items.size());
            return new PageInfo<>(this.allService.findByIds(items));
        }
        int infoPageNum=pageNum-items.size()/10;
        PageHelper.startPage(infoPageNum,pageSize);
        List<InformationAll>list=this.allService.findOrderByPointNumber();
        PageInfo<InformationAll> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPages(pageInfo.getPages()+(items.size()/10));
        return pageInfo;
    }

    @Override
    public PageInfo<BaseShow> recommendation(Long userID, Integer pageNum, Integer pageSize) throws Exception {
        List<Long> items = mahout.baseUser(userID, pageSize * pageNum);
        int count=items.size()-(pageNum-1)*pageNum;
        if (count>=10){
            // List<Long> longs = items.subList((pageNum - 1) * pageNum, items.size());
            return new PageInfo<>(this.allService.findByIds1(items));
        }
        int infoPageNum=pageNum-items.size()/10;
        PageHelper.startPage(infoPageNum,pageSize);
        List<BaseShow>list=this.allService.findOrderByPointNumber1();
        if(userID!=null){
	        for(BaseShow l:list){
	        	Integer i = this.praiseMapper.i(userID,l.getId());
	        	l.setStatus(i);
	        }
        }
        List<BaseShow> commentNum = informationAllMapper.selectCommentNum();
        if(userID!=null){
        	
        	 for(int i = 0;i<list.size();i++){
        		 Users user = new Users();
        		 user.setId(userID);
 				Praise p = new Praise();
 				p.setUsers(user);
 				p.setInformation(Integer.parseInt(list.get(i).getId() + ""));
 				List<Praise> isPraise = praiseMapper.selectExist(p);
 				if (isPraise.size() == 0) {
 					list.get(i).setIsPraise(0);
 				} else {
 					list.get(i).setIsPraise(1);
 				}
     			for(int j =0;j<commentNum.size();j++){
     				if(list.get(i).getId().equals((commentNum.get(j).getId()))){
     					list.get(i).setCommentNum(commentNum.get(j).getCommentNum());
     					break;
     				}
     			}
     		}
        }else{
        	 for(int i = 0;i<list.size();i++){
     			for(int j =0;j<commentNum.size();j++){
     				if(list.get(i).getId().equals((commentNum.get(j).getId()))){
     					list.get(i).setCommentNum(commentNum.get(j).getCommentNum());
     					break;
     				}
     			}
     		}
        }
       
        PageInfo<BaseShow> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPages(pageInfo.getPages()+(items.size()/10));
        return pageInfo;
    }
    

    @Override
    public BrowsingHistory findByUserAndItem(Long itemID, Long userID) {
        //log.info("-->findByUserAndItem");
        return this.browsingHistoryMapper.selectByUserAndItem(new BrowsingHistory(userID,itemID));
    }
    
    
}
