package dataCollectionJar;

import com.synpowertech.dataCollectionJar.initialization.JedisUtil4OPS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringTest {

	/**
	 * @Title: test1
	 * @Description: 导入设备关联关系表
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月19日下午12:53:23
	 */
	@Test
	public void test1() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			Thread.sleep(1000);
		}
	}

}
