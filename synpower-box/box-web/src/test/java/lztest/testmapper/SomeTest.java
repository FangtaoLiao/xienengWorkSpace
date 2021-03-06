package lztest.testmapper;

import com.synpower.constant.TimeEXP;
import com.synpower.lang.ServiceException;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class SomeTest {
    @Test
    public void test1() {

        System.out.println(Util.dateStringToLong("2018-09-03", TimeEXP.exp1));
        List<String> timeArrayList1 = TimeUtil.
                getTimeList(TimeUtil.getNextOrLastTime(Calendar.YEAR, "2018-08-20", -1, TimeEXP.exp1),
                        TimeUtil.getNextOrLastTime(Calendar.YEAR, "2018-09-03", -1, TimeEXP.exp1),
                        TimeEXP.exp1, Calendar.DAY_OF_MONTH);
        List<String> timeArrayList2 = TimeUtil.
                getTimeList("2018-08-20",
                        "2018-09-03",
                        TimeEXP.exp1, Calendar.DAY_OF_MONTH);
        System.out.println(Util.dateStringToLong(TimeUtil.
                getNextOrLastTime(Calendar.YEAR, "2018-09-03", -1, TimeEXP.exp1), TimeEXP.exp1));
    }

    @Test
    public void test2() throws ServiceException, ParseException {
        List<Integer> allDevIds = new ArrayList<>();
        allDevIds.add(1);
        allDevIds.add(1);
        allDevIds.add(1);
        allDevIds.add(1);
        allDevIds.add(1);
        List<String> strDevs = allDevIds.stream().map(String::valueOf).collect(Collectors.toList());
        System.out.println(strDevs);
    }
    //    File desktopDir = FileSystemView.getFileSystemView()
    //            .getHomeDirectory();
    //    String desktopPath = desktopDir.getAbsolutePath();
    //    System.out.println(desktopPath);
    //}
}
