package com.synpower.web;

import javax.xml.ws.RequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synpower.handler.ErrorHandler;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.service.CacheClearService;
import com.synpower.util.MessageBeanUtil;
import com.synpower.util.ServiceUtil;

/**
 * 缓存清理类
 * 
 * @author ybj
 * @Description
 * @Date 2018年8月8日
 */
@RestController
@RequestMapping("/cache")
public class CacheClearController extends ErrorHandler {

	@Autowired
	private CacheClearService cacheClearService;

	@RequestMapping("/getPlantStatus")
	public int getPlantStatus(String pId) {
		return ServiceUtil.getPlantStatus(pId);
	}

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月27日下午6:20:41
	 * @Description -_- 清理采集服务器和本服务器的所有缓存
	 * @param json
	 * @return
	 */
	@RequestMapping("/clearAll")
	public MessageBean clearAll(@RequestBody String json) {

		boolean flag = cacheClearService.clearAll();

		if (flag) {
			return new MessageBean(Header.STATUS_SUCESS, Msg.CACHE_CLEAR_SUCESS);
		} else {
			return new MessageBean(Header.STATUS_SUCESS, Msg.CACHE_CLEAR_FAILED);
		}
	}

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月27日下午6:20:46
	 * @Description -_-更新光伏电站历史收益和历史用电的缓存<br>
	 *              脏数据如果没当天处理第二天处理后 但是缓存中还是错误的数据 必须重启服务器<br>
	 *              之所以必须重启服务器是应为该缓存一天只更新一次<br>
	 *              但是有了此接口可直接更新缓存 不需要重启服务器
	 * @param json
	 * @return
	 */
	@RequestMapping("/clearPVPlantHistory")
	public MessageBean clearPVPlantHistory(@RequestBody String json) {
		cacheClearService.clearPVPlantHistory();
		return MessageBeanUtil.getOkMB(Msg.CACHE_CLEAR_SUCESS);
	}

}
