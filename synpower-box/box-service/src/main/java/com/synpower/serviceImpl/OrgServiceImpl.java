package com.synpower.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysRights;
import com.synpower.bean.SysRole;
import com.synpower.bean.SysUser;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysRoleMapper;
import com.synpower.dao.SysRoleRightsMapper;
import com.synpower.dao.SysScreenMapper;
import com.synpower.dao.SysUserMapper;
import com.synpower.dao.SysUserRoleMapper;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.OrgService;
import com.synpower.util.ChineseToEnglish;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;
import com.synpower.util.ZTreeNode;

@Service
public class OrgServiceImpl implements OrgService{
	private Logger logger = Logger.getLogger(OrgServiceImpl.class);
	@Autowired
	private SysOrgMapper mapper;
	@Autowired
	private SysUserMapper umapper;
	@Autowired
	private SysRoleMapper rmapper;
	@Autowired
	private SysUserRoleMapper userRoleMapper;
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired
	private SysRoleRightsMapper roleRightsMapper;
	@Autowired
	private SysScreenMapper screenMapper;
	@Autowired
	private ConfigParam configParam;
	/** 
	  * @Title:  getAllOrgByUid 
	  * @Description:根据用户ID获取其相关组织树
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月10日下午5:08:04
	*/ 
	@Override
	public MessageBean getAllOrgByUid(String jsonData,Session session) throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg=new MessageBean();
		Map<String, Object> map=Util.parseURL(jsonData);
		User u=session.getAttribute(map.get("tokenId")+"");
		SysOrg orgNow=((SysOrg)u.getUserOrg());
		String orgId=orgNow.getId().intValue()+"";
		List<SysOrg>orgList=mapper.getAllOrg();
		ZTreeNode rootNode=new ZTreeNode();
		//装载根节点
		List<ZTreeNode>resultList=new ArrayList<>();
		for (int i = 0; i < orgList.size(); i++) {
			SysOrg org=orgList.get(i);
			if (orgId.equals(org.getId()+"")) {
				rootNode.setId(org.getId()+"");
				rootNode.setName(org.getOrgName());
				rootNode.setOpen(true);
				rootNode.setpId("0");
				rootNode.setIconSkin("ztree"+org.getId());
				resultList.add(rootNode);
				orgList.remove(i);
				break;
			}
		}
		//装载协能共创 
		if (orgNow.getFatherId()!=0) {
			for (int i = 0; i <  orgList.size(); i++) {
				SysOrg org=orgList.get(i);
				if ("0".equals(org.getId()+"")) {
					//清空结果集
					resultList.clear();
					ZTreeNode fatherNode=new ZTreeNode();
					fatherNode.setId(org.getId()+"");
					fatherNode.setName(org.getOrgName());
					fatherNode.setOpen(true);
					fatherNode.setpId("0");
					fatherNode.setIconSkin("ztree"+org.getId());
					resultList.add(fatherNode);
					rootNode.setpId(org.getId()+"");
					resultList.add(rootNode);
				}
			}
		}
		//从第二级开始解析
		resultList=ServiceUtil.getTree(orgList,rootNode,resultList,2);
		msg.setBody(resultList);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}
	@Override
	public MessageBean getOrgTree(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg=new MessageBean();
		Map<String, Object> map=Util.parseURL(str);
		User u=session.getAttribute(map.get("tokenId")+"");
		//当前登录用户的组织
		SysOrg orgNow=((SysOrg)u.getUserOrg());
		//当前登录用户的组织id
		String orgId=orgNow.getId().intValue()+"";
		List<SysOrg>orgList=mapper.getAllOrg();
		//装载根节点
		List resultList=new ArrayList<>();
		Map<String, Object> fatherMap = new HashMap<String,Object>();
		for (int i = 0; i < orgList.size(); i++) {
			SysOrg org=orgList.get(i);
			if (orgId.equals(org.getId()+"")) {
				fatherMap.put("check", "false");
				fatherMap.put("id", org.getId());
				fatherMap.put("icon", "");
//				fatherMap.put("isParent", "true");
				fatherMap.put("name", org.getOrgName());
				fatherMap.put("open", "true");
				fatherMap.put("pId", "0");
				fatherMap.put("orgType", org.getOrgType());
				fatherMap.put("icon", "/images/setting/file.png");
				orgList.remove(i);
				fatherMap.put("children", getChildTree(org.getId()+"",orgList));
				resultList.add(fatherMap);
				break;
			}
		}
		msg.setBody(resultList);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}
	public List getChildTree(String orgId,List<SysOrg> orgList){ 
		List childList = new ArrayList<>();
		for (int i = 0; i < orgList.size(); i++) {
			SysOrg org=orgList.get(i);
			if(orgId.equals(org.getFatherId()+"")){
				Map<String, Object> childMap = new HashMap<String,Object>();
				childMap.put("id", org.getId());
				childMap.put("pId", org.getFatherId());
				childMap.put("icon", "");
				childMap.put("iconSkin", "icon01");
				childMap.put("name", org.getOrgName());
				childMap.put("orgType", org.getOrgType());
				childMap.put("icon", "/images/setting/file2.png");
				childMap.put("children", getChildTree(org.getId()+"",orgList));
				childList.add(childMap);
			}
		}
		return childList;
	}
	@Override
	public MessageBean getOrgInfo(String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg=new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u=session.getAttribute(map.get("tokenId")+"");
		String orgId = map.get("orgId")+"";
		SysOrg sysOrg = null;
		SysUser user = null;
		if(!"null".equals(orgId) && !"".equals(orgId)){
			sysOrg = mapper.getOrgInfoByUser(orgId);
			user = umapper.getOrgUser(orgId);
		}else{
			String defaultOrgId = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
			sysOrg = mapper.getOrgInfoByUser(defaultOrgId);
			user = umapper.getOrgUser(defaultOrgId);
		}
		if (sysOrg!=null) {
			Map<String, Object> resultMap = new HashMap<String,Object>();
			resultMap.put("orgName", sysOrg.getOrgName());
			resultMap.put("orgId", sysOrg.getId()+"");
			resultMap.put("orgCode", sysOrg.getOrgCode());
			resultMap.put("status", sysOrg.getOrgStatus());
			resultMap.put("orgType", sysOrg.getOrgType());
			if(user != null){
				resultMap.put("loginId", user.getLoginId());
				resultMap.put("email", user.getEmail());
				resultMap.put("userTel", user.getUserTel());
				resultMap.put("userId", user.getId()+"");
			}
			msg.setBody(resultMap);
			msg.setMsg("获取组织信息成功");
			msg.setCode(Header.STATUS_SUCESS);
		}else{
			throw new ServiceException("获取组织信息失败");
		}
		return msg;
	}
	
	@Transactional
	@Override
	public MessageBean addOrgInfo(String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u=session.getAttribute(map.get("tokenId")+"");
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		//装载新加组织信息
		SysOrg sysOrg = new SysOrg();
		sysOrg.setOrgName(map.get("orgName")+"");
		String orgCode = getOrgCode();
		sysOrg.setOrgCode(orgCode);
		sysOrg.setOrgStatus(map.get("orgStatus")+"");
		sysOrg.setOrgType(map.get("orgType")+"");
		sysOrg.setOrgValid("0");
		sysOrg.setFatherId(Integer.parseInt(map.get("pid")+""));
		sysOrg.setCreateTime(System.currentTimeMillis());
		sysOrg.setCreateUser(Integer.parseInt(u.getId()) );
//		String systemLogo = findSystemLogo(sysOrg.getFatherId());
//		sysOrg.setSystemLogo(systemLogo);
//		String systemName = findSystemName(sysOrg.getFatherId());
//		sysOrg.setSystemName(systemName);
//		String screenLogo = findScreenLogo(sysOrg.getFatherId());
//		sysOrg.setScreenLogo(screenLogo);
		//执行添加、查看更改结果
		int result = mapper.addOrgInfo(sysOrg);
		logger.info(StringUtil.appendStr("状态result",result,"   ",sysOrg.toString()));
		//新增组织后新增组织对应的大屏信息
		int screenResult = screenMapper.insertScreenByOrgId(String.valueOf(sysOrg.getId()));
		
		String loginId = map.get("loginId")+"";
		String email = map.get("email")+"";
		String password = map.get("password")+"";
		String userTel = map.get("userTel")+"";
		if(!"null".equals(loginId) && !"".equals(loginId)){
			if(result != 0){
				//新增用户的时候默认增加组织管理人员
				SysUser user = new SysUser();
				user.setLoginId(loginId);
				if(!"null".equals(email) && !"".equals(email)){
					user.setEmail(email);
				}
				user.setPassword(password);
				user.setUserTel(map.get("userTel")+"");
//				user.setUserName("orgAdmin");
//				user.setPinyinSearch(ChineseToEnglish.getPingYin("orgAdmin"));
				user.setOrgId(sysOrg.getId());
				user.setCreateTime(System.currentTimeMillis());
				user.setUserStatus("1");
				user.setUserValid("0");
				user.setDefaultType("1");
				user.setUserType("0");
				user.setUseTel("0");
				int UserResult  =umapper.insertAdminUser(user);
				if(UserResult != 0){
					Map<String, Object> paramMap = new HashMap<String,Object>();
					//添加默认管理员权限
					paramMap.put("roleId", "1");
					paramMap.put("userId", user.getId());
					paramMap.put("createTime", System.currentTimeMillis());
					paramMap.put("createUser", u.getId());
					int roleResult = userRoleMapper.insertUserRole(paramMap);
					if(roleResult != 0){
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.ADD_ORG_SUCCUESS);
					}else{
						logger.warn(StringUtil.appendStr("新增管理员权限失败! 操作者:",logUserName));
						msg.setCode(Header.STATUS_FAILED);
						msg.setMsg(Msg.ADD_ORG_FAILED);
					}
				}else{
					logger.warn(StringUtil.appendStr("新增管理员失败! 操作者:",logUserName));
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg(Msg.ADD_ORG_FAILED);
				}
			}else{
				logger.warn(StringUtil.appendStr("新增组织失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.ADD_ORG_FAILED);
			}
		}else{
			if(result != 0){
				logger.info(StringUtil.appendStr("新增组织成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(Msg.ADD_ORG_SUCCUESS);
			}else{
				logger.warn(StringUtil.appendStr("新增组织失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.ADD_ORG_FAILED);
			}
		}
		return msg;
	}
	public String findSystemLogo(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg fatherOrg = mapper.getOrgById(String.valueOf(fatherId));
			String systemLogo = fatherOrg.getSystemLogo();
			if(systemLogo == null || systemLogo.equals("")){
				return findSystemLogo(fatherOrg.getFatherId());
			}else{
				String imgUrl=configParam.getSystemPhotoUrl()+systemLogo.substring(systemLogo.lastIndexOf("/"));
				File destFile = new File(imgUrl);
				if (destFile.exists()) {
					String suffix = String.valueOf(System.currentTimeMillis());
					String targetUrl = configParam.getSystemPhotoUrl()+"/"+suffix+imgUrl.substring(imgUrl.lastIndexOf("."));
					copyFile(imgUrl, targetUrl);
					result += configParam.getSystemPhotoRequestUrl()+targetUrl.substring(targetUrl.lastIndexOf("/"));
				}
				return result;
			}
		}else{
			return null;
		}
	}
	public String findScreenLogo(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg fatherOrg = mapper.getOrgById(String.valueOf(fatherId));
			String screenLogo = fatherOrg.getScreenLogo();
			if(screenLogo == null || screenLogo.equals("")){
				return findScreenLogo(fatherOrg.getFatherId());
			}else{
				String imgUrl=configParam.getSystemPhotoUrl()+screenLogo.substring(screenLogo.lastIndexOf("/"));
				File destFile = new File(imgUrl);
				if (destFile.exists()) {
					String suffix = String.valueOf(System.currentTimeMillis());
					String targetUrl = configParam.getSystemPhotoUrl()+"/"+suffix+imgUrl.substring(imgUrl.lastIndexOf("."));
					copyFile(imgUrl, targetUrl);
					result += configParam.getSystemPhotoRequestUrl()+targetUrl.substring(targetUrl.lastIndexOf("/"));
				}
				return result;
			}
		}else{
			return null;
		}
	}
	public String findSystemName(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg fatherOrg = mapper.getOrgById(fatherId+"");
			String systemName = fatherOrg.getSystemName();
			if(systemName == null || systemName.equals("")){
				return findSystemName(fatherOrg.getFatherId());
			}else{
				result += systemName;
				return result;
			}
		}else{
			return null;
		}
	}
	public void copyFile(String src, String target) {
		File srcFile = new File(src);
		File targetFile = new File(target);
		try {
			InputStream in = new FileInputStream(srcFile);
			OutputStream out = new FileOutputStream(targetFile);
			byte[] bytes = new byte[1024];
			int len = -1;
			while ((len = in.read(bytes)) != -1) {
				out.write(bytes, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	  * @Title:  saveRightsOfRole 
	  * @Description: 赋予默认角色相关权限
	  * @param result2/u/role/right/msg
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月10日下午2:28:20
	 */
	public MessageBean saveRightsOfRole(int result2,User u,SysRole role,String[] right,MessageBean msg) throws SessionException, SessionTimeoutException, ServiceException {
		String uId = u.getId();
		
			Map<String,Object> map = new HashMap<>();
			//封装角色、权限相关参数 进行添加
			map.put("uId", uId);
			map.put("roleId", role.getId());
			map.put("list", right);
			map.put("createTime", System.currentTimeMillis());
			int status=rmapper.saveRightsOfRole(map);
			if (status>0) {
				logger.info(" 新增角色成功! 操作人ID: "+uId+" 角色ID: "+role.getId()+"权限范围"+right.toString());
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.ROLE_SAVE_SUCCUESS);
			}else{
				logger.warn(" 新增角色失败!! 操作人ID: "+uId+"权限范围"+Arrays.toString(right));
				throw new ServiceException(Msg.ROLE_SAVE_FAIL);
			}
		
		return msg;
	}
	
	
	/**
	 * 
	  * @Title:  commonStatics 
	  * @Description:  封装默认组织的角色参数 
	  * @param roleName/json/Datasession
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: SysRole
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月10日下午2:29:20
	 */
	public SysRole commonStatics(String roleName,String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException {
		Map<String, Object> map = Util.parseURL(jsonData);
		User u=session.getAttribute(map.get("tokenId")+"");
		String org_id = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
		//新增组织添加角色参数
		SysRole role = new SysRole();
		role.setCreateTime(System.currentTimeMillis());
		role.setFatherId(u.getRoleId());
		role.setLastModifyUser(Integer.parseInt(u.getId()));
		role.setOrgId(Integer.parseInt(org_id));
		role.setRoleName(roleName);
		role.setRoleStatus("1");
		role.setRoleValid("0");
		role.setSupStatus("0");
		return role;
	}
	@Transactional(rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateOrgInfo(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg=new MessageBean();
		//tokenId获取用户信息、对应组织ID
		Map<String, Object> map = Util.parseURL(jsonData);
		User u=session.getAttribute(map.get("tokenId")+"");
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		//组装前台新组织信息
		SysOrg sysOrg = new SysOrg();
		sysOrg.setId(Integer.parseInt(map.get("orgId")+""));
		sysOrg.setOrgName(map.get("orgName")+"");
		sysOrg.setOrgCode(map.get("orgCode")+"");
		sysOrg.setOrgStatus(map.get("orgStatus")+"");
		sysOrg.setLastModifyUser(Integer.parseInt(u.getId()));
		sysOrg.setLastModifyTime(System.currentTimeMillis());
		//执行更改、查看更改结果
		int result = mapper.updateOrg(sysOrg);
		String loginId = map.get("loginId")+"";
		String email = map.get("email")+"";
		String userTel = map.get("userTel")+"";
		if(!"null".equals(loginId) && !"".equals(loginId)){
			if(result != 0){
				SysUser user = new SysUser();
				user.setId(Integer.parseInt(map.get("userId")+""));
				user.setLoginId(loginId);
				user.setEmail(email);
				user.setUserTel(userTel);
				user.setLastModifyTime(System.currentTimeMillis());
				user.setLastModifyUser(Integer.parseInt(u.getId()));
				int userResult = umapper.updateOrgUser(user);
				if(userResult != 0){
					logger.info(StringUtil.appendStr("修改组织信息成功! 操作者:",logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg("修改组织信息成功");
				}else{
					logger.warn(StringUtil.appendStr("修改组织信息失败! 操作者:",logUserName));
					throw new ServiceException("修改组织信息和管理员信息失败");
				}
			}else{
				logger.warn(StringUtil.appendStr("修改组织信息失败! 操作者:",logUserName));
				throw new ServiceException("修改组织信息失败");
			}
		}else{
			if(result != 0){
				logger.info(StringUtil.appendStr("修改组织信息成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("修改组织信息成功");
			}else{
				logger.warn(StringUtil.appendStr("修改组织信息失败! 操作者:",logUserName));
				throw new ServiceException("修改组织信息失败");
			}
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean deleteOrgInfo(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg=new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId")+"");
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String org_id = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
		if(org_id.equals(String.valueOf(map.get("orgId")))){
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("不能删除自己的组织");
			return msg;
		}
		//得到所有组织列表
		List<SysOrg>orgList=mapper.getAllOrg();
		List<String>reList=new ArrayList<>();
		//查出组织id有哪些
		reList.add(map.get("orgId")+"");
		reList = ServiceUtil.getTree(orgList, map.get("orgId")+"",reList);
		// 查询该组织下面是否还有账号
		List<SysUser> userList = umapper.getUserByOrgIds(reList);
		if(userList.size() > 0){
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("该组织下有账号信息，请先将账号从该组织移除后再进行删除操作");
			return msg;
		}
		Map<String,Object> parameterMap = new HashMap<String,Object>();
		parameterMap.put("reList", reList);
		// 查询该组织下面是否还有电站
		List<PlantInfo> plantList = plantMapper.getPlantByOrgIds(parameterMap);
		if(plantList.size() > 0){
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("该组织下有电站信息，请先将电站从该组织移除后再进行删除操作");
			return msg;
		}
		parameterMap.put("lastModifyTime", System.currentTimeMillis());
		parameterMap.put("lastModifyUser", u.getId());
		//执行删除、查看更改结果
		int result = mapper.deleteOrg(parameterMap);
		if(result != 0){
			int screenResult = screenMapper.deleteScreenByOrgId(reList);
			int userResult = umapper.updateAdminValid(reList);
			logger.info(StringUtil.appendStr("删除组织信息成功! 操作者:",logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg("删除组织信息成功");
		}else{
			logger.warn(StringUtil.appendStr("删除组织信息失败! 操作者:",logUserName));
			throw new ServiceException("删除组织信息失败");
		}
		return msg;
	}
	
	@Override
	public MessageBean getOrgAgentor(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		
		MessageBean msg=new MessageBean();
		//tokenId获取用户信息、对应组织ID
		Map<String, Object> map = Util.parseURL(jsonData);
		User u=session.getAttribute(map.get("tokenId")+"");
		String org_id = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
		
		String userName = map.get("userName")+"";
		//根据id、查询条件进行查询 返回查询结果
		SysOrg sysOrg = mapper.getOrgAgentor(userName,org_id);
		
		if (sysOrg!=null) {
			map.clear();
			map.put("orgAgentorName", sysOrg.getOrgAgentorName());
			map.put("orgAngetorId", sysOrg.getOrgAngetorId());
			map.put("orgAngetorTel", sysOrg.getOrgAngetorTel());	
			msg.setBody(map);
			msg.setMsg("获取组织代表成功");
			msg.setCode(Header.STATUS_SUCESS);
		}else{
			throw new ServiceException("查无此人，获取组织代表信息失败");
		}
		return msg;
	}

	@Override
	public MessageBean getOrgList(String jsonData, Session session,HttpServletRequest request) throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg=new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		//URL获取tokenID
		User u = session.getAttribute(request.getParameter("tokenId"));
		//String org_id = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
		//得到所有组织列表
		List<SysOrg>orgList=mapper.getAllOrg();
		List<String>reList=new ArrayList<>();
		//判断是否有选中的组织id，若没有则获取当前登录用户的组织id
		if(map.get("orgId")!= null && !"".equals(map.get("orgId"))){
			reList.add(map.get("orgId")+"");
			reList = ServiceUtil.getTree(orgList, map.get("orgId")+"",reList);
		}else{
			reList.add(((SysOrg)u.getUserOrg()).getId()+"");
			reList = ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);
		}
		// 获取模糊查询用户姓名
		String orgName = (String) map.get("orgName");
		Map<String, Object> newMap = new HashMap<String,Object>();
		//System.out.println(reList);
		newMap.put("orgName", orgName);
		newMap.put("reList", reList);
		int count = mapper.countOrg(newMap);
		//System.out.println(count+"----------------------------------");
		// 获取每页显示几条数据
		Integer each_page_num = Integer.parseInt(map.get("limit")+"");
		// 获取显示第几页的数据
		Integer alarm_page_num = Integer.parseInt(map.get("offset")+"");
		// 获取排序方式
		String order = (String) map.get("order");
		// 获取排序列名称
		String sort = (String) map.get("sort");
		int min = (alarm_page_num - 1) * each_page_num;
		if (min < 0) {
			min = 0;
		}
		int max = each_page_num;
		if (max > count) {
			max = count;
		}
		Map<String, Object> parameterMap = new HashMap<String,Object>();
		//组装分页查询参数并获得结果集
		parameterMap.put("orgName", orgName);
		parameterMap.put("min", min);
		parameterMap.put("max", max);
		parameterMap.put("sort", sort);
		parameterMap.put("order", order);
		parameterMap.put("reList", reList);
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<SysOrg> list = mapper.listOrgInfo(parameterMap);
		//装载根节点
		for (SysOrg org : list) {
			Map<String, String> mapTemp = new HashMap<String, String>();
			mapTemp.put("orgName", org.getOrgName()+"");
			mapTemp.put("orgCode", org.getOrgCode()+"");
			mapTemp.put("orgValiCode", org.getOrgValiCode()+"");
			mapTemp.put("orgAgentorName", org.getOrgAgentorName()+"");
			mapTemp.put("orgAngetorTel", org.getOrgAngetorTel()+"");
			mapTemp.put("orgStatus", org.getOrgStatus()+"");
			resultList.add(mapTemp);
		}	
		Map<String, Object> reMap = new HashMap<String,Object>();
		reMap.put("total", count);
		reMap.put("rows", resultList);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(reMap);
		return msg;
	
	}

	@Override
	public MessageBean getRoleList(String jsonData, Session session, HttpServletRequest request) throws SessionException, SessionTimeoutException,ServiceException{
		MessageBean msg=new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId")+"");
		List<SysRights> rights = mapper.getRightsByUser(Integer.parseInt(u.getId()));
		if (rights!=null) {
			msg.setBody(rights);
			msg.setMsg("获取用户权限成功");
			msg.setCode(Header.STATUS_SUCESS);
		}else{
			
			throw new ServiceException("获取用户权限失败");
		}
		return msg;
	}

	@Override
	public MessageBean showRoleAndRights(String jsonData, Session session, HttpServletRequest request)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg=new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId")+"");
		//获取用户id
		String id = u.getId();
		String org_id = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
		List<SysRole> list = rmapper.getActualOrgRoles(org_id);
		//复用map
		map.clear();
		//角色名称列表
		List<Object> nameList = new ArrayList<>();
		//角色id列表
		List<Object> idList = new ArrayList<>();
		for (SysRole sysRole : list) {
			idList.add(sysRole.getId());
			nameList.add(sysRole.getRoleName());
		}
		map.put("list3", idList);
		//获取角色对应权限列表
		List<SysRights> rightsList = mapper.getRightsByRole(map);
		for (SysRights sysRights : rightsList) {
			sysRights.getRightsName();
		}
		msg.setBody(rightsList);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg("获取角色对应权限成功");
		return msg;
	}

	public String getOrgCode(){
		return getCode();
	}
	public String getCode(){
		//生成以X开头加上5位随机数的组织机构代码
		StringBuffer buffer=new StringBuffer();
		buffer.append("X");
		for (int i = 0; i < 5; i++) {
			buffer.append((int)(Math.random() * 9 ));
		}
		String orgCode = buffer.toString();
		List<Map<String, Object>> list = mapper.getOrgCode();
		if (Util.isNotBlank(list)) {
			boolean b=true;
			for (Map<String, Object> map : list) {
				if(orgCode.equals(map.get("org_code"))){
					//相同则继续递归递归生成
					return getCode();
				}
			}
		}
		return orgCode;
	}

	@Override
	public MessageBean selectOrgIsNull(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId")+"");
		//得到所有组织列表
		List<SysOrg>orgList=mapper.getAllOrg();
		List<String>reList=new ArrayList<>();
		//查出组织id有哪些
		reList.add(map.get("orgId")+"");
		reList = ServiceUtil.getTree(orgList, map.get("orgId")+"",reList);
		//查询该组织下面是否还有人
		List<SysUser> list = umapper.selectOrgIsNull(reList);
		if(list.size() != 0){
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(Msg.USER_EXISTS);
		}else{
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(Msg.ORGUSER_NOT_EXISTS);
		}
		return msg;
	}


}
