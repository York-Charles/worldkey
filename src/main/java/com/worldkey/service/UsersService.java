package com.worldkey.service;

import com.github.pagehelper.PageInfo;
import com.worldkey.entity.Presentor;
import com.worldkey.entity.Users;
import com.worldkey.exception.Z406Exception;
import com.worldkey.util.Pager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface UsersService {
    int updateByPrimaryKeySelective(Users usersVo);
    int reg(Users usersVo,Users recommendedCode);

    int checkName(String loginName);
    int checkPetName(String petName);
    Users findByToken(String token);
    String login(String loginName, String password);
    int logout(String token);
    Users login(Users user);
    Users findByLoginName(String loginName);
    Users changeInfo(/*MultipartFile image,*/ Users usersVo, String host, String token);
    int addBalance(Users users);
    BigDecimal findBalanceByID(Long id);

    PageInfo<Users> findAll(Integer pageNum, Integer pageSize);

    void getUsersExcel(HttpServletResponse response) throws IOException;

    Integer getPraiseNum(Long id);

    Integer addJd(Integer releaseAwardsJd, Long id);

    Integer addOrSubZs(Integer zsNum, Long id);

    Users getJdAndZsAndKb(Long id);

    Integer addOrSubKb(BigDecimal kbNum, Long id);

    Integer kb2Zs(Integer kbNum, Long id);
    //5.11
    
    PageInfo<Users> findUserByGroup(Integer groupId,Integer pageNum,Integer pageSize);
    
    Users selectGroupLeader(Integer groupId);
    
    PageInfo<Users> findUserByGroup1(Integer groupId,Integer pageNum,Integer pageSize);
    
    //6.7 个人详细信息展示
    Integer updateBackground(Users user);//转入updateSelective
    
    Map<String,Object> userDetails(Long userId);
    
    Users getUserDetail(Long id);
    
    Integer editFond(Users user);
    
    Integer insertModule(Users record);
    
    Map<String,Object> reg1(Users usersVo);
    
    Users findByTelNum(String phone);
    
    //6.25
    PageInfo<Users> getFans(Integer userId,Integer pageNum,Integer pageSize);
    
    PageInfo<Users> getStars(Integer userId,Integer pageNum,Integer pageSize);
    
    Pager<Presentor> getPresentors(Long id,Integer pageNum,Integer pageSize);

    Users selectByPrimaryKey(Long id);
    
    Map<String,Object> getRelationsAndComments(Long userId);
    
    Integer updatePersonalBrand(Long userId);
}
