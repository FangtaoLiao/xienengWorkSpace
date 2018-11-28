package com.synpower.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synpower.bean.SysAddressLocation;
import com.synpower.bean.SysUser;
import com.synpower.constant.PTAC;
import com.synpower.dao.SysAddressLocationMapper;
import com.synpower.dao.SysUserMapper;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.NotRegisterException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.WeiXinExption;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.LoginService;
import com.synpower.service.PlantInfoService;
import com.synpower.util.AesCbcUtil;
import com.synpower.util.ApiUtil;
import com.synpower.util.DownloadUtils;
import com.synpower.util.EEServiceUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.Util;
import com.synpower.weixin.WeixinOper;

/*****************************************************************************
 * @Package: com.synpower.serviceImpl ClassName: LoginServiceImpl
 * @Description:登录相关功能
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年10月26日上午10:12:03 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
@Service
public class LoginServiceImpl implements LoginService {
	private Logger logger = Logger.getLogger(LoginServiceImpl.class);
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private SysAddressLocationMapper addressLocationMappr;

	/**
	 * @Title: loginIn
	 * @Description:1 用户名和密码登录 邮箱和密码登录 电话号码和密码登录 2扫码的时候只需要 用户名 或者电话号码或者邮箱登录
	 *                3用户所在的角色或者组织或者自己本身处于禁用状态,都不允许登录
	 * @return: MessageBean
	 * @throws Exception
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月10日下午5:08:04
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public MessageBean loginIn(String jsonData, Long sessionId, Session session, HttpServletRequest request)
			throws Exception {
		MessageBean msg = new MessageBean();
		// 将前端请求数据装载为对象
		Map<String, Object> map = Util.parseWEIXINURL(jsonData);
		String requestType = String.valueOf(map.get("requestType"));
		SysUser userTemp = new SysUser();
		userTemp.setRequestType(requestType);
		userTemp.setLoginId(String.valueOf(map.get("loginId")));
		userTemp.setPassword(String.valueOf(map.get("password")));
		if ("1".equals(requestType)) {
			// 执行数据条件查询
			List<SysUser> userList = userMapper.getUserByIdAndPsw(userTemp);
			if (userList == null || userList.isEmpty()) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,输入错误!");
				throw new ServiceException(Msg.LOGIN_ERROR_PSW);
			}
			SysUser sysUser = userList.get(0);
			// 角色停用或者用户或者用户所在的机构被停用都不能登录
			if ("0".equals(sysUser.getUserType())
					&& ("0".equals(sysUser.getUserStatus()) || "0".equals(sysUser.getUserRole().getRoleStatus())
							|| "0".equals(sysUser.getUserOrg().getOrgStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户已被冻结!");
				throw new ServiceException(Msg.LOGIN_UNABLE);

			} else if ("0".equals(sysUser.getUserType())
					&& ("2".equals(sysUser.getUserStatus()) || "2".equals(sysUser.getUserRole().getRoleStatus())
							|| "2".equals(sysUser.getUserOrg().getOrgStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户还未激活!");
				throw new ServiceException(Msg.LOGIN_UNACTIVE);
				// 业主账户 不校验组织是否被禁用
			} else if ("1".equals(sysUser.getUserType())
					&& ("0".equals(sysUser.getUserStatus()) || "0".equals(sysUser.getUserRole().getRoleStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户已被冻结!");
				throw new ServiceException(Msg.LOGIN_UNABLE);
			} else if ("1".equals(sysUser.getUserType())
					&& ("2".equals(sysUser.getUserStatus()) || "2".equals(sysUser.getUserRole().getRoleStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户还未激活!");
				throw new ServiceException(Msg.LOGIN_UNACTIVE);
			} else if ("1".equals(sysUser.getUserStatus())) {
				// 将数据库记录装载到session需要的序列化对象中取
				User user = fillIn(sysUser, sessionId, request);
				user.setPlantType(plantInfoService.getPlantTypeForUser(user, 1, 1));
				user.setPlantTypeList(plantInfoService.getPlantTypeForUser(user, 1, 0));
				session.setAttribute(String.valueOf(sessionId), user);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(user);
				logger.info(" 用户: " + user.getUserTel() + " 登录成功,IP:" + Util.getIpByRequest(request) + "!");
			}
			return msg;
			// 2为小电监控 3为小电能控
		} else if ("2".equals(requestType) || "3".equals(requestType)) {// 当在微信登陆时,只传微信号
			String weixinNo = userTemp.getLoginId();
			if (StringUtils.isBlank(weixinNo) || "null".equals(weixinNo)) {
				throw new ServiceException("获取微信号失败,请重新授权");
			}
			Map<String, String> paraMap = new HashMap<>();
			// ===========ybj
			// 置入appid和secret
			EEServiceUtil.addAppidAndSecret(paraMap, requestType);
			// ============
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
			userTemp.setLoginId(unionid);
			List<SysUser> list = userMapper.getUserByIdAndPsw(userTemp);
			if (list == null || list.isEmpty()) {
				throw new NotRegisterException("没有查询到该用户,请注册!" + unionid);
			}
			SysUser sysUser = list.get(0);
			if ("0".equals(sysUser.getUserType())
					&& ("0".equals(sysUser.getUserStatus()) || "0".equals(sysUser.getUserRole().getRoleStatus())
							|| "0".equals(sysUser.getUserOrg().getOrgStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户已被冻结!");
				throw new ServiceException(Msg.LOGIN_UNABLE);
			} else if ("0".equals(sysUser.getUserType())
					&& ("2".equals(sysUser.getUserStatus()) || "2".equals(sysUser.getUserRole().getRoleStatus())
							|| "2".equals(sysUser.getUserOrg().getOrgStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户还未激活!");
				throw new NotRegisterException(Msg.LOGIN_UNACTIVE);
				// 业主账户 不校验组织是否被禁用
			} else if ("1".equals(sysUser.getUserType())
					&& ("0".equals(sysUser.getUserStatus()) || "0".equals(sysUser.getUserRole().getRoleStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户已被冻结!");
				throw new ServiceException(Msg.LOGIN_UNABLE);
			} else if ("1".equals(sysUser.getUserType())
					&& ("2".equals(sysUser.getUserStatus()) || "2".equals(sysUser.getUserRole().getRoleStatus()))) {
				logger.warn(" 用户: " + (userTemp.getUserTel() == null
						? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
						: userTemp.getUserTel()) + " 尝试登录失败,账户还未激活!");
				throw new NotRegisterException(Msg.LOGIN_UNACTIVE);
			} else {

				if (Util.isNotBlank(tempMap)) {
					if (!Util.isNotBlank(sysUser.getWeixinUUID())) {
						sysUser.setWeixinUUID(unionid);
					}
					// 填充用户头像
					if (!Util.isNotBlank(sysUser.getIconUrl())) {
						String iconName = null;
						// 下载用户头像
						try {
							iconName = "icon" + System.currentTimeMillis() + ".png";
							DownloadUtils.downloadHTTPFile(String.valueOf(tempMap.get("avatarUrl")), iconName,
									configParam.getIconURL());
							sysUser.setIconUrl(configParam.getIconRequestURL() + File.separator + iconName);
						} catch (Exception e) {
							logger.error("下载设置头像失败" + weixinRes);
						}
					}
					userMapper.updateUserUUID(sysUser);
					logger.info(" 补充用户:" + sysUser.getId() + "的uuid 或头像成功");
				}
			}
			User user = fillIn(sysUser, sessionId, request);
			user.setPlantType(plantInfoService.getPlantTypeForUserWX(user, 1));
			/**
			 * @author ybj
			 */
			// ====
			user.setRequestType(requestType);
			// ====
			session.setAttribute(String.valueOf(sessionId), user);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(user);
			return msg;
		} else {
			logger.warn(" 用户: " + (userTemp.getUserTel() == null
					? userTemp.getLoginId() == null ? userTemp.getEmail() : userTemp.getLoginId()
					: userTemp.getUserTel()) + " 尝试登录失败,登录方式受限!");
			throw new ServiceException(Msg.NOT_SUPPORT_TYPE);
		}
	}

	/**
	 * @Title: loginOut
	 * @Description:退出
	 * @return: MessageBean
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月10日下午5:08:04
	 */
	@Override
	public MessageBean loginOut(String jsonData, Session session, HttpServletRequest request)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		User user = session.getAttribute(tokenId);
		String uId = user.getId() + "_" + user.getUserName();
		session.remove(tokenId);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(Msg.LOGIN_OUT);
		logger.info(" 用户: " + uId + "_" + user.getUserName() + " 退出成功,IP:" + Util.getIpByRequest(request) + "!");
		return msg;
	}

	@Transactional
	@Override
	public User fillIn(SysUser u, Long sessionId, HttpServletRequest request)
			throws SessionException, SessionTimeoutException, ServiceException {
		User user = new User();
		String userType = u.getUserType();
		if ("0".equals(userType)) {
			user.setId(u.getId() + "");
			user.setUserName(u.getUserName());
			user.setRoleId(u.getUserRole().getId() + "");
			// user.setRights(ServiceUtil.parseRights(u.getRightList()));
			user.setTokenId(sessionId + "");
			user.setEmail(u.getEmail());
			user.setGender(u.getGender());
			user.setIconUrl(u.getIconUrl());
			user.setUserTel(u.getUserTel() + "");
			user.setLoginId(u.getLoginId());
			user.setUserOrg(u.getUserOrg());
			user.setUserType(userType);
			List<Integer> plantList = plantInfoService.getPlantsForUser(user, 1);

			sortType(plantList, user);
		} else if ("1".equals(userType)) {
			user.setId(u.getId() + "");
			user.setUserName(u.getUserName());
			user.setRoleId(u.getUserRole().getId() + "");
			// user.setRights(ServiceUtil.parseRights(u.getRightList()));
			user.setUserTel(u.getUserTel());
			user.setLoginId(u.getLoginId());
			user.setUserOrg(u.getUserOrg());
			user.setUserType(userType);
			user.setTokenId(sessionId + "");
			user.setIconUrl(u.getIconUrl());
			List<Integer> plantList = plantInfoService.getPlantsForUser(user, 1);

			sortType(plantList, user);
		}
		// 更新用户最后登录时间和IP
		SysUser changeUser = new SysUser();
		changeUser.setId(u.getId());
		changeUser.setLoginIp(Util.getIpByRequest(request));
		changeUser.setLastLoginTime(System.currentTimeMillis());
		// 更改登录时间
		userMapper.updateLoginInfo(changeUser);
		return user;
	}

	@Override
	public MessageBean push(Map<String, Object> map, Session session, long sessionId, HttpServletRequest request)
			throws ServiceException, SessionException {
		MessageBean msg = new MessageBean();
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = null;
		try {
			u = session.getAttribute(tokenId);
			if (u != null) {
				List<SysUser> user = userMapper.getUserByLoginId(u.getLoginId());
				u = fillIn(user.get(0), sessionId, request);
				u.setPlantType(plantInfoService.getPlantTypeForUser(u, 1, 1));
				u.setPlantTypeList(plantInfoService.getPlantTypeForUser(u, 1, 0));
				session.setAttribute(String.valueOf(sessionId), u);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(u);
			} else {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.LOGIN_FAILD);
			}
		} catch (SessionException e) {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.LOGIN_FAILD);
		} catch (SessionTimeoutException e) {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.LOGIN_FAILD);
		}
		return msg;
	}

	@Override
	public MessageBean getAddrLocation(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, String> map = Util.parseURLCode(jsonData);
		String addrName = map.get("addrName");
		if (Util.isNotBlank(addrName)) {
			SysAddressLocation addr = addressLocationMappr.getLocationByName(addrName);
			if (addr == null) {
				map.clear();
				Map<String, Object> mapResult = ApiUtil.getLocation(addrName, configParam.getBaiduAppAk());
				// {status=0, message=转换成功, locations=[{lng=102.885993, lat=30.355998}]}
				// {status=0, result={location={lng=104.11412925347894, lat=37.5503394745908},
				// precise=0, confidence=0, level=国家}}
				if ("0".equals(String.valueOf(mapResult.get("status")))) {
					Map<String, Object> resultMap = (Map<String, Object>) mapResult.get("result");
					Map<String, Object> location = (Map<String, Object>) resultMap.get("location");
					map.put("addrName", addrName);
					String lat = String.valueOf(location.get("lat"));
					String lng = String.valueOf(location.get("lng"));
					resultMap = ApiUtil.locationBDToTX(lat, lng, configParam.getTxApiKey());
					List<Map<String, Object>> locations = (List<Map<String, Object>>) resultMap.get("locations");
					lat = String.valueOf(locations.get(0).get("lat"));
					lng = String.valueOf(locations.get(0).get("lng"));
					map.put("lat", lat);
					map.put("lng", lng);
					addr = new SysAddressLocation();
					addr.setAreaName(addrName);
					addr.setLocationX(lat);
					addr.setLocationY(lng);
					int status = addressLocationMappr.saveAddrLocation(addr);
					msg.setBody(map);
					msg.setCode(Header.STATUS_SUCESS);
				}
			} else {
				map.clear();
				map.put("addrName", addrName);
				map.put("lat", addr.getLocationX());
				map.put("lng", addr.getLocationY());
				msg.setBody(map);
				msg.setCode(Header.STATUS_SUCESS);

			}

		} else {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.PARAM_NOT_COMPLETE);
		}
		return msg;
	}

	/**
	 * 没用了好像
	 */
	@Override
	public MessageBean getWxLogin(String jsonData, Session session, Long sessionId, HttpServletRequest request)
			throws SessionException, SessionTimeoutException, ServiceException, IOException {
		MessageBean msg = new MessageBean();
		Map<String, String> map = Util.parseURLCode(jsonData);
		// 微信扫码的Code
		String code = map.get("code");
		if (Util.isNotBlank(code)) {
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("appid", configParam.getAppid());
			paraMap.put("secret", configParam.getSecret());
			paraMap.put("code", code);
			paraMap.put("grant_type", "authorization_code");
			// 通过code获得微信返回的accessToken
			Map<String, String> weixinRs = WeixinOper.visit("https://api.weixin.qq.com/sns/oauth2/access_token",
					paraMap);
			String accessToken = String.valueOf(weixinRs.get("access_token"));
			String openid = weixinRs.get("openid");
			if (StringUtils.isNotBlank(accessToken)) {
				paraMap.clear();
				paraMap.put("access_token", accessToken);
				paraMap.put("openid", openid);
				// 校验accessToken是否可用
				Map<String, String> weixinRsAuth = WeixinOper.visit("https://api.weixin.qq.com/sns/auth", paraMap);
				String errcode = String.valueOf(weixinRsAuth.get("errcode"));
				boolean b = false;
				if ("0".equals(errcode)) {
					b = true;
				} else {
					paraMap.clear();
					paraMap.put("appid", configParam.getAppid());
					paraMap.put("grant_type", "refresh_token");
					paraMap.put("refresh_token", weixinRs.get("refresh_token"));
					Map<String, String> weixinRsRefush = WeixinOper
							.visit("https://api.weixin.qq.com/sns/oauth2/refresh_token", paraMap);
					String refushToken = weixinRsRefush.get("access_token");
					if (StringUtils.isNotBlank(refushToken)) {
						b = true;
					}
				}
				if (b) {
					paraMap.clear();
					paraMap.put("access_token", accessToken);
					paraMap.put("openid", openid);
					paraMap.put("lang", "zh_CN");
					Map<String, String> weixinRs3 = WeixinOper.visit("https://api.weixin.qq.com/sns/userinfo", paraMap);
					String uuid = String.valueOf(weixinRs3.get("unionid"));
					if (Util.isNotBlank(uuid)) {
						SysUser sysUser = userMapper.getUserByUUID(uuid);
						if ("0".equals(sysUser.getUserType()) && ("0".equals(sysUser.getUserStatus())
								|| "0".equals(sysUser.getUserRole().getRoleStatus())
								|| "0".equals(sysUser.getUserOrg().getOrgStatus()))) {
							logger.warn(" 用户: " + (sysUser.getUserTel() == null
									? sysUser.getLoginId() == null ? sysUser.getEmail() : sysUser.getLoginId()
									: sysUser.getUserTel()) + " 尝试登录失败,账户已被冻结!");
							throw new ServiceException(Msg.LOGIN_UNABLE);

						} else if ("0".equals(sysUser.getUserType()) && ("2".equals(sysUser.getUserStatus())
								|| "2".equals(sysUser.getUserRole().getRoleStatus())
								|| "2".equals(sysUser.getUserOrg().getOrgStatus()))) {
							logger.warn(" 用户: " + (sysUser.getUserTel() == null
									? sysUser.getLoginId() == null ? sysUser.getEmail() : sysUser.getLoginId()
									: sysUser.getUserTel()) + " 尝试登录失败,账户还未激活!");
							throw new ServiceException(Msg.LOGIN_UNACTIVE);
							// 业主账户 不校验组织是否被禁用
						} else if ("1".equals(sysUser.getUserType()) && ("0".equals(sysUser.getUserStatus())
								|| "0".equals(sysUser.getUserRole().getRoleStatus()))) {
							logger.warn(" 用户: " + (sysUser.getUserTel() == null
									? sysUser.getLoginId() == null ? sysUser.getEmail() : sysUser.getLoginId()
									: sysUser.getUserTel()) + " 尝试登录失败,账户已被冻结!");
							throw new ServiceException(Msg.LOGIN_UNABLE);
						} else if ("1".equals(sysUser.getUserType()) && ("2".equals(sysUser.getUserStatus())
								|| "2".equals(sysUser.getUserRole().getRoleStatus()))) {
							logger.warn(" 用户: " + (sysUser.getUserTel() == null
									? sysUser.getLoginId() == null ? sysUser.getEmail() : sysUser.getLoginId()
									: sysUser.getUserTel()) + " 尝试登录失败,账户还未激活!");
							throw new ServiceException(Msg.LOGIN_UNACTIVE);
						} else if ("1".equals(sysUser.getUserStatus())) {
							// 将数据库记录装载到session需要的序列化对象中取
							User user = fillIn(sysUser, sessionId, request);
							user.setPlantType(plantInfoService.getPlantTypeForUser(user, 1, 1));
							user.setPlantTypeList(plantInfoService.getPlantTypeForUser(user, 1, 0));
							session.setAttribute(String.valueOf(sessionId), user);
							msg.setCode(Header.STATUS_SUCESS);
							msg.setBody(user);
							logger.info(
									" 用户: " + user.getUserTel() + " 扫码登录成功,IP:" + Util.getIpByRequest(request) + "!");
						}
					} else {
						msg.setCode(Header.STATUS_FAILED);
						msg.setMsg(Msg.WEIXIN_CODE_ERROR);
					}
				} else {
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg(Msg.WEIXIN_CODE_ERROR);
				}
			} else {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.WEIXIN_CODE_ERROR);
			}
		} else {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.PARAM_NOT_COMPLETE);
		}
		return msg;
	}

	/**
	 * 
	 * @author ybj
	 * @date 2018年9月6日下午7:27:33
	 * @Description -_-电站列表数量分类
	 */
	public void sortType(List<Integer> plantList, User user) {
		// ===电站数量分类 ybj
		// 光伏电站数量
		int pv = 0;
		// 光伏电站id 只有一个电站时 才设置 其它情况设置为 -1 下同
		int pvPlantId = -1;
		// 储能电站数量
		int es = 0;
		int esPlantId = -1;
		// 能效电站数量
		int ee = 0;
		int eePlantId = -1;
		if (Util.isNotBlank(plantList)) {
			for (Integer plantId : plantList) {
				String plantType = SystemCache.plantTypeList.get(String.valueOf(plantId));
				if (plantType != null) {
					switch (Integer.parseInt(plantType)) {
					case PTAC.PHOTOVOLTAIC:
						pv++;
						pvPlantId = plantId;
						break;
					case PTAC.STOREDENERGY:
						es++;
						esPlantId = plantId;
						break;
					case PTAC.ENERGYEFFICPLANT:
						ee++;
						eePlantId = plantId;
						break;
					default:
						break;
					}
				}
			}
		}
		user.setPlantNum(pv + "," + es + "," + ee);
		if (pv != 1) {
			pvPlantId = -1;
		}
		if (es != 1) {
			esPlantId = -1;
		}
		if (ee != 1) {
			eePlantId = -1;
		}
		user.setPlantId(pvPlantId + "," + esPlantId + "," + eePlantId);
		// ===
	}

	public static void main(String[] args) throws Exception {
		// wx207e73ae2c9d9ab3
		/*
		 * Map<String,String> paraMap = new HashMap<>();
		 * paraMap.put("appid","wx99ad9f51c7a56983");
		 * paraMap.put("secret","4ad8291151159382510ab05840834101");
		 * paraMap.put("code","061M9Msx16IzJg0Uhgtx1zZ0tx1M9MsU");
		 * paraMap.put("grant_type","authorization_code"); Map<String,String> weixinRs =
		 * WeixinOper.visit("https://api.weixin.qq.com/sns/oauth2/access_token",paraMap)
		 * ; System.out.println(weixinRs);
		 */

		/*
		 * paraMap.clear(); paraMap.put("access_token", weixinRs.get("access_token"));
		 * paraMap.put("openid",weixinRs.get("openid") ); Map<String,String> weixinRs2 =
		 * WeixinOper.visit("https://api.weixin.qq.com/sns/auth",paraMap);
		 * System.out.println(weixinRs2);
		 * 
		 * paraMap.clear(); paraMap.put("access_token", weixinRs.get("access_token"));
		 * paraMap.put("openid",weixinRs.get("openid") ); paraMap.put("lang","zh_CN");
		 * Map<String,String> weixinRs3 =
		 * WeixinOper.visit("https://api.weixin.qq.com/sns/userinfo",paraMap);
		 * System.out.println(weixinRs3);
		 */
		// 寇娇 o-Bm21XmtttRhTgjpdmM3-EfOT2Q uuid
		/*
		 * paraMap.put("appid","wx207e73ae2c9d9ab3");
		 * paraMap.put("secret","bb75f98ca7281a8bea441fcafd90cca3");
		 * paraMap.put("js_code","013IJiZM1rP3s51ZKIZM1XVEZM1IJiZa");
		 * paraMap.put("grant_type","authorization_code"); Map<String,String> weixinRs =
		 * WeixinOper.visit("https://api.weixin.qq.com/sns/oauth2/access_token",paraMap)
		 * ; System.out.println(weixinRs); String encryptedData=
		 * "4NV4S+dVf6i9HkuNJmj8fluuA5RJ7m5ti77pPNGkazw5cK6ljYJR+H8wLrnIiC4gEDzWLVzMhDfUMQlvunegmtgxjrZydZW3H2KrAW7mD1+6YjmV14Zq059/8I8hIw3IvZLETDqdPO6uyvdUJ658vXEGK2XICMxgCNVKiIm95/mqYvAoWzntPfDhcqrHvaxJKKWrpOjwE7apUPGKIMo0PQGC0+7sq7dFZDwj3U46SpDLjhiHYVjiX/zqTSP+/jzawJlbubp8SQr69nkARP+JLppBosBFYkPZwYt6vOorjtbZq6Om0BdWYKhBMfDn3p/0RwfP6RqbxU/f6ldAcqisxz96Rif6RMpBYRzZfi9q1Ngz66V6HPZ2Pm4kEo6cVSXYtjpH0sh9LElYxBvNxIOPK1T8u0oag3vYzZ1RIUMfe7fqc9h0P405KTNb6V3MtMC7tjpkr86fEmH1U3ILbg8K5OkwWBCQZW3PFURv087pPThYFYgSnKC90nXTk2so1PY+IZpXj1PL3XgfhuykVLpqpw==";
		 * String iv="icvo88vXZWgAmnhJMeUbCA=="; String
		 * string=AesCbcUtil.decrypt(encryptedData, weixinRs.get("session_key"), iv,
		 * "UTF-8"); Map<String, Object>resultMap=Util.parseURL(string);
		 * System.out.println(string); System.out.println(resultMap.get("unionId"));
		 */
	}
}
