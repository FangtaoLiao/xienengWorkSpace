package lztest.testmapper;

import com.synpower.bean.DataElectricMeter;
import com.synpower.bean.ElectricStorageData;
import com.synpower.constant.TimeEXP;
import com.synpower.dao.DataElectricMeterMapper;
import com.synpower.dao.ElectricStorageDataMapper;
import com.synpower.quartz.TimedTasks;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * @author lz&ybj on 2018/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/webmvc-config.xml" })
public class ElectricStorageDataMapperTest {

    @Autowired
    private DataElectricMeterMapper dataElectricMeterMapper;
    @Autowired
    private ElectricStorageDataMapper electricStorageDataMapper;
    @Autowired
    private TimedTasks timedTasks;

    @Test
    public void test0() throws Exception {
        //timedTasks.storageData();
        System.out.println(Util.longToDateString(1514779199999l, TimeEXP.exp1));
    }

    @Test
    public void test1() {

        float yjian = 0;
        float yfeng = 0;
        float yping = 0;
        float ygu = 0;
        float wjian = 0;
        float wfeng = 0;
        float wping = 0;
        float wgu = 0;
        float yg = 0;
        float wg = 0;

        long currDayStartTime = Util.getBeforDayMin(5);
        long currDayEndTime = TimeUtil.getCurrDayEndTime(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currDayStartTime);

        while (calendar.getTimeInMillis() < currDayEndTime) {

            DataElectricMeter d = new DataElectricMeter();
            calendar.add(Calendar.MINUTE, 5);
            long millis = calendar.getTimeInMillis();
            yjian += 5d;
            yfeng += 5d;
            yping += 5d;
            ygu += 1d;
            wjian += 1d;
            wfeng += 1d;
            wping += 1d;
            wgu += 1d;
            yg = yjian + yfeng + yping + ygu;
            wg = wjian + wfeng + wping + wgu;
            d.setEmPosJianActiveEnergy(yjian);
            d.setEmPosFengActiveEnergy(yfeng);
            d.setEmPosPingActiveEnergy(yping);
            d.setEmPosGuActiveEnergy(ygu);
            d.setEmPosJianReactiveEnergy(wjian);
            d.setEmPosFengReactiveEnergy(wfeng);
            d.setEmPosPingReactiveEnergy(wping);
            d.setEmPosGuReactiveEnergy(wgu);
            d.setEmPosActiveEnergy(yg);
            d.setEmPosReactiveEnergy(wg);
            d.setDataTime(millis);
            d.setPlant(63);
            d.setDeviceId(588);
            dataElectricMeterMapper.insert(d);
        }

    }

    @Test
    public void test02() {
        double[] elecs = {499.34, 484.25, 485.65, 462.58, 473.63, 485.24, 492.57, 480.21};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        int cMonth = calendar.get(Calendar.MONTH);
        double pElec = elecs[0] / 31;
        double jElec = 0;
        Random random = new Random();
        ArrayList<ElectricStorageData> dataArrayList = new ArrayList<>();
        for (int i = 0; cMonth != 8; ) {
            if (i < 8) {
                int aMonth = calendar.get(Calendar.MONTH);
                if (aMonth != cMonth) {
                    cMonth = calendar.get(Calendar.MONTH);
                    i++;
                    try {
                        pElec = elecs[i] / 30;
                    } catch (Exception e) {
                        break;
                    }
                }
                double v = (random.nextDouble() * 6) - 3;
                jElec = pElec + v;
                System.out.println(Util.longToDateString(calendar.getTimeInMillis(), TimeEXP.exp1) + "//" + jElec);
                ElectricStorageData dayElec = new ElectricStorageData(jElec, jElec * 1.19, 0d,
                        0d, 0d, 0d, 0d, 0d,
                        jElec, jElec * 1.19, 0d, calendar.getTimeInMillis(), 63,
                        0d, 0d, 0d, 0d);
                dataArrayList.add(dayElec);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        electricStorageDataMapper.insertDataForStotage(dataArrayList);
    }

}
