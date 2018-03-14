/*
 * Copyright (c) 2018. CK. All rights reserved.
 */

package com.tyj.jhpt.dao;

import com.github.fartherp.framework.core.dao.SqlMapDao;
import com.tyj.jhpt.bo.CarLocation;

/**
 * This class corresponds to the database table `tb_car_location`
 */
public interface CarLocationDao extends SqlMapDao<CarLocation, Long> {
}