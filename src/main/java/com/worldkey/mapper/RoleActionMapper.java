package com.worldkey.mapper;

import com.worldkey.entity.RoleAction;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleActionMapper {
    int insert(RoleAction record);

    int insertSelective(RoleAction record);

	int delete(RoleAction record);

	List<RoleAction> selectByAction(Integer id);

	@Delete("DELETE FROM role_action " +
            "WHERE role_action.role=#{id}")
    void deleteRoleActionByRole(Integer id);
}