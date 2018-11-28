package dataCollectionJar;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.synpowertech.dataCollectionJar.dao.CollJsonModelDetailExtendMapper;
import com.synpowertech.dataCollectionJar.dao.CollModelDetailMqttMapper;
import com.synpowertech.dataCollectionJar.dao.CollSignalLabelMapper;
import com.synpowertech.dataCollectionJar.dao.CollYkytExpandMapper;
import com.synpowertech.dataCollectionJar.dao.CollYxExpandMapper;
import com.synpowertech.dataCollectionJar.dao.CollectorMessageStructureMapper;
import com.synpowertech.dataCollectionJar.dao.DataYxMapper;
import com.synpowertech.dataCollectionJar.dao.DeviceMapper;
import com.synpowertech.dataCollectionJar.dao.FieldSignalGuidMappingMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw10ktlAMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw12ktlAMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw20ktlAMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw33ktlAMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw36ktlAMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw36ktlBMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw60ktlAMapper;
import com.synpowertech.dataCollectionJar.domain.CollJsonModelDetailExtend;
import com.synpowertech.dataCollectionJar.domain.CollModelDetailMqtt;
import com.synpowertech.dataCollectionJar.domain.CollSignalLabel;
import com.synpowertech.dataCollectionJar.domain.CollYkytExpand;
import com.synpowertech.dataCollectionJar.domain.CollYxExpand;
import com.synpowertech.dataCollectionJar.domain.CollectorMessageStructure;
import com.synpowertech.dataCollectionJar.domain.Device;
import com.synpowertech.dataCollectionJar.domain.FieldSignalGuidMapping;
import com.synpowertech.dataCollectionJar.domain.PointHw10ktlA;
import com.synpowertech.dataCollectionJar.domain.PointHw12ktlA;
import com.synpowertech.dataCollectionJar.domain.PointHw20ktlA;
import com.synpowertech.dataCollectionJar.domain.PointHw33ktlA;
import com.synpowertech.dataCollectionJar.domain.PointHw36ktlA;
import com.synpowertech.dataCollectionJar.domain.PointHw36ktlB;
import com.synpowertech.dataCollectionJar.domain.PointHw60ktlA;
import com.synpowertech.dataCollectionJar.utils.TimeUtil4Xml;
import com.synpowertech.dataCollectionJar.utils.XmlParseUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SimulationDataInput {

	@Autowired
	CollModelDetailMqttMapper collModelDetailMqttMapper;
	@Autowired
	CollYxExpandMapper collYxExpandMapper;
	@Autowired
	CollSignalLabelMapper collSignalLabelMapper;
	@Autowired
	CollYkytExpandMapper collYkytExpandMapper;
	@Autowired
	FieldSignalGuidMappingMapper fieldSignalGuidMappingMapper;
	@Autowired
	PointHw36ktlAMapper pointHw36ktlAMapper;
	@Autowired
	PointHw12ktlAMapper pointHw12ktlAMapper;
	@Autowired
	PointHw36ktlBMapper pointHw36ktlBMapper;
	@Autowired
	PointHw60ktlAMapper pointHw60ktlAMapper;
	@Autowired
	PointHw20ktlAMapper pointHw20ktlAMapper;
	@Autowired
	PointHw33ktlAMapper pointHw33ktlAMapper;
	@Autowired
	PointHw10ktlAMapper pointHw10ktlAMapper;
	
	@Autowired
	DeviceMapper deviceMapper;

	@Autowired
	DataYxMapper dataYxMapper;

	@Autowired
	CollectorMessageStructureMapper collMapper;
	
	@Autowired
	CollJsonModelDetailExtendMapper collJsonMDEMapper;

	String fileName = "C:\\Users\\SP0011\\Desktop\\point.xls";// Excel文件所在路径


	@Test
	public void test() throws InterruptedException {
		//collModelDetailMqttMapper();
		//collYxExpandMapper();
		//collYkytExpandMapper();
		//fieldSignalGuidMappingMapper();
	}

	
	/**
	 * @Title: test1
	 * @Description: 导入设备关联关系表
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午12:53:23
	 */
	@Test
	public void collectorMessageStructure() {

		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\第二版本初始化文件\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\生产库9个模拟电站\\mqtt模拟数据36KTL初始化文件-测试删除.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\mqtt模拟数据12KTL初始化文件.xls"; // Excel文件所在路径
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(0); // 从工作区中取得页（Sheet）

			for (int i = 0; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容

				if (i < 1) {
					continue;
				}

			    Integer collectorId = null;
			    
			    String collectorSn = null;

			 	Integer dataModel = null;

			    Integer deviceId = null;

			    String tableName = null;

			    Integer parseOrder = null;

			    String size = null;

			    String valid = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());
					switch (j) {
					case 0:
						collectorId = Integer.parseInt(cell.getContents());
						break;
					case 1:
						collectorSn = cell.getContents();
						break;
					case 2:
						dataModel = Integer.parseInt(cell.getContents());
						break;
					case 3:
						deviceId = Integer.parseInt(cell.getContents());
						break;
					case 4:
						tableName = cell.getContents();
						break;
					case 5:
						parseOrder = Integer.parseInt(cell.getContents());
						break;
					case 6:
						size = cell.getContents();
						break;
					case 7:
						valid = cell.getContents();
						break;
					default:
						break;
					}
				}
				System.out.println();

				CollectorMessageStructure collectorMessageStructure = new CollectorMessageStructure(collectorId, collectorSn, dataModel, deviceId, tableName, parseOrder, size, valid);
//				collMapper.insertSelective(collectorMessageStructure);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: test2
	 * @Description: mqtt对照表,一般先注释插入语句，确保无错再放开插入
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午1:31:34
	 */
	@Test
	public void collModelDetailMqttMapper() {
		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\第二版本初始化文件\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\mqtt模拟数据12KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-60ktl-a\\SUN2000 V300R001 MODBUS接口定义描述---整理.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\04 特隆美\\20180327模拟文件\\mqtt模拟数据特隆美电表初始化文件.xls"; // Excel文件所在路径
//			mqtt模拟数据特隆美PCS初始化文件
//			mqtt模拟数据特隆美电表初始化文件
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw_20ktl_a\\mqtt模拟数据20KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw-10ktl-a\\mqtt模拟数据10KTL初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\hw-36ktl-c\\mqtt模拟数据36KTL-C初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500tba_a\\mqtt模拟数据500tbkt_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500xj_a\\mqtt模拟数据500xj_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\xj_GZH-601\\mqtt模拟数据xj_gzh-601_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\SCR-SDR16\\mqtt模拟数据sdr16_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\SG1000_a\\mqtt模拟数据SG1000_a初始化文件.xls";
//			String fileName = "C:\\Users\\SP0012\\Desktop\\point.xls";
			
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(0); // 从工作区中取得页（Sheet）
			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容

				String signalName = null;
			    Integer dataModel = null;
			    String signalType = null;
			    Integer addressId = null;
			    Integer signalSubId = null;
			    String signalGuid = null;
			    String realType = null;
			    Byte dataType = null;
			    Integer startBit = null;
			    Integer bitLength = null;
			    Float dataGain = null;
			    Float correctionFactor = null;
			    Float minval = null;
			    Float maxval = null;
			    String dataUnit = null;
			    String singleRegister = null;
			    String specialProcess = null;
			    String visibility = null;
			    String push = null;
			    String valid = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());

					switch (j ) {
					case 0:
						signalName = cell.getContents();
						break;
					case 1:
						dataModel = Integer.valueOf(cell.getContents());
						break;
					case 2:
						signalType = (cell.getContents());
						break;
					case 3:
						addressId = Integer.valueOf(cell.getContents());
						break;
					case 4:
						signalSubId = Integer.valueOf(cell.getContents());
						break;
					case 5:
						signalGuid = cell.getContents();
						break;
					case 6:
						realType = (cell.getContents());
						break;
					case 7:
						dataType = Byte.valueOf(cell.getContents());
						break;
					case 8:
						startBit = Integer.valueOf(cell.getContents());
						break;
					case 9:
						bitLength = Integer.valueOf(cell.getContents());
						break;
					case 10:
						dataGain = Float.valueOf(cell.getContents());
						break;
					case 11:
						correctionFactor = Float.valueOf(cell.getContents());
						break;
					case 12:
						minval = Float.valueOf(cell.getContents());
						break;
					case 13:
						maxval = Float.valueOf(cell.getContents());
						break;
					case 14:
						dataUnit = (cell.getContents());
						break;
					case 15:
						singleRegister = (cell.getContents());
						break;
					case 16:
						specialProcess = (cell.getContents());
						break;
					case 17:
						visibility = (cell.getContents());
						break;
					case 18:
						push = (cell.getContents());
						break;
					case 19:
						valid = (cell.getContents());
						break;
					default:
						break;
					}
				}

				System.out.println();
				CollModelDetailMqtt collModelDetailMqtt = new CollModelDetailMqtt(signalName, dataModel, signalType, addressId, signalSubId, signalGuid, realType, dataType, startBit, bitLength, dataGain, correctionFactor, minval, maxval, dataUnit, singleRegister, specialProcess, visibility, push, valid);
				collModelDetailMqttMapper.insertSelective(collModelDetailMqtt);
				
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: test3
	 * @Description: 遥信信号表
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午1:48:09
	 */
	@Test
	public void collYxExpandMapper() {
		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\第二版本初始化文件\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\mqtt模拟数据12KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-60ktl-a\\SUN2000 V300R001 MODBUS接口定义描述---整理.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\04 特隆美\\20180327模拟文件\\mqtt模拟数据特隆美BMS初始化文件.xls"; // Excel文件所在路径
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw_20ktl_a\\mqtt模拟数据20KTL初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw-10ktl-a\\mqtt模拟数据10KTL初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\hw-36ktl-c\\mqtt模拟数据36KTL-C初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\1000_tba_a\\mqtt模拟数据1000_tba_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500tba_a\\mqtt模拟数据500tbkt_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500xj_a\\mqtt模拟数据500xj_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\SCR-SDR16\\mqtt模拟数据sdr16_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\SG1000_a\\mqtt模拟数据SG1000_a初始化文件.xls";
//			String fileName = "C:\\Users\\SP0012\\Desktop\\point.xls";
			
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(1); // 从工作区中取得页（Sheet）
			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容
				String signalGuid = null;
				String signalName = null;
				Integer dataModel = null;
				Integer ycAlarm = null;
				Integer alarmBit = null;
				Integer alarmBitLength = null;
				String alarmType = null;
				String statusName = null;
				Integer statusValue = null;
				String alarmLevel = null;
				String suggest = null;
				String alarmReason = null;
				String faultPoint = null;
				String valid = null;
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());

					switch (j ) {
					case 0:
						signalGuid = (cell.getContents());
						break;
					case 1:
						signalName = (cell.getContents());
						break;
					case 2:
						dataModel = Integer.valueOf(cell.getContents());
						break;
					case 3:
						ycAlarm = Integer.valueOf(cell.getContents());
						break;
					case 4:
						alarmBit = Integer.valueOf(cell.getContents());
						break;
					case 5:
						alarmBitLength = Integer.valueOf(cell.getContents());
						break;
					case 6:
						alarmType = (cell.getContents());
						break;
					case 7:
						statusName = (cell.getContents());
						break;
					case 8:
						statusValue = Integer.valueOf(cell.getContents());
						break;
					case 9:
						alarmLevel = (cell.getContents());
						break;
					case 10:
						suggest = (cell.getContents());
						break;
					case 11:
						alarmReason = (cell.getContents());
						break;
					case 12:
						faultPoint = (cell.getContents());
						break;
					case 13:
						valid = (cell.getContents());
						break;
					default:
						break;
					}

				}
				CollYxExpand collYxExpand = new CollYxExpand(signalGuid, signalName, dataModel, ycAlarm, alarmBit, alarmBitLength, alarmType, statusName, statusValue, alarmLevel, suggest, alarmReason, faultPoint, valid);
				collYxExpandMapper.insertSelective(collYxExpand);

			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: test4
	 * @Description: 遥控遥调对照
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午2:45:51
	 */
	@Test
	public void collYkytExpandMapper() {
		String k = "" ;
		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\第二版本初始化文件\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\mqtt模拟数据12KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-60ktl-a\\SUN2000 V300R001 MODBUS接口定义描述---整理.xls"; // Excel文件所在路径
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw_20ktl_a\\mqtt模拟数据20KTL初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw-10ktl-a\\mqtt模拟数据10KTL初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\hw-36ktl-c\\mqtt模拟数据36KTL-C初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\1000_tba_a\\mqtt模拟数据1000_tba_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500tba_a\\mqtt模拟数据500tbkt_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500xj_a\\mqtt模拟数据500xj_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\SG1000_a\\mqtt模拟数据SG1000_a初始化文件.xls";
//			String fileName = "C:\\Users\\SP0012\\Desktop\\point.xls";
			
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(2); // 从工作区中取得页（Sheet）
			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容
				Integer dataModel = null;
				String signalGuid = null;
				String signalName = null;
				Integer ycControl = null;
				Integer controlBit = null;
				Integer statusValue = null;
				String valid = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());

					switch (j ) {
					case 0:
						dataModel = Integer.valueOf(cell.getContents());
						break;
					case 1:
						signalGuid = (cell.getContents());
						break;
					case 2:
						signalName = (cell.getContents());
						break;
					case 3:
						ycControl = Integer.valueOf(cell.getContents());
						break;
					case 4:
						controlBit = Integer.valueOf(cell.getContents());
						break;
					case 5:
						statusValue = Integer.valueOf(cell.getContents());
						break;
					case 6:
						valid = (cell.getContents());
						break;
					default:
						break;
					}

				}
				System.out.println();
				CollYkytExpand collYkytExpand = new CollYkytExpand(dataModel,signalGuid, signalName, ycControl, controlBit,
						statusValue, valid);
				collYkytExpandMapper.insertSelective(collYkytExpand);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出错的：" + k);
		} 

	}

	/**
	 * @Title: test5
	 * @Description: 存库字段
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午3:28:44
	 */
	@Test
	public void collSignalLabelMapper() {
		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\第二版本初始化文件\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\mqtt模拟数据12KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\BMS-PCS-电表存库字段.xls"; // Excel文件所在路径
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\1000_tba_a\\mqtt模拟数据1000_tba_a初始化文件.xls";
//			String fileName = "C:\\Users\\SP0012\\Desktop\\point.xls";
			

			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(3); // 从工作区中取得页（Sheet）
			
//			Sheet sheet = wb.getSheet(0); // 从工作区中取得页（Sheet）
			
			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容
		
				String tablename = null;
				String field = null;
				Byte languageId = null;
				String fieldLabel = null;
				Integer deviceType = null;
				Integer priority = null;
				String unit = null;
				String compare = null;
				String valid = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());

					switch (j) {
					case 0:
						tablename = (cell.getContents());
						break;
					case 1:
						field = (cell.getContents());
						break;
					case 2:
						languageId = Byte.valueOf(cell.getContents());
						break;
					case 3:
						fieldLabel = (cell.getContents());
						break;
					case 4:
						deviceType = Integer.valueOf(cell.getContents());
						break;
					case 5:
						priority = Integer.valueOf(cell.getContents());
						break;
					case 6:
						unit = (cell.getContents());
						break;
					case 7:
						compare= (cell.getContents());
						break;
					case 8:
						valid = (cell.getContents());
						break;
					default:
						break;
					}

				}
				System.out.println();
				CollSignalLabel collSignalLabel = new CollSignalLabel(tablename, field, languageId, fieldLabel,
						deviceType, priority,unit,compare, valid);
				collSignalLabelMapper.insertSelective(collSignalLabel);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws InterruptedException
	 * @Title: test6
	 * @Description: 存库字段对应表
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午3:36:17
	 */
	@Test
	public void fieldSignalGuidMappingMapper() throws InterruptedException {
		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\第二版本初始化文件\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\mqtt模拟数据12KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-60ktl-a\\SUN2000 V300R001 MODBUS接口定义描述---整理.xls"; // Excel文件所在路径
//			String fileName = "E:\\临时文件\\特隆美\\20180327模拟文件\\模拟设备入库映射.xls"; // Excel文件所在路径
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw_20ktl_a\\mqtt模拟数据20KTL初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw_33ktl_a\\SUN2000 33~40KTLMODBUS接口定义描述---整理.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋\\hw-10ktl-a\\mqtt模拟数据10KTL初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\hw-36ktl-c\\mqtt模拟数据36KTL-C初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\1000_tba_a\\mqtt模拟数据1000_tba_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500tba_a\\mqtt模拟数据500tbkt_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\500xj_a\\mqtt模拟数据500xj_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\xj_GZH-601\\mqtt模拟数据xj_gzh-601_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\SCR-SDR16\\mqtt模拟数据sdr16_a初始化文件.xls";
//			String fileName = "E:\\协能产品发布资料\\光伏采集器配置文件样板\\南京畅洋自主合成点\\SG1000_a\\mqtt模拟数据SG1000_a初始化文件.xls";
//			String fileName = "C:\\Users\\SP0012\\Desktop\\point.xls";
			
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(4); // 从工作区中取得页（Sheet）
//			Sheet sheet = wb.getSheet(0); // 从工作区中取得页（Sheet）
			
			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容
				Integer collId = null;
				Integer dataModel = null;

				String signalGuid = null;

				String valid = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());

					switch (j) {
					case 0:
						collId = Integer.valueOf(cell.getContents());
						break;
					case 1:
						dataModel = Integer.valueOf(cell.getContents());
						break;
					case 2:
						signalGuid = (cell.getContents());
						break;
					case 3:
						valid = (cell.getContents());
						break;
					default:
						break;
					}

				}
				System.out.println();
				FieldSignalGuidMapping fieldSignalGuidMapping = new FieldSignalGuidMapping(collId,dataModel, signalGuid, valid);

				Thread.sleep(100);
				fieldSignalGuidMappingMapper.insertSelective(fieldSignalGuidMapping);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * @Title: test7
	 * @Description: 新增设备导入
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午12:53:23
	 */
	@Test
	public void deviceMapper() {
		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\第二版本初始化文件\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\生产库9个模拟电站\\mqtt模拟数据36KTL初始化文件.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集精简配置文件\\生产库9个模拟电站\\mqtt模拟数据36KTL初始化文件-测试删除.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\mqtt模拟数据12KTL初始化文件.xls"; // Excel文件所在路径

			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(6); // 从工作区中取得页（Sheet）

			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容


				String deviceName = null;
			    String deviceSn = null;
			    Integer deviceType = null;
			    Integer deviceModelId = null;
			    String connAddr = null;
			    Integer connPort = null;
			    Integer plantId = null;
			    Long connTime = null;
			    Integer connProtocol = null;
			    Integer dataMode = null;
			    String manufacture = null;
			    Float power = null;
			    Integer supid = null;
			    Integer maxNum = null;
			    String deviceValid = null;
			    String deviceStatus = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());
					switch (j) {
					case 0:
						deviceName = (cell.getContents());
						break;
					case 1:
						deviceSn = cell.getContents();
						break;
					case 2:
						deviceType = Integer.parseInt(cell.getContents());
						break;
					case 3:
						deviceModelId = Integer.parseInt(cell.getContents());
						break;
					case 4:
						connAddr = cell.getContents();
						break;
					case 5:
						connPort = Integer.parseInt(cell.getContents());
						break;
					case 6:
						plantId = Integer.parseInt(cell.getContents());
						break;
					case 7:
						connTime = Long.parseLong(cell.getContents());
						break;
					case 8:
						connProtocol = Integer.parseInt(cell.getContents());
						break;
					case 9:
						dataMode = Integer.parseInt(cell.getContents());
						break;
					case 10:
						manufacture = (cell.getContents());
						break;
					case 11:
						power = Float.parseFloat(cell.getContents());
						break;
					case 12:
						supid = Integer.parseInt(cell.getContents());
						break;
					case 13:
						maxNum = Integer.parseInt(cell.getContents());
						break;
					case 14:
						deviceValid = (cell.getContents());
						break;
					case 15:
						deviceStatus = (cell.getContents());
						break;
					default:
						break;
					}
				}
				System.out.println();

				Device device = new Device(deviceName, deviceSn, deviceType, deviceModelId, connAddr, connPort, plantId, connTime, connProtocol, dataMode, manufacture, power, supid,maxNum, deviceValid, deviceStatus);
				deviceMapper.insertSelective(device);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	  * @Title:  testJsonExtend 
	  * @Description:  TODO : Json扩展点表导入
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年6月8日下午2:00:54
	 */
	@Test
	public void testJsonExtend() {
		try {
			//String fileName = "C:\\Users\\SP0012\\Desktop\\point.xls"; // Excel文件所在路径
			
			
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(0); // 从工作区中取得页（Sheet）
			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容


			    Integer dataModel = null;
			    String regAddress = null;
			    String funCode = null;
			    String dynamic = null;
			    String signalGuid = null;
			    String valid = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());

					switch (j ) {
					case 0:
						dataModel = Integer.valueOf(cell.getContents());
						break;
					case 1:
						regAddress = cell.getContents();
						break;
					case 2:
						funCode = (cell.getContents());
						break;
					case 3:
						dynamic = (cell.getContents());
						break;
					case 4:
						signalGuid = (cell.getContents());
						break;
					case 5:
						valid = cell.getContents();
						break;
					case 6:
						valid = (cell.getContents());
						break;
					default:
						break;
					}
				}

				System.out.println();
				CollJsonModelDetailExtend collJsonModelDetailExtend = new CollJsonModelDetailExtend(dataModel, regAddress, funCode, dynamic, signalGuid, valid);
				collJsonMDEMapper.insertSelective(collJsonModelDetailExtend);
				
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	@Test
	public void testModel() {
		try {
//			String fileName = "E:\\项目资料\\光伏项目资料\\模拟数据导入\\mqtt模拟数据20171123.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0010\\Desktop\\mqtt模拟数据36KTL-VX217503532.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-12ktl-a\\点表资料\\初始化点表-20180305.xls"; // Excel文件所在路径
//			String fileName = "D:\\光伏采集器配置文件样板\\南京畅洋\\hw-60ktl-a\\初始化点表-20180314.xls"; // Excel文件所在路径
//			String fileName = "C:\\Users\\SP0012\\Desktop\\point.xls"; // Excel文件所在路径
			
			
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(6); // 从工作区中取得页（Sheet）
			for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容


				Integer regAddress = null;
				String signalName = null;
			    String signalType = null;
			    Integer addressId = null;
			    Integer signalSubId = null;
			    String signalGuid = null;
			    String realType = null;
			    Byte dataType = null;
			    Integer startBit = null;
			    Integer bitLength = null;
			    Float dataGain = null;
			    Float correctionFactor = null;
			    Float minval = null;
			    Float maxval = null;
			    String dataUnit = null;
			    String singleRegister = null;
			    String specialProcess = null;
			    String visibility = null;
			    String push = null;
			    String valid = null;

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());

					switch (j ) {
					case 0:
						regAddress = Integer.valueOf(cell.getContents());
						break;
					case 1:
						signalName = cell.getContents();
						break;
					case 2:
						signalType = (cell.getContents());
						break;
					case 3:
						addressId = Integer.valueOf(cell.getContents());
						break;
					case 4:
						signalSubId = Integer.valueOf(cell.getContents());
						break;
					case 5:
						signalGuid = cell.getContents();
						break;
					case 6:
						realType = (cell.getContents());
						break;
					case 7:
						dataType = Byte.valueOf(cell.getContents());
						break;
					case 8:
						startBit = Integer.valueOf(cell.getContents());
						break;
					case 9:
						bitLength = Integer.valueOf(cell.getContents());
						break;
					case 10:
						dataGain = Float.valueOf(cell.getContents());
						break;
					case 11:
						correctionFactor = Float.valueOf(cell.getContents());
						break;
					case 12:
						minval = Float.valueOf(cell.getContents());
						break;
					case 13:
						maxval = Float.valueOf(cell.getContents());
						break;
					case 14:
						dataUnit = (cell.getContents());
						break;
					case 15:
						singleRegister = (cell.getContents());
						break;
					case 16:
						specialProcess = (cell.getContents());
						break;
					case 17:
						visibility = (cell.getContents());
						break;
					case 18:
						push = (cell.getContents());
						break;
					case 19:
						valid = (cell.getContents());
						break;
					default:
						break;
					}
				}

				System.out.println();
				PointHw10ktlA pointHw10ktlA = new PointHw10ktlA(regAddress, signalName, signalType, addressId, signalSubId, signalGuid, realType, dataType, startBit, bitLength, dataGain, correctionFactor, minval, maxval, dataUnit, singleRegister, specialProcess, visibility, push, valid);
				pointHw10ktlAMapper.insertSelective(pointHw10ktlA);
				
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testXml() {
		String sessionId = "3235325345";

		String collector_id = "VX817393345";
		Map<String, Map<String, String>> ykYtMap = new HashMap<String, Map<String, String>>();
		Map<String, String> sss = new HashMap<String, String>();
		sss.put("VX817393345_YK_0", "1");
		ykYtMap.put("1", sss);

		String map2XmlStr = XmlParseUtil.map2XmlStr(sessionId, collector_id, ykYtMap);
		System.out.println("下发的xml：!" + map2XmlStr);
	}

	@Test
	public void testDom4j() {
		Document doc = DocumentHelper.createDocument();

		Element mqttData = doc.addElement("mqttData");
		Element session = mqttData.addElement("Session");
		session.addAttribute("id", "sdrfwwertwer");

		Element dataCollector = session.addElement("DataCollector");
		dataCollector.addAttribute("id", "seewstrew");

		dataCollector.addElement("time").setText(TimeUtil4Xml.getXmlTimeStr());

		Element ykType = dataCollector.addElement("type");
		ykType.addAttribute("Type", "Yk");

		Element ytType = dataCollector.addElement("type");
		ytType.addAttribute("Type", "Yt");

		ykType.addElement("yk").addAttribute("pId", "0").setText("2");
		System.out.println("德玛西亚");
		System.out.println(ykType.selectNodes("//DataCollector/type[1]/yk").size());
		System.out.println(ytType.selectNodes("//DataCollector/type[2]/yt").size());

		// 封装完毕，检查是否遥控或遥调子节点存在与否，不存在就删除
		if (ykType.selectNodes("//DataCollector/type[1]/yk").size() == 0) {
			dataCollector.remove(ykType);
		}
		if (ytType.selectNodes("//DataCollector/type[2]/yt").size() == 0) {
			dataCollector.remove(ytType);
			System.out.println("德玛西亚");
		}
		// 转化成xml字符串
		StringWriter sw = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");

		try {
			XMLWriter xmlWriter = new XMLWriter(sw, format);
			xmlWriter.write(doc);

		} catch (IOException e) {
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
			}
		}

		System.out.println(sw.toString());
	}

	
	
}
