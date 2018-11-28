package com.synpower.strategy.CommonSerch;

public class SearchCondition {

    //排序的条件
    public static final String CON_PPR = "ppr";
    public static final String CON_GENTOTAL = "genTotal";
    public static final String CON_CAPACITY = "capacity";


    public static final String ORDER_UP = "ASC";
    public static final String ORDER_DOWN = "DESC";
    private String orderName = "Default";
    private String order = ORDER_DOWN;
    private int start = 0;
    private int length = 10;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
