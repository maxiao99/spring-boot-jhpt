/*
 * Copyright (c) 2017. CK. All rights reserved.
 */

package com.tyj.jhpt.service;

import com.github.fartherp.framework.core.service.GenericService;
import com.tyj.jhpt.bo.RanliaoDianchi;

import java.util.List;
import java.util.Map;

/**
 * This class corresponds to the database table `tb_ranliao_dianchi`
 */
public interface RanliaoDianchiService extends GenericService<RanliaoDianchi, Long> {

    List<RanliaoDianchi> findPageRanliaoDianchi(Map<String, Object> map);
}