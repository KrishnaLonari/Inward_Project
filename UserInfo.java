/*
 * @(#)UserInfo.java	05-Mar-2012
 *
 * Copyright 2012. All Rights Reserved.
 *
 * This software is the proprietary information of Avenues*******.
 * Use is subject to license terms.
 *
 */
package com.api.remitGuru.component.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.api.remitGuru.web.controller.RGUserController;

/**
 * This is a Info class for RGUser
 *
 *
 *
 * @author  *******
 * @version 	1.00, 05-Mar-2012
 */

public class UserInfo  implements java.io.Serializable {

	private Hashtable extraParam = new Hashtable();	
	private String userId;
	private String roleId;
	private String roleName;
	private String loginId;
	private String password;
	private String nickName;
	private String emailId;
	private String reportsTo;
	private String registrationDate;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private String dob;

	private String mobile;
	private String state;
	private String countryCode; //Registration Country Code
	private String marketingRef;
	private String language;
	private String loginulId;
	private String targetURL;
	
	private String hostAddress;	
	private String loginTime;
	private String lastLoginTime;
	private String ulStatus;				
	private String userAgent;				
	private String accept;					
	private String acceptLanguage;  		
	private String acceptCharSet;   		
	private String acceptEncoding;  		
	private String connection;				
	private String host;				
	private String referer;		
	private String sessionId;
	
	private boolean forceChangePassword = false;
	private String isActive;
	private String isLocked;
	private String startDateofAccValidity;
	private String endDateofAccValidity;
	private String noOfAttempts = "0"; //used to store no of login attempts
	
	private boolean isLoggedIn = false;
	private boolean isRegistered = false;
	private boolean isVerified = false;

	private String groupId;
	private String groupName;
	private String loginSrNo;

	private boolean licence = false;
	private String theme;
	
	private String roleType; 
	private String country;   //Registration Country
	private String isNegativeCountry; //Registration Country is negative flag
	
	private String txnCountryCode;
	private String txnCountry;
	
	private String txnCurrencyCode;
	private String txnCurrency;

	private String recvCountryCode;
	private String recvCountry;

	private String recvCurrencyCode;
	private String recvCurrency;	

	private String refererPage = "";
	private String pageName;
	
	private String timeZone;
	
	private String qt_txnCountryCode = "";
	private String qt_txnCurrencyCode = "";
	private String qt_txnAmount = "";
	private String qt_txnParam = "";
	
	private String registrationFlag = "";
	private String regAuthStatus = "";
	private String regRecordStatus = "";
	private String branchCode = "";
	
	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	//Ip data added
	private ArrayList userIPData = null;
	private String ipAddress = null;
	
	public String getParam(String paramName) {
		String val = "";
		try
		{
			val = extraParam.get(paramName).toString();
		}catch(Throwable t){
			val = "";
		}
		return val;
	}
	
	public Hashtable returnParam() {
		return extraParam;
	}

	public void addParam(String paramName,String paramValue) {
		this.extraParam.put(paramName, paramValue);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public ArrayList getUserIPData() {
		return userIPData;
	}

	public void setUserIPData(ArrayList userIPData) {
		this.userIPData = userIPData;
	}

	/**
	 * @return the txnCurrencyCode
	 */
	public String getTxnCurrencyCode() {
		return txnCurrencyCode;
	}

	/**
	 * @param txnCurrencyCode the txnCurrencyCode to set
	 */
	public void setTxnCurrencyCode(String txnCurrencyCode) {
		this.txnCurrencyCode = txnCurrencyCode;
	}

	/**
	 * @return the txnCurrency
	 */
	public String getTxnCurrency() {
		return txnCurrency;
	}

	/**
	 * @param txnCurrency the txnCurrency to set
	 */
	public void setTxnCurrency(String txnCurrency) {
		this.txnCurrency = txnCurrency;
	}

	private String txnIsNegativeCountry;
	
	private String webName;

	//Merchant Payment Parameter
	private boolean isPaymentTxn = false;

	private String merchGroupId;
	private String merchUserId;
	private String merchLoginid;
	private String merchNickName;
	private String merchRoleId;
	private String merchRoleType;

	private String merchFirstName;
	private String merchMiddleName;
	private String merchLastName;
	private String merchTxnAmount;
	private String merchTxnSendingAmount;
	private String merchTxnDestinationAmount;
	private String merchTxnSendMode;
	private String merchTxnProgramCode;
	private String merchantParam;

	private String merchSendCountry;
	private String merchSendCurrency;
	private String merchRecvCountry;
	private String merchRecvCurrency;

	private String merchOrderNo;
	private String merchMessage;
	private String merchTxnPurpose;
	private String merchSuccessURL;
	private String merchFailURL;
	private String merchRequestId;
	private String merchRequestIdentity;
	
	//End Merchant Payment Parameter
	
	public int logoutUser(String ulId, String groupId)
	{     
		RGUserController rgUserCntrl = new RGUserController();
		int insertResult		= -1;
		
		try
		{
			insertResult = rgUserCntrl.logoutUser(ulId, groupId);
		}
		catch(Throwable t)
		{
			Logger.log("Error in logoutUser", "UserInfo.java", "User", t, Logger.CRITICAL);
			insertResult = -1;
		}
		
		doLogout();

		return insertResult;
	} 
	
	public boolean doLogout() {

		//groupId	= null;			
		userId		= null;
		roleId		= null;
		loginId		= null;
		emailId		= null;
		registrationDate = null;
		firstName	= null;
		middleName	= null;
		lastName	= null;
		gender		= null;
		dob			= null;
		lastLoginTime = null;
		forceChangePassword = true;	

		isActive	= null;
		isLocked	= null;
		mobile		= null;
		state		= null;
		countryCode	= null;
		marketingRef= null;
		theme		= null;
		language	= null;

		isLoggedIn	= false;
		isRegistered= false;
		isVerified	= false;
		
		noOfAttempts= "0";
		
		isPaymentTxn = false;

		merchGroupId 	= null;
		merchUserId		= null;
		merchLoginid	= null;
		merchNickName	= null;
		merchRoleId		= null;
		merchRoleType	= null;

		merchFirstName	= null;
		merchMiddleName	= null;
		merchLastName	= null;
		merchTxnAmount	= null;
		merchTxnSendMode= null;	
		merchantParam	= null;

		merchSendCountry = null;
		merchSendCurrency= null;
		merchRecvCountry = null;
		merchRecvCurrency= null;

		merchOrderNo	= null;
		merchMessage	= null;
		
		userIPData		= null;
		ipAddress		= null;
		extraParam		= new Hashtable();
		branchCode  	= null;
		
		return true;
	}
	
	public boolean isGroupExists(String groupId)
	{
		RGUserController rgUserCntrl = new RGUserController();
		boolean isGroupIdProper = false;
		
		try
		{
			isGroupIdProper = rgUserCntrl.isGroupExists(groupId);
		}
		catch(Throwable t)
		{
			isGroupIdProper	= false;
		}
		
		return isGroupIdProper;
	}	 
	
	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public boolean getLicence() {
		return this.licence;
	}

	public void setLicence(boolean licence) {
		this.licence = licence;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getReportsTo() {
		return this.reportsTo;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getRegistrationDate() {
		return this.registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public boolean getForceChangePassword() {
		return this.forceChangePassword;
	}

	public void setForceChangePassword(boolean forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public String getStartDateofAccValidity() {
		return this.startDateofAccValidity;
	}

	public void setStartDateofAccValidity(String startDateofAccValidity) {
		this.startDateofAccValidity = startDateofAccValidity;
	}
	
	public String getEndDateofAccValidity() {
		return this.endDateofAccValidity;
	}

	public void setEndDateofAccValidity(String endDateofAccValidity) {
		this.endDateofAccValidity = endDateofAccValidity;
	}

	public boolean getIsLoggedIn() {
		return this.isLoggedIn;
	}

	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public boolean getIsRegistered() {
		return this.isRegistered;
	}

	public void setIsRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public boolean getIsVerified() {
		return this.isVerified;
	}

	public void setIsVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getLoginSrNo() {
		return this.loginSrNo;
	}

	public void setLoginSrNo(String loginSrNo) {
		this.loginSrNo = loginSrNo;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setMarketingRef(String marketingRef) {
		this.marketingRef = marketingRef;
	}

	public String getMarketingRef() {
		return marketingRef;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setLoginulId(String loginulId) {
		this.loginulId = loginulId;
	}

	public String getLoginulId() {
		return loginulId;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginTime() {
		return loginTime;
	}
	
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setUlStatus(String ulStatus) {
		this.ulStatus = ulStatus;
	}

	public String getUlStatus() {
		return ulStatus;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getAccept() {
		return accept;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptCharSet(String acceptCharSet) {
		this.acceptCharSet = acceptCharSet;
	}

	public String getAcceptCharSet() {
		return acceptCharSet;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getConnection() {
		return connection;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getReferer() {
		return referer;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setIsNegativeCountry(String isNegativeCountry) {
		this.isNegativeCountry = isNegativeCountry;
	}

	public String getIsNegativeCountry() {
		return isNegativeCountry;
	}
	
	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getWebName() {
		return webName;
	}

	public void setTxnCountryCode(String txnCountryCode) {
		this.txnCountryCode = txnCountryCode;
	}

	public String getTxnCountryCode() {
		return txnCountryCode;
	}

	public void setTxnCountry(String txnCountry) {
		this.txnCountry = txnCountry;
	}

	public String getTxnCountry() {
		return txnCountry;
	}

	public void setTxnIsNegativeCountry(String txnIsNegativeCountry) {
		this.txnIsNegativeCountry = txnIsNegativeCountry;
	}

	public String getTxnIsNegativeCountry() {
		return txnIsNegativeCountry;
	}

	/**
	 * @param noOfAttempts the noOfAttempts to set
	 */
	public void setNoOfAttempts(String noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}

	/**
	 * @return the noOfAttempts
	 */
	public String getNoOfAttempts() {
		return noOfAttempts;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	
	//Merchant Payment Param
	/**
	 * @return the isPaymentTxn
	 */
	public boolean isPaymentTxn() {
		return isPaymentTxn;
	}

	/**
	 * @param isPaymentTxn the isPaymentTxn to set
	 */
	public void setPaymentTxn(boolean isPaymentTxn) {
		this.isPaymentTxn = isPaymentTxn;
	}

	/**
	 * @return the merchGroupId
	 */
	public String getMerchGroupId() {
		return merchGroupId;
	}

	/**
	 * @param merchGroupId the merchGroupId to set
	 */
	public void setMerchGroupId(String merchGroupId) {
		this.merchGroupId = merchGroupId;
	}

	/**
	 * @return the merchUserId
	 */
	public String getMerchUserId() {
		return merchUserId;
	}

	/**
	 * @param merchUserId the merchUserId to set
	 */
	public void setMerchUserId(String merchUserId) {
		this.merchUserId = merchUserId;
	}

	/**
	 * @return the merchLoginid
	 */
	public String getMerchLoginid() {
		return merchLoginid;
	}

	/**
	 * @param merchLoginid the merchLoginid to set
	 */
	public void setMerchLoginid(String merchLoginid) {
		this.merchLoginid = merchLoginid;
	}

	/**
	 * @return the merchNickName
	 */
	public String getMerchNickName() {
		return merchNickName;
	}

	/**
	 * @param merchNickName the merchNickName to set
	 */
	public void setMerchNickName(String merchNickName) {
		this.merchNickName = merchNickName;
	}

	/**
	 * @return the merchRoleId
	 */
	public String getMerchRoleId() {
		return merchRoleId;
	}

	/**
	 * @param merchRoleId the merchRoleId to set
	 */
	public void setMerchRoleId(String merchRoleId) {
		this.merchRoleId = merchRoleId;
	}

	/**
	 * @return the merchRoleType
	 */
	public String getMerchRoleType() {
		return merchRoleType;
	}

	/**
	 * @param merchRoleType the merchRoleType to set
	 */
	public void setMerchRoleType(String merchRoleType) {
		this.merchRoleType = merchRoleType;
	}

	/**
	 * @return the merchFirstName
	 */
	public String getMerchFirstName() {
		return merchFirstName;
	}

	/**
	 * @param merchFirstName the merchFirstName to set
	 */
	public void setMerchFirstName(String merchFirstName) {
		this.merchFirstName = merchFirstName;
	}

	/**
	 * @return the merchMiddleName
	 */
	public String getMerchMiddleName() {
		return merchMiddleName;
	}

	/**
	 * @param merchMiddleName the merchMiddleName to set
	 */
	public void setMerchMiddleName(String merchMiddleName) {
		this.merchMiddleName = merchMiddleName;
	}

	/**
	 * @return the merchLastName
	 */
	public String getMerchLastName() {
		return merchLastName;
	}

	/**
	 * @param merchLastName the merchLastName to set
	 */
	public void setMerchLastName(String merchLastName) {
		this.merchLastName = merchLastName;
	}

	/**
	 * @return the merchTxnAmount
	 */
	public String getMerchTxnAmount() {
		return merchTxnAmount;
	}

	/**
	 * @param merchTxnDestinationAmount the merchTxnDestinationAmount to set
	 */
	public void setMerchTxnDestinationAmount(String merchTxnDestinationAmount) {
		this.merchTxnDestinationAmount = merchTxnDestinationAmount;
	}

	/**
	 * @return the merchTxnDestinationAmount
	 */
	public String getMerchTxnDestinationAmount() {
		return merchTxnDestinationAmount;
	}

	/**
	 * @param merchTxnAmount the merchTxnAmount to set
	 */
	public void setMerchTxnAmount(String merchTxnAmount) {
		this.merchTxnAmount = merchTxnAmount;
	}

	/**
	 * @param merchTxnSendingAmount the merchTxnSendingAmount to set
	 */
	public void setMerchTxnSendingAmount(String merchTxnSendingAmount) {
		this.merchTxnSendingAmount = merchTxnSendingAmount;
	}

	/**
	 * @return the merchTxnSendingAmount
	 */
	public String getMerchTxnSendingAmount() {
		return merchTxnSendingAmount;
	}

	/**
	 * @return the merchTxnSendMode
	 */
	public String getMerchTxnSendMode() {
		return merchTxnSendMode;
	}

	/**
	 * @param merchTxnSendMode the merchTxnSendMode to set
	 */
	public void setMerchTxnSendMode(String merchTxnSendMode) {
		this.merchTxnSendMode = merchTxnSendMode;
	}

	/**
	 * @param merchTxnProgramCode the merchTxnProgramCode to set
	 */
	public void setMerchTxnProgramCode(String merchTxnProgramCode) {
		this.merchTxnProgramCode = merchTxnProgramCode;
	}

	/**
	 * @return the merchTxnProgramCode
	 */
	public String getMerchTxnProgramCode() {
		return merchTxnProgramCode;
	}

	/**
	 * @return the merchantParam
	 */
	public String getMerchantParam() {
		return merchantParam;
	}

	/**
	 * @param merchantParam the merchantParam to set
	 */
	public void setMerchantParam(String merchantParam) {
		this.merchantParam = merchantParam;
	}

	/**
	 * @return the merchSendCountry
	 */
	public String getMerchSendCountry() {
		return merchSendCountry;
	}

	/**
	 * @param merchSendCountry the merchSendCountry to set
	 */
	public void setMerchSendCountry(String merchSendCountry) {
		this.merchSendCountry = merchSendCountry;
	}

	/**
	 * @return the merchSendCurrency
	 */
	public String getMerchSendCurrency() {
		return merchSendCurrency;
	}

	/**
	 * @param merchSendCurrency the merchSendCurrency to set
	 */
	public void setMerchSendCurrency(String merchSendCurrency) {
		this.merchSendCurrency = merchSendCurrency;
	}

	/**
	 * @return the merchRecvCountry
	 */
	public String getMerchRecvCountry() {
		return merchRecvCountry;
	}

	/**
	 * @param merchRecvCountry the merchRecvCountry to set
	 */
	public void setMerchRecvCountry(String merchRecvCountry) {
		this.merchRecvCountry = merchRecvCountry;
	}

	/**
	 * @return the merchRecvCurrency
	 */
	public String getMerchRecvCurrency() {
		return merchRecvCurrency;
	}

	/**
	 * @param merchRecvCurrency the merchRecvCurrency to set
	 */
	public void setMerchRecvCurrency(String merchRecvCurrency) {
		this.merchRecvCurrency = merchRecvCurrency;
	}

	/**
	 * @return the merchOrderNo
	 */
	public String getMerchOrderNo() {
		return merchOrderNo;
	}

	/**
	 * @param merchOrderNo the merchOrderNo to set
	 */
	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}
	//End Merchant Payment Param

	/**
	 * @param merchMessage the merchMessage to set
	 */
	public void setMerchMessage(String merchMessage) {
		this.merchMessage = merchMessage;
	}

	/**
	 * @return the merchMessage
	 */
	public String getMerchMessage() {
		return merchMessage;
	}

	/**
	 * @param merchTxnPurpose the merchTxnPurpose to set
	 */
	public void setMerchTxnPurpose(String merchTxnPurpose) {
		this.merchTxnPurpose = merchTxnPurpose;
	}

	/**
	 * @return the merchTxnPurpose
	 */
	public String getMerchTxnPurpose() {
		return merchTxnPurpose;
	}

	/**
	 * @param merchSuccessURL the merchSuccessURL to set
	 */
	public void setMerchSuccessURL(String merchSuccessURL) {
		this.merchSuccessURL = merchSuccessURL;
	}

	/**
	 * @return the merchSuccessURL
	 */
	public String getMerchSuccessURL() {
		return merchSuccessURL;
	}

	/**
	 * @param merchFailURL the merchFailURL to set
	 */
	public void setMerchFailURL(String merchFailURL) {
		this.merchFailURL = merchFailURL;
	}

	/**
	 * @return the merchFailURL
	 */
	public String getMerchFailURL() {
		return merchFailURL;
	}

	/**
	 * @param targetURL the targetURL to set
	 */
	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	/**
	 * @return the targetURL
	 */
	public String getTargetURL() {
		return targetURL;
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

	/**
	 * @return the recvCountry
	 */
	public String getRecvCountry() {
		return recvCountry;
	}

	/**
	 * @param recvCountry the recvCountry to set
	 */
	public void setRecvCountry(String recvCountry) {
		this.recvCountry = recvCountry;
	}

	/**
	 * @return the recvCurrencyCode
	 */
	public String getRecvCurrencyCode() {
		return recvCurrencyCode;
	}

	/**
	 * @param recvCurrencyCode the recvCurrencyCode to set
	 */
	public void setRecvCurrencyCode(String recvCurrencyCode) {
		this.recvCurrencyCode = recvCurrencyCode;
	}

	/**
	 * @return the recvCurrency
	 */
	public String getRecvCurrency() {
		return recvCurrency;
	}

	/**
	 * @param recvCurrency the recvCurrency to set
	 */
	public void setRecvCurrency(String recvCurrency) {
		this.recvCurrency = recvCurrency;
	}

	/**
	 * @param refererPage the refererPage to set
	 */
	public void setRefererPage(String refererPage) {
		this.refererPage = refererPage;
	}

	/**
	 * @return the refererPage
	 */
	public String getRefererPage() {
		return refererPage;
	}

	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
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
	
	/**
	 * @return the qt_txnCountryCode
	 */
	public String getQt_txnCountryCode() {
		return qt_txnCountryCode;
	}

	/**
	 * @param qt_txnCountryCode the qt_txnCountryCode to set
	 */
	public void setQt_txnCountryCode(String qt_txnCountryCode) {
		this.qt_txnCountryCode = qt_txnCountryCode;
	}

	/**
	 * @return the qt_txnCurrencyCode
	 */
	public String getQt_txnCurrencyCode() {
		return qt_txnCurrencyCode;
	}

	/**
	 * @param qt_txnCurrencyCode the qt_txnCurrencyCode to set
	 */
	public void setQt_txnCurrencyCode(String qt_txnCurrencyCode) {
		this.qt_txnCurrencyCode = qt_txnCurrencyCode;
	}

	/**
	 * @return the qt_txnAmount
	 */
	public String getQt_txnAmount() {
		return qt_txnAmount;
	}

	/**
	 * @param qt_txnAmount the qt_txnAmount to set
	 */
	public void setQt_txnAmount(String qt_txnAmount) {
		this.qt_txnAmount = qt_txnAmount;
	}

	/**
	 * @return the qt_txnParam
	 */
	public String getQt_txnParam() {
		return qt_txnParam;
	}

	/**
	 * @param qt_txnParam the qt_txnParam to set
	 */
	public void setQt_txnParam(String qt_txnParam) {
		this.qt_txnParam = qt_txnParam;
	}
	
	/**
	 * @return the registrationFlag
	 */
	public String getRegistrationFlag() {
		return registrationFlag;
	}

	/**
	 * @param registrationFlag the registrationFlag to set
	 */
	public void setRegistrationFlag(String registrationFlag) {
		this.registrationFlag = registrationFlag;
	}
	
	public String getRegAuthStatus() {
		return regAuthStatus;
	}

	public void setRegAuthStatus(String regAuthStatus) {
		this.regAuthStatus = regAuthStatus;
	}

	public String getRegRecordStatus() {
		return regRecordStatus;
	}

	public void setRegRecordStatus(String regRecordStatus) {
		this.regRecordStatus = regRecordStatus;
	}
	
	public String getMerchRequestId() {
		return merchRequestId;
	}

	public void setMerchRequestId(String merchRequestId) {
		this.merchRequestId = merchRequestId;
	}
	
	public String getMerchRequestIdentity() {
		return merchRequestIdentity;
	}

	public void setMerchRequestIdentity(String merchRequestIdentity) {
		this.merchRequestIdentity = merchRequestIdentity;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", roleId=" + roleId
				+ ", roleName=" + roleName + ", loginId=" + loginId
				+ ", password=" + password + ", nickName=" + nickName
				+ ", emailId=" + emailId + ", reportsTo=" + reportsTo
				+ ", registrationDate=" + registrationDate + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", gender=" + gender + ", dob=" + dob
				+ ", mobile=" + mobile + ", state=" + state + ", countryCode="
				+ countryCode + ", marketingRef=" + marketingRef
				+ ", language=" + language + ", loginulId=" + loginulId
				+ ", targetURL=" + targetURL + ", hostAddress=" + hostAddress
				+ ", loginTime=" + loginTime + ", lastLoginTime="
				+ lastLoginTime + ", ulStatus=" + ulStatus + ", userAgent="
				+ userAgent + ", accept=" + accept + ", acceptLanguage="
				+ acceptLanguage + ", acceptCharSet=" + acceptCharSet
				+ ", acceptEncoding=" + acceptEncoding + ", connection="
				+ connection + ", host=" + host + ", referer=" + referer
				+ ", sessionId=" + sessionId + ", forceChangePassword="
				+ forceChangePassword + ", isActive=" + isActive
				+ ", isLocked=" + isLocked + ", startDateofAccValidity="
				+ startDateofAccValidity + ", endDateofAccValidity="
				+ endDateofAccValidity + ", noOfAttempts=" + noOfAttempts
				+ ", isLoggedIn=" + isLoggedIn + ", isRegistered="
				+ isRegistered + ", isVerified=" + isVerified + ", groupId="
				+ groupId + ", groupName=" + groupName + ", loginSrNo="
				+ loginSrNo + ", licence=" + licence + ", theme=" + theme
				+ ", roleType=" + roleType + ", country=" + country
				+ ", isNegativeCountry=" + isNegativeCountry
				+ ", txnCountryCode=" + txnCountryCode + ", txnCountry="
				+ txnCountry + ", txnCurrencyCode=" + txnCurrencyCode
				+ ", txnCurrency=" + txnCurrency + ", recvCountryCode="
				+ recvCountryCode + ", recvCountry=" + recvCountry
				+ ", recvCurrencyCode=" + recvCurrencyCode + ", recvCurrency="
				+ recvCurrency + ", refererPage=" + refererPage + ", pageName="
				+ pageName + ", timeZone=" + timeZone + ", qt_txnCountryCode="
				+ qt_txnCountryCode + ", qt_txnCurrencyCode="
				+ qt_txnCurrencyCode + ", qt_txnAmount=" + qt_txnAmount
				+ ", qt_txnParam=" + qt_txnParam+  ", registrationFlag=" + registrationFlag
				+ ",regAuthStatus=" + regAuthStatus + ", regRecordStatus=" + regRecordStatus
				+ ", txnIsNegativeCountry=" + txnIsNegativeCountry
				+ ", webName=" + webName + ", isPaymentTxn=" + isPaymentTxn
				+ ", merchGroupId=" + merchGroupId + ", merchUserId="
				+ merchUserId + ", merchLoginid=" + merchLoginid
				+ ", merchNickName=" + merchNickName + ", merchRoleId="
				+ merchRoleId + ", merchRoleType=" + merchRoleType
				+ ", merchFirstName=" + merchFirstName + ", merchMiddleName="
				+ merchMiddleName + ", merchLastName=" + merchLastName
				+ ", merchTxnAmount=" + merchTxnAmount
				+ ", merchTxnSendingAmount=" + merchTxnSendingAmount
				+ ", merchTxnDestinationAmount=" + merchTxnDestinationAmount
				+ ", merchTxnSendMode=" + merchTxnSendMode
				+ ", merchTxnProgramCode=" + merchTxnProgramCode
				+ ", merchantParam=" + merchantParam + ", merchSendCountry="
				+ merchSendCountry + ", merchSendCurrency=" + merchSendCurrency
				+ ", merchRecvCountry=" + merchRecvCountry
				+ ", merchRecvCurrency=" + merchRecvCurrency
				+ ", merchOrderNo=" + merchOrderNo + ", merchMessage="
				+ merchMessage + ", merchTxnPurpose=" + merchTxnPurpose
				+ ", merchSuccessURL=" + merchSuccessURL + ", merchFailURL="
				+ merchFailURL + ", merchRequestId=" + merchRequestId
				+ ", merchRequestIdentity=" + merchRequestIdentity 
				+ ", userIPData="+userIPData+", ipAddress="+ipAddress+"]";
	}

	

}
