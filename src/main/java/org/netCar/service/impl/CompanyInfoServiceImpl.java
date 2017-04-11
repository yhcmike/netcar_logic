package org.netCar.service.impl;

import org.netCar.dao.CompanyInfoDao;
import org.netCar.domain.CompanyInfo;
import org.netCar.service.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

	
	@Autowired
	CompanyInfoDao companyInfoDao;
	
	@Override
	public void save(CompanyInfo companyInfo) {
		companyInfoDao.save(companyInfo);
	}
	

	@Override
	public void update(CompanyInfo companyInfo) {

	}

	@Override
	public void changeStatus(Integer id, Integer status) {

	}

	@Override
	public CompanyInfo getById(Integer id) {
		return null;
	}

}
