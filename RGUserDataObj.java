/**
 * 
 */
package com.api.remitGuru.component.util;

/**
 * @author mansi.gandhi
 *
 */
public class RGUserDataObj implements java.io.Serializable{
	
	private String loggedInGroupId = null;
	private String loggedInEmailId = null;
	private String hostAddress = null;
	private String groupId = null;
	private String roleId = null;
	private String roleType = null;
	private String loginId = null;
	private String password = null;
	private String emailId = null;
	private String alternateEmailId = null;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String gender = null;
	private String dob = null;
	private String telNo = null;
	private String fax = null;
	private String mobile = null;
	private String address1 = null;
	private String address2 = null;
	private String address3 = null;
	private String street = null;
	private String city = null;
	private String countryCode = null;
	private String state = null;
	private String zip = null;
	private String education = null;
	private String industry = null;
	private String income = null;
	private String marketingRef = null;
	private String periodicUpdates = "N";
	
	private String passportNo = null;
	private String passportIssuePlace = null;
	private String passportExpiryDt = null;
	private String passportNationality = null;
	private String mrzNo = null;
	private String passportPersonalNo = null;
	private String drivingLicenseNo = null;
	
	private String address4 = null;
	private String address5 = null;
	
	private String nin = null;
	private String sin = null;

	private String smsFlag = "S";
	private String recvAddFlag = "Y";
	private String accAddFlag = "Y";    		
	private String txnBlockFlag = "N";		
	private String regAuthStatus = "A";		
	private String regRecordStatus = "U";  
	private String regMakerRemarks = ""; 
	private String regCheckerRemarks = "";
	private String txnAuthStatus = "A";   
	private String txnRecordStatus = "U";  
	private String txnMakerRemarks = "";  
	private String txnCheckerRemarks = "";
	private String theme = "Default";
	private String language = "en";
	private String timeZone = "+05:30";
	private String ssn = "";
	private String txnBounced = "0";
	private String uniqueIdentifierType = null;
	private String uniqueIdentifierValue = null;
	private String pageReferer = null;
	private String recvCountryCode = null;
	private String branchCode = null;
	private String channel = null;
	private String tellerLoginId = null;
	
	public String getTellerLoginId() {
		return tellerLoginId;
	}



	public void setTellerLoginId(String tellerLoginId) {
		this.tellerLoginId = tellerLoginId;
	}



	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}



	/**
	 * @return the loggedInGroupId
	 */
	public String getLoggedInGroupId() {
		return loggedInGroupId;
	}



	/**
	 * @param loggedInGroupId the loggedInGroupId to set
	 */
	public void setLoggedInGroupId(String loggedInGroupId) {
		this.loggedInGroupId = loggedInGroupId;
	}



	/**
	 * @return the loggedInEmailId
	 */
	public String getLoggedInEmailId() {
		return loggedInEmailId;
	}



	/**
	 * @param loggedInEmailId the loggedInEmailId to set
	 */
	public void setLoggedInEmailId(String loggedInEmailId) {
		this.loggedInEmailId = loggedInEmailId;
	}



	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}



	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}



	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}



	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}



	/**
	 * @return the hostAddress
	 */
	public String getHostAddress() {
		return hostAddress;
	}



	/**
	 * @param hostAddress the hostAddress to set
	 */
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}



	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}



	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}



	/**
	 * @return the roleType
	 */
	public String getRoleType() {
		return roleType;
	}



	/**
	 * @param roleType the roleType to set
	 */
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}



	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}



	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}



	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}



	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}



	/**
	 * @return the alternateEmailId
	 */
	public String getAlternateEmailId() {
		return alternateEmailId;
	}



	/**
	 * @param alternateEmailId the alternateEmailId to set
	 */
	public void setAlternateEmailId(String alternateEmailId) {
		this.alternateEmailId = alternateEmailId;
	}



	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}



	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}



	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}



	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}



	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}



	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}



	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}



	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}



	/**
	 * @return the telNo
	 */
	public String getTelNo() {
		return telNo;
	}



	/**
	 * @param telNo the telNo to set
	 */
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}



	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}



	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}



	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}



	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}



	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}



	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}



	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}



	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}



	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}



	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}



	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}



	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}



	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}



	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}



	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}



	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}



	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}



	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}



	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}



	/**
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}



	/**
	 * @param education the education to set
	 */
	public void setEducation(String education) {
		this.education = education;
	}



	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}



	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}



	/**
	 * @return the income
	 */
	public String getIncome() {
		return income;
	}



	/**
	 * @param income the income to set
	 */
	public void setIncome(String income) {
		this.income = income;
	}



	/**
	 * @return the marketingRef
	 */
	public String getMarketingRef() {
		return marketingRef;
	}



	/**
	 * @param marketingRef the marketingRef to set
	 */
	public void setMarketingRef(String marketingRef) {
		this.marketingRef = marketingRef;
	}



	/**
	 * @return the periodicUpdates
	 */
	public String getPeriodicUpdates() {
		return periodicUpdates;
	}



	/**
	 * @param periodicUpdates the periodicUpdates to set
	 */
	public void setPeriodicUpdates(String periodicUpdates) {
		this.periodicUpdates = periodicUpdates;
	}



	/**
	 * @return the passportNo
	 */
	public String getPassportNo() {
		return passportNo;
	}



	/**
	 * @param passportNo the passportNo to set
	 */
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}



	/**
	 * @return the passportIssuePlace
	 */
	public String getPassportIssuePlace() {
		return passportIssuePlace;
	}



	/**
	 * @param passportIssuePlace the passportIssuePlace to set
	 */
	public void setPassportIssuePlace(String passportIssuePlace) {
		this.passportIssuePlace = passportIssuePlace;
	}



	/**
	 * @return the passportExpiryDt
	 */
	public String getPassportExpiryDt() {
		return passportExpiryDt;
	}



	/**
	 * @param passportExpiryDt the passportExpiryDt to set
	 */
	public void setPassportExpiryDt(String passportExpiryDt) {
		this.passportExpiryDt = passportExpiryDt;
	}



	/**
	 * @return the passportNationality
	 */
	public String getPassportNationality() {
		return passportNationality;
	}



	/**
	 * @param passportNationality the passportNationality to set
	 */
	public void setPassportNationality(String passportNationality) {
		this.passportNationality = passportNationality;
	}



	/**
	 * @return the mrzNo
	 */
	public String getMrzNo() {
		return mrzNo;
	}



	/**
	 * @param mrzNo the mrzNo to set
	 */
	public void setMrzNo(String mrzNo) {
		this.mrzNo = mrzNo;
	}



	/**
	 * @return the passportPersonalNo
	 */
	public String getPassportPersonalNo() {
		return passportPersonalNo;
	}



	/**
	 * @param passportPersonalNo the passportPersonalNo to set
	 */
	public void setPassportPersonalNo(String passportPersonalNo) {
		this.passportPersonalNo = passportPersonalNo;
	}



	/**
	 * @return the drivingLicenseNo
	 */
	public String getDrivingLicenseNo() {
		return drivingLicenseNo;
	}



	/**
	 * @param drivingLicenseNo the drivingLicenseNo to set
	 */
	public void setDrivingLicenseNo(String drivingLicenseNo) {
		this.drivingLicenseNo = drivingLicenseNo;
	}



	/**
	 * @return the address4
	 */
	public String getAddress4() {
		return address4;
	}



	/**
	 * @param address4 the address4 to set
	 */
	public void setAddress4(String address4) {
		this.address4 = address4;
	}



	/**
	 * @return the address5
	 */
	public String getAddress5() {
		return address5;
	}



	/**
	 * @param address5 the address5 to set
	 */
	public void setAddress5(String address5) {
		this.address5 = address5;
	}



	/**
	 * @return the nin
	 */
	public String getNin() {
		return nin;
	}



	/**
	 * @param nin the nin to set
	 */
	public void setNin(String nin) {
		this.nin = nin;
	}


	/**
	 * @return the smsFlag
	 */
	public String getSmsFlag() {
		return smsFlag;
	}



	/**
	 * @param smsFlag the smsFlag to set
	 */
	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}



	/**
	 * @return the recvAddFlag
	 */
	public String getRecvAddFlag() {
		return recvAddFlag;
	}



	/**
	 * @param recvAddFlag the recvAddFlag to set
	 */
	public void setRecvAddFlag(String recvAddFlag) {
		this.recvAddFlag = recvAddFlag;
	}



	/**
	 * @return the accAddFlag
	 */
	public String getAccAddFlag() {
		return accAddFlag;
	}



	/**
	 * @param accAddFlag the accAddFlag to set
	 */
	public void setAccAddFlag(String accAddFlag) {
		this.accAddFlag = accAddFlag;
	}



	/**
	 * @return the txnBlockFlag
	 */
	public String getTxnBlockFlag() {
		return txnBlockFlag;
	}



	/**
	 * @param txnBlockFlag the txnBlockFlag to set
	 */
	public void setTxnBlockFlag(String txnBlockFlag) {
		this.txnBlockFlag = txnBlockFlag;
	}



	/**
	 * @return the regAuthStatus
	 */
	public String getRegAuthStatus() {
		return regAuthStatus;
	}



	/**
	 * @param regAuthStatus the regAuthStatus to set
	 */
	public void setRegAuthStatus(String regAuthStatus) {
		this.regAuthStatus = regAuthStatus;
	}



	/**
	 * @return the regRecordStatus
	 */
	public String getRegRecordStatus() {
		return regRecordStatus;
	}



	/**
	 * @param regRecordStatus the regRecordStatus to set
	 */
	public void setRegRecordStatus(String regRecordStatus) {
		this.regRecordStatus = regRecordStatus;
	}



	/**
	 * @return the regMakerRemarks
	 */
	public String getRegMakerRemarks() {
		return regMakerRemarks;
	}



	/**
	 * @param regMakerRemarks the regMakerRemarks to set
	 */
	public void setRegMakerRemarks(String regMakerRemarks) {
		this.regMakerRemarks = regMakerRemarks;
	}



	/**
	 * @return the regCheckerRemarks
	 */
	public String getRegCheckerRemarks() {
		return regCheckerRemarks;
	}



	/**
	 * @param regCheckerRemarks the regCheckerRemarks to set
	 */
	public void setRegCheckerRemarks(String regCheckerRemarks) {
		this.regCheckerRemarks = regCheckerRemarks;
	}



	/**
	 * @return the txnAuthStatus
	 */
	public String getTxnAuthStatus() {
		return txnAuthStatus;
	}



	/**
	 * @param txnAuthStatus the txnAuthStatus to set
	 */
	public void setTxnAuthStatus(String txnAuthStatus) {
		this.txnAuthStatus = txnAuthStatus;
	}



	/**
	 * @return the txnRecordStatus
	 */
	public String getTxnRecordStatus() {
		return txnRecordStatus;
	}



	/**
	 * @param txnRecordStatus the txnRecordStatus to set
	 */
	public void setTxnRecordStatus(String txnRecordStatus) {
		this.txnRecordStatus = txnRecordStatus;
	}



	/**
	 * @return the txnMakerRemarks
	 */
	public String getTxnMakerRemarks() {
		return txnMakerRemarks;
	}



	/**
	 * @param txnMakerRemarks the txnMakerRemarks to set
	 */
	public void setTxnMakerRemarks(String txnMakerRemarks) {
		this.txnMakerRemarks = txnMakerRemarks;
	}



	/**
	 * @return the txnCheckerRemarks
	 */
	public String getTxnCheckerRemarks() {
		return txnCheckerRemarks;
	}



	/**
	 * @param txnCheckerRemarks the txnCheckerRemarks to set
	 */
	public void setTxnCheckerRemarks(String txnCheckerRemarks) {
		this.txnCheckerRemarks = txnCheckerRemarks;
	}



	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}



	/**
	 * @param theme the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}



	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}



	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}



	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}



	/**
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}



	/**
	 * @return the txnBounced
	 */
	public String getTxnBounced() {
		return txnBounced;
	}



	/**
	 * @param txnBounced the txnBounced to set
	 */
	public void setTxnBounced(String txnBounced) {
		this.txnBounced = txnBounced;
	}
	
	/**
	 * @return the sin
	 */
	public String getSin() {
		return sin;
	}



	/**
	 * @param sin the sin to set
	 */
	public void setSin(String sin) {
		this.sin = sin;
	}
	
	
	
	/**
	 * @return the uniqueIdentifierType
	 */
	public String getUniqueIdentifierType() {
		return uniqueIdentifierType;
	}



	/**
	 * @param uniqueIdentifierType the uniqueIdentifierType to set
	 */
	public void setUniqueIdentifierType(String uniqueIdentifierType) {
		this.uniqueIdentifierType = uniqueIdentifierType;
	}



	/**
	 * @return the uniqueIdentifierValue
	 */
	public String getUniqueIdentifierValue() {
		return uniqueIdentifierValue;
	}



	/**
	 * @param uniqueIdentifierValue the uniqueIdentifierValue to set
	 */
	public void setUniqueIdentifierValue(String uniqueIdentifierValue) {
		this.uniqueIdentifierValue = uniqueIdentifierValue;
	}



	/**
	 * @return the pageReferer
	 */
	public String getPageReferer() {
		return pageReferer;
	}



	/**
	 * @param pageReferer the pageReferer to set
	 */
	public void setPageReferer(String pageReferer) {
		this.pageReferer = pageReferer;
	}



	/**
	 * @return the recvCountryCode
	 */
	public String getRecvCountryCode() {
		return recvCountryCode;
	}

	/**
	 * @param recvCountryCode the recvCountryCode to set
	 */
	public void setRecvCountryCode(String recvCountryCode) {
		this.recvCountryCode = recvCountryCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode  = branchCode;
	}
	
	public String getBranchCode() {
		return branchCode;
	}

	/**
	 * This method will reset all the fields in the data object
	 */
	public void resetRGUserDataObj()
	{
		 loggedInGroupId = null;
		 loggedInEmailId = null;
		 hostAddress = null;
		 groupId = null;
		 roleId = null;
		 roleType = null;
		 loginId = null;
		 password = null;
		 emailId = null;
		 alternateEmailId = null;
		 firstName = null;
		 middleName = null;
		 lastName = null;
		 gender = null;
		 dob = null;
		 telNo = null;
		 fax = null;
		 mobile = null;
		 address1 = null;
		 address2 = null;
		 address3 = null;
		 street = null;
		 city = null;
		 countryCode = null;
		 state = null;
		 zip = null;
		 education = null;
		 industry = null;
		 income = null;
		 marketingRef = null;
		 periodicUpdates = null;
	
		 passportNo = null;
		 passportIssuePlace = null;
		 passportExpiryDt = null;
		 passportNationality = null;
		 mrzNo = null;
		 passportPersonalNo = null;
		 drivingLicenseNo = null;
	
		 address4 = null;
		 address5 = null;
	
		 nin = null;
		 sin = null;
		 
		 recvCountryCode = null;
		 	 
		 smsFlag = "S";
		 recvAddFlag = "Y";
		 accAddFlag = "Y";    		
		 txnBlockFlag = "N";		
		 regAuthStatus = "A";		
		 regRecordStatus = "U";  
		 regMakerRemarks = ""; 
		 regCheckerRemarks = "";
		 txnAuthStatus = "A";   
		 txnRecordStatus = "U";  
		 txnMakerRemarks = "";  
		 txnCheckerRemarks = "";
		 theme = "Default";
		 language = "en";
		 timeZone = "+05:30";
		 ssn = "";
		 txnBounced = "0";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RGUserDataObj ["
				+ (loggedInGroupId != null ? "loggedInGroupId="
						+ loggedInGroupId + ", " : "")
				+ (loggedInEmailId != null ? "loggedInEmailId="
						+ loggedInEmailId + ", " : "")
				+ (hostAddress != null ? "hostAddress=" + hostAddress + ", "
						: "")
				+ (groupId != null ? "groupId=" + groupId + ", " : "")
				+ (roleId != null ? "roleId=" + roleId + ", " : "")
				+ (roleType != null ? "roleType=" + roleType + ", " : "")
				+ (loginId != null ? "loginId=" + loginId + ", " : "")
				+ (password != null ? "password=" + password + ", " : "")
				+ (emailId != null ? "emailId=" + emailId + ", " : "")
				+ (alternateEmailId != null ? "alternateEmailId="
						+ alternateEmailId + ", " : "")
				+ (firstName != null ? "firstName=" + firstName + ", " : "")
				+ (middleName != null ? "middleName=" + middleName + ", " : "")
				+ (lastName != null ? "lastName=" + lastName + ", " : "")
				+ (gender != null ? "gender=" + gender + ", " : "")
				+ (dob != null ? "dob=" + dob + ", " : "")
				+ (telNo != null ? "telNo=" + telNo + ", " : "")
				+ (fax != null ? "fax=" + fax + ", " : "")
				+ (mobile != null ? "mobile=" + mobile + ", " : "")
				+ (address1 != null ? "address1=" + address1 + ", " : "")
				+ (address2 != null ? "address2=" + address2 + ", " : "")
				+ (address3 != null ? "address3=" + address3 + ", " : "")
				+ (street != null ? "street=" + street + ", " : "")
				+ (city != null ? "city=" + city + ", " : "")
				+ (countryCode != null ? "countryCode=" + countryCode + ", "
						: "")
				+ (state != null ? "state=" + state + ", " : "")
				+ (zip != null ? "zip=" + zip + ", " : "")
				+ (education != null ? "education=" + education + ", " : "")
				+ (industry != null ? "industry=" + industry + ", " : "")
				+ (income != null ? "income=" + income + ", " : "")
				+ (marketingRef != null ? "marketingRef=" + marketingRef + ", "
						: "")
				+ (periodicUpdates != null ? "periodicUpdates="
						+ periodicUpdates + ", " : "")
				+ (passportNo != null ? "passportNo=" + passportNo + ", " : "")
				+ (passportIssuePlace != null ? "passportIssuePlace="
						+ passportIssuePlace + ", " : "")
				+ (passportExpiryDt != null ? "passportExpiryDt="
						+ passportExpiryDt + ", " : "")
				+ (passportNationality != null ? "passportNationality="
						+ passportNationality + ", " : "")
				+ (mrzNo != null ? "mrzNo=" + mrzNo + ", " : "")
				+ (passportPersonalNo != null ? "passportPersonalNo="
						+ passportPersonalNo + ", " : "")
				+ (drivingLicenseNo != null ? "drivingLicenseNo="
						+ drivingLicenseNo + ", " : "")
				+ (address4 != null ? "address4=" + address4 + ", " : "")
				+ (address5 != null ? "address5=" + address5 + ", " : "")
				+ (nin != null ? "nin=" + nin + ", " : "")
				+ (sin != null ? "sin=" + sin + ", " : "")
				+ (recvCountryCode != null ? "recvCountryCode=" + recvCountryCode + ", " : "")
				+ (smsFlag != null ? "smsFlag=" + smsFlag + ", " : "")
				+ (recvAddFlag != null ? "recvAddFlag=" + recvAddFlag + ", "
						: "")
				+ (accAddFlag != null ? "accAddFlag=" + accAddFlag + ", " : "")
				+ (txnBlockFlag != null ? "txnBlockFlag=" + txnBlockFlag + ", "
						: "")
				+ (regAuthStatus != null ? "regAuthStatus=" + regAuthStatus
						+ ", " : "")
				+ (regRecordStatus != null ? "regRecordStatus="
						+ regRecordStatus + ", " : "")
				+ (regMakerRemarks != null ? "regMakerRemarks="
						+ regMakerRemarks + ", " : "")
				+ (regCheckerRemarks != null ? "regCheckerRemarks="
						+ regCheckerRemarks + ", " : "")
				+ (txnAuthStatus != null ? "txnAuthStatus=" + txnAuthStatus
						+ ", " : "")
				+ (txnRecordStatus != null ? "txnRecordStatus="
						+ txnRecordStatus + ", " : "")
				+ (txnMakerRemarks != null ? "txnMakerRemarks="
						+ txnMakerRemarks + ", " : "")
				+ (txnCheckerRemarks != null ? "txnCheckerRemarks="
						+ txnCheckerRemarks + ", " : "")
				+ (theme != null ? "theme=" + theme + ", " : "")
				+ (language != null ? "language=" + language + ", " : "")
				+ (timeZone != null ? "timeZone=" + timeZone + ", " : "")
				+ (ssn != null ? "ssn=" + ssn + ", " : "")
				+ (txnBounced != null ? "txnBounced=" + txnBounced : "") + "]";
	}



	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}



	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}



	
}
