package com.synpower.util;

import java.util.Map;

public class WxTemplate {

	private String template_id;

	private String touser;

	private Map<String, TemplateData> data;

	private String form_id;

	public String getTemplate_id() {

		return template_id;

	}

	public void setTemplate_id(String template_id) {

		this.template_id = template_id;

	}

	public String getTouser() {

		return touser;

	}

	public void setTouser(String touser) {

		this.touser = touser;

	}

	public Map<String, TemplateData> getData() {

		return data;

	}

	public void setData(Map<String, TemplateData> data) {

		this.data = data;

	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}
}
