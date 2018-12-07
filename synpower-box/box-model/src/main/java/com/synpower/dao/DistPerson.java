package com.synpower.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DistPerson {

    List<Map<String,Object>> getDistPerson();
}
