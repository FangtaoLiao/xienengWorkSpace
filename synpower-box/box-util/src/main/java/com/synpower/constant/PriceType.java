package com.synpower.constant;
/**
 * @Author lz
 * @Description 价格详情的类型
 * @Date 16:31 2018/8/22
 * @return
 **/
public enum PriceType {
    JIAN(1,"jEnergy"),FENG(2,"fEnergy"),PING(3,"pEnergy"),GU(4,"gEnergy");
    private String description;
    private Integer num;
    PriceType(Integer num,String description){
        this.num = num;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getNum() {
        return num;
    }
}
