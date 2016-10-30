package com.bplow.search.mapper;


import java.util.List;

import com.bplow.search.domain.SrContent;

public interface SrContentMapper {
	
    int insert(SrContent record);

    List<SrContent> queryForPage(SrContent example);

    int update(SrContent record);
    
}