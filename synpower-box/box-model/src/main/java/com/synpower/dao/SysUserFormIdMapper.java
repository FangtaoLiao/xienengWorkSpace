package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.SysUserFormId;

@Repository
public interface SysUserFormIdMapper {
	public int insertUserFormId(List<Map<String, String>> list);
	public SysUserFormId getFormIdInfo(String userId);
	public int deleteFormIdInfo(int formId);
	public int getCount(String userId);
}