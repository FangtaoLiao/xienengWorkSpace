package com.synpower.util;

/**
 * @author lz on 2018/8/23.
 */
public class SFUtil {
    /**
     * @return double
     * @Author lz
     * @Description //TODO
     * @Date 11:39 2018/8/23
     * @Param [activey有功, reActive无功]
     **/
    public static double getPFactor(double active, double reActive) {
        if (active == 0d) {
            return 1d;
        }
        double v = reActive / active;
        double pow = v * v + 1;
        return ApproUtil.getRoundDouble(1 / Math.sqrt(pow));
    }

    public static void main(String args[]) {

        System.out.println(Util.getCurrentTimeStr("yyyy-M"));

        System.out.println(getPFactor(50000, 59000));

        System.out.println(String.format("%.2f", 1.0));
    }

}
