package com.synpower.dao;

import com.synpower.bean.SysSharedLinks;

public interface SysSharedLinksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysSharedLinks record);

    int insertSelective(SysSharedLinks record);

    SysSharedLinks selectByLinkCode(String sharedLinkCode);

    int updateByPrimaryKeySelective(SysSharedLinks record);
    public int updateLinkUserdUser(SysSharedLinks record);
    public int updateLinkTime(SysSharedLinks record);
    int updateByPrimaryKey(SysSharedLinks record);
    public SysSharedLinks getRegisterCodeByCode(String sharedLinkCode);
}