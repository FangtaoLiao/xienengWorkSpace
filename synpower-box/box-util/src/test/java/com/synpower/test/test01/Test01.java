package com.synpower.test.test01;

import com.synpower.lang.ServiceException;
import com.synpower.util.Util;
import org.junit.Test;

public class Test01 {

	@Test
	public void test01() throws ServiceException {
		//long curr = Util.strFormate("2018-10", "yyyy-MM");
		//Calendar calendar = Calendar.getInstance();
		//calendar.setTimeInMillis(curr);
		//System.out.println(curr);
		//System.out.println(calendar.getTimeInMillis());
		//calendar.add(Calendar.MONTH, 1);
		//long next = calendar.getTimeInMillis();
		//System.out.println(calendar.getTimeInMillis());
		System.out.println();
	}

	@Test
	public void test02() {
		Double[] d = new Double[] { 0d, 0d, 0d, 0d, 5d, 0d, 0d, 0d, 0d, 0d };
		double maxDoubleFromArray = Util.getMaxDoubleFromArray(d);
		System.out.println(maxDoubleFromArray);
	}
	@Test
	public void test03() {
		String str = "2018-08-30";
		System.out.println(str.substring(0,str.lastIndexOf("-")));
	}



}
