package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.SysRoleRights;
@Repository
public interface SysRoleRightsMapper {
	public List<Integer> listRoleRights();
	public int insertRoleRights(List list);
}