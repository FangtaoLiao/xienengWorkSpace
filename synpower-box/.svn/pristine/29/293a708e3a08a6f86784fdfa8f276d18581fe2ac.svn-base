package lztest.testmapper;

import com.synpower.constant.TimeEXP;
import com.synpower.lang.ServiceException;
import com.synpower.msg.Msg;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        String time = TimeUtil.formatTime(timeMillis, "yyyy-MM-dd");
        System.out.println(time);
        SimpleDateFormat f3 = new SimpleDateFormat("yyyy-MM-dd");
        long str2Long = f3.parse(time).getTime();
        System.out.println(str2Long);
        System.out.println(TimeUtil.formatTime(str2Long, "yyyy-MM-dd HH:mm:ss"));
        //System.out.println(TimeUtil.formatTime(str2Long, "yyyy-MM-dd HH:mm:ss"));
    }
    //    File desktopDir = FileSystemView.getFileSystemView()
    //            .getHomeDirectory();
    //    String desktopPath = desktopDir.getAbsolutePath();
    //    System.out.println(desktopPath);
    //}
}
