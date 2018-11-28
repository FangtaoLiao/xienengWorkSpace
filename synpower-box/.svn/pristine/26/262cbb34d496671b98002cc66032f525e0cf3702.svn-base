package com.synpower.web;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.filter.config.ConfigTools;
import com.synpower.bean.CollDevice;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.PowerPriceMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.SmsException;
import com.synpower.mobile.MobileUtil;
import com.synpower.msg.MessageBean;
import com.synpower.service.MonitorService;
import com.synpower.service.PlantInfoService;
import com.synpower.service.StatisticalGainService;
import com.synpower.service.WXmonitorService;
import com.synpower.util.ApiUtil;
import com.synpower.util.CacheUtil;
import com.synpower.util.SystemCache;

/*****************************************************************************
 * @Package: com.synpower.web
 * ClassName: OrgController
 * @Description: 组织机构
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年10月25日上午10:33:03   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
@RestController
@RequestMapping(value="/screen")
public class Sp12Controller extends ErrorHandler{
	@Autowired
	private MobileUtil mobileUtil;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private SysOrgMapper orgMapper;
    @Autowired
    private PlantInfoMapper plantMapper;
    @Autowired
    private StatisticalGainService statisticalGainService;
    @Autowired
    private PowerPriceMapper priceMapper;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private WXmonitorService wxmonitorService;
    @Autowired
    private PlantInfoService plantInfoService;	
	 
	 /*public static void main(String[] args) throws Exception { 
		 String password = "Synpower-2017";
         System.out.println("密码[ "+password+" ]的加密信息如下：\n");
         String [] keyPair = ConfigTools.genKeyPair(512);
         //私钥
         String privateKey = keyPair[0];
         //公钥
         String publicKey = keyPair[1];
         //用私钥加密后的密文
         password = ConfigTools.encrypt(privateKey, password);
         System.out.println("privateKey:"+privateKey);   
         System.out.println("publicKey:"+publicKey);    
         System.out.println("password:"+password);
		 

	 }*/
	
    public static void main(String[] args) throws Exception {
		 /*String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOvOjn3yxXewfarGBIo4hcVuWFqMz2PvuhNkJgWsI+BZ1jq3umgeZzJUscnhy+mzEXf0rsEs7KguC4JJxDQzAU0CAwEAAQ==";
		 String pass = "xXySbtko/UbA2Du+rR0usX4iUJNdCEXhA1qTdrM6cCKEUMS8unQV8zpktunxYW/0s8D2j0wfgDd6QjcIMD2Xgw==";
		 String decrypt = ConfigTools.decrypt(publicKey, pass);
		 System.out.println(decrypt);*/
		 /*TokenPojo tp = new TokenPojo();
		 System.out.println(tp.getAccessToken());
		 Map<String,Object> map = new HashMap<>();
			map.put("134046139", 1);
			map.put("134046132", 2);
			String valueOf = String.valueOf(map);
			System.out.println(valueOf);*/
		// int A[] = { 5, 2, 9, 4, 7, 6, 1, 3, 8,8 };// 从小到大二分插入排序
//    	CollDevice c4 = new CollDevice();c4.setPlantId(5);
//		 CollDevice c1 = new CollDevice();c1.setPlantId(1);
//		 CollDevice c2 = new CollDevice();c2.setPlantId(2);
//		 CollDevice c3 = new CollDevice();c3.setPlantId(2);
//		 List<CollDevice> l = new ArrayList<>();
//		 l.add(c1);
//		 l.add(c2);
//		 l.add(c3);
//		 l.add(c4);
//	     int n = l.size();
//	     InsertionSortDichotomy2(l, n);
//	     System.out.println("二分插入排序结果：");
//	     for (int i = 0; i < n; i++)
//	     {
//	    	 System.out.println(l.get(i).getPlantId());
//	     }
//	     System.out.println("\n");


	     
	     String password = "1234";
         System.out.println("密码[ "+password+" ]的加密信息如下：\n");
         String [] keyPair = ConfigTools.genKeyPair(512);
         //私钥
         String privateKey = keyPair[0];
         //公钥
         String publicKey = keyPair[1];
         //用私钥加密后的密文
         password = ConfigTools.encrypt(privateKey, password);
         System.out.println("privateKey:"+privateKey);   
         System.out.println("publicKey:"+publicKey);    
         System.out.println("password:"+password);
			
	 }
    
    static <T> void InsertionSortDichotomy2(List<T> l1, int n)
	 {
	     for (int i = 1; i < n; i++)
	     {
	         int get = ((CollDevice) l1.get(i)).getPlantId();                    // 右手抓到一张扑克牌
	         int left = 0;                    // 拿在左手上的牌总是排序好的，所以可以用二分法
	         int right = i - 1;                // 手牌左右边界进行初始化
	         while (left <= right)            // 采用二分法定位新牌的位置
	         {
	             int mid = (left + right) / 2;
	             if (((CollDevice) l1.get(mid)).getPlantId() > get)
	                 right = mid - 1;
	             else
	                 left = mid + 1;
	         }
	         for (int j = i - 1; j >= left; j--)    // 将欲插入新牌位置右边的牌整体向右移动一个单位
	         {
	             Integer plantId1 = ((CollDevice) l1.get(j+1)).getPlantId(); 
	             Integer plantId2 = ((CollDevice) l1.get(j)).getPlantId();
	             plantId1 = plantId2;
	         }
	         Integer plantId = ((CollDevice) l1.get(left)).getPlantId();
	         plantId = get;                    // 将抓到的牌插入手牌
	     }
	 }
	 
	/* static void InsertionSortDichotomy(int A[], int n)
	 {
	     for (int i = 1; i < n; i++)
	     {
	         int get = A[i];                    // 右手抓到一张扑克牌
	         int left = 0;                    // 拿在左手上的牌总是排序好的，所以可以用二分法
	         int right = i - 1;                // 手牌左右边界进行初始化
	         while (left <= right)            // 采用二分法定位新牌的位置
	         {
	             int mid = (left + right) / 2;
	             if (A[mid] > get)
	                 right = mid - 1;
	             else
	                 left = mid + 1;
	         }
	         for (int j = i - 1; j >= left; j--)    // 将欲插入新牌位置右边的牌整体向右移动一个单位
	         {
	             A[j + 1] = A[j];
	         }
	         A[left] = get;                    // 将抓到的牌插入手牌
	     }
	 }*/

	 
	
}