package com.synpower.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.service.Sp10Service;

@RestController
@RequestMapping(value="/Sp10User")
public class Sp10Controller extends ErrorHandler{
	@Autowired
	private Sp10Service service;
}
