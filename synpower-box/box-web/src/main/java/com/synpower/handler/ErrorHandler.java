package com.synpower.handler;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.synpower.lang.EnergyMonitorException;
import com.synpower.lang.NotRegisterException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.SmsException;
import com.synpower.lang.WeiXinExption;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.util.MessageBeanUtil;

/**
 * 异常处理
 *
 * @author PC043
 */
public class ErrorHandler extends BaseHandler {
	private Logger logger = Logger.getLogger(ErrorHandler.class);

	@ExceptionHandler(ServiceException.class)
	public MessageBean exceptionServiceError(ServiceException exception) {
		MessageBean bean = new MessageBean();
		bean.setCode(Header.STATUS_FAILED);
		bean.setMsg(exception.getMessage());
		logger.error(exception.getMessage(), exception);
		return bean;
	}

	@ExceptionHandler(NullPointerException.class)
	public MessageBean exceptionServiceError(NullPointerException exception) {
		MessageBean bean = new MessageBean();
		bean.setCode(Header.STATUS_NULL_ERROR);
		bean.setMsg(Msg.TYPE_UNKNOWN_ERROR);
		logger.error(exception.getMessage(), exception);
		return bean;
	}

	@ExceptionHandler(SessionTimeoutException.class)
	public MessageBean exceptionServiceError(SessionTimeoutException exception) {
		MessageBean bean = new MessageBean();
		bean.setCode(Header.STATUS_LOGIN_TIMEOUT_ERROR);
		bean.setMsg(exception.getMessage());
		logger.error(exception.getMessage(), exception);
		return bean;
	}

	@ExceptionHandler(SmsException.class)
	public MessageBean exceptionServiceError(SmsException exception) {
		MessageBean bean = new MessageBean();
		bean.setCode(Header.STATUS_FAILED);
		bean.setMsg(Msg.TYPE_SMS_ERROR + exception.getMessage());
		logger.error(exception.getMessage(), exception);
		return bean;
	}

	@ExceptionHandler(NotRegisterException.class)
	public MessageBean exceptionServiceError(NotRegisterException exception) {
		MessageBean bean = new MessageBean();
		bean.setCode(Header.STATUS_LOGIN_NO_REGITSER);
		bean.setMsg(exception.getMessage());
		logger.error(exception.getMessage(), exception);
		return bean;
	}

	@ExceptionHandler(WeiXinExption.class)
	public MessageBean exceptionServiceError(WeiXinExption exception) {
		MessageBean bean = new MessageBean();
		bean.setCode(Header.STATUS_WEIXIN_ERROR);
		bean.setMsg(Msg.TYPE_WEIXIN_ERROR);
		logger.error(exception.getMessage(), exception);
		return bean;
	}

	@ExceptionHandler(EnergyMonitorException.class)
	public MessageBean exceptionServiceError(EnergyMonitorException exception) {
		logger.error(exception.getMessage(), exception);
		return MessageBeanUtil.getErrorMB(Msg.DATA_ERROR);
	}

	@ExceptionHandler(Throwable.class)
	public MessageBean throwableError(Throwable throwable) {
		MessageBean bean = new MessageBean();
		bean.setCode(Header.STATUS_FATAL_ERROR);
		bean.setMsg(Msg.TYPE_UNKNOWN_ERROR);
		logger.error(throwable.getMessage(), throwable);
		return bean;
	}
}
