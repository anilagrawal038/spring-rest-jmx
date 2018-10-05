package com.san.mbean.impl;

import com.san.mbean.bean.ClientSideMXBean;

public class ClientSideBeanImpl implements ClientSideMXBean {

	@Override
	public String getInfo() {
		return "This bean is not registerd on application/web server";
	}

}
