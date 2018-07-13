package com.worldkey.service;

import com.worldkey.entity.Friend;
import com.worldkey.entity.FriendExample;
import com.worldkey.entity.Record;

import io.rong.models.CodeSuccessResult;
import io.rong.models.TokenResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author HP
 */
public interface FriendService {


    TokenResult getToken(String userId, String name, String portraitUri) throws Exception;
    CodeSuccessResult refreshInfo(String userId, String name, String portraitUri) throws Exception;
    CodeSuccessResult addFriend(String fromUserId, String type, String message, String... toUserId)
            throws Exception;
    List<FriendExample> findFriend(String loginName);
    int checkFriend(Friend f);

    String updateHeadImg(String token, String host, MultipartFile image) throws Exception;

    int delFriend(Friend friend);
    
    
    int record(String userId,String toUsersID,String message);
    List<Record> select(String toUserId);
}
