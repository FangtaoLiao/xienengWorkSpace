package com.synpower.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synpower.bean.SysOrg;
import com.synpower.bean.SysRights;
import com.synpower.bean.SysRole;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysRightsMapper;
import com.synpower.dao.SysRoleMapper;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.RoleService;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.Util;
@Service
public class RoleServiceImpl implements RoleService{
	private Logger logger = Logger.getLogger(RoleServiceImpl.class);
	@Autowired
	private SysRoleMapper mapper;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private SysRightsMapper rightsMapper;
	/** 
	  * @Title:  updateRightsByRid 
	  * @Description:更改角色权限会影响以当前角色为树根节点的整个树节点上的角色权限
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月10日下午5:08:04
	*/ 
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public MessageBean updateRightsByRid(Session session,String jsonData,HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map=Util.parseURL(jsonData);
		User u=session.getAttribute(map.get("tokenId")+"");
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		MessageBean msg=new MessageBean();
		String roleId =map.get("roleId")+"";
//		if(roleId.equals("1")){
//			msg.setCode(Header.STATUS_FAILED);
//			msg.setMsg("管理员属于内默认角色 无法操作");
//			return msg;
//		}else if(roleId.equals("2")){
//			msg.setCode(Header.STATUS_FAILED);
//			msg.setMsg("电站业主属于内默认角色 无法操作");
//			return msg;
//		}
		String rights=map.get("rights")+"";
		String tokenId=map.get("tokenId")+"";
		String uId=session.getAttribute(tokenId).getId();
		String[] right=null;
		if (StringUtils.isNotBlank(rights)) {
			 right=StringUtils.split(rights, ",");
		}
		//获取当前组织及下属组织的角色
		List<SysOrg>orgList=orgMapper.getAllOrg();
		String orgId=((SysOrg)session.getAttribute(tokenId).getUserOrg()).getId()+"";
		List<String>orgIdList=new ArrayList<>(10);
		orgIdList.add(orgId);
		orgIdList=ServiceUtil.getTree(orgList, orgId, orgIdList);
		List<SysRole>roleList=mapper.getAllRolesByOrgList(orgIdList);
		List<String>roles=new ArrayList<>(10);
		//树状解析当前角色创建的所有角色集合
		roles=ServiceUtil.getRoleIdList(roleList, roleId, roles);
		if(Util.isNotBlank(roles)){
			//删掉此角色下属的角色失效的权限
			Map<String, Object>delMap=new HashMap<>(2);
			delMap.put("roles",roles );
			delMap.put("rights", right);
			mapper.deleteRightOfRoles(delMap);
		}
		//删除角色原来绑定的权限
		int count=mapper.deleteRightsOfRole(roleId);
		if (Util.isNotBlank(right)) {
			//插入角色对应的权限
			Map paramMap=new HashMap<>();
			paramMap.put("uId", uId);
			paramMap.put("roleId", roleId);
			paramMap.put("list", right);
			paramMap.put("createTime", System.currentTimeMillis());
			int status=mapper.saveRightsOfRole(paramMap);
			if (status>0) {
				//更改角色最后更改时间和更改人
				paramMap.clear();
				paramMap.put("lastModifyUser", uId);
				paramMap.put("lastModifyTime", System.currentTimeMillis());
				paramMap.put("id", roleId);
				int valid=mapper.updateRoleInfo(paramMap);
				if (valid>0) {
					logger.info(StringUtil.appendStr(" 角色权限更改成功! 操作者:",logUserName)+" 更改角色ID: "+roleId+"权限范围"+right.toString());
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ROLE_UPDATE_SUCCUESS);
				}else{
					logger.error(StringUtil.appendStr(" 角色权限更改失败! 操作者:",logUserName+" 更改角色ID: "+roleId+"权限范围"+right.toString()));
					throw new ServiceException(Msg.ROLE_UPDATE_FAIL);
				}
			}else{
				logger.error(StringUtil.appendStr(" 角色权限更改失败! 操作者:",logUserName+" 更改角色ID: "+roleId+"权限范围"+right.toString()));
				throw new ServiceException(Msg.ROLE_UPDATE_FAIL);
			}
		}
		return msg;
	}
	/** 
	  * @Title:  saveRoleAndRights 
	  * @Description:新增角色和权限
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月10日下午5:08:04
	*/ 
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MessageBean saveRoleAndRights(Session session, String jsonData, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg=new MessageBean();
		Map<String, Object>map=Util.parseURL(jsonData);
//		String rights=map.get("rights")+"";
		String roleName=map.get("roleName")+"";
		User u=session.getAttribute(map.get("tokenId")+"");
		String roleValid = "0";
		String roleStatus = "1";
		String supStatus = "0";
		String orgId = String.valueOf(map.get("orgId"));
		if("null".equals(orgId) || "".equals(orgId)){
			orgId=((SysOrg)u.getUserOrg()).getId().intValue()+"";
		}
//		String orgId=((SysOrg)u.getUserOrg()).getId().intValue()+"";
		String uId=u.getId();
		String[] right={"5","17","29"};
//		if (StringUtils.isNotBlank(rights)) {
//			 right=StringUtils.split(rights, ",");
//		}
		//对象重复利用
		map.clear();
		//新增角色的相关参数
		SysRole role=new SysRole();
		role.setCreateTime(System.currentTimeMillis());
		role.setOrgId(Integer.parseInt(orgId));
		role.setFatherId(u.getRoleId());
		role.setRoleValid(roleValid);
		role.setRoleStatus(roleStatus);
		role.setSupStatus(supStatus);
		role.setRoleName(roleName);
		int roleId=mapper.saveNewRole(role);
		if (roleId>0) {
			//当角色的权限为空时,不需要做任何操作
			if (right!=null&&right.length>0) {
				map.clear();
				map.put("uId", uId);
				map.put("roleId", role.getId());
				map.put("list", right);
				map.put("createTime", System.currentTimeMillis());
				int status=mapper.saveRightsOfRole(map);
				if (status>0) {
					logger.info(" 新增角色成功! 操作人ID: "+uId+" 角色ID: "+roleId+"权限范围"+right.toString());
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ROLE_SAVE_SUCCUESS);
				}else{
					logger.warn(" 新增角色失败!! 操作人ID: "+uId+"权限范围"+Arrays.toString(right));
					throw new ServiceException(Msg.ROLE_SAVE_FAIL);
				}
			}else{
				logger.info(" 新增角色成功! 操作人ID: "+uId+" 角色ID: "+roleId);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.ROLE_SAVE_SUCCUESS);
			}
		}else {
			logger.warn(" 新增角色失败! 操作人ID: "+uId+"权限范围"+Arrays.toString(right));
			throw new ServiceException(Msg.ROLE_SAVE_FAIL);
		}
		return msg;
	}
	@Override
	public MessageBean getAllRights(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<String,Object>(2);
		Map<String, Object> msgMap = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(msgMap.get("tokenId")));
		String userRoleId = u.getRoleId();
		String roleId = String.valueOf(msgMap.get("roleId"));
		//判断当前登录用户是不是系统管理员，若是则显示编辑按钮
		if(!userRoleId.equals("4")){
			//showType为0时表示：不显示编辑按钮；为1是表示：显示编辑按钮
			resultMap.put("showType", 0);
			//判断传入角色是否为默认，若不是则不显示编辑按钮
			if(roleId.equals("1") || roleId.equals("2")){
				List defaultList = getDefaultRights(roleId);
				resultMap.put("list", defaultList);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(resultMap);
				return msg;
			}else{
				resultMap.put("showType", 1);
			}
		}else{
			resultMap.put("showType", 1);
		}
		//查询当前角色所拥有的权限
		List<SysRights> roleRightsList = rightsMapper.getRightsByRoleId(roleId);
		//查询所有父类权限
//		List<SysRights> list = rightsMapper.getFatherRights();
		//查询当前登录者的角色所拥有的权限
		List<SysRights> list = rightsMapper.getFatherRightsByRoleId(userRoleId);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(Util.isNotBlank(list)){
			for (SysRights sysRights : list) {
				Map<String, Object> map = new HashMap<String,Object>(10);
				map.put("id", sysRights.getId());
				map.put("name", sysRights.getRightsName());
				map.put("pId", "0");
				map.put("open", false);
				map.put("describeMsg", sysRights.getRightsDesc());
				map.put("check_Child_State", "0");
				map.put("editLook", sysRights.getEditLook());
				if(Util.isNotBlank(roleRightsList)){
					for (SysRights sysRights1 : roleRightsList) {
						if(sysRights.getId() == sysRights1.getId()){
							// checked：false表示没有选中;true表示选中;
							map.put("checked", true);
							map.put("check_Child_State", "2");
							break;
						}else{
							map.put("checked", false);
						}
					}
				}else{
					map.put("checked", false);
				}
				List<Map<String, Object>> resultChildList = getRightsChild(sysRights.getId()+"", roleRightsList,userRoleId);
				boolean flag = false;
				boolean flag1 = false;
				int index = 0;
				int index1 = 0;
				//判断子类是不是全选中
				for (Map<String, Object> map2 : resultChildList) {
					// 判断子类是否都没有被勾选   
					if(map2.get("checked").equals(false) && map2.get("check_Child_State").equals("0")){
						flag = true;
						index++;
					}
					// 判断子类是否都为半全选状态
					if((map2.get("checked").equals(true) && map2.get("check_Child_State").equals("1"))){
						flag1 = true;
						index1++;
					}
				}
				if(flag){
					//如果子类都没有被勾选那么就是半全选状态
					if(index != resultChildList.size()){
						map.put("checked", true);
						map.put("check_Child_State", "1");
					}
				}
				if(flag1){
					if(index1 == resultChildList.size()){
						map.put("checked", true);
						map.put("check_Child_State", "1");
					}
				}
				if(resultChildList.size() > 0){
					map.put("children", resultChildList);
				}
				resultList.add(map);
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		resultMap.put("list", resultList);
		msg.setBody(resultMap);
		return msg;
	}
	public List<Map<String, Object>> getRightsChild(String rightsId,List<SysRights> roleRightsList,String userRoleId){
		List<Map<String, Object>> resultChildList = new ArrayList<Map<String,Object>>();
//		List<SysRights> childList = rightsMapper.getChildRights(rightsId);
		Map<String, String> paramMap = new HashMap<String,String>(2);
		paramMap.put("fatherId", rightsId);
		paramMap.put("roleId", userRoleId);
		List<SysRights> childList = rightsMapper.getChildRightsByRoleId(paramMap);
		if(Util.isNotBlank(childList)){
			for (SysRights sysRights2 : childList) {
				Map<String, Object> resultChildMap = new HashMap<String,Object>();//29
				Integer id = sysRights2.getId();
				resultChildMap.put("id", id);
				resultChildMap.put("name", sysRights2.getRightsName());
				resultChildMap.put("describeMsg", sysRights2.getRightsDesc());
				resultChildMap.put("pId", rightsId);
				resultChildMap.put("open", false);
				resultChildMap.put("check_Child_State", "0");
				resultChildMap.put("editLook", sysRights2.getEditLook());
				if(id == 29){
					resultChildMap.put("chkDisabled", true);
				}else{
					resultChildMap.put("chkDisabled", false);
				}
				if(Util.isNotBlank(roleRightsList)){
					for (SysRights sysRights3 : roleRightsList) {
						//如果当前选中的角色与所有角色一个相同，那么showType为1（前端状态变为选中），否则showType为0（前端状态为不选中）
						if(id == sysRights3.getId()){
							resultChildMap.put("checked", true);
							resultChildMap.put("check_Child_State", "2");
							break;
						}else{
							resultChildMap.put("checked", false);
						}
					}
				}else{
					resultChildMap.put("checked", false);
				}
				List<Map<String, Object>> resultList = getRightsChild(sysRights2.getId()+"", roleRightsList,userRoleId);
				//判断是否有子集
				boolean hasChild = false;
				if(resultList.size() > 0){
					resultChildMap.put("children", resultList);
					hasChild = true;
				}
				resultChildMap.put("hasChild", hasChild);
				boolean flag = false;
				boolean flag1 = false;
				int index = 0;
				int index1 = 0;
				//判断子类是不是全选中
				for (Map<String, Object> map2 : resultList) {
					if(!map2.get("chkDisabled").equals(true)){
						if(map2.get("checked").equals(false) && map2.get("check_Child_State").equals("0")){
							flag = true;
							index++;
						}
						if((map2.get("checked").equals(true) && map2.get("check_Child_State").equals("1"))){
							flag1 = true;
							index1++;
						}
					}
				}
				if(flag){
					if(index != resultList.size()){
						resultChildMap.put("checked", true);
						resultChildMap.put("check_Child_State", "1");
					}
				}
				if(flag1){
					if(index1 == resultList.size()){
						resultChildMap.put("checked", true);
						resultChildMap.put("check_Child_State", "1");
					}
				}
				if(id == 29){
					resultChildMap.put("chkDisabled", true);
					Iterator<String> iter = resultChildMap.keySet().iterator();
			        while (iter.hasNext()) {
			            String key = iter.next();
			            if (key.equals("checked") || key.equals("check_Child_State")) {
			                iter.remove();
			                resultChildMap.remove(key);
			            }
			        }
				}
				resultChildList.add(resultChildMap);
			}
		}
		return resultChildList;
	}
	public List getDefaultRights(String roleId){
		List resultList=new ArrayList<>();
		List<SysRights> roleRightsList = rightsMapper.getRightsByRoleId(roleId);
		if(Util.isNotBlank(roleRightsList)){
			//装载父节点
			for (int i = 0; i < roleRightsList.size(); i++) {
				SysRights sysRights = roleRightsList.get(i);
				if(sysRights.getFatherId() == 0){
					Map<String, Object> map = new HashMap<String,Object>(10);
					map.put("id", sysRights.getId());
					map.put("name", sysRights.getRightsName());
					map.put("pId", "0");
					map.put("open", false);
					map.put("describeMsg", sysRights.getRightsDesc());
					map.put("editLook", sysRights.getEditLook());
					map.put("chkDisabled", true);
					//减少递归范围
					//装载子节点
					map.put("children", getChildrenTree(sysRights.getId()+"",roleRightsList));
					resultList.add(map);
				}
			}
		}
		return resultList;
	}
	public List getChildrenTree(String rightsId,List<SysRights> roleRightsList){
		List childList = new ArrayList<>();
		for (int i = 0; i < roleRightsList.size(); i++) {
			SysRights sysRights=roleRightsList.get(i);
			if(rightsId.equals(sysRights.getFatherId()+"")){
				Map<String, Object> childMap = new HashMap<String,Object>();
				childMap.put("id", sysRights.getId());
				childMap.put("name", sysRights.getRightsName());
				childMap.put("pId", sysRights.getFatherId());
				childMap.put("open", false);
				childMap.put("describeMsg", sysRights.getRightsDesc());
				childMap.put("editLook", sysRights.getEditLook());
				childMap.put("chkDisabled", true);
				childMap.put("children", getChildrenTree(sysRights.getId()+"",roleRightsList));
				childList.add(childMap);
			}
		}
		return childList;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MessageBean updateRoleValid(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId")+"");
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String roleId = String.valueOf(map.get("roleId"));
		//判断是否是默认角色
		if(roleId.equals("1") || roleId.equals("2")){
			msg.setCode(Header.STATUS_FAILED);
			msg.setBody("不能删除默认角色！");
			return msg;
		}
		int result = mapper.updateRoleValid(roleId);
		if(result != 0){
			logger.info(StringUtil.appendStr("角色删除成功! 操作者:",logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(Msg.DELETE_SUCCUESS);
		}else{
			logger.error(StringUtil.appendStr("角色删除失败! 操作者:",logUserName));
			msg.setCode(Header.STATUS_FAILED);
			msg.setBody(Msg.DELETE_FAILED);
		}
		return msg;
	}
	public static void main(String[] args) {
		Map resultChildMap = new HashMap<>();
		resultChildMap.put("id", 1);
		resultChildMap.put("name", 2);
		resultChildMap.put("describeMsg", 3);
		resultChildMap.put("pId", 4);
		resultChildMap.put("open", false);
		resultChildMap.put("check_Child_State", "0");
		resultChildMap.put("checked", true);
		resultChildMap.put("editLook", 5);
		resultChildMap.put("chkDisabled", true);
		Iterator<String> iter = resultChildMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (key.equals("checked") || key.equals("check_Child_State")) {
                iter.remove();
                resultChildMap.remove(key);
            }
        }
        System.out.println(resultChildMap);
	}
}
