package com.bplow.search.mapper;

import java.util.List;
import com.bplow.search.domain.TOrder1;

public interface TOrder1Mapper {

    int deleteByPrimaryKey(Integer orderId);

    int insert(TOrder1 record);

    List<TOrder1> selectByExample(TOrder1 example);

    TOrder1 selectByPrimaryKey(Integer orderId);

}