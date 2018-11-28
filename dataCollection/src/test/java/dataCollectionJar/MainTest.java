package dataCollectionJar;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synpowertech.dataCollectionJar.dao.DataYxMapper;
import com.synpowertech.dataCollectionJar.dao.DeviceMapper;
import com.synpowertech.dataCollectionJar.domain.DataYx;
import com.synpowertech.dataCollectionJar.domain.Device;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil4signal;
import com.synpowertech.dataCollectionJar.initialization.MqttMessagePublisher;
import com.synpowertech.dataCollectionJar.initialization.RabbitMqProducer;
import com.synpowertech.dataCollectionJar.service.IYkYtService;
@Service
public class MainTest {

	/*@Autowired
	static
	JedisUtil jedisUtil;*/
	
	/**
	 * 使用的时自动注入,这里会报空指针异常
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	static
	ApplicationContext applicationContext;
	
/*	@Autowired
	private static JedisUtil  jedisUtil;*/
		
	public static void main(String[] args) throws IOException {
		
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		//测试rmi
		IYkYtService ykYtService = (IYkYtService) applicationContext.getBean("rmiUserService");
		try {
			System.out.println(ykYtService.sayHello("rmi调用成功"));
			
			Map<Integer, Map<String, Integer>> dataModelSetMap = new HashMap<Integer, Map<String,Integer>>();
			HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
			HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
			hashMap.put("1", 8);
			hashMap.put("2", 8);
			hashMap2.put("1", 8);
			dataModelSetMap.put(1, hashMap);
			dataModelSetMap.put(2, hashMap2);
			boolean dataModelSet = ykYtService.dataModelSet("003b00453237510d30313734", dataModelSetMap);
			System.out.println("设置结果：" + dataModelSet);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println("正常启动运行");
//		DataYxMapper dataYxMapper = applicationContext.getBean(DataYxMapper.class);
//		System.out.println(dataYxMapper);
//		System.out.println(dataYxMapper.insert(new DataYx(2, 5, System.currentTimeMillis(),null, "1","0")));

//		testAxtiveSub();//测试动态订阅
		
		/*DeviceMapper deviceMapper = applicationContext.getBean(DeviceMapper.class);
		System.out.println(deviceMapper);
		System.out.println(deviceMapper.selectByPrimaryKey(1).getDeviceName());
		testq();
		
		
		List<Device> devices = deviceMapper.selectAll();
		for (Device device : devices) {
			System.out.println(device.getDeviceName());
		}
		
		Float f = 0.001f;
		Float f2 = 0.001f;
		
		float f3 = f * f2 + f;
		System.out.println(f3);
		*/
		
		/*RabbitMqProducer rabbitMqProducer = applicationContext.getBean(RabbitMqProducer.class);*/
		
		/*int i = 0;
        while (i<5) {
        	HashMap<String, Map<String, String>> resultMap = new HashMap<String, Map<String,String>>();
    		//字段结果映射
    		Map<String, String> timeAndSessionMap = new HashMap<String, String>();
    		timeAndSessionMap.put("field", "we");
    		timeAndSessionMap.put("field2", "werw");
    		resultMap.put("dec", timeAndSessionMap);
    		RabbitMqProducer.sendMessage(resultMap);
            System.out.println("发送第"+i+"消息");
            i++;
        }*/
		/*try {
			RabbitMqProducer.sendMessage("rabbit信息发送测试成功");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		//测试rmi
		/*IYkYtService ykYtService = (IYkYtService) applicationContext.getBean("rmiUserService");
		try {
			System.out.println(ykYtService.sayHello("rmi调用成功"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
	
		//测试mqtt客户端
		/*MqttPahoMessageHandler handler = (MqttPahoMessageHandler) applicationContext.getBean("mqttHandler");
		System.out.println(handler);
		System.out.println(handler.getClientId());
		//mqtt工具类发送信息
		MqttMessagePublisher.ykYt("mqtt信息发送成功");*/
		
		/*System.in.read();*/
/*		String sessionId = "3235325345";
		
		String collector_id = "VX817393345";
		Map<String, Map<String, String>> ykYtMap = new HashMap<String, Map<String,String>>();
		Map<String, String> sss = new HashMap<String, String>();
		sss.put("VX817393345_YK_0", "1");
		ykYtMap.put("1", sss);
		
		String map2XmlStr = XmlParseUtil.map2XmlStr(sessionId,collector_id, ykYtMap);
		System.out.println("下发的xml：!" + map2XmlStr);
		*/
		
		//测试回滚
//		txTest();
		
	}
	
	private static void testAxtiveSub() {
		Timer timer1 = new Timer("定时插入设备");
		Timer timer2 = new Timer("定时删除设备");
		DeviceMapper deviceMapper = applicationContext.getBean(DeviceMapper.class);
		Device device = deviceMapper.selectByPrimaryKey(3);
		
		
		timer1.schedule(new TimerTask() {
			int count = 1;
			
			@Override
			public void run() {
				device.setDeviceModelId(3);
				device.setDeviceSn(device.getDeviceSn() + count);
				device.setId(null);
				deviceMapper.insertSelective(device);
				count = count + 1;
				if (count > 5) {
					this.cancel();
				}
				
				
			}
		}, 0, 300000);
		
		timer2.scheduleAtFixedRate(new TimerTask() {
			int count = 5;
			@Override
			public void run() {
				deviceMapper.deleteByPrimaryKey(count + 4);
				count = count - 1;
				if (count < 1) {
					this.cancel();
				}
			}
		}, 1500000, 300000);
		
		Timer timer3 = new Timer("再次增加设备");
		
		timer3.schedule(new TimerTask() {
			int count = 1;
			
			@Override
			public void run() {
				device.setDeviceModelId(3);
				device.setDeviceSn(device.getDeviceSn() + count);
				device.setId(null);
				deviceMapper.insertSelective(device);
				count = count + 1;
				if (count > 5) {
					this.cancel();
				}
			}
		}, 3000000, 300000);

	}
	
	private static void testq() {
		/*JedisUtil  jedisUtil = new JedisUtil();*/
		JedisUtil.setString("qwer", "英雄联盟111");
		System.out.println(JedisUtil.getString("qwer"));
	}
	
	@Transactional
	public static void txTest() {
		DeviceMapper deviceMapper = applicationContext.getBean(DeviceMapper.class);
//		SqlSessionFactory bean = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
//		SqlSession sqlSession = SqlSessionUtils.getSqlSession((SqlSessionFactory) bean);
//		SqlSession sqlSession = bean.openSession(false);
		Device device = new Device();
		device.setConnTime(543634264L);
		device.setConnProtocol(2);
		device.setDataMode(2);
		device.setDeviceModelId(2);
		device.setDeviceType(1);
	/*	try {
			sqlSession.insert("com.synpowertech.dataCollectionJar.dao.DeviceMapper.insertSelective", device);
			int i = 1/0;
		sqlSession.commit();
			//		deviceMapper.insertSelective(device);
//			throw new RuntimeException("测试异常");
		} catch (Exception e) {
			// handle exception
			sqlSession.rollback();
		}finally {
			//handle finally clause
			sqlSession.close();
		}*/
		
		deviceMapper.insertSelective(device);
		
		int i = 1/0;
		
	}
}


