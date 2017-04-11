package org.netCar.service;

import org.netCar.domain.CompanyInfo;

public interface CompanyInfoService {

	public void save(CompanyInfo companyInfo);
	/**
	 * 更新
	 */
	public void update(CompanyInfo companyInfo);
	public void changeStatus(Integer id,Integer status);
	public CompanyInfo getById(Integer id);
	
}
