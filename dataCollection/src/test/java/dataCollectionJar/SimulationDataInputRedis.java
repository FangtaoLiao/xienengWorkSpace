package dataCollectionJar;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

import com.synpowertech.dataCollectionJar.dao.CollModelDetailMqttMapper;
import com.synpowertech.dataCollectionJar.dao.CollSignalLabelMapper;
import com.synpowertech.dataCollectionJar.dao.CollYkytExpandMapper;
import com.synpowertech.dataCollectionJar.dao.CollYxExpandMapper;
import com.synpowertech.dataCollectionJar.dao.CollectorMessageStructureMapper;
import com.synpowertech.dataCollectionJar.dao.DataYxMapper;
import com.synpowertech.dataCollectionJar.dao.DeviceMapper;
import com.synpowertech.dataCollectionJar.dao.FieldSignalGuidMappingMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw12ktlAMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw36ktlAMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw36ktlBMapper;
import com.synpowertech.dataCollectionJar.dao.PointHw60ktlAMapper;
import com.synpowertech.dataCollectionJar.domain.CollModelDetailMqtt;
import com.synpowertech.dataCollectionJar.domain.CollSignalLabel;
import com.synpowertech.dataCollectionJar.domain.CollYkytExpand;
import com.synpowertech.dataCollectionJar.domain.CollYxExpand;
import com.synpowertech.dataCollectionJar.domain.CollectorMessageStructure;
import com.synpowertech.dataCollectionJar.domain.Device;
import com.synpowertech.dataCollectionJar.domain.FieldSignalGuidMapping;
import com.synpowertech.dataCollectionJar.domain.PointHw12ktlA;
import com.synpowertech.dataCollectionJar.domain.PointHw36ktlA;
import com.synpowertech.dataCollectionJar.domain.PointHw36ktlB;
import com.synpowertech.dataCollectionJar.domain.PointHw60ktlA;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import com.synpowertech.dataCollectionJar.utils.TimeUtil4Xml;
import com.synpowertech.dataCollectionJar.utils.XmlParseUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SimulationDataInputRedis {

	/**
	 * @Title: test1
	 * @Description: 导入设备关联关系表
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午12:53:23
	 */
	@Test
	public void test1() {
		try {
			String fileName = "C:\\Users\\SP0010\\Desktop\\04 特隆美\\20180327模拟文件\\模拟设备redis.xls"; // Excel文件所在路径
			File file = new File(fileName); // 创建文件对象
			Workbook wb = Workbook.getWorkbook(file); // 从文件流中获取Excel工作区对象（WorkBook）
			Sheet sheet = wb.getSheet(1); // 从工作区中取得页（Sheet）
//			 Sheet sheet = wb.getSheet(2); // 从工作区中取得页（Sheet）
//			 Sheet sheet = wb.getSheet(3); // 从工作区中取得页（Sheet）

			for (int i = 0; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if (cell.getContents() == null || "".equals(cell.getContents())) {
						continue;
					}
					System.out.println(cell.getContents());
					JedisUtil.setStringInMap("883", cell.getContents(), new Random().nextInt(100) + "");
//					JedisUtil.setStringInMap("884", cell.getContents(), new Random().nextInt(100) + "");
//					JedisUtil.setStringInMap("885", cell.getContents(), new Random().nextInt(100) + "");
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
