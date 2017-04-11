package org.netCar.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "companyinfo")
public class CompanyInfo extends IdEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7037131893638920792L;
	
	/*
	 * 名称
	 */
	@Column(name = "companyId")
	private String companyId;

	@Column(name = "companyName")
    private String companyName;

	@Column(name = "identifier")
    private String identifier;

	@Column(name = "address")
    private Integer address;

	@Column(name = "businessScope")
    private String businessScope;

	@Column(name = "contactAddress")
    private String contactAddress;

	@Column(name = "economicType")
    private String economicType;

	@Column(name = "regCapital")
    private String regCapital;

	@Column(name = "legalName")
    private String legalName;

	@Column(name = "legalID")
    private String legalID;

	@Column(name = "legalPhone")
    private String legalPhone;

	@Column(name = "legalPhoto")
    private String legalPhoto;

	@Column(name = "state")
    private Integer state;

	@Column(name = "flag")
    private Integer flag;

	@Column(name = "updateTime")
    private Long updateTime;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Integer getAddress() {
		return address;
	}

	public void setAddress(Integer address) {
		this.address = address;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getEconomicType() {
		return economicType;
	}

	public void setEconomicType(String economicType) {
		this.economicType = economicType;
	}

	public String getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getLegalID() {
		return legalID;
	}

	public void setLegalID(String legalID) {
		this.legalID = legalID;
	}

	public String getLegalPhone() {
		return legalPhone;
	}

	public void setLegalPhone(String legalPhone) {
		this.legalPhone = legalPhone;
	}

	public String getLegalPhoto() {
		return legalPhoto;
	}

	public void setLegalPhoto(String legalPhoto) {
		this.legalPhoto = legalPhoto;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

    
    
}