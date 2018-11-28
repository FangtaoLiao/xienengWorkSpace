package com.synpower.dao;

import java.util.List;
import java.util.Map;

import com.synpower.bean.SysRole;
import com.synpower.bean.SysRights;
import com.synpower.bean.SysUser;

public interface SysUserMapper {
	/** 
	  * @Title:  getUserByIdAndPsw 
	  * @Description:  登录 
	  * @param user
	  * @return: List<SysUser>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年10月27日下午2:27:53
	*/ 
	public List<SysUser> getUserByIdAndPsw (SysUser user);
	
	/** 
	  * @Title:  insertUserInfo 
	  * @Description:  新用户创建 
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:18:58
	*/ 
	public int insertUserInfo(SysUser user);
	/** 
	  * @Title:  getUserByLoginId 
	  * @Description:  查询登录账号是否存在 
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:20:33
	*/ 
	public List<SysUser> getUserByLoginId(String loginId);
	/** 
	  * @Title:  getUserByTel 
	  * @Description:  查询电话号码是否存在  
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:21:16
	*/
	public List<SysUser> getUserByTel(String tel);
	/** 
	  * @Title:  getUserByTel 
	  * @Description:  查询邮箱是否存在  
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:21:16
	*/
	public List<SysUser> getUserByEmail(String email);
	
	/** 
	  * @Title:  updateUserInfo 
	  * @Description: 更改登录信息 
	  * @param user
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年10月27日下午2:28:50
	*/ 
	public int updateLoginInfo(SysUser user);
	/** 
	  * @Title:  countUser 
	  * @Description:  获取满足条件的账户总数 
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月27日下午6:20:09
	*/ 
	public int countUser(Map map);
	/** 
	  * @Title:  listUserInfo 
	  * @Description:  获取账户列表 
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月27日下午6:19:15
	*/ 
	public List<Map> listUserInfo(Map map);
	/** 
	  * @Title:  updateUserStatus 
	  * @Description:  启用/停用 
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月27日下午6:20:51
	*/ 
	public int updateUserStatus(Map map);
	/** 
	  * @Title:  updateUserValid 
	  * @Description:  账户列表的删除 
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:13:31
	*/
	public int updateUserValid(Map map);
	public List<SysRole> getRoleByOrgId(Map map);
	public SysUser getUserInfoById(String userId);
	public int updateUserById(Map map);
	public int updateRoleById(Map map);
	/** 
	  * @Title:  getUseInfo 
	  * @Description:  我的信息模块的基本信息展示 
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:15:04
	*/ 
	public SysUser getUserInfo(String userId);
	/** 
	  * @Title:  updateUserInfo 
	  * @Description:  我的信息模块的修改
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:16:07
	*/
	public int updateUserInfo(SysUser user);
	public List<SysUser> getTelNotMyself(Map map);
	public List<SysUser> getEmailNotMyself(Map map);
	public List<SysUser> getLoginNotMyself(Map map);
	/** 
	  * @Title:  deletePlantUser 
	  * @Description:  电站设置模块中的删除业主信息 
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:45:38
	*/ 
	public int deletePlantUser(Map map);
	/** 
	  * @Title:  updatePlantUser 
	  * @Description:  电站设置模块中的修改业主信息 
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:46:38
	*/ 
	public int updatePlantUser(Map map);

	/** 
	  * @Title:  getRightsByUid 
	  * @Description:  TODO 
	  * @param parseInt
	  * @return: List<SysRights>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月13日上午10:28:05
	*/
	public List<SysRights> getRightsByUid(int id);
	public int updateUserIcon(String userId);
	public int insertUserIcon(Map map);
	public int inserNewUser(SysUser user);
	public SysUser selectUserInfo(String userId);
	public int updateAccountById(Map map);
	public List<SysUser> getUserPwd(Map map);
	public int updatePwd(Map map);
	public SysUser getOrgUser(String org_id);
	public int insertAdminUser(SysUser user);
	public int updateOrgUser(SysUser user);
	public List<SysUser> selectOrgIsNull(List<String> list);
	/**
	 * 根据微信号查询账号
	 * @param weixin 微信号
	 * @return 账号信息
	 */
	public List<SysUser> getUserByWeixin(String weixin);

	public List<SysUser> getUserByPlant(Map<String, Object> paramMap);
	public List<Integer> getUsers(Map<String, Object> map);
	public List<Integer> getPidByUser(Map<String, Object> map);
	/** 
	  * @Title:  updateUserTel 
	  * @Description:  微信修改用户电话号码 
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月3日下午2:34:39
	*/ 
	public int updateUserTel(Map map);
	/** 
	  * @Title:  updateUserName 
	  * @Description:  微信修改用户名称 
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月3日下午2:52:57
	*/ 
	public int updateUserName(Map map);

	/** 
	  * @Title:  insertPlantUser 
	  * @Description:  添加业主账户 
	  * @param user
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月10日上午10:33:07
	*/ 
	public int insertPlantUser(SysUser user);
	
	/** 
	  * @Title:  getUnactiveUser 
	  * @Description:  根据手机号获取待激活的用户 
	  * @param tel
	  * @return: SysUser
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月10日上午10:47:47
	*/ 
	public SysUser getUnactiveUser(String tel);
	public int updateUser(SysUser user);
	/**
	  * @Title:  getUserByPlant2 
	  * @Description:  TODO 
	  * @param contacts
	  * @return: SysUser
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月11日下午2:23:52
	*/
	public SysUser getUserjoinPlant(String contacts);
	
	/** 
	  * @Title:  getUsersByIds 
	  * @Description:  根据id集合获取用户集合 
	  * @param ids
	  * @return: List<SysUser>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月12日下午2:23:22
	*/ 
	public List<SysUser>getUsersByIds(List list);

	/** 
	  * @Title:  updateUser4Plant 
	  * @Description:  TODO 
	  * @param parameterMap
	  * @return: int
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月15日下午3:45:33
	*/
	public int updateUser4Plant(Map<String, Object> parameterMap);
	/** 
	  * @Title:  updateUserOrg 
	  * @Description: 修改账户组织 
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月18日上午10:31:14
	*/ 
	public int updateUserOrg(Map map);
	/** 
	  * @Title:  getWXUserByTel 
	  * @Description:  TODO 
	  * @param tel
	  * @return: List<SysUser>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月18日上午10:59:07
	*/ 
	public List<SysUser> getWXUserByTel(String tel);
	/** 
	  * @Title:  updateAdminValid 
	  * @Description:  删除组织默认管理员账号 
	  * @param list
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年2月1日下午2:04:21
	*/ 
	public int updateAdminValid(List list);
	public List<SysUser> getUserByUid(Integer id);
	public int updateUserUUID(SysUser sysUser);
	public SysUser getUserByUUID(String uuid);
	public  int updateByPrimaryKeySelective(SysUser record);
	public int  insertSelective(SysUser record);
	public List<SysUser> getUserByOrgIds(List list);
}