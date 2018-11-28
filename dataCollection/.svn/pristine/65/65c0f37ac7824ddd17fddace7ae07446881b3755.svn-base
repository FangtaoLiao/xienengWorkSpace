package com.synpowertech.dataCollectionJar.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: AddressComparator
 * @Description: TODO 为满足自制数采下发点表地址从小到大的排序要求，对List进行排序
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年6月4日下午3:57:22   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class AddressComparator implements Comparator<String>{

	@Override
	public int compare(String o1, String o2) {
		o1 = o1.substring(0,4);
		o2 = o2.substring(0,4);
		long addr1 = RadixProcessUtil.HexStr2Long(o1, false);
		long addr2 = RadixProcessUtil.HexStr2Long(o2, false);
		//从小到大排列
		if (addr1 > addr2) {
			return 1;
		} else if (addr1 < addr2) {
			return -1;
		} else {
			return 0;
		}
	}

}






