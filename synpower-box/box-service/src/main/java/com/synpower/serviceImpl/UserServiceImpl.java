package com.synpower.serviceImpl;

import com.synpower.bean.*;
import com.synpower.constant.PTAC;
import com.synpower.dao.*;
import com.synpower.lang.*;
import com.synpower.mobile.MobileUtil;
import com.synpower.mobile.msg.GenVcode;
import com.synpower.mobile.msg.MobileMsg;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.LoginService;
import com.synpower.service.PlantInfoService;
import com.synpower.service.UserService;
import com.synpower.util.*;
import com.synpower.weixin.WeixinOper;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysOrgMapper orgMapper;
    @Autowired
    private MobileUtil mobileUtil;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysSharedLinksMapper sysSharedLinksMapper;
    @Autowired
    private ConfigParam configParam;
    @Autowired
    private LoginService loginService;
    @Autowired
    private SysUserPlantMapper userPlantMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private PlantInfoMapper plantInfoMapper;
    @Autowired
    private SysUserFormIdMapper formIdMapper;
    @Autowired
    private PlantInfoService plantInfoService;
    @Resource(name = "transactionManager")
    private DataSourceTransactionManager transactionManager;

    /**
     * @param str
     * @throws ServiceException
     * @Title: getSimilar
     * @Description: 经销商查询
     * @return: MessageBean
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月26日下午5:17:51
     */
    @Override
    public MessageBean getSimilar(String str, MessageBean msg) throws ServiceException {
        List<SysOrg> list = new ArrayList<SysOrg>();
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        Map<String, Object> map = Util.parseURL(str);
        String orgName = map.get("orgName") + "";
        list = orgMapper.getSimilar(orgName);
        for (SysOrg sysOrg : list) {
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("name", sysOrg.getOrgName());
            resultMap.put("value", sysOrg.getId() + "");
            resultList.add(resultMap);
        }
        msg.setBody(resultList);
        return msg;
    }

    /**
     * @param str
     * @throws Exception
     * @Title: insertUserInfo
     * @Description: 新用户创建
     * @return: MessageBean
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月26日下午5:18:58
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean insertUserInfo(String str, Session session, long sessionId, HttpServletRequest request)
            throws Exception {
        MessageBean msg = new MessageBean();
        // 将前端请求数据装载为对象
        Map<String, Object> map = Util.parseWEIXINURL(str);
        SysUser user = new SysUser();
        String weixinNo = String.valueOf(map.get("weixin"));
        String userName = String.valueOf(map.get("userId"));
        if (StringUtils.isBlank(weixinNo) || "null".equals(weixinNo)) {
            throw new ServiceException("获取微信号失败,请重新授权");
        }
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("appid", configParam.getAppid2());
        paraMap.put("secret", configParam.getSecret2());
        paraMap.put("js_code", weixinNo);
        paraMap.put("grant_type", "authorization_code");
        Map<String, String> weixinRs = WeixinOper.visit(configParam.getWeixinUrl(), paraMap);
        String openId = weixinRs.get("openid");

        // 有些用户有unionId 有些没有的需要解密得到
        // ================
        String unionid = weixinRs.get("unionid");
        Map<String, Object> tempMap = null;

        // 应为下面需要这些数据 所以哪怕上面有unionid下面也要执行一次
        String encryptedData = String.valueOf(map.get("encryptedData"));
        String iv = String.valueOf(map.get("iv"));

        String weixinRes = "";
        try {
            encryptedData = encryptedData.replace(" ", "+");
            iv = iv.replace(" ", "+");
            weixinRes = AesCbcUtil.decrypt(encryptedData, weixinRs.get("session_key"), iv, "UTF-8");
            tempMap = Util.parseWEIXINURL(weixinRes);
        } catch (Exception e) {
            logger.error(" 获取用户头像和unionId等失败", e);
        }
        if (Util.isNotBlank(tempMap)) {
            unionid = String.valueOf(tempMap.get("unionId"));
        }

        // ================
        if (StringUtils.isBlank(unionid)) {
            throw new WeiXinExption(weixinRs.get("errmsg"));
        }

        // 检查手机号是否有绑定的待激活用户
        SysUser userTemp = userMapper.getUnactiveUser(String.valueOf(map.get("telephone")));
        if (userTemp != null) {
            user.setPinyinSearch(ChineseToEnglish.getPingYin(userName));
            user.setPassword(String.valueOf(map.get("password")));
            user.setUserName(userName);
            user.setUserTel(String.valueOf(map.get("telephone")));
            user.setUserWeixin(openId);
            encryptedData = encryptedData.replace(" ", "+");
            iv = iv.replace(" ", "+");
            if (Util.isNotBlank(tempMap)) {
                // 填充用户头像
                if (!Util.isNotBlank(user.getIconUrl())) {
                    // 下载用户头像
                    String iconName = null;
                    try {
                        iconName = "icon" + System.currentTimeMillis() + ".png";
                        DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
                                configParam.getIconURL());
                        user.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
                    } catch (Exception e) {
                        logger.error("下载设置头像失败" + weixinRes);
                    }
                }
                logger.info(" 自动更新用户信息,获取用户的uuid 头像成功" + unionid);
            }
            user.setWeixinUUID(unionid);
            String gender = userTemp.getGender();
            if (StringUtil.isBlank(gender)) {
                user.setGender("1");
            } else {
                user.setGender(gender);
            }
            user.setLastModifyTime(System.currentTimeMillis());
            user.setLastModifyUser(userTemp.getId());
            user.setUserValid("0");
            user.setUserStatus("1");
            String type = userTemp.getUserType();
            if (StringUtil.isBlank(type)) {
                user.setUserType("1");
            } else {
                user.setUserType(type);
            }
            user.setId(userTemp.getId());

            // user.setOrgId(Integer.parseInt(configParam.getDefaultOrg()));
            // 执行数据新增
            int result = userMapper.updateByPrimaryKeySelective(user);
            // 组装数据
            SysUser userTem = userMapper.getUserInfo(String.valueOf(user.getId()));
            if (userTem.getUserRole() == null) {
                paraMap.clear();
                paraMap.put("roleId", configParam.getDefaultRole());
                paraMap.put("userId", String.valueOf(user.getId()));
                paraMap.put("createTime", String.valueOf(System.currentTimeMillis()));
                paraMap.put("createUser", String.valueOf(user.getId()));
                result = userRoleMapper.insertUserRole(paraMap);
            }
            if (result != 0) {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
                TransactionStatus status = transactionManager.getTransaction(def);
                transactionManager.commit(status);
                user.setRequestType("2");
                user.setLoginId(unionid);
                List<SysUser> userList = userMapper.getUserByIdAndPsw(user);
                User u = loginService.fillIn(userList.get(0), sessionId, request);
                session.setAttribute(sessionId + "", u);
                // map.clear();
                // map.put("tokenId",sessionId+"");
                msg.setBody(u);
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.REGISTER_SUCCUESS);
            } else {
                throw new ServiceException(Msg.REGISTER_FAILED);
            }
            logger.info("关联账户信息:" + user.toString());
        } else {
            user.setPinyinSearch(ChineseToEnglish.getPingYin(userName));
            user.setPassword(String.valueOf(map.get("password")));
            user.setUserName(userName);
            user.setUserTel(String.valueOf(map.get("telephone")));
            user.setUserWeixin(openId);
            user.setGender("1");
            user.setCreateTime(System.currentTimeMillis());
            user.setUserValid("0");
            user.setUserStatus("1");
            user.setUserType("1");
            // 下载用户头像
            String iconName = null;
            try {
                iconName = "icon" + System.currentTimeMillis() + ".png";
                DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
                        configParam.getIconURL());
                user.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
            } catch (Exception e) {
                logger.error("下载设置头像失败" + weixinRes);
            }

            // 填充用户头像
            user.setWeixinUUID(unionid);
            // user.setOrgId(Integer.parseInt(configParam.getDefaultOrg()));
            // 执行数据新增
            int result = userMapper.insertUserInfo(user);
            // 组装数据
            paraMap.clear();
            paraMap.put("roleId", configParam.getDefaultRole());
            paraMap.put("userId", String.valueOf(user.getId()));
            paraMap.put("createTime", String.valueOf(System.currentTimeMillis()));
            paraMap.put("createUser", String.valueOf(user.getId()));
            result = userRoleMapper.insertUserRole(paraMap);
            if (result != 0) {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
                TransactionStatus status = transactionManager.getTransaction(def);
                transactionManager.commit(status);
                user.setRequestType("2");
                user.setLoginId(unionid);
                List<SysUser> userList = userMapper.getUserByIdAndPsw(user);
                User u = loginService.fillIn(userList.get(0), sessionId, request);
                session.setAttribute(sessionId + "", u);
                map.clear();
                map.put("tokenId", sessionId + "");
                msg.setBody(map);
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.REGISTER_SUCCUESS);
            } else {
                throw new ServiceException(Msg.REGISTER_FAILED);
            }
            logger.info("save new user msg:" + user.toString());
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    @Override
    public MessageBean insertPlantUser(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        // 将前端请求数据装载为对象
        SysUser user = (SysUser) Util.parseURL(str, "com.synpower.bean.SysUser");
        Map<String, String> map = new HashMap<String, String>();
        User u = session.getAttribute(map.get("tokenId"));
        // 设置基本属性
        user.setGender("1");
        user.setOrgId(Integer.parseInt(((SysOrg) u.getUserOrg()).getId() + ""));
        user.setPinyinSearch(ChineseToEnglish.getPingYin(user.getUserName()));
        user.setLastModifyTime(System.currentTimeMillis());
        user.setLastModifyUser(0);
        user.setCreateTime(System.currentTimeMillis());
        user.setUserValid("0");
        user.setUserStatus("1");
        user.setUserType("0");
        // 执行数据新增
        int result = userMapper.insertUserInfo(user);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("userId", user.getId() + "");
        // 组装数据
        MessageBean msg = new MessageBean();
        if (result != 0) {
            logger.info(StringUtil.appendStr("新增电站业主:", user.toString()));
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(resultMap);
            msg.setMsg(Msg.ADD_SUCCUESS);
        } else {
            throw new ServiceException(Msg.ADD_FAILED);
        }
        return msg;
    }

    /**
     * @param str
     * @throws ServiceException
     * @Title: getUserByLoginId
     * @Description: 查询登录账号是否存在
     * @return: MessageBean
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月26日下午5:20:33
     */
    @Override
    public MessageBean getUserByLoginId(String str) throws SessionException, ServiceException {
        // 将前端请求数据装载为对象
        SysUser user = (SysUser) Util.parseURL(str, "com.synpower.bean.SysUser");
        List<SysUser> list = userMapper.getUserByLoginId(user.getLoginId());
        MessageBean msg = new MessageBean();
        if (list == null && list.isEmpty()) {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.USERNAME_NOT_EXISTS);
        } else {
            throw new ServiceException(Msg.USERNAME_EXISTS);
        }
        return msg;
    }

    /**
     * @param str
     * @throws SessionException
     * @throws ServiceException
     * @Title: getUserByTel
     * @Description: 查询电话号码是否存在
     * @return: MessageBean
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月26日下午5:21:16
     */
    @Override
    public MessageBean getUserByTel(String str) throws SessionException, ServiceException {
        // 将前端请求数据装载为对象
        SysUser user = (SysUser) Util.parseURL(str, "com.synpower.bean.SysUser");
        List<SysUser> list = userMapper.getUserByTel(user.getUserTel());
        MessageBean msg = new MessageBean();
        if (list == null || list.isEmpty()) {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.PHONE_NUMBER_NOT_EXISTS);
        } else {
            throw new ServiceException(Msg.PHONE_NUMBER_EXISTS);
        }
        return msg;
    }

    /**
     * @param str
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException: MessageBean
     * @throws ServiceException
     * @Title: listUserInfo
     * @Description: 获取账户列表
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月27日下午6:19:15
     */
    @Override
    public MessageBean listUserInfo(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        // 得到所有组织列表
        List<SysOrg> orgList = orgMapper.getAllOrg();
        List<String> reList = new ArrayList<>();
        // 判断是否有选中的组织id，若没有则获取当前登录用户的组织id
        if (map.get("orgId") != null && !"".equals(map.get("orgId"))) {
            reList.add(map.get("orgId") + "");
            reList = ServiceUtil.getTree(orgList, map.get("orgId") + "", reList);
        } else {
            reList.add(((SysOrg) u.getUserOrg()).getId() + "");
            reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
        }
        // 获取每页显示几条数据
        Integer each_page_num = Integer.parseInt(map.get("length") + "");
        // 获取起始位置
        Integer alarm_page_num = Integer.parseInt(map.get("start") + "");
        // 获取输入框的内容
        String content = (String) map.get("content");
        // 获取状态
        String status = (String) map.get("status");
        // 获取角色
        String roleId = (String) map.get("roleId");
        List<Integer> roleIdList = new ArrayList<Integer>();
        if (Util.isNotBlank(roleId)) {
            // 根据角色id获取该id相同名称的id集合
            List<SysRole> sysRoleList = roleMapper.getSameNameById(roleId);
            for (SysRole sysRole : sysRoleList) {
                roleIdList.add(sysRole.getId());
            }
        }
        Map<String, Object> newMap = new HashMap<String, Object>();
        newMap.put("content", content);
        newMap.put("reList", reList);
        newMap.put("status", status);
        newMap.put("roleIdList", roleIdList);
        int count = userMapper.countUser(newMap);
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("content", content);
        parameterMap.put("min", alarm_page_num);
        parameterMap.put("max", each_page_num);
        parameterMap.put("reList", reList);
        parameterMap.put("status", status);
        parameterMap.put("roleIdList", roleIdList);
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        List<Map> list = userMapper.listUserInfo(parameterMap);
        if (Util.isNotBlank(list)) {
            for (Map map1 : list) {
                Map<String, String> resultMap = new HashMap<String, String>();
                String email = String.valueOf(map1.get("email"));
                String icon = String.valueOf(map1.get("iconUrl"));
                String userTel = String.valueOf(map1.get("userTel"));
                String userName = String.valueOf(map1.get("userName"));
                resultMap.put("id", map1.get("id") + "");
                resultMap.put("loginId", map1.get("loginId") + "");
                resultMap.put("roleName", map1.get("roleName") + "");
                resultMap.put("userName", userName);
                resultMap.put("status", map1.get("userStatus") + "");
                resultMap.put("orgId", map1.get("org_id") + "");
                resultMap.put("orgName", map1.get("org_name") + "");
                resultMap.put("useTel", map1.get("use_tel") + "");
                if (!Util.isNotBlank(userName)) {
                    resultMap.put("userName", "--");
                } else {
                    resultMap.put("userName", userName);
                }
                if (!Util.isNotBlank(email)) {
                    resultMap.put("email", "--");
                } else {
                    resultMap.put("email", email);
                }
                if (!Util.isNotBlank(icon)) {
                    resultMap.put("icon", "");
                } else {
                    resultMap.put("icon", icon);
                }
                if (!Util.isNotBlank(userTel)) {
                    resultMap.put("userTel", "--");
                } else {
                    resultMap.put("userTel", userTel);
                }
                resultList.add(resultMap);
            }
        }
        // List<SysUser> list = userMapper.listUserInfo(parameterMap);
//		if(Util.isNotBlank(list)){
//			for (SysUser sysUser : list) {
//				Map<String, String> resultMap = new HashMap<String, String>();
//				resultMap.put("id", sysUser.getId()+"");
//				resultMap.put("loginId", sysUser.getLoginId());
//				SysRole role=sysUser.getUserRole();
//				resultMap.put("roleName", role.getRoleName());
//				resultMap.put("userName", sysUser.getUserName());
//				resultMap.put("userTel", sysUser.getUserTel());
//				resultMap.put("status", sysUser.getUserStatus());
//				if(null == sysUser.getEmail() || "".equals(sysUser.getEmail())){
//					resultMap.put("email", "--");
//				}else{
//					resultMap.put("email", sysUser.getEmail());
//				}
//				resultMap.put("orgId", sysUser.getOrgId()+"");
//				resultMap.put("orgName", sysUser.getUserOrg().getOrgName());
//				resultMap.put("icon", sysUser.getIconUrl());
//				resultList.add(resultMap);
//			}
//		}
        Map<String, Object> reMap = new HashMap<String, Object>();
        reMap.put("draw", String.valueOf(map.get("draw")));
        reMap.put("recordsTotal", count);
        reMap.put("recordsFiltered", count);
        reMap.put("data", resultList);
        MessageBean msg = new MessageBean();
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(reMap);
        return msg;
    }

    /**
     * @param str
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException: MessageBean
     * @throws ServiceException
     * @Title: updateUserStatus
     * @Description: 启用/停用
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月27日下午6:20:51
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MessageBean updateUserStatus(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        String status = (String) map.get("status");
        String uid = (String) map.get("id");
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("id", u.getId());
        parameterMap.put("status", status);
        parameterMap.put("lastModifyTime", System.currentTimeMillis());
        parameterMap.put("uid", uid);
        int result = userMapper.updateUserStatus(parameterMap);
        MessageBean msg = new MessageBean();
        if (result != 0) {
            if ("0".equals(status)) {
                logger.info(StringUtil.appendStr(" 账户id", uid, "停用更改成功! 操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.DISABLE_SUCCUESS);
            } else if ("1".equals(status)) {
                logger.info(StringUtil.appendStr(" 账户id", uid, "启用更改成功! 操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.ENABLE_SUCCUESS);
            }
        }
        return msg;
    }

    /**
     * @param str
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException: MessageBean
     * @throws ServiceException
     * @Title: updateUserValid
     * @Description: 账户列表的删除
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月30日下午5:13:31
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean updateUserValid(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        String uid = (String) map.get("ids");
        String[] ids = uid.split(",");
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("id", u.getId());
        parameterMap.put("lastModifyTime", System.currentTimeMillis());
        parameterMap.put("ids", ids);
        int result = userMapper.updateUserValid(parameterMap);
        MessageBean msg = new MessageBean();
        if (result != 0) {
            logger.info(StringUtil.appendStr(" 账户集合", ids, "删除成功! 操作者:", logUserName));
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.DELETE_SUCCUESS);
        } else {
            logger.error(StringUtil.appendStr(" 账户集合", ids, "账户删除失败! 操作者:", logUserName));
            throw new ServiceException(Msg.DELETE_FAILED);
        }
        return msg;
    }

    @Override
    public MessageBean getRoleByOrgId(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        // 通过session获取到当前登录用户信息
        User u = session.getAttribute(map.get("tokenId") + "");
        String orgId = String.valueOf(map.get("orgId"));
        if (!Util.isNotBlank(orgId)) {
            orgId = String.valueOf(((SysOrg) u.getUserOrg()).getId());
        }
        // 得到所有组织列表
//		List<SysOrg>orgList=orgMapper.getAllOrg();
//		List<String>reList=new ArrayList<>();
//		//判断是否有选中的组织id，若没有则获取当前登录用户的组织id
//		if(Util.isNotBlank(orgId)){
//			reList.add(orgId);
//			reList = ServiceUtil.getTree(orgList, orgId,reList);
//		}else{
//			reList.add(((SysOrg)u.getUserOrg()).getId()+"");
//			reList = ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);
//		}
//		Map<String, Object> parameterMap = new HashMap<String,Object>();
//		parameterMap.put("reList", reList);
        List<SysRole> list = roleMapper.getRoleByOrgId(orgId);
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        MessageBean msg = new MessageBean();
        if (Util.isNotBlank(list)) {
            for (SysRole sysRole : list) {
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("name", sysRole.getRoleName());
                resultMap.put("value", sysRole.getId() + "");
                resultList.add(resultMap);
            }
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(resultList);
            msg.setMsg(Msg.SELECT_SUCCUESS);
        } else {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.NO_OPTIONS);
        }
        return msg;
    }

    @Override
    public MessageBean getRoleAll(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        // 通过session获取到当前登录用户信息
        User u = session.getAttribute(map.get("tokenId") + "");
        String orgId = String.valueOf(map.get("orgId"));
        // 得到所有组织列表
        List<SysOrg> orgList = orgMapper.getAllOrg();
        List<String> reList = new ArrayList<>();
        // 判断是否有选中的组织id，若没有则获取当前登录用户的组织id
        if (Util.isNotBlank(orgId)) {
            reList.add(orgId);
            reList = ServiceUtil.getTree(orgList, orgId, reList);
        } else {
            reList.add(((SysOrg) u.getUserOrg()).getId() + "");
            reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
        }
        List<SysRole> list = roleMapper.getRoleAll(reList);
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        MessageBean msg = new MessageBean();
        if (Util.isNotBlank(list)) {
            for (SysRole sysRole : list) {
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("name", sysRole.getRoleName());
                resultMap.put("value", sysRole.getId() + "");
                resultList.add(resultMap);
            }
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(resultList);
            msg.setMsg(Msg.SELECT_SUCCUESS);
        } else {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.NO_OPTIONS);
        }
        return msg;
    }

    @Override
    public MessageBean getUserInfoById(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        String userId = map.get("userId") + "";
        SysUser user = userMapper.getUserInfoById(userId);
        MessageBean msg = new MessageBean();
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("userId", user.getId() + "");
        resultMap.put("userName", user.getUserName());
        resultMap.put("userTel", user.getUserTel());
        resultMap.put("email", user.getEmail());
        resultMap.put("password", user.getPassword());
        resultMap.put("roleName", user.getUserRole().getRoleName());
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(resultMap);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MessageBean updateUserAndRole(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("userId", map.get("userId"));
        parameterMap.put("userName", map.get("userName"));
        parameterMap.put("userTel", map.get("userTel"));
        parameterMap.put("email", map.get("email"));
        parameterMap.put("password", map.get("password"));
        parameterMap.put("lastModifyUser", u.getId());
        parameterMap.put("lastModifyTime", System.currentTimeMillis());
        int result = userMapper.updateUserById(parameterMap);
        Map<String, Object> roleMap = new HashMap<String, Object>();
        roleMap.put("userId", map.get("userId"));
        roleMap.put("roleId", map.get("roleId"));
        int roleResult = userMapper.updateRoleById(roleMap);
        MessageBean msg = new MessageBean();
        if (result != 0 && roleResult != 0) {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.UPDATE_SUCCUESS);
        } else {
            throw new ServiceException(Msg.UPDATE_FAILED);
        }
        return msg;
    }

    /**
     * @param str
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException: MessageBean
     * @throws ServiceException
     * @Title: getUseInfo
     * @Description: 我的信息模块的基本信息展示
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月30日下午5:15:04
     */
    @Override
    public MessageBean getUserInfo(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        // 通过session获取到当前登录用户信息
        User u = session.getAttribute(map.get("tokenId") + "");
        // 根据用户id查询出用户信息
        SysUser user = userMapper.getUserInfo(u.getId());
        MessageBean msg = new MessageBean();
        if (null != user) {
            // 组装返回前台的数据
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("userId", user.getId() + "");
            resultMap.put("icon", user.getIconUrl());
            resultMap.put("userName", user.getUserName());
            resultMap.put("userTel", user.getUserTel());
            resultMap.put("loginId", user.getLoginId());
            resultMap.put("email", user.getEmail());
            resultMap.put("password", user.getPassword());
            resultMap.put("avatarUrl", user.getIconUrl());
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(resultMap);
            msg.setMsg(Msg.SELECT_SUCCUESS);
        } else {
            msg.setCode(Header.STATUS_NULL_ERROR);
            msg.setMsg(Msg.USER_NOT_EXISTS);
        }
        return msg;
    }

    /**
     * @param str
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException: MessageBean
     * @throws ServiceException
     * @Title: updateUserInfo
     * @Description: 我的信息模块的修改
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月30日下午5:16:07
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    @Override
    public MessageBean updateUserInfo(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        // 通过session获取到当前登录用户信息
        User u = session.getAttribute(map.get("tokenId") + "");
        // 当前登录人的id和名字
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        SysUser user = new SysUser();
        user.setId(Integer.parseInt(u.getId()));
        user.setUserName(map.get("userName") + "");
        user.setUserTel(map.get("userTel") + "");
        user.setLoginId(map.get("loginId") + "");
        user.setEmail(map.get("email") + "");
        user.setLastModifyTime(System.currentTimeMillis());
        int result = userMapper.updateUserInfo(user);
        MessageBean msg = new MessageBean();
        if (result != 0) {
            logger.info(StringUtil.appendStr("修改个人信息成功! 操作者:", logUserName));
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.UPDATE_MYSELF_SUCCUESS);
        } else {
            logger.error(StringUtil.appendStr("修改个人信息失败! 操作者:", logUserName));
            throw new ServiceException(Msg.UPDATE_MYSELF_FAILED);
        }
        return msg;
    }

    /**
     * @param str
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException
     * @throws ServiceException:       MessageBean
     * @Title: getUserNotMyself
     * @Description: 我的信息模块电话唯一性效验
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月31日下午1:46:25
     */
    @Override
    public MessageBean getUserNotMyself(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        // 通过session获取到当前登录用户信息
        User u = session.getAttribute(map.get("tokenId") + "");
        String userId = (String) map.get("userId");
        String id = u.getId();
        // 判断是否传入userId，若没有传则用当前登录用户的id
        if (userId != null && !"null".equals(userId) && !"".equals(userId)) {
            id = userId;
        }
        String userTel = (String) map.get("userTel");
        String email = (String) map.get("email");
        String loginId = (String) map.get("loginId");
        if (null != userTel && !"".equals(userTel)) {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("id", id);
            parameterMap.put("userTel", userTel);
            List<SysUser> list = userMapper.getTelNotMyself(parameterMap);
            if (list == null || list.size() == 0) {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.PHONE_NUMBER_NOT_EXISTS);
            } else {
                throw new ServiceException(Msg.PHONE_NUMBER_EXISTS);
            }
        } else if (null != email && !"".equals(email)) {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("id", id);
            parameterMap.put("email", email);
            List<SysUser> list = userMapper.getEmailNotMyself(parameterMap);
            if (list == null || list.size() == 0) {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.EMAIL_NUMBER_NOT_EXISTS);
            } else {
                throw new ServiceException(Msg.EMAIL_NUMBER_EXISTS);
            }
        } else if (null != loginId && !"".equals(loginId)) {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("id", id);
            parameterMap.put("loginId", loginId);
            List<SysUser> list = userMapper.getLoginNotMyself(parameterMap);
            if (list == null || list.size() == 0) {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.LOGIN_NUMBER_NOT_EXISTS);
            } else {
                throw new ServiceException(Msg.LOGIN_NUMBER_EXISTS);
            }
        }
        return msg;
    }

    @Override
    public MessageBean getUnique(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        // 将前端请求数据装载为map
        Map<String, Object> map = Util.parseURL(str);
        String userTel = (String) map.get("userTel");
        String email = (String) map.get("email");
        String loginId = (String) map.get("loginId");
        if (null != userTel && !"".equals(userTel)) {
            List<SysUser> list = userMapper.getUserByTel(userTel);
            if (list == null || list.size() == 0) {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.PHONE_NUMBER_NOT_EXISTS);
            } else {
                throw new ServiceException(Msg.PHONE_NUMBER_EXISTS);
            }
        } else if (null != email && !"".equals(email)) {
            List<SysUser> list = userMapper.getUserByEmail(email);
            if (list == null || list.size() == 0) {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.EMAIL_NUMBER_NOT_EXISTS);
            } else {
                throw new ServiceException(Msg.EMAIL_NUMBER_EXISTS);
            }
        } else if (null != loginId && !"".equals(loginId)) {
            List<SysUser> list = userMapper.getUserByLoginId(loginId);
            if (list == null || list.size() == 0) {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.LOGIN_NUMBER_NOT_EXISTS);
            } else {
                throw new ServiceException(Msg.LOGIN_NUMBER_EXISTS);
            }
        }
        return msg;
    }

    @Override
    public MessageBean queryUserRolSrc(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        // String tokenId=Util.parseURL(jsonData).get("tokenId")+"";
        Map<String, Object> map = Util.parseURL(jsonData);
        List<Node> resultList = null;
        String uId = String.valueOf(map.get("uId"));
        if (Util.isNotBlank(uId)) {
            Integer userId = Integer.parseInt(uId);
            List<SysRights> rights = userMapper.getRightsByUid(userId);
            if (Util.isNotBlank(rights)) {
                resultList = ServiceUtil.parseRights(rights);
            }
        }
        msg.setBody(resultList);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    @Override
    public MessageBean updateUserIcon(HttpServletRequest request, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        User u = session.getAttribute(request.getParameter("tokenId"));
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        String imgUrl = request.getParameter("imgUrl");
        if (null != imgUrl && !"".equals(imgUrl) && !"null".equals(imgUrl)) {
            // 将用户原先的头像设置为空
            int result = userMapper.updateUserIcon(u.getId());
            if (result == 0) {
                logger.error(StringUtil.appendStr(" 头像置空失败! 操作者:", logUserName));
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg(Msg.NULL_PHOTO_FAILED);
                return msg;
            }
            // 数据库操作成功后删除文件中用户的头像图片
            imgUrl = configParam.getIconURL() + imgUrl.substring(imgUrl.lastIndexOf("/"));
            File destFile = new File(imgUrl);
            if (destFile.exists()) {
                logger.info(StringUtil.appendStr(" 头像文件删除成功! 操作者:", logUserName));
                destFile.delete();
            } else {
                logger.error(StringUtil.appendStr(" 头像文件删除失败! 操作者:", logUserName));
            }
        }
        // 删除后重新上传用户更改后的头像
        String pathDir = configParam.getIconURL();
        String iconRequestUrl = configParam.getIconRequestURL();
        List<String> path = DownloadUtils.uploadPhoto(request, pathDir, iconRequestUrl);
        if (!Util.isNotBlank(path)) {
            if (Util.isNotBlank(imgUrl)) {
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg("图片上传失败!");
            }
        } else {
            StringBuffer buffer = new StringBuffer();
            if (path.size()>1) {
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg("只能上传一张图片，请核实后上传");
                return msg;
            }
            for (int i = 0, size = path.size(); i < size; i++) {
                buffer.append(path.get(i));
            }
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("userId", u.getId());
            parameterMap.put("url", buffer.toString());
            parameterMap.put("createTime", System.currentTimeMillis());
            int re = userMapper.insertUserIcon(parameterMap);
            if (re != 0) {
                logger.info(StringUtil.appendStr(" 头像上传成功! 操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.ICON_UPLOAD_SUCCUESS);
            } else {
                logger.error(StringUtil.appendStr(" 头像上传失败! 操作者:", logUserName));
                throw new ServiceException(Msg.UPLOAD_FAILED);
            }
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    @Override
    public MessageBean insertNewUser(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        SysUser user = new SysUser();
        String useTel = String.valueOf(map.get("useTel"));
        // useTel为1时：使用电话号码作为登录账号；为2时：使用填的内容作为登录账号；
        if (useTel.equals("1")) {
            user.setLoginId(map.get("userTel") + "");
        } else if (useTel.equals("0")) {
            user.setLoginId(map.get("loginId") + "");
        }
        user.setUseTel(useTel);
        user.setPassword(map.get("password") + "");
        user.setUserName(map.get("userName") + "");
        user.setGender("1");
        user.setUserTel(map.get("userTel") + "");
        user.setEmail(map.get("email") + "");
        user.setOrgId(Integer.parseInt(map.get("orgId") + ""));
        user.setPinyinSearch(ChineseToEnglish.getPingYin(map.get("userName") + ""));
        user.setLastModifyTime(System.currentTimeMillis());
        user.setLastModifyUser(Integer.parseInt(u.getId()));
        user.setCreateTime(System.currentTimeMillis());
        user.setUserValid("0");
        user.setUserStatus(map.get("status") + "");
        user.setUserType("0");
        int result = userMapper.inserNewUser(user);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("roleId", map.get("roleId") + "");
        paramMap.put("userId", user.getId());
        paramMap.put("createTime", System.currentTimeMillis());
        paramMap.put("createUser", u.getId());
        int roleResult = userRoleMapper.insertUserRole(paramMap);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("userId", user.getId() + "");
        // 组装数据
        MessageBean msg = new MessageBean();
        if (result != 0 && roleResult != 0) {
            logger.info(StringUtil.appendStr(" 新增账户成功! 操作者:", logUserName));
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(resultMap);
            msg.setMsg(Msg.REGISTER_SUCCUESS);
        } else {
            logger.error(StringUtil.appendStr(" 新增账户失败! 操作者:", logUserName));
            throw new ServiceException(Msg.REGISTER_FAILED);
        }
        return msg;
    }

    /**
     * @param request
     * @param file
     * @param userId
     * @throws IllegalStateException
     * @throws IOException:          void
     * @Title: iconUtil
     * @Description: 上传头像的方法
     * @lastEditor: SP0009
     * @lastEdit: 2017年12月7日上午11:48:25
     */
    public int iconUtil(HttpServletRequest request, File file, String userId)
            throws IllegalStateException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String pathDir = configParam.getIconURL();
        String iconRequestUrl = configParam.getIconRequestURL();
        MultipartFile multipartFile = multipartRequest.getFile("file");
//		String logoRealPathDir = request.getSession().getServletContext().getRealPath(pathDir);
        String logoRealPathDir = pathDir;
        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists()) {
            logoSaveFile.mkdirs();
        }
        /** 获取文件的后缀 **/
        String suffix = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        /** 拼成完整的文件保存路径加文件 **/
        String name = +System.currentTimeMillis() + suffix;
        String fileName = logoRealPathDir + "/" + name;
        file = new File(fileName);
//		String data = file.getPath();
        String data = iconRequestUrl + "/" + System.currentTimeMillis() + suffix;
        multipartFile.transferTo(file);
        String path = file.getPath();
        path = path.substring(path.indexOf("files"));
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("userId", userId);
        parameterMap.put("url", path);
        parameterMap.put("createTime", System.currentTimeMillis());
        int re = userMapper.insertUserIcon(parameterMap);
        return re;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean uploadUserIcon(HttpServletRequest request, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        User u = session.getAttribute(request.getParameter("tokenId"));
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        String userId = request.getParameter("userId");
        String imgUrl = request.getParameter("imgUrl");
        if (null != imgUrl && !"".equals(imgUrl) && !"null".equals(imgUrl)) {
            // 将用户原先的头像设置为空
            int result = userMapper.updateUserIcon(userId);
            if (result == 0) {
                logger.error(StringUtil.appendStr(" 头像置空失败! 操作者:", logUserName));
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg(Msg.NULL_PHOTO_FAILED);
                return msg;
            }
            // 数据库操作成功后删除文件中用户的头像图片
            imgUrl = configParam.getIconURL() + imgUrl.substring(imgUrl.lastIndexOf("/"));
            File destFile = new File(imgUrl);
            if (destFile.exists()) {
                logger.info(StringUtil.appendStr(" 头像文件删除成功! 操作者:", logUserName));
                destFile.delete();
            } else {
                logger.error(StringUtil.appendStr(" 头像文件删除失败! 操作者:", logUserName));
            }
        }
        String pathDir = configParam.getIconURL();
        String iconRequestUrl = configParam.getIconRequestURL();
        List<String> path = DownloadUtils.uploadPhoto(request, pathDir, iconRequestUrl);
        if (!Util.isNotBlank(path)) {
            if (Util.isNotBlank(imgUrl)) {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg("图片删除成功");
            } else {
                msg.setCode(Header.STATUS_SUCESS);
            }
        } else {
            StringBuffer buffer = new StringBuffer();
            SysUser user = userMapper.getUserInfoById(userId);
            if (Util.isNotBlank(user.getIconUrl())) {
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg("只能上传一张图片，请核实后上传");
                return msg;
            }
            for (int i = 0; i < path.size(); i++) {
                buffer.append(path.get(i));
            }
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("userId", userId);
            parameterMap.put("url", buffer.toString());
            parameterMap.put("createTime", System.currentTimeMillis());
            int re = userMapper.insertUserIcon(parameterMap);
            if (re != 0) {
                logger.info(StringUtil.appendStr(" 头像上传成功! 操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.ICON_UPLOAD_SUCCUESS);
            } else {
                logger.error(StringUtil.appendStr(" 头像上传失败! 操作者:", logUserName));
                throw new ServiceException(Msg.UPLOAD_FAILED);
            }
        }
//		File file=null;
//		try {
//			int re = iconUtil(request, file, userId);
//			if(re != 0){
//				logger.info(StringUtil.appendStr(" 头像上传成功! 操作者:",logUserName));
//				msg.setCode(Header.STATUS_SUCESS);
//				msg.setMsg(Msg.ICON_UPLOAD_SUCCUESS);
//			}else{
//				logger.error(StringUtil.appendStr(" 头像上传失败! 操作者:",logUserName));
//				throw new ServiceException(Msg.UPLOAD_FAILED);
//			}
//		} catch (Exception e) {
//			logger.error(StringUtil.appendStr(" 头像上传失败! 操作者:",logUserName));
//			e.printStackTrace();
//			throw new ServiceException(Msg.UPLOAD_FAILED);
//		}
        return msg;
    }

    @Override
    public MessageBean selectUserInfo(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        String userId = map.get("userId") + "";
        SysUser user = userMapper.selectUserInfo(userId);
        if (null != user) {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("userName", user.getUserName());
            resultMap.put("org", user.getUserOrg().getOrgName());
            resultMap.put("orgId", user.getUserOrg().getId());
            resultMap.put("role", user.getUserRole().getRoleName());
            resultMap.put("roleId", user.getUserRole().getId());
            resultMap.put("userTel", user.getUserTel());
            resultMap.put("email", user.getEmail());
            resultMap.put("loginId", user.getLoginId());
            resultMap.put("status", user.getUserStatus());
            resultMap.put("icon", user.getIconUrl());
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(resultMap);
            msg.setMsg(Msg.SELECT_SUCCUESS);
        } else {
            msg.setCode(Header.STATUS_NULL_ERROR);
            msg.setMsg(Msg.USER_NOT_EXISTS);
        }
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean updateUserById(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        // 组装查询需要的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", map.get("userId") + "");
        paramMap.put("userName", map.get("userName") + "");
        paramMap.put("pinYin", ChineseToEnglish.getPingYin(map.get("userName") + ""));
        paramMap.put("orgId", map.get("orgId") + "");
        paramMap.put("userTel", map.get("userTel") + "");
        paramMap.put("email", map.get("email") + "");
        String useTel = map.get("useTel") + "";
        if (useTel.equals("1")) {
            String userTel = map.get("userTel") + "";
            List<SysUser> userByLoginId = userMapper.getUserByLoginId(userTel);
            if (!CollectionUtils.isEmpty(userByLoginId)) {
                if (!userTel.equals(userByLoginId.get(0).getUserTel())) {
                    logger.error(StringUtil.appendStr("账户信息修改失败! 操作者:", logUserName));
                    throw new ServiceException(Msg.PHONE_NUMBER_2LOGIN_NUMBER_EXISTS);
                }
            }
            paramMap.put("loginId", map.get("userTel") + "");
        } else if (useTel.equals("0")) {
            paramMap.put("loginId", map.get("loginId") + "");
        }
        paramMap.put("status", map.get("status") + "");
        paramMap.put("lastModifyTime", System.currentTimeMillis());
        paramMap.put("lastModifyUser", u.getId());
        paramMap.put("useTel", map.get("useTel") + "");
        int result = userMapper.updateAccountById(paramMap);
        if (result != 0) {
            paramMap.clear();
            paramMap.put("userId", map.get("userId") + "");
            paramMap.put("roleId", String.valueOf(map.get("roleId")));
            int re = userRoleMapper.updateUserRoleId(paramMap);
            if (re != 0) {
                logger.info(StringUtil.appendStr("账户信息修改成功! 操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.UPDATE_SUCCUESS);
            } else {
                logger.error(StringUtil.appendStr("账户信息修改失败! 操作者:", logUserName));
                throw new ServiceException(Msg.UPDATE_FAILED);
            }
        } else {
            logger.error(StringUtil.appendStr("账户信息修改失败! 操作者:", logUserName));
            throw new ServiceException(Msg.UPDATE_FAILED);
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MessageBean updateUserPwd(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", map.get("userId"));
        paramMap.put("password", map.get("OldPassword"));
        // 匹配原密码（若没有则说明原密码输入不正确）
        List<SysUser> list = userMapper.getUserPwd(paramMap);
        if (list.size() != 0) {
            paramMap.put("password", map.get("NewPassword"));
            paramMap.put("lastModifyTime", System.currentTimeMillis());
            paramMap.put("lastModifyUser", u.getId());
            int result = userMapper.updatePwd(paramMap);
            if (result != 0) {
                logger.info(StringUtil.appendStr("修改密码成功! 操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.UPDATE_SUCCUESS);
            } else {
                logger.error(StringUtil.appendStr("修改密码失败! 操作者:", logUserName));
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg(Msg.UPDATE_FAILED);
            }
        } else {
            logger.info(StringUtil.appendStr("修改密码失败! 操作者:", logUserName));
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.ORIGINAL_PASSWORD_ERROR);
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MessageBean updateDirectPwd(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        // 当前登录人的id和名称
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", map.get("userId"));
        paramMap.put("password", map.get("NewPassword"));
        paramMap.put("lastModifyTime", System.currentTimeMillis());
        paramMap.put("lastModifyUser", u.getId());
        int result = userMapper.updatePwd(paramMap);
        if (result != 0) {
            logger.info(StringUtil.appendStr("修改密码成功! 操作者:", logUserName));
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.UPDATE_SUCCUESS);
        } else {
            logger.error(StringUtil.appendStr("修改密码失败! 操作者:", logUserName));
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.UPDATE_FAILED);
        }
        return msg;
    }

    @Override
    public MessageBean getValidCode(String str) throws SessionException, ServiceException, SmsException, IOException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        String telNo = String.valueOf(map.get("userTel"));
        ServiceUtil.checkTelNo(telNo);
        /*
         * String message=cacheUtil2.getString(telNo); String validate=null; if
         * (message==null||"null".equals(message)||"".equals(message)) { GenVcode
         * genVcode = new GenVcode(); validate=genVcode.randomCode(); }else{
         * validate=message; }
         */
        GenVcode genVcode = new GenVcode();
        String validate = genVcode.randomCode();
        MobileMsg sms = new MobileMsg();
        StringBuilder builder = new StringBuilder();
        Set<String> set = new HashSet<String>();
        set.add(telNo);
        sms.setContent(builder.toString());
        sms.setSendTime("");
        sms.setTelNo(set);
        sms.setContent(PropertiesUtil.getSMSTemplate("validateSMS", new String[]{validate}));
        int code = mobileUtil.sendMsg(sms);
        if (code > 0) {
            code = 100;
            msg.setCode(Header.STATUS_SUCESS);
            map.clear();
            map.put("validate", validate);
            msg.setBody(map);
            msg.setMsg(mobileUtil.getResponseCode().get(code));
            logger.info("短信下发成功 :" + " 电话号码:" + telNo + " 验证码:" + validate);
            /*
             * if (message==null||"null".equals(message)||"".equals(message)) {
             * cacheUtil2.setEx(telNo, 15*60, validate); }
             */
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(mobileUtil.getResponseCode().get(code));
            logger.info("短信下发失败 :" + " 电话号码:" + telNo + " 验证码:" + validate + "code: " + code);
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MessageBean updateUserTel(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", u.getId());
        paramMap.put("userTel", String.valueOf(map.get("userTel")));
        paramMap.put("lastModifyTime", System.currentTimeMillis());
        paramMap.put("lastModifyUser", u.getId());
        int result = userMapper.updateUserTel(paramMap);
        if (result != 0) {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(Msg.UPDATE_SUCCUESS);
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.UPDATE_FAILED);
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MessageBean updateUserName(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", u.getId());
        paramMap.put("userName", String.valueOf(map.get("userName")));
        paramMap.put("lastModifyTime", System.currentTimeMillis());
        paramMap.put("lastModifyUser", u.getId());
        int result = userMapper.updateUserName(paramMap);
        if (result != 0) {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(Msg.UPDATE_SUCCUESS);
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.UPDATE_FAILED);
        }
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean updateOwner(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, String> map = Util.parseURLCode(jsonData);
        String updateUid = map.get("id");
        String userName = map.get("userName");
        String userTel = map.get("userTel");
        String tokenId = map.get("tokenId");
        User user = session.getAttribute(tokenId);
        String uID = user.getId();
        long time = System.currentTimeMillis();
        map.clear();
        map.put("lastModifyUser", uID);
        map.put("lastModifyTime", String.valueOf(time));
        map.put("userId", updateUid);
        map.put("userTel", userTel);
        map.put("userName", userName);
        if (!StringUtil.isBlank(userTel) || !StringUtil.isBlank(userName)) {
            if (!StringUtil.isBlank(userTel)) {
                int count = userMapper.updateUserTel(map);
                if (count == 0) {
                    throw new ServiceException(Msg.UPDATE_FAILED);
                }
            }
            if (!StringUtil.isBlank(userName)) {
                int count = userMapper.updateUserName(map);
                if (count == 0) {
                    throw new ServiceException(Msg.UPDATE_FAILED);
                }
            }
            logger.info("修改业主账户信息成功! 操作者:" + user.getId() + "_" + user.getUserName());
        } else {
            logger.error("修改业主账户信息失败! 操作者:" + user.getId() + "_" + user.getUserName());
            throw new ServiceException(Msg.PARAM_NOT_COMPLETE);
        }
        msg.setMsg(Msg.UPLOAD_SUCCUESS);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean savePlantOwner(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, String> map = Util.parseURLCode(jsonData);
        String tokenId = map.get("tokenId");
        User u = session.getAttribute(tokenId);
        String userName = map.get("userName");
        String userTel = map.get("userTel");
        String plantId = map.get("plantId");
        long time = System.currentTimeMillis();
        PlantInfo plant = plantInfoMapper.getPlantById(plantId);
        SysUser user = new SysUser();
        user.setPinyinSearch(ChineseToEnglish.getPingYin(userName));
        user.setUserName(userName);
        user.setUserTel(userTel);
        user.setGender("1");
        user.setCreateTime(time);
        user.setUserValid("0");
        user.setUserStatus("2");
        user.setUserType("1");
        user.setOrgId(plant.getOrgId());
        /** 新增业主账户 */
        int result = userMapper.insertPlantUser(user);
        if (result > 0) {
            logger.info("新增业主账户成功! " + user + "操作者:" + u.getId() + "_" + u.getUserName());
            /** 添加业主账户与电站的关联 */
            SysUserPlant userPlant = new SysUserPlant();
            userPlant.setCreateTime(time);
            userPlant.setPlantId(Integer.parseInt(plantId));
            userPlant.setUserId(user.getId());
            userPlant.setCreateUser(Integer.parseInt(u.getId()));
            result = userPlantMapper.saveUserPlant(userPlant);
            if (result > 0) {
                logger.info("业主账户关联电站成功! " + user.toString() + "操作者:" + u.getId() + "_" + u.getUserName());
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.ADD_SUCCUESS);
            } else {
                logger.error("业主账户关联电站失败! " + user.toString() + "操作者:" + u.getId() + "_" + u.getUserName());
                throw new ServiceException(Msg.ADD_FAILED);
            }

        } else {
            logger.error("新增业主账户失败! " + user.toString() + "操作者:" + u.getId() + "_" + u.getUserName());
            throw new ServiceException(Msg.ADD_FAILED);
        }
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean deleteOwner(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, String> map = Util.parseURLCode(jsonData);
        String tokenId = map.get("tokenId");
        User u = session.getAttribute(tokenId);
        String uId = map.get("id");
        String plantId = map.get("plantId");
        long time = System.currentTimeMillis();
        String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lastModifyTime", System.currentTimeMillis());
        paramMap.put("lastModifyUser", u.getId());
        paramMap.put("id", uId);
        int result = userMapper.deletePlantUser(map);
        if (result > 0) {
            logger.info(StringUtil.appendStr("删除业主账户成功! 业主账户id", uId, "电站id:", plantId, "操作者:", logUserName));
            /** 添加业主账户与电站的关联 */
            SysUserPlant userPlant = new SysUserPlant();
            userPlant.setPlantId(Integer.parseInt(plantId));
            userPlant.setUserId(Integer.parseInt(uId));
            result = userPlantMapper.delUserPlant(userPlant);
            if (result > 0) {
                result = userRoleMapper.delUserRole(Integer.parseInt(uId));
                logger.info(StringUtil.appendStr("删除业主与电站管理成功! 业主账户id", uId, "电站id:", plantId, "操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.DELETE_SUCCUESS);
            } else {
                logger.error(StringUtil.appendStr("删除业主与电站管理失败! 业主账户id", uId, "电站id:", plantId, "操作者:", logUserName));
                throw new ServiceException(Msg.DELETE_FAILED);
            }

        } else {
            logger.error(StringUtil.appendStr("删除业主账户失败! 业主账户id", uId, "电站id:", plantId, "操作者:", logUserName));
            throw new ServiceException(Msg.DELETE_FAILED);
        }
        return msg;
    }

    @Override
    public MessageBean wxgetUserByTel(String jsonData)
            throws ServiceException, SessionException, SessionTimeoutException {
        SysUser user = (SysUser) Util.parseURL(jsonData, "com.synpower.bean.SysUser");
        List<SysUser> list = userMapper.getWXUserByTel(user.getUserTel());
        MessageBean msg = new MessageBean();
        if (list == null || list.isEmpty()) {
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.PHONE_NUMBER_NOT_EXISTS);
        } else {
            throw new ServiceException(Msg.PHONE_NUMBER_EXISTS);
        }
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean saveSharedLink(String jsonData, Session session, long linkCode)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String tokenId = String.valueOf(map.get("tokenId"));
        String orgId = String.valueOf(map.get("orgId"));
        String roleId = String.valueOf(map.get("roleId"));
        String plantsId = String.valueOf(map.get("plantsId"));
        String userType = String.valueOf(map.get("userType"));
        int userId = Integer.parseInt(session.getAttribute(tokenId).getId());
        // 角色和组织必不可少
        if (Util.isNotBlank(roleId) && Util.isNotBlank(orgId) && Util.isNotBlank(userType)) {
            SysSharedLinks link = new SysSharedLinks();
            link.setCreateTime(System.currentTimeMillis());
            link.setCreateUser(userId);
            link.setOrgId(Integer.parseInt(orgId));
            link.setRoleId(Integer.parseInt(roleId));
            link.setSharedLinkCode(String.valueOf(linkCode));
            link.setUserType(userType);
            link.setStatus("1");
            if (Util.isNotBlank(plantsId)) {
                link.setPlantsId(plantsId);
            }
            int status = sysSharedLinksMapper.insertSelective(link);
            logger.info(StringUtil.appendStr("创建链接成功", link.toString()));
            map.clear();
            map.put("linkCode", String.valueOf(linkCode));
            msg.setBody(map);
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.CREATE_LINK_SUCCESS);
        } else {
            logger.info(StringUtil.appendStr("创建链接失败,缺少必须参数,创建者:", userId));
            throw new ServiceException(Msg.PARAM_NOT_COMPLETE);
        }
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean quicklyRegister(String jsonData, Session session, long sessionId, HttpServletRequest request)
            throws Exception {
        MessageBean msg = new MessageBean();
        // 将前端请求数据装载为对象
        Map<String, Object> map = Util.parseWEIXINURL(jsonData);
        String linkCode = String.valueOf(map.get("linkCode"));
        SysSharedLinks link = sysSharedLinksMapper.getRegisterCodeByCode(linkCode);
        if (link != null) {
            String weixinNo = String.valueOf(map.get("weixinNo"));
            if (StringUtils.isBlank(weixinNo) || "null".equals(weixinNo)) {
                throw new ServiceException("获取微信号失败,请重新授权");
            }
            String userName = String.valueOf(map.get("userId"));
            String telephone = String.valueOf(map.get("telephone"));
            String requestType = String.valueOf(map.get("requestType"));

            SysUser user = new SysUser();
            String nowUserType = link.getUserType();
            // 检查手机号是否有绑定的待激活用户
            SysUser userTemp = userMapper.getUnactiveUser(telephone);
            Map<String, String> paraMap = new HashMap<>();
            // ===========ybj
            // 置入appid和secret
            EEServiceUtil.addAppidAndSecret(paraMap, requestType);
            // ============
            paraMap.put("js_code", weixinNo);
            paraMap.put("grant_type", "authorization_code");
            Map<String, String> weixinRs = WeixinOper.visit(configParam.getWeixinUrl(), paraMap);
            String openId = weixinRs.get("openid");

            // 有些用户有unionId 有些没有的需要解密
            // ================
            String unionid = weixinRs.get("unionid");
            Map<String, Object> tempMap = null;

            // 应为下面需要这些数据 所以哪怕上面有unionid下面也要执行一次
            String encryptedData = String.valueOf(map.get("encryptedData"));
            String iv = String.valueOf(map.get("iv"));

            String weixinRes = "";
            try {
                encryptedData = encryptedData.replace(" ", "+");
                iv = iv.replace(" ", "+");
                weixinRes = AesCbcUtil.decrypt(encryptedData, weixinRs.get("session_key"), iv, "UTF-8");
                tempMap = Util.parseWEIXINURL(weixinRes);
            } catch (Exception e) {
                logger.error(" 获取用户头像和unionId等失败", e);
            }
            if (Util.isNotBlank(tempMap)) {
                unionid = String.valueOf(tempMap.get("unionId"));
            }
            // ================

            if (StringUtils.isBlank(unionid)) {
                throw new WeiXinExption(weixinRs.get("errmsg"));
            }

            if (Util.isNotBlank(tempMap)) {
                // 填充用户头像
                if (!Util.isNotBlank(user.getIconUrl())) {
                    // 下载用户头像
                    String iconName = null;
                    try {
                        iconName = "icon" + System.currentTimeMillis() + ".png";
                        DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
                                configParam.getIconURL());
                        user.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
                    } catch (Exception e) {
                        logger.error("下载设置头像失败" + weixinRes);
                    }
                }
            }
            String plants = link.getPlantsId();
            /** 转为个人账户 */
            if ("1".equals(nowUserType)) {
                // 新用户注册
                if (userTemp == null) {
                    user.setLoginId(String.valueOf(map.get("telephone")));
                    user.setPinyinSearch(ChineseToEnglish.getPingYin(userName));
                    user.setPassword(String.valueOf(map.get("password")));
                    user.setUserName(userName);
                    user.setUserTel(String.valueOf(map.get("telephone")));
                    user.setUserWeixin(openId);
                    user.setGender("1");
                    user.setCreateTime(System.currentTimeMillis());
                    user.setUserValid("0");
                    user.setUserStatus("1");
                    user.setUserType(nowUserType);
                    // 填充用户头像
                    user.setWeixinUUID(unionid);
                    user.setOrgId(link.getOrgId());
                    String iconName = "icon" + System.currentTimeMillis() + ".png";
                    // 执行数据新增
                    int result = userMapper.insertSelective(user);
                    logger.info("quick register save new user msg:" + user.toString());
                    // 组装数据
                    paraMap.clear();
                    paraMap.put("roleId", link.getRoleId() + "");
                    paraMap.put("createTime", String.valueOf(System.currentTimeMillis()));
                    paraMap.put("createUser", String.valueOf(user.getId()));
                    paraMap.put("userId", String.valueOf(user.getId()));
                    result = userRoleMapper.insertUserRole(paraMap);
                    logger.info("quick register save new userRole msg:" + paraMap.toString());
                    if (Util.isNotBlank(plants)) {
                        String[] plantArr = StringUtils.split(plants, ",");
                        SysUserPlant userPlant = new SysUserPlant();
                        userPlant.setCreateTime(System.currentTimeMillis());
                        userPlant.setUserId(user.getId());
                        userPlant.setCreateUser(user.getId());
                        for (String plantId : plantArr) {
                            userPlant.setPlantId(Integer.parseInt(plantId));
                            result = userPlantMapper.saveUserPlant(userPlant);
                        }
                        logger.info("quick register save userPlant msg:" + plants);
                    }
                    // 老用户只是为了变更电站
                } else {
                    user.setPinyinSearch(ChineseToEnglish.getPingYin(userName));
                    user.setPassword(String.valueOf(map.get("password")));
                    user.setUserName(userName);
                    user.setUserTel(String.valueOf(map.get("telephone")));
                    user.setUserWeixin(openId);
                    if (!Util.isNotBlank(userTemp.getIconUrl())) {
                        // 下载用户头像
                        String iconName = null;
                        try {
                            iconName = "icon" + System.currentTimeMillis() + ".png";
                            DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
                                    configParam.getIconURL());
                            user.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
                        } catch (Exception e) {
                            logger.error("下载设置头像失败" + weixinRes);
                        }
                    }
                    user.setWeixinUUID(unionid);
                    String gender = userTemp.getGender();
                    if (StringUtil.isBlank(gender)) {
                        user.setGender("1");
                    } else {
                        user.setGender(gender);
                    }
                    user.setLastModifyTime(System.currentTimeMillis());
                    user.setLastModifyUser(userTemp.getId());
                    user.setUserValid("0");
                    user.setUserStatus("1");
                    String type = userTemp.getUserType();
                    if (StringUtil.isBlank(type)) {
                        user.setUserType(nowUserType);
                    } else {
                        user.setUserType(type);
                    }
                    user.setId(userTemp.getId());

                    // user.setOrgId(Integer.parseInt(configParam.getDefaultOrg()));
                    // 执行数据新增
                    int result = userMapper.updateByPrimaryKeySelective(user);
                    // 如果用户以前为业主则不做任何操作
                    if (userTemp.getUserRole() == null) {
                        paraMap.clear();
                        paraMap.put("roleId", link.getRoleId() + "");
                        paraMap.put("userId", String.valueOf(user.getId()));
                        paraMap.put("createTime", String.valueOf(System.currentTimeMillis()));
                        paraMap.put("userId", String.valueOf(user.getId()));
                        result = userRoleMapper.insertUserRole(paraMap);
                    } else if (!"1".equals(userTemp.getUserType())) {
                        userRoleMapper.delUserRole(user.getId());
                        paraMap.clear();
                        paraMap.put("roleId", link.getRoleId() + "");
                        paraMap.put("userId", String.valueOf(user.getId()));
                        paraMap.put("createTime", String.valueOf(System.currentTimeMillis()));
                        paraMap.put("createUser", String.valueOf(user.getId()));
                        result = userRoleMapper.insertUserRole(paraMap);
                    }
                    if (Util.isNotBlank(plants)) {
                        String[] plantArr = StringUtils.split(plants, ",");
                        User temp = new User();
                        temp.setId(user.getId() + "");
                        temp.setUserType("1");
                        List<Integer> userPlants = plantInfoService.getPlantsForUser(temp, 1);
                        List<Integer> resultPlant = new ArrayList<>();
                        if (Util.isNotBlank(userPlants)) {
                            for (String plantId : plantArr) {
                                boolean status = true;
                                Integer tempId = Integer.parseInt(plantId);
                                for (Integer integer : userPlants) {
                                    if (tempId == integer) {
                                        status = false;
                                        break;
                                    }
                                }
                                if (status) {
                                    resultPlant.add(tempId);
                                }
                            }
                        } else {
                            for (String plantId : plantArr) {
                                resultPlant.add(Integer.parseInt(plantId));
                            }
                        }
                        SysUserPlant userPlant = new SysUserPlant();
                        userPlant.setCreateTime(System.currentTimeMillis());
                        userPlant.setUserId(user.getId());
                        userPlant.setCreateUser(user.getId());
                        for (Integer integer : resultPlant) {
                            userPlant.setPlantId(integer);
                            userPlantMapper.saveUserPlant(userPlant);
                        }
                        logger.info("quick register save userPlant msg:" + plants);
                    }
                }
                /** 转为企业账户 */
            } else {
                if (userTemp == null) {
                    user.setLoginId(String.valueOf(map.get("telephone")));
                    user.setPinyinSearch(ChineseToEnglish.getPingYin(userName));
                    user.setPassword(String.valueOf(map.get("password")));
                    user.setUserName(userName);
                    user.setUserTel(String.valueOf(map.get("telephone")));
                    user.setUserWeixin(openId);
                    user.setGender("1");
                    user.setCreateTime(System.currentTimeMillis());
                    user.setUserValid("0");
                    user.setUserStatus("1");
                    user.setUserType(nowUserType);
                    // 填充用户头像
                    user.setWeixinUUID(unionid);
                    user.setOrgId(link.getOrgId());
                    // 下载用户头像
                    String iconName = null;
                    try {
                        iconName = "icon" + System.currentTimeMillis() + ".png";
                        DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
                                configParam.getIconURL());
                        user.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
                    } catch (Exception e) {
                        logger.error("下载设置头像失败" + weixinRes);
                    }
                    // 执行数据新增
                    int result = userMapper.insertSelective(user);
                    logger.info("quick register save new user msg:" + user.toString());
                    // 组装数据
                    paraMap.clear();
                    paraMap.put("roleId", link.getRoleId() + "");
                    paraMap.put("createTime", String.valueOf(System.currentTimeMillis()));
                    paraMap.put("createUser", String.valueOf(user.getId()));
                    paraMap.put("userId", String.valueOf(user.getId()));
                    result = userRoleMapper.insertUserRole(paraMap);
                    logger.info("quick register save new userRole msg:" + paraMap.toString());
                    // 老用户变更组织,角色,如果以前是个人用户删除电站
                } else {
                    user.setPinyinSearch(ChineseToEnglish.getPingYin(userName));
                    user.setPassword(String.valueOf(map.get("password")));
                    user.setUserName(userName);
                    user.setUserTel(String.valueOf(map.get("telephone")));
                    user.setUserWeixin(openId);
                    if (!Util.isNotBlank(userTemp.getIconUrl())) {
                        // 下载用户头像
                        String iconName = null;
                        try {
                            iconName = "icon" + System.currentTimeMillis() + ".png";
                            DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
                                    configParam.getIconURL());
                            user.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
                        } catch (Exception e) {
                            logger.error("下载设置头像失败" + weixinRes);
                        }
                    }
                    user.setWeixinUUID(unionid);
                    String gender = userTemp.getGender();
                    if (StringUtil.isBlank(gender)) {
                        user.setGender("1");
                    } else {
                        user.setGender(gender);
                    }
                    user.setLastModifyTime(System.currentTimeMillis());
                    user.setLastModifyUser(userTemp.getId());
                    user.setUserValid("0");
                    user.setUserStatus("1");
                    String type = userTemp.getUserType();
                    user.setUserType(nowUserType);
                    user.setId(userTemp.getId());

                    // user.setOrgId(Integer.parseInt(configParam.getDefaultOrg()));
                    // 执行数据新增
                    int result = userMapper.updateByPrimaryKeySelective(user);
                    // 组装数据
                    if (userTemp.getUserRole() != null) {
                        userRoleMapper.delUserRole(user.getId());
                    }
                    paraMap.clear();
                    paraMap.put("roleId", link.getRoleId() + "");
                    paraMap.put("userId", String.valueOf(user.getId()));
                    paraMap.put("createTime", String.valueOf(System.currentTimeMillis()));
                    paraMap.put("createUser", String.valueOf(user.getId()));
                    result = userRoleMapper.insertUserRole(paraMap);
                    if ("1".equals(userTemp.getUserType())) {
                        // 删掉用户电站
                        userPlantMapper.delUserAllPlant(user.getId());
                    }
                }
            }
            link.setUsedUser(openId);
            link.setUsedTime(System.currentTimeMillis());
            link.setStatus("0");
            sysSharedLinksMapper.updateLinkTime(link);
            msg.setCode(Header.STATUS_SUCESS);
            msg.setMsg(Msg.REGISTER_SUCCUESS);
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.NOT_FIND_LINK);
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    @Override
    public MessageBean insertUserFormId(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        List<Map<String, String>> list = (List<Map<String, String>>) map.get("formIds");
        User u = session.getAttribute(map.get("tokenId") + "");
        String userId = u.getId();
        String logUserName = StringUtil.appendStrNoBlank(userId, "_", u.getUserName());
        if (Util.isNotBlank(list)) {
            for (int i = 0, size = list.size(); i < size; i++) {
                Map<String, String> tempMap = list.get(i);
                tempMap.put("userId", userId);
            }
            int result = formIdMapper.insertUserFormId(list);
            if (result > 0) {
                logger.info(StringUtil.appendStr("新增formId成功!", "操作者:", logUserName));
                msg.setCode(Header.STATUS_SUCESS);
                msg.setMsg(Msg.ADD_SUCCUESS);
            } else {
                logger.error(StringUtil.appendStr("新增formId失败!", "操作者:", logUserName));
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg(Msg.ADD_FAILED);
            }
        }
        return msg;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    @Override
    public MessageBean getPushInfo(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException, ParseException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        String userId = u.getId();
        String logUserName = StringUtil.appendStrNoBlank(userId, "_", u.getUserName());
        SysUserFormId formId = getCanUseFormId(u);
        if (formId != null) {
            // 获取access_token
            Map<String, String> paraMap = new HashMap<>();
            paraMap.put("appid", configParam.getAppid2());
            paraMap.put("secret", configParam.getSecret2());
            paraMap.put("grant_type", "client_credential");
            String access = WxPushUtil.getAccessToken(paraMap).get("access_token");
            // 向登录用户发送消息
            SysUser user = userMapper.getUserInfoById(userId);
            Map<String, Object> weixinRs = sendMessageWX(user, formId, access, "1");
            int delete = formIdMapper.deleteFormIdInfo(formId.getId());
            if (delete > 0) {
                logger.info(StringUtil.appendStr("删除formId成功! formId为：", formId.getId(), "操作者:", logUserName));
            } else {
                logger.error(StringUtil.appendStr("删除formId失败!", "操作者:", logUserName));
            }
            // 消息发送成功后，检查formId的条数，如果只有一条那么通知用户登录小电监控获取formId，否则将不能继续推送消息
            int errcode = (int) weixinRs.get("errcode");
            String errmsg = (String) weixinRs.get("errmsg");
            if (errcode == 0 && errmsg.equals("ok")) {
                int count = formIdMapper.getCount(userId);
                if (count == 1) {
                    // 上面的formId已经被删除，需要重新获取formId
                    SysUserFormId formId1 = getCanUseFormId(u);
                    Map<String, Object> weixinRs1 = sendMessageWX(user, formId1, access, "2");
                    int delete1 = formIdMapper.deleteFormIdInfo(formId1.getId());
                    if (delete1 > 0) {
                        logger.info(StringUtil.appendStr("删除formId成功! formId为：", formId1.getId(), "操作者:", logUserName));
                    } else {
                        logger.error(StringUtil.appendStr("删除formId失败!", "操作者:", logUserName));
                    }
                }
            }
            msg.setCode(Header.STATUS_SUCESS);
            msg.setBody(weixinRs);
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg("没有可用formId");
        }
        return msg;
    }

    public SysUserFormId getCanUseFormId(User user) throws ParseException {
        String userId = user.getId();
        String logUserName = StringUtil.appendStrNoBlank(userId, "_", user.getUserName());
        SysUserFormId formId = formIdMapper.getFormIdInfo(userId);
        if (formId != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dBegin = new Date(formId.getTime());
            Date dEnd = new Date(System.currentTimeMillis());
            long day = TimeUtil.getDatePoor(dBegin, dEnd);
            if (day > 7) {
                int delete = formIdMapper.deleteFormIdInfo(formId.getId());
                if (delete > 0) {
                    logger.info(StringUtil.appendStr("删除formId成功! formId为：", formId.getId(), "操作者:", logUserName));
                } else {
                    logger.error(StringUtil.appendStr("删除formId失败!", "操作者:", logUserName));
                }
                return getCanUseFormId(user);
            } else {
                return formId;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("appid", "wx207e73ae2c9d9ab3");
        paraMap.put("secret", "bb75f98ca7281a8bea441fcafd90cca3");
        paraMap.put("js_code", "003OjgrK1pef360YRatK1ETcrK1OjgrB");
        paraMap.put("grant_type", "authorization_code");

        Map<String, String> weixinRs = WeixinOper.visit("https://api.weixin.qq.com/sns/jscode2session", paraMap);
        String openId = weixinRs.get("openid");
        // 有些用户有unionId 有些没有的需要解密得到
        // ================
        String unionid = weixinRs.get("unionid");
        Map<String, Object> tempMap = null;

        // 应为下面需要这些数据 所以哪怕上面有unionid下面也要执行一次
//		String encryptedData = String.valueOf(paraMap.get("encryptedData"));
//		String iv = String.valueOf(paraMap.get("iv"));
        String encryptedData = "U6nNiB3wlf+Sd0zAR0O2KVb5hyVvdT3SZeghNGs4HTcPXfzrOuXuxQ60y/BR+HpVJ1qlQADDfDsgYZ/8AFhRLLWJu+cQC9ogb7ux6XLKoKyGhHIwhyC0Uz5LtoiPBJbIvz+11GZlCLpjlBdRGRW/bx0QRPwr/nef3CP5iU0aPVpngoavsS9OanJAKBICORek55jjnozXiVbB0EMyWsEgPeRu07nGjHn/wg6+Na5u0AttwqNaqMS0/GlVbgsCDpjPK1vKi9uo6acEqR2s8leCZ+oC1fP9lknXXjAwF9miKgX2ARHY5BcL8nuP0ndDGYetYMAc6OwmTVHXmuNRDzhJjUHNwML3q6oZQusrvm3nCdBYvJaSGuy/ngTlV1BPSbV5XvtKnJAApaskfVbh8pnUUK07SMhUMtAAaCAi9GJup2QhJc17rFvLawHZZxyZp2OmmHuoglRUCQs8tf1Ak87xTxNd8W+2vu7VbOF8W9fLa+ZGN/DkriD4UxSFJl9lcdxFtinVMG8wYzJGj6hfVLSwPA==";
        String iv = "3OVRvp4I3S7vf+PgShGzwg==";
        String weixinRes = "";
        try {
            encryptedData = encryptedData.replace(" ", "+");
            iv = iv.replace(" ", "+");
            weixinRes = AesCbcUtil.decrypt(encryptedData, weixinRs.get("session_key"), iv, "UTF-8");
            tempMap = Util.parseWEIXINURL(weixinRes);
        } catch (Exception e) {
        }
        if (Util.isNotBlank(tempMap)) {
            unionid = String.valueOf(tempMap.get("unionId"));
        }
        if (StringUtils.isBlank(unionid)) {
            throw new WeiXinExption(weixinRs.get("errmsg"));
        }
        // 检查手机号是否有绑定的待激活用户

        System.out.println(weixinRes);
        DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), "headPic", "d:" + File.separator);
    }

    public Map<String, TemplateData> getTemplateWX(String type) {
        Map<String, TemplateData> map = new HashMap<String, TemplateData>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        switch (type) {
            case "1":
                TemplateData first = new TemplateData();
                first.setColor("#173177");
                first.setValue("四川省成都市环球中心");
                map.put("keyword1", first);
                TemplateData name = new TemplateData();
                name.setColor("#173177");
                name.setValue(format.format(new Date()));
                map.put("keyword2", name);
                TemplateData remark = new TemplateData();
                remark.setColor("#173177");
                remark.setValue("分布式光伏管理系统");
                map.put("keyword3", remark);
                break;
            case "2":
                TemplateData first1 = new TemplateData();
                first1.setColor("#173177");
                first1.setValue("您推送消息的条数已用完");
                map.put("keyword1", first1);
                TemplateData name1 = new TemplateData();
                name1.setColor("#173177");
                name1.setValue(format.format(new Date()));
                map.put("keyword2", name1);
                TemplateData remark1 = new TemplateData();
                remark1.setColor("#173177");
                remark1.setValue("请您登录小电监控获取推送条数");
                map.put("keyword3", remark1);
                TemplateData address = new TemplateData();
                address.setColor("#173177");
                address.setValue("四川省成都市环球中心");
                map.put("keyword4", address);
                break;
            default:
                break;
        }
        return map;
    }

    public Map<String, Object> sendMessageWX(SysUser user, SysUserFormId formId, String access, String type) {
        Map<String, Object> weixinRs = null;
        WxTemplate template = new WxTemplate();
        // 用户微信号
        template.setTouser(user.getUserWeixin());
        // 模板id
        switch (type) {
            case "1":
                template.setTemplate_id(configParam.getTemplateId1());
                break;
            case "2":
                template.setTemplate_id(configParam.getTemplateId2());
                break;
            default:
                break;
        }
        // 用户的formId
        template.setForm_id(formId.getValue());
        // 编辑模板内容(根据模板具体内容进行改变)
        Map<String, TemplateData> m = getTemplateWX(type);
        template.setData(m);
        JSONObject json = JSONObject.fromObject((template));
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access;
        try {
            weixinRs = WeixinOper.visitByJson(url, json);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return weixinRs;
    }

    public List getUserOrgByToken(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException, ParseException {
        List resultList = new ArrayList<>();
        Map<String, Object> map = Util.parseWEIXINURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        List<String> reList = new ArrayList<>();
        // 获取所有组织
        List<SysOrg> orgList = orgMapper.getAllOrg();
        reList.add(((SysOrg) u.getUserOrg()).getId() + "");
        reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
        List<SysOrg> orgNameList = orgMapper.getOrgNameByIds(reList);
        if (Util.isNotBlank(orgNameList)) {
            for (int i = 0, size = orgNameList.size(); i < size; i++) {
                Map<String, Object> tempMap = new HashMap<String, Object>();
                SysOrg org = orgNameList.get(i);
                tempMap.put("id", org.getId());
                tempMap.put("value", org.getOrgName());
                resultList.add(tempMap);
            }
        }
        return resultList;
    }

    public List getUserRoleByToken(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException, ParseException {
        List resultList = new ArrayList<>();
        Map<String, Object> map = Util.parseURL(str);
        User u = session.getAttribute(map.get("tokenId") + "");
        List<String> reList = new ArrayList<>();
        // 获取所有组织
        List<SysOrg> orgList = orgMapper.getAllOrg();
        reList.add(((SysOrg) u.getUserOrg()).getId() + "");
        reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
        List<SysRole> roleList = roleMapper.getRoleByOrgIds(reList);
        if (Util.isNotBlank(roleList)) {
            for (int i = 0, size = roleList.size(); i < size; i++) {
                Map<String, Object> tempMap = new HashMap<String, Object>();
                SysRole role = roleList.get(i);
                tempMap.put("id", role.getId());
                tempMap.put("value", role.getRoleName());
                resultList.add(tempMap);
            }
        }
        return resultList;
    }

    public List getUserPlantByToken(String str, Session session)
            throws SessionException, SessionTimeoutException, ServiceException, ParseException {
        List resultList = new ArrayList<>();
        Map<String, Object> map = Util.parseURL(str);
        String tokenId = map.get("tokenId") + "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Integer> plants = plantInfoService.getPlantsForUser(tokenId, session, 1);

        if (Util.isNotBlank(plants)) {
            String plantType = String.valueOf(map.get("plantType"));
            if (Util.isEmpty(plantType)) {
                //过滤 不需要能耗的
                ArrayList<Integer> tPlantIdList = new ArrayList<>();
                List<String> pList = SystemCache.plantTypeMap.get(String.valueOf(PTAC.ENERGYEFFICPLANT));
                if (!CollectionUtils.isEmpty(pList)) {
                    pList.forEach(e -> tPlantIdList.add(Integer.valueOf(e)));
                    plants.removeAll(tPlantIdList);
                }
            } else {
                //能耗的
                List<Integer> tPlantIdList = new ArrayList<>();
                List<String> pList = SystemCache.plantTypeMap.get(plantType);
                if (!CollectionUtils.isEmpty(pList)) {
                    pList.forEach(e -> tPlantIdList.add(Integer.valueOf(e)));
                    plants.retainAll(tPlantIdList);
                }
            }

            List<PlantInfo> plantInfo = plantInfoMapper.getPlantByIds(plants);
            if (Util.isNotBlank(plantInfo)) {
                for (int i = 0, size = plantInfo.size(); i < size; i++) {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    PlantInfo plant = plantInfo.get(i);
                    tempMap.put("id", plant.getId());
                    tempMap.put("value", plant.getPlantName());
                    tempMap.put("creationTime", format.format(plant.getCreateTime()));
                    resultList.add(tempMap);
                }
            }
        }
        return resultList;
    }

    @Override
    public MessageBean getInfoForLink(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException, ParseException {
        Map<String, Object> map = Util.parseURL(jsonData);
        MessageBean msg = new MessageBean();
        String userType = String.valueOf(map.get("userType"));
        if ("0".equals(userType)) {
            map.clear();
            map.put("orgList", getUserOrgByToken(jsonData, session));
            map.put("roleList", getUserRoleByToken(jsonData, session));
        } else {
            List plantList = getUserPlantByToken(jsonData, session);
            map.put("plantList", plantList);
        }
        msg.setBody(map);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean checkLinkCode(String jsonData) throws ServiceException, IOException, WeiXinExption {
        Map<String, Object> map = Util.parseWEIXINURL(jsonData);
        MessageBean msg = new MessageBean();
        String linkCode = String.valueOf(map.get("linkCode"));
        String weixinNo = String.valueOf(map.get("weixinNo"));
        String requestType = String.valueOf(map.get("requestType"));
        if (Util.isNotBlank(linkCode) && Util.isNotBlank(weixinNo)) {
            long nowTime = System.currentTimeMillis();
            SysSharedLinks link = sysSharedLinksMapper.selectByLinkCode(linkCode);
            if (link != null) {
                long createTime = link.getCreateTime();
                if ((nowTime - createTime) <= 60 * 60 * 1000) {
                    Map<String, String> paraMap = new HashMap<>();
                    // ========ybj

                    EEServiceUtil.addAppidAndSecret(paraMap, requestType);
                    // ========
                    paraMap.put("js_code", weixinNo);
                    paraMap.put("grant_type", "authorization_code");
                    Map<String, String> weixinRs = WeixinOper.visit(configParam.getWeixinUrl(), paraMap);

                    // 有些用户有unionId 有些没有的需要解密得到
                    // ================
                    String unionid = weixinRs.get("unionid");
                    Map<String, Object> tempMap = null;

                    // 应为下面需要这些数据 所以哪怕上面有unionid下面也要执行一次
                    String encryptedData = String.valueOf(map.get("encryptedData"));
                    String iv = String.valueOf(map.get("iv"));

                    String weixinRes = "";
                    try {
                        encryptedData = encryptedData.replace(" ", "+");
                        iv = iv.replace(" ", "+");
                        weixinRes = AesCbcUtil.decrypt(encryptedData, weixinRs.get("session_key"), iv, "UTF-8");
                        tempMap = Util.parseWEIXINURL(weixinRes);
                    } catch (Exception e) {
                        logger.error(" 获取用户头像和unionId等失败", e);
                    }
                    if (Util.isNotBlank(tempMap)) {
                        unionid = String.valueOf(tempMap.get("unionId"));
                    }

                    // ================

                    if (StringUtils.isBlank(unionid)) {
                        throw new WeiXinExption(weixinRs.get("errmsg"));
                    }

                    boolean b = true;
                    if (Util.isNotBlank(link.getUsedUser())) {
                        if (!link.getUsedUser().equals(unionid)) {
                            b = false;
                        }
                    }
                    if (b) {
                        map.clear();
                        SysUser user = userMapper.getUserInfoById(link.getCreateUser() + "");
                        map.put("createUser", user.getUserName());
                        String userType = link.getUserType();
                        if ("1".equals(userType)) {
                            String plants = link.getPlantsId();
                            List<String> plantName = new ArrayList<>();
                            if (Util.isNotBlank(plants)) {
                                String[] plantArr = StringUtils.split(plants, ",");
                                List<PlantInfo> plantList = plantInfoMapper.getPlantByIds(Arrays.asList(plantArr));
                                if (Util.isNotBlank(plantList)) {
                                    for (PlantInfo plantInfo : plantList) {
                                        plantName.add(plantInfo.getPlantName());
                                    }
                                }
                            }
                            map.put("plantList", plantName);
                            map.put("userType", userType);
                        } else {
                            SysOrg org = orgMapper.getOrgById(link.getOrgId() + "");
                            SysRole role = roleMapper.getRoleById(link.getRoleId());
                            map.put("roleName", role.getRoleName());
                            map.put("orgName", org.getOrgName());
                            map.put("userType", userType);
                        }
                        SysSharedLinks updateLink = new SysSharedLinks();
                        updateLink.setUsedUser(unionid);
                        updateLink.setSharedLinkCode(linkCode);
                        updateLink.setStatus("1");
                        sysSharedLinksMapper.updateLinkUserdUser(updateLink);
                        msg.setCode(Header.STATUS_SUCESS);
                        msg.setBody(map);
                    } else {
                        msg.setCode(Header.STATUS_FAILED);
                        msg.setMsg(Msg.LINK_LOCKED);
                    }
                } else {
                    msg.setCode(Header.STATUS_FAILED);
                    msg.setMsg(Msg.LINK_UNUSEFUL);
                }
            } else {
                msg.setCode(Header.STATUS_FAILED);
                msg.setMsg(Msg.NOT_FIND_LINK);
            }
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.PARAM_NOT_COMPLETE);
        }
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public MessageBean checkRegisterUser(String jsonData) throws Exception {
        Map<String, Object> map = Util.parseWEIXINURL(jsonData);
        MessageBean msg = new MessageBean();
        String linkCode = String.valueOf(map.get("linkCode"));
        String weixinNo = String.valueOf(map.get("weixinNo"));
        String telephone = String.valueOf(map.get("telephone"));
        String requestType = String.valueOf(map.get("requestType"));
        SysUser userTemp = userMapper.getUnactiveUser(telephone);
        if (userTemp == null) {
            throw new NotRegisterException("没有查询到该用户,请注册!" + telephone);
        } else {
            Map<String, String> paraMap = new HashMap<>();
            // ===ybj
            EEServiceUtil.addAppidAndSecret(paraMap, requestType);
            // ===
            paraMap.put("js_code", weixinNo);
            paraMap.put("grant_type", "authorization_code");
            Map<String, String> weixinRs = WeixinOper.visit(configParam.getWeixinUrl(), paraMap);
            String openId = weixinRs.get("openid");
            // 有些用户有unionId 有些没有的需要解密得到
            // ================
            String unionid = weixinRs.get("unionid");
            Map<String, Object> tempMap = null;

            // 应为下面需要这些数据 所以哪怕上面有unionid下面也要执行一次
            String encryptedData = String.valueOf(map.get("encryptedData"));
            String iv = String.valueOf(map.get("iv"));

            String weixinRes = "";
            try {
                encryptedData = encryptedData.replace(" ", "+");
                iv = iv.replace(" ", "+");
                weixinRes = AesCbcUtil.decrypt(encryptedData, weixinRs.get("session_key"), iv, "UTF-8");
                tempMap = Util.parseWEIXINURL(weixinRes);
            } catch (Exception e) {
                logger.error(" 获取用户头像和unionId等失败", e);
            }
            if (Util.isNotBlank(tempMap)) {
                unionid = String.valueOf(tempMap.get("unionId"));
            }
            // ================
            if (StringUtils.isBlank(unionid)) {
                throw new WeiXinExption(weixinRs.get("errmsg"));
            }
            SysSharedLinks link = sysSharedLinksMapper.selectByLinkCode(linkCode);
            SysUser updateUser = new SysUser();
            updateUser.setId(userTemp.getId());
            updateUser.setUserType(link.getUserType());
            updateUser.setOrgId(link.getOrgId());
            updateUser.setLastModifyTime(System.currentTimeMillis());
            updateUser.setLastModifyUser(userTemp.getId());
            updateUser.setUserWeixin(openId);
            updateUser.setWeixinUUID(unionid);
            updateUser.setUserStatus("1");
            updateUser.setUserValid("0");

            if (Util.isNotBlank(tempMap)) {
                // 填充用户头像
                if (!Util.isNotBlank(userTemp.getIconUrl())) {
                    // 下载用户头像
                    String iconName = null;
                    try {
                        iconName = "icon" + System.currentTimeMillis() + ".png";
                        DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
                                configParam.getIconURL());
                        updateUser.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
                    } catch (Exception e) {
                        logger.error("下载设置头像失败" + weixinRes);
                    }
                }
                logger.info(" 自动关联账户,获取用户的uuid 头像成功" + unionid);
            }
            updateUser.setWeixinUUID(unionid);
            updateUser.setUserWeixin(openId);

            userMapper.updateByPrimaryKeySelective(updateUser);
            if ("1".equals(userTemp.getUserType()) && "0".equals(link.getUserType())) {
                // 删掉用户电站
                userPlantMapper.delUserAllPlant(userTemp.getId());
            }
            if ("1".equals(link.getUserType())) {
                String plants = link.getPlantsId();
                if (Util.isNotBlank(plants)) {
                    String[] plantArr = StringUtils.split(plants, ",");
                    SysUserPlant userPlant = new SysUserPlant();
                    User user = new User();
                    user.setId(updateUser.getId() + "");
                    user.setUserType("1");
                    List<Integer> userPlants = plantInfoService.getPlantsForUser(user, 1);
                    List<Integer> resultPlant = new ArrayList<>();
                    if (Util.isNotBlank(userPlants)) {
                        for (String plantId : plantArr) {
                            boolean status = true;
                            Integer tempId = Integer.parseInt(plantId);
                            for (Integer integer : userPlants) {
                                if (tempId == integer) {
                                    status = false;
                                    break;
                                }
                            }
                            if (status) {
                                resultPlant.add(tempId);
                            }
                        }
                    } else {
                        for (String plantId : plantArr) {
                            resultPlant.add(Integer.parseInt(plantId));
                        }
                    }
                    userPlant.setCreateTime(System.currentTimeMillis());
                    userPlant.setUserId(updateUser.getId());
                    userPlant.setCreateUser(updateUser.getId());
                    for (Integer integer : resultPlant) {
                        userPlant.setPlantId(integer);
                        userPlantMapper.saveUserPlant(userPlant);
                    }
                    logger.info("quick register save userPlant msg:" + resultPlant);
                }
            }
            if (userTemp.getUserRole() == null || userTemp.getUserRole().getId() != link.getRoleId()) {
                userRoleMapper.delUserRole(userTemp.getId());
                // {roleId},#{userId},#{createTime},#{createUser})
                map.clear();
                map.put("userId", userTemp.getId());
                map.put("roleId", link.getRoleId());
                map.put("createTime", System.currentTimeMillis());
                map.put("createUser", userTemp.getId());
                userRoleMapper.insertUserRole(map);
                logger.info("quick register save new userRole msg:" + map.toString());
            }
            link.setUsedUser(openId);
            link.setUsedTime(System.currentTimeMillis());
            link.setStatus("0");
            sysSharedLinksMapper.updateLinkTime(link);
        }
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }
}