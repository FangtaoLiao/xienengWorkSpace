package com.synpower.constant;
/**
 * @Author lz
 * @Description 单位进制管理
 * @Date 13:57 2018/8/24
 * @Param
 * @return
 **/
public enum UNIT {
    ELEC(10000d,100000000d,"度","万度","亿度"),RMB(10000d,100000000d,"元","万元","亿元");
    private String bUnit1;
    private String bUnit2;
    private String bUnit3;
    //转换进制1
    private Double traH1;
    //转换进制2
    private Double traH2;

    UNIT(Double traH1, Double traH2,String bUnit1, String bUnit2, String bUnit3 ) {
        this.bUnit1 = bUnit1;
        this.bUnit2 = bUnit2;
        this.bUnit3 = bUnit3;
        this.traH1 = traH1;
        this.traH2 = traH2;
    }

    public String getbUnit1() {
        return bUnit1;
    }

    public String getbUnit2() {
        return bUnit2;
    }

    public String getbUnit3() {
        return bUnit3;
    }

    public Double getTraH1() {
        return traH1;
    }

    public Double getTraH2() {
        return traH2;
    }
}
