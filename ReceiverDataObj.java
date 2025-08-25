package com.api.remitGuru.component.util;

public class ReceiverDataObj implements java.io.Serializable {
		
	private String loggedInGroupId	= null;
	private String loggedInUserId	= null;
	private String loggedInLoginId	= null;
	private String loggedInRoleId	= null;
	
	private String groupId			= null;
	private String userId			= null;
	private String loginId			= null;
	private String nickName			= null;
	private String roleIdRecv		= null;
	private String roleType			= null;
	private String firstName		= null;
	private String middleName		= null;
	private String lastName			= null;
	private String gender			= null;
	private String dob				= null;
	private String address1			= null;
	private String countryCode		= null;
	private String state			= null;
	private String city				= null;
	private String pincode			= null;
	private String emailId			= null;
	private String alternateEmailId	= null;	
	private String resPhone			= null;
	private String offPhone			= null;
	private String mobile			= null;
	private String fax				= null;
	private String receiveModeCode	= null;
	private String accHolderName	= null;
	private String accNumber		= null;
	private String accType			= null;
	private String micr				= null;
	private String bankId			= null;
	private String bankName			= null;
	private String bankBranch		= null;
	private String bankCity			= null;
	private String bankState		= null;
	private String isPartnerBank	= null;
	private String nearestLogisticCity	= null;
	private String ipaddress		= null;
	private String roleIdSender		= null;
	private String groupIdRecv		= null;
	private String loginIdRecv		= null;
	private String isActive			= "Y";
	
	private String sendAmount = null;
	private String sendCountryCode = null;
	private String sendCurrencyCode = null;
	private String status = null;
	private String sendFirstName = null;
	private String sendLastName = null;
	private String sendMobileNo = null;
	private String remark = null;
	private String uniqueIdentifierType = null;
	private String uniqueIdentifierValue = null;
	private String relationship = null;
	private String field1 = null;
	private String field2 = null;
	private String purpose = null;
	private String subPurpose = null;
	private String currencyCode = null;
	private String bankDetails = null;
	private String recvType = null;
	private String branchCode = null;	
	private String tellerLoginId = null;	
	private String channel = null;	
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the roleIdRecv
	 */
	public String getRoleIdRecv() {
		return roleIdRecv;
	}
	/**
	 * @param roleIdRecv the roleIdRecv to set
	 */
	public void setRoleIdRecv(String roleIdRecv) {
		this.roleIdRecv = roleIdRecv;
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
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}
	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		this.pincode = pincode;
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
	 * @return the resPhone
	 */
	public String getResPhone() {
		return resPhone;
	}
	/**
	 * @param resPhone the resPhone to set
	 */
	public void setResPhone(String resPhone) {
		this.resPhone = resPhone;
	}
	/**
	 * @return the offPhone
	 */
	public String getOffPhone() {
		return offPhone;
	}
	/**
	 * @param offPhone the offPhone to set
	 */
	public void setOffPhone(String offPhone) {
		this.offPhone = offPhone;
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
	 * @return the receiveModeCode
	 */
	public String getReceiveModeCode() {
		return receiveModeCode;
	}
	/**
	 * @param receiveModeCode the receiveModeCode to set
	 */
	public void setReceiveModeCode(String receiveModeCode) {
		this.receiveModeCode = receiveModeCode;
	}
	/**
	 * @return the accHolderName
	 */
	public String getAccHolderName() {
		return accHolderName;
	}
	/**
	 * @param accHolderName the accHolderName to set
	 */
	public void setAccHolderName(String accHolderName) {
		this.accHolderName = accHolderName;
	}
	/**
	 * @return the accNumber
	 */
	public String getAccNumber() {
		return accNumber;
	}
	/**
	 * @param accNumber the accNumber to set
	 */
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	/**
	 * @return the accType
	 */
	public String getAccType() {
		return accType;
	}
	/**
	 * @param accType the accType to set
	 */
	public void setAccType(String accType) {
		this.accType = accType;
	}
	/**
	 * @return the micr
	 */
	public String getMicr() {
		return micr;
	}
	/**
	 * @param micr the micr to set
	 */
	public void setMicr(String micr) {
		this.micr = micr;
	}
	/**
	 * @return the bankId
	 */
	public String getBankId() {
		return bankId;
	}
	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the bankBranch
	 */
	public String getBankBranch() {
		return bankBranch;
	}
	/**
	 * @param bankBranch the bankBranch to set
	 */
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	/**
	 * @return the bankCity
	 */
	public String getBankCity() {
		return bankCity;
	}
	/**
	 * @param bankCity the bankCity to set
	 */
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	/**
	 * @return the bankState
	 */
	public String getBankState() {
		return bankState;
	}
	/**
	 * @param bankState the bankState to set
	 */
	public void setBankState(String bankState) {
		this.bankState = bankState;
	}
	/**
	 * @return the isPartnerBank
	 */
	public String getIsPartnerBank() {
		return isPartnerBank;
	}
	/**
	 * @param isPartnerBank the isPartnerBank to set
	 */
	public void setIsPartnerBank(String isPartnerBank) {
		this.isPartnerBank = isPartnerBank;
	}
	/**
	 * @return the nearestLogisticCity
	 */
	public String getNearestLogisticCity() {
		return nearestLogisticCity;
	}
	/**
	 * @param nearestLogisticCity the nearestLogisticCity to set
	 */
	public void setNearestLogisticCity(String nearestLogisticCity) {
		this.nearestLogisticCity = nearestLogisticCity;
	}
	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}
	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	/**
	 * @return the roleIdSender
	 */
	public String getRoleIdSender() {
		return roleIdSender;
	}
	/**
	 * @param roleIdSender the roleIdSender to set
	 */
	public void setRoleIdSender(String roleIdSender) {
		this.roleIdSender = roleIdSender;
	}
	/**
	 * @return the groupIdRecv
	 */
	public String getGroupIdRecv() {
		return groupIdRecv;
	}
	/**
	 * @param groupIdRecv the groupIdRecv to set
	 */
	public void setGroupIdRecv(String groupIdRecv) {
		this.groupIdRecv = groupIdRecv;
	}
	/**
	 * @return the loginIdRecv
	 */
	public String getLoginIdRecv() {
		return loginIdRecv;
	}
	/**
	 * @param loginIdRecv the loginIdRecv to set
	 */
	public void setLoginIdRecv(String loginIdRecv) {
		this.loginIdRecv = loginIdRecv;
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
	 * @return the loggedInUserId
	 */
	public String getLoggedInUserId() {
		return loggedInUserId;
	}
	/**
	 * @param loggedInUserId the loggedInUserId to set
	 */
	public void setLoggedInUserId(String loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}
	/**
	 * @return the loggedInLoginId
	 */
	public String getLoggedInLoginId() {
		return loggedInLoginId;
	}
	/**
	 * @param loggedInLoginId the loggedInLoginId to set
	 */
	public void setLoggedInLoginId(String loggedInLoginId) {
		this.loggedInLoginId = loggedInLoginId;
	}
	/**
	 * @return the loggedInRoleId
	 */
	public String getLoggedInRoleId() {
		return loggedInRoleId;
	}
	/**
	 * @param loggedInRoleId the loggedInRoleId to set
	 */
	public void setLoggedInRoleId(String loggedInRoleId) {
		this.loggedInRoleId = loggedInRoleId;
	}
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * @return the sendAmount
	 */
	public String getSendAmount() {
		return sendAmount;
	}
	/**
	 * @param sendAmount the sendAmount to set
	 */
	public void setSendAmount(String sendAmount) {
		this.sendAmount = sendAmount;
	}
	/**
	 * @return the sendCountryCode
	 */
	public String getSendCountryCode() {
		return sendCountryCode;
	}
	/**
	 * @param sendCountryCode the sendCountryCode to set
	 */
	public void setSendCountryCode(String sendCountryCode) {
		this.sendCountryCode = sendCountryCode;
	}
	/**
	 * @return the sendCurrencyCode
	 */
	public String getSendCurrencyCode() {
		return sendCurrencyCode;
	}
	/**
	 * @param sendCurrencyCode the sendCurrencyCode to set
	 */
	public void setSendCurrencyCode(String sendCurrencyCode) {
		this.sendCurrencyCode = sendCurrencyCode;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the sendFirstName
	 */
	public String getSendFirstName() {
		return sendFirstName;
	}
	/**
	 * @param sendFirstName the sendFirstName to set
	 */
	public void setSendFirstName(String sendFirstName) {
		this.sendFirstName = sendFirstName;
	}
	/**
	 * @return the sendLastName
	 */
	public String getSendLastName() {
		return sendLastName;
	}
	/**
	 * @param sendLastName the sendLastName to set
	 */
	public void setSendLastName(String sendLastName) {
		this.sendLastName = sendLastName;
	}
	/**
	 * @return the sendMobileNo
	 */
	public String getSendMobileNo() {
		return sendMobileNo;
	}
	/**
	 * @param sendMobileNo the sendMobileNo to set
	 */
	public void setSendMobileNo(String sendMobileNo) {
		this.sendMobileNo = sendMobileNo;
	}
		
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * @return the relationship
	 */
	public String getRelationshipValue() {
		return relationship;
	}
	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationshipValue(String relationship) {
		this.relationship = relationship;
	}
	
	/**
	 * @return the field1
	 */
	public String getField1Value() {
		return field1;
	}
	/**
	 * @param field1 the field1 to set
	 */
	public void setField1Value(String field1) {
		this.field1 = field1;
	}	
	
	/**
	 * @return the field2
	 */
	public String getField2Value() {
		return field2;
	}
	/**
	 * @param field2 the field2 to set
	 */
	public void setField2Value(String field2) {
		this.field2 = field2;
	}
	

	/**
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}
	/**
	 * @param purpose the purpose to set
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	/**
	 * @return the subPurpose
	 */
	public String getSubPurpose() {
		return subPurpose;
	}
	/**
	 * @param subPurpose the subPurpose to set
	 */
	public void setSubPurpose(String subPurpose) {
		this.subPurpose = subPurpose;
	}
	
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	/**
	 * @return the bankDetails
	 */
	public String getBankDetails() {
		return bankDetails;
	}
	/**
	 * @param bankDetails the bankDetails to set
	 */
	public void setBankDetails(String bankDetails) {
		this.bankDetails = bankDetails;
	}
	
	/**
	 * @return the recvType
	 */
	public String getRecvType() {
		return recvType;
	}
	/**
	 * @param bankDetails the bankDetails to set
	 */
	public void setRecvType(String recvType) {
		this.recvType = recvType;
	}
	
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
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
	
	public void resetReceiverDataObj()
	{
		loggedInGroupId	= null;             
		loggedInUserId	= null;             
		loggedInLoginId	= null;             
		loggedInRoleId	= null;             
		                                             
		groupId			= null;                     
		userId			= null;                     
		loginId			= null;                     
		nickName		= null;                     
		roleIdRecv		= null;                     
		roleType		= null;                     
		firstName		= null;             
		middleName		= null;             
		lastName		= null;             
		gender			= null;             
		dob				= null;     
		address1		= null;             
		countryCode		= null;             
		state			= null;             
		city			= null;     
		pincode			= null;             
		emailId			= null;             
		alternateEmailId= null;	                    
		resPhone		= null;             
		offPhone		= null;             
		mobile			= null;             
		fax				= null;     
		receiveModeCode	= null;                     
		accHolderName	= null;                     
		accNumber		= null;             
		accType			= null;             
		micr			= null;  
		bankId			= null;
		bankName		= null;             
		bankBranch		= null;             
		bankCity		= null;             
		bankState		= null;             
		isPartnerBank	= null;                     
		nearestLogisticCity	= null;             
		ipaddress		= null;             
		roleIdSender	= null;             
		groupIdRecv		= null;             
		loginIdRecv		= null;             
		isActive		= "Y";
		sendAmount = null;
		sendCountryCode = null;
		sendCurrencyCode = null;
		status = null;
		sendFirstName = null;
		sendLastName = null;
		sendMobileNo = null;
		remark = null;
		uniqueIdentifierType = null;
		uniqueIdentifierValue = null;
		relationship = null;
		field1 = null;
		field2 = null;
		purpose = null;
		subPurpose = null;
		currencyCode = null;
		bankDetails = null;
		recvType  =  null;
		branchCode = null;	 
		tellerLoginId = null;		
		channel = null;	     	
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReceiverDataObj ["
				+ (loggedInGroupId != null ? "loggedInGroupId="
						+ loggedInGroupId + ", " : "")
				+ (loggedInUserId != null ? "loggedInUserId=" + loggedInUserId
						+ ", " : "")
				+ (loggedInLoginId != null ? "loggedInLoginId="
						+ loggedInLoginId + ", " : "")
				+ (loggedInRoleId != null ? "loggedInRoleId=" + loggedInRoleId
						+ ", " : "")
				+ (groupId != null ? "groupId=" + groupId + ", " : "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ (loginId != null ? "loginId=" + loginId + ", " : "")
				+ (nickName != null ? "nickName=" + nickName + ", " : "")
				+ (roleIdRecv != null ? "roleIdRecv=" + roleIdRecv + ", " : "")
				+ (roleType != null ? "roleType=" + roleType + ", " : "")
				+ (firstName != null ? "firstName=" + firstName + ", " : "")
				+ (middleName != null ? "middleName=" + middleName + ", " : "")
				+ (lastName != null ? "lastName=" + lastName + ", " : "")
				+ (gender != null ? "gender=" + gender + ", " : "")
				+ (dob != null ? "dob=" + dob + ", " : "")
				+ (address1 != null ? "address1=" + address1 + ", " : "")
				+ (countryCode != null ? "countryCode=" + countryCode + ", "
						: "")
				+ (state != null ? "state=" + state + ", " : "")
				+ (city != null ? "city=" + city + ", " : "")
				+ (pincode != null ? "pincode=" + pincode + ", " : "")
				+ (emailId != null ? "emailId=" + emailId + ", " : "")
				+ (alternateEmailId != null ? "alternateEmailId="
						+ alternateEmailId + ", " : "")
				+ (resPhone != null ? "resPhone=" + resPhone + ", " : "")
				+ (offPhone != null ? "offPhone=" + offPhone + ", " : "")
				+ (mobile != null ? "mobile=" + mobile + ", " : "")
				+ (fax != null ? "fax=" + fax + ", " : "")
				+ (receiveModeCode != null ? "receiveModeCode="
						+ receiveModeCode + ", " : "")
				+ (accHolderName != null ? "accHolderName=" + accHolderName
						+ ", " : "")
				+ (accNumber != null ? "accNumber=" + accNumber + ", " : "")
				+ (accType != null ? "accType=" + accType + ", " : "")
				+ (micr != null ? "micr=" + micr + ", " : "")
				+ (bankId != null ? "bankId=" + bankId + ", " : "")
				+ (bankName != null ? "bankName=" + bankName + ", " : "")
				+ (bankBranch != null ? "bankBranch=" + bankBranch + ", " : "")
				+ (bankCity != null ? "bankCity=" + bankCity + ", " : "")
				+ (bankState != null ? "bankState=" + bankState + ", " : "")
				+ (isPartnerBank != null ? "isPartnerBank=" + isPartnerBank
						+ ", " : "")
				+ (nearestLogisticCity != null ? "nearestLogisticCity="
						+ nearestLogisticCity + ", " : "")
				+ (ipaddress != null ? "ipaddress=" + ipaddress + ", " : "")
				+ (roleIdSender != null ? "roleIdSender=" + roleIdSender + ", "
						: "")
				+ (groupIdRecv != null ? "groupIdRecv=" + groupIdRecv + ", "
						: "")
				+ (loginIdRecv != null ? "loginIdRecv=" + loginIdRecv + ", "
						: "")
						
				+ (sendAmount != null ? "sendAmount=" + sendAmount + ", "
						: "")
				+ (sendCountryCode != null ? "sendCountryCode=" + sendCountryCode + ", "
						: "")
				+ (sendCurrencyCode != null ? "sendCurrencyCode=" + sendCurrencyCode + ", "
						: "")
				+ (status != null ? "status=" + status + ", "
						: "")
				+ (sendFirstName != null ? "sendFirstName=" + sendFirstName + ", "
						: "")
				+ (sendLastName != null ? "sendLastName=" + sendLastName + ", "
						: "")
				+ (sendMobileNo != null ? "sendMobileNo=" + sendMobileNo + ", "
						: "")						
				+ (remark != null ? "remark=" + remark + ", "
						: "")
				+ (uniqueIdentifierType != null ? "uniqueIdentifierType=" + uniqueIdentifierType + ", "
						: "")
				+ (uniqueIdentifierValue != null ? "uniqueIdentifierValue=" + uniqueIdentifierValue + ", "
						: "")
				+ (relationship != null ? "relationship=" + relationship + ", "
						: "")
				+ (field1 != null ? "field1=" + field1 + ", "
						: "")
				+ (field2 != null ? "field2=" + field2 + ", "
						: "")
				+ (isActive != null ? "isActive=" + isActive + ", "
						: "")
				+ (purpose != null ? "purpose=" + purpose + ", "
						: "")
				+ (subPurpose != null ? "subPurpose=" + subPurpose + ", "
						: "")
				+ (currencyCode != null ? "currencyCode=" + currencyCode + ", "
						: "")
				+ (bankDetails != null ? "bankDetails=" + bankDetails + ", "
						: "")
				+ (recvType != null ? "recvType=" + recvType + ", "
						: "")
				+ (branchCode != null ? "branchCode=" + branchCode + ", "
						: "")
				+ (tellerLoginId != null ? "tellerLoginId=" + tellerLoginId + ", "
						: "")
				+ (channel != null ? "channel=" + channel + ", "
						: "")
				+ "]";
	}	
}
