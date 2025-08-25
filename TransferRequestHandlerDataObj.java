/**
 * 
 */
package com.api.remitGuru.component.util;

import java.util.ArrayList;

/**
 * @author 
 *
 */
public class TransferRequestHandlerDataObj implements java.io.Serializable{
		
	private String dataFormat = null;
	private String requestData = null;
	private String responseData = null;	
	private String webName = null;	
	private String ipAddress = null;	
	private String key = null;

	private String requestId = null;
	private String requestType = null;
	private String channelId = null;
	private String clientId = null;
	private String groupId = null;
	private String loginId = null;
	private String sessionId = null;

	private String responseId = null;
	private String responseType = null;
	private String userId = null;
	private String loginulId = null;
	
	private String trdId = null;

	private String password = null;
	private String newPassword = null;
	private String noofAttempts = null;
	
	private String country = null;
	private String sendCountry = null;
	private String recvCountry = null;
	private ArrayList countryList = new ArrayList();	
	
	private String sendMode = null;		
	private ArrayList sendModeList = new ArrayList();

	private String receiver = null;		
	private ArrayList receiverList = new ArrayList();

	private String amount = null;
	private String destAmount = null;
	private String bankName = null;		
	private ArrayList bankNameList = new ArrayList();

	private String accountNo = null;
	private ArrayList accountNoList = new ArrayList();
	private String purpose = null;		
	private ArrayList purposeList = new ArrayList();
	
	private String promoCode = null;		
	private ArrayList promoCodeList = new ArrayList();

	private String gender = null;
	private String dob = null;
	private String email = null;
	private String address1 = null;
	private String address2 = null; 
	private String address3 = null; 
	private String mobileNo = null;
	private String mobileCntryCode = null;
	private String phoneNo = null;

	private String currencyRate = null;
	private String currencyCode = null;
	private String fee = null;

	private String reasonCode = null;	
	private ArrayList reasonCodeList = new ArrayList();
	private String reason = null;		
	private ArrayList reasonList = new ArrayList();

	private String rgtn = null;
	private String txnRefNumber = null;
	private ArrayList rgtnList = new ArrayList();
	private String status = null;	
	private ArrayList statusList = new ArrayList();
	
	private String bankAcc_labelCode = null;	
	private String bankAcc_valueCode = null;
	private String bankAcc_captionCode = null;
	
	private String traceNo= null;
	
	private String firstName = null;
	private String lastName = null;
	private String lastLoginTime = null;
	private String address = null;
	private String personalMessage = null;
	
	private String ulId = null;
	private String orderNo = null;	
	
	private String sendCurrency = null;
	private String statusCode = null;
	private String statusDesc = null;
	private String subStatusCode = null;
	private String subStatusDesc = null;
	private String middleName = null;
	private String bookingDate = null;
	private String processingDate = null;
	private String disbursementDate = null;
	
	private String recvNickName = null;
	private String recvMode = null;
	private String recvModeDesc = null;
	
	private String smsMobile = null;
	private String smsMobileCountryCode = null;
	private String smsMessage = null;
	private String smsRemitUsrId = null;
	private String smsRemitTxnId = null;
	private String trackingId = "";
	
	private boolean isRiskLimitCompatible = false;
	private String sendAmtMsg = null;
	private String sendAmtErrCode = null;
	
	private String roleType = null;
	private String noOfTxn = null;
	private String frequency = null;
	private String achAccount = null;
	
	private String roleId = null;
	
	private String notificationType = null;
	private String subNotification = null;
	
	private String startIndex = null;
	private String hasNext = null;
	
	private ReceiverDataObj recvDataObj;
	private RGUserDataObj rgUserDataObj;
	private TransactionDataObj txnDataObj;
	
	private String docType;
	private String docExtn;
	private String docFileName;
	private String docFileSize;
	private String docFile;
	
	private String trackingNumber;
	private String oneCardId;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String sendCity;
	private String sendState;
	private String sendZipCode;
	private String sendSSN;

	/**
	 * @return the dataFormat
	 */
	public String getDataFormat() {
		return dataFormat;
	}

	/**
	 * @param dataFormat the dataFormat to set
	 */
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	/**
	 * @return the requestData
	 */
	public String getRequestData() {
		return requestData;
	}

	/**
	 * @param requestData the requestData to set
	 */
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	/**
	 * @return the responseData
	 */
	public String getResponseData() {
		return responseData;
	}

	/**
	 * @param responseData the responseData to set
	 */
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	/**
	 * @return the webName
	 */
	public String getWebName() {
		return webName;
	}

	/**
	 * @param webName the webName to set
	 */
	public void setWebName(String webName) {
		this.webName = webName;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
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
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the responseId
	 */
	public String getResponseId() {
		return responseId;
	}

	/**
	 * @param responseId the responseId to set
	 */
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	/**
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * @param responseType the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
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
	 * @return the trdId
	 */
	public String getTrdId() {
		return trdId;
	}

	/**
	 * @param trdId the trdId to set
	 */
	public void setTrdId(String trdId) {
		this.trdId = trdId;
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
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the noofAttempts
	 */
	public String getNoofAttempts() {
		return noofAttempts;
	}

	/**
	 * @param noofAttempts the noofAttempts to set
	 */
	public void setNoofAttempts(String noofAttempts) {
		this.noofAttempts = noofAttempts;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the sendCountry
	 */
	public String getSendCountry() {
		return sendCountry;
	}

	/**
	 * @param sendCountry the sendCountry to set
	 */
	public void setSendCountry(String sendCountry) {
		this.sendCountry = sendCountry;
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
	 * @return the countryList
	 */
	public ArrayList getCountryList() {
		return countryList;
	}

	/**
	 * @param countryList the countryList to set
	 */
	public void setCountryList(ArrayList countryList) {
		this.countryList = countryList;
	}

	/**
	 * @return the sendMode
	 */
	public String getSendMode() {
		return sendMode;
	}

	/**
	 * @param sendMode the sendMode to set
	 */
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}

	/**
	 * @return the sendModeList
	 */
	public ArrayList getSendModeList() {
		return sendModeList;
	}

	/**
	 * @param sendModeList the sendModeList to set
	 */
	public void setSendModeList(ArrayList sendModeList) {
		this.sendModeList = sendModeList;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the receiverList
	 */
	public ArrayList getReceiverList() {
		return receiverList;
	}

	/**
	 * @param receiverList the receiverList to set
	 */
	public void setReceiverList(ArrayList receiverList) {
		this.receiverList = receiverList;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	/**
	 * @return the destAmount
	 */
	public String getDestAmount() {
		return destAmount;
	}

	/**
	 * @param destAmount the destAmount to set
	 */
	public void setDestAmount(String destAmount) {
		this.destAmount = destAmount;
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
	 * @return the bankNameList
	 */
	public ArrayList getBankNameList() {
		return bankNameList;
	}

	/**
	 * @param bankNameList the bankNameList to set
	 */
	public void setBankNameList(ArrayList bankNameList) {
		this.bankNameList = bankNameList;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	/**
	 * @return the accountNoList
	 */
	public ArrayList getAccountNoList() {
		return accountNoList;
	}

	/**
	 * @param accountNoList the accountNoList to set
	 */
	public void setAccountNoList(ArrayList accountNoList) {
		this.accountNoList = accountNoList;
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
	 * @return the purposeList
	 */
	public ArrayList getPurposeList() {
		return purposeList;
	}

	/**
	 * @param purposeList the purposeList to set
	 */
	public void setPurposeList(ArrayList purposeList) {
		this.purposeList = purposeList;
	}

	/**
	 * @return the promoCode
	 */
	public String getPromoCode() {
		return promoCode;
	}

	/**
	 * @param promoCode the promoCode to set
	 */
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	/**
	 * @return the promoCodeList
	 */
	public ArrayList getPromoCodeList() {
		return promoCodeList;
	}

	/**
	 * @param promoCodeList the promoCodeList to set
	 */
	public void setPromoCodeList(ArrayList promoCodeList) {
		this.promoCodeList = promoCodeList;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the mobileNo
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * @param mobileNo the mobileNo to set
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * @return the mobileCntryCode
	 */
	public String getMobileCntryCode() {
		return mobileCntryCode;
	}

	/**
	 * @param mobileCntryCode the mobileCntryCode to set
	 */
	public void setMobileCntryCode(String mobileCntryCode) {
		this.mobileCntryCode = mobileCntryCode;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @return the currencyRate
	 */
	public String getCurrencyRate() {
		return currencyRate;
	}

	/**
	 * @param currencyRate the currencyRate to set
	 */
	public void setCurrencyRate(String currencyRate) {
		this.currencyRate = currencyRate;
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
	 * @return the fee
	 */
	public String getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(String fee) {
		this.fee = fee;
	}

	/**
	 * @return the reasonCode
	 */
	public String getReasonCode() {
		return reasonCode;
	}

	/**
	 * @param reasonCode the reasonCode to set
	 */
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	/**
	 * @return the reasonCodeList
	 */
	public ArrayList getReasonCodeList() {
		return reasonCodeList;
	}

	/**
	 * @param reasonCodeList the reasonCodeList to set
	 */
	public void setReasonCodeList(ArrayList reasonCodeList) {
		this.reasonCodeList = reasonCodeList;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the reasonList
	 */
	public ArrayList getReasonList() {
		return reasonList;
	}

	/**
	 * @param reasonList the reasonList to set
	 */
	public void setReasonList(ArrayList reasonList) {
		this.reasonList = reasonList;
	}

	/**
	 * @return the rgtn
	 */
	public String getRgtn() {
		return rgtn;
	}

	/**
	 * @param rgtn the rgtn to set
	 */
	public void setRgtn(String rgtn) {
		this.rgtn = rgtn;
	}

	/**
	 * @return the rgtnList
	 */
	public ArrayList getRgtnList() {
		return rgtnList;
	}

	/**
	 * @param rgtnList the rgtnList to set
	 */
	public void setRgtnList(ArrayList rgtnList) {
		this.rgtnList = rgtnList;
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
	 * @return the statusList
	 */
	public ArrayList getStatusList() {
		return statusList;
	}

	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(ArrayList statusList) {
		this.statusList = statusList;
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
	 * @return the lastLoginTime
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the personalMessage
	 */
	public String getPersonalMessage() {
		return personalMessage;
	}

	/**
	 * @param personalMessage the personalMessage to set
	 */
	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}

	/**
	 * @return the ulId
	 */
	public String getUlId() {
		return ulId;
	}

	/**
	 * @param ulId the ulId to set
	 */
	public void setUlId(String ulId) {
		this.ulId = ulId;
	}
	
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the sendCurrency
	 */
	public String getSendCurrency() {
		return sendCurrency;
	}

	/**
	 * @param sendCurrency the sendCurrency to set
	 */
	public void setSendCurrency(String sendCurrency) {
		this.sendCurrency = sendCurrency;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusDesc
	 */
	public String getStatusDesc() {
		return statusDesc;
	}

	/**
	 * @param statusDesc the statusDesc to set
	 */
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	/**
	 * @return the subStatusCode
	 */
	public String getSubStatusCode() {
		return subStatusCode;
	}

	/**
	 * @param subStatusCode the subStatusCode to set
	 */
	public void setSubStatusCode(String subStatusCode) {
		this.subStatusCode = subStatusCode;
	}

	/**
	 * @return the subStatusDesc
	 */
	public String getSubStatusDesc() {
		return subStatusDesc;
	}

	/**
	 * @param subStatusDesc the subStatusDesc to set
	 */
	public void setSubStatusDesc(String subStatusDesc) {
		this.subStatusDesc = subStatusDesc;
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
	 * @return the bookingDate
	 */
	public String getBookingDate() {
		return bookingDate;
	}

	/**
	 * @param bookingDate the bookingDate to set
	 */
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	/**
	 * @return the processingDate
	 */
	public String getProcessingDate() {
		return processingDate;
	}

	/**
	 * @param processingDate the processingDate to set
	 */
	public void setProcessingDate(String processingDate) {
		this.processingDate = processingDate;
	}

	/**
	 * @return the disbursementDate
	 */
	public String getDisbursementDate() {
		return disbursementDate;
	}

	/**
	 * @param disbursementDate the disbursementDate to set
	 */
	public void setDisbursementDate(String disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	/**
	 * @return the recvNickName
	 */
	public String getRecvNickName() {
		return recvNickName;
	}

	/**
	 * @param recvNickName the recvNickName to set
	 */
	public void setRecvNickName(String recvNickName) {
		this.recvNickName = recvNickName;
	}

	/**
	 * @return the recvMode
	 */
	public String getRecvMode() {
		return recvMode;
	}

	/**
	 * @param recvMode the recvMode to set
	 */
	public void setRecvMode(String recvMode) {
		this.recvMode = recvMode;
	}

	/**
	 * @return the recvModeDesc
	 */
	public String getRecvModeDesc() {
		return recvModeDesc;
	}

	/**
	 * @param recvModeDesc the recvModeDesc to set
	 */
	public void setRecvModeDesc(String recvModeDesc) {
		this.recvModeDesc = recvModeDesc;
	}

	/**
	 * @return the recvDataObj
	 */
	public ReceiverDataObj getRecvDataObj() {
		return recvDataObj;
	}

	/**
	 * @param recvDataObj the recvDataObj to set
	 */
	public void setRecvDataObj(ReceiverDataObj recvDataObj) {
		this.recvDataObj = recvDataObj;
	}

	/**
	 * @return the rgUserDataObj
	 */
	public RGUserDataObj getRgUserDataObj() {
		return rgUserDataObj;
	}

	/**
	 * @param rgUserDataObj the rgUserDataObj to set
	 */
	public void setRgUserDataObj(RGUserDataObj rgUserDataObj) {
		this.rgUserDataObj = rgUserDataObj;
	}

	/**
	 * @return the txnDataObj
	 */
	public TransactionDataObj getTxnDataObj() {
		return txnDataObj;
	}

	/**
	 * @param txnDataObj the txnDataObj to set
	 */
	public void setTxnDataObj(TransactionDataObj txnDataObj) {
		this.txnDataObj = txnDataObj;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "TransferRequestHandlerDataObj ["
				+ (dataFormat != null ? "dataFormat=" + dataFormat + ", " : "")
				+ (requestData != null ? "requestData=" + requestData + ", "
						: "")
				+ (responseData != null ? "responseData=" + responseData + ", "
						: "")
				+ (webName != null ? "webName=" + webName + ", " : "")
				+ (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "")
				+ (key != null ? "key=" + key + ", " : "")
				+ (requestId != null ? "requestId=" + requestId + ", " : "")
				+ (requestType != null ? "requestType=" + requestType + ", "
						: "")
				+ (channelId != null ? "channelId=" + channelId + ", " : "")
				+ (clientId != null ? "clientId=" + clientId + ", " : "")
				+ (groupId != null ? "groupId=" + groupId + ", " : "")
				+ (loginId != null ? "loginId=" + loginId + ", " : "")
				+ (sessionId != null ? "sessionId=" + sessionId + ", " : "")
				+ (responseId != null ? "responseId=" + responseId + ", " : "")
				+ (responseType != null ? "responseType=" + responseType + ", "
						: "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ (trdId != null ? "trdId=" + trdId + ", " : "")
				+ (password != null ? "password=" + password + ", " : "")
				+ (newPassword != null ? "newPassword=" + newPassword + ", " : "")
				+ (noofAttempts != null ? "noofAttempts=" + noofAttempts + ", "
						: "")
				+ (country != null ? "country=" + country + ", " : "")
				+ (sendCountry != null ? "sendCountry=" + sendCountry + ", "
						: "")
				+ (recvCountry != null ? "recvCountry=" + recvCountry + ", "
						: "")
				+ (countryList != null ? "countryList="
						+ countryList.subList(0,
								Math.min(countryList.size(), maxLen)) + ", "
						: "")
				+ (sendMode != null ? "sendMode=" + sendMode + ", " : "")
				+ (sendModeList != null ? "sendModeList="
						+ sendModeList.subList(0,
								Math.min(sendModeList.size(), maxLen)) + ", "
						: "")
				+ (receiver != null ? "receiver=" + receiver + ", " : "")
				+ (receiverList != null ? "receiverList="
						+ receiverList.subList(0,
								Math.min(receiverList.size(), maxLen)) + ", "
						: "")
				+ (amount != null ? "amount=" + amount + ", " : "")
				+ (destAmount != null ? "destAmount=" + destAmount + ", " : "")
				+ (bankName != null ? "bankName=" + bankName + ", " : "")
				+ (bankNameList != null ? "bankNameList="
						+ bankNameList.subList(0,
								Math.min(bankNameList.size(), maxLen)) + ", "
						: "")
				+ (accountNo != null ? "accountNo=" + accountNo + ", " : "")
				+ (accountNoList != null ? "accountNoList="
						+ accountNoList.subList(0,
								Math.min(accountNoList.size(), maxLen)) + ", "
						: "")
				+ (purpose != null ? "purpose=" + purpose + ", " : "")
				+ (purposeList != null ? "purposeList="
						+ purposeList.subList(0,
								Math.min(purposeList.size(), maxLen)) + ", "
						: "")
				+ (gender != null ? "gender=" + gender + ", " : "")
				+ (dob != null ? "dob=" + dob + ", " : "")
				+ (email != null ? "email=" + email + ", " : "")
				+ (address1 != null ? "address1=" + address1 + ", " : "")
				+ (address2 != null ? "address2=" + address2 + ", " : "")
				+ (address3 != null ? "address3=" + address3 + ", " : "")
				+ (mobileNo != null ? "mobileNo=" + mobileNo + ", " : "")
				+ (phoneNo != null ? "phoneNo=" + phoneNo + ", " : "")
				+ (currencyRate != null ? "currencyRate=" + currencyRate + ", "
						: "")
				+ (currencyCode != null ? "currencyCode=" + currencyCode + ", "
						: "")
				+ (fee != null ? "fee=" + fee + ", " : "")
				+ (reasonCode != null ? "reasonCode=" + reasonCode + ", " : "")
				+ (reasonCodeList != null ? "reasonCodeList="
						+ reasonCodeList.subList(0,
								Math.min(reasonCodeList.size(), maxLen)) + ", "
						: "")
				+ (reason != null ? "reason=" + reason + ", " : "")
				+ (reasonList != null ? "reasonList="
						+ reasonList.subList(0,
								Math.min(reasonList.size(), maxLen)) + ", "
						: "")
				+ (rgtn != null ? "rgtn=" + rgtn + ", " : "")
				+ (rgtnList != null ? "rgtnList="
						+ rgtnList
								.subList(0, Math.min(rgtnList.size(), maxLen))
						+ ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (statusList != null ? "statusList="
						+ statusList.subList(0,
								Math.min(statusList.size(), maxLen)) + ", "
						: "")
				+ (firstName != null ? "firstName=" + firstName + ", " : "")
				+ (lastName != null ? "lastName=" + lastName + ", " : "")
				+ (lastLoginTime != null ? "lastLoginTime=" + lastLoginTime
						+ ", " : "")
				+ (address != null ? "address=" + address + ", " : "")
				+ (personalMessage != null ? "personalMessage="
						+ personalMessage + ", " : "")
				+ (ulId != null ? "ulId=" + ulId + ", " : "") 
				+ (orderNo != null ? "orderNo=" + orderNo + ", " : "") 
				+ (sendCurrency != null ? "sendCurrency=" + sendCurrency + ", " : "")
				+ (statusCode != null ? "statusCode=" + statusCode + ", " : "")
				+ (statusDesc != null ? "statusDesc=" + statusDesc + ", " : "")
				+ (subStatusCode != null ? "subStatusCode=" + subStatusCode + ", " : "")
				+ (subStatusDesc != null ? "subStatusDesc=" + subStatusDesc + ", " : "")
				+ (middleName != null ? "middleName=" + middleName + ", " : "")
				+ (bookingDate != null ? "bookingDate=" + bookingDate + ", " : "")
				+ (processingDate != null ? "processingDate=" + processingDate + ", " : "")
				+ (disbursementDate != null ? "disbursementDate=" + disbursementDate + ", " : "")
				+ (recvNickName != null ? "recvNickName=" + recvNickName + ", " : "")
				+ (recvMode != null ? "recvMode=" + recvMode + ", " : "")
				+ (recvModeDesc != null ? "recvModeDesc=" + recvModeDesc : "")
				+ (smsMobile != null ? "smsMobile=" + smsMobile : "")
				+ (smsMessage != null ? "smsMessage=" + smsMessage : "")
				+ (smsRemitTxnId != null ? "smsRemitTxnId=" + smsRemitTxnId : "")
				+ (smsRemitUsrId != null ? "smsRemitUsrId=" + smsRemitUsrId : "")
				+ ("isRiskLimitCompatible=" + isRiskLimitCompatible)
				+ (sendAmtMsg != null ? "sendAmtMsg=" + sendAmtMsg : "")
				+ (sendAmtErrCode != null ? "sendAmtErrCode=" + sendAmtErrCode : "")
				+ (roleType != null ? "roleType=" + roleType : "")
				+ (noOfTxn != null ? "noOfTxn=" + noOfTxn : "")
				+ (frequency != null ? "frequency=" + frequency : "")
				+ (achAccount != null ? "achAccount=" + achAccount : "")
				+ (roleId != null ? "roleId=" + roleId : "")
				+ (notificationType != null ? "notificationType=" + notificationType : "")
				+ (subNotification != null ? "subNotification=" + subNotification : "")
				+ (docType != null ? "docType=" + docType : "")
				+ (docExtn != null ? "docExtn=" + docExtn : "")
				+ (docFileName != null ? "docFileName=" + docFileName : "")
				+ (docFileSize != null ? "docFileSize=" + docFileSize : "")
				+ (docFile != null ? "docFile=" + docFile : "")
				+ "]";
	}
	
	/**
	 * @param loginulId the loginulId to set
	 */
	public void setLoginulId(String loginulId) {
		this.loginulId = loginulId;
	}

	/**
	 * @return the loginulId
	 */
	public String getLoginulId() {
		return loginulId;
	}

	/**
	 * @param bankAcc_labelCode the bankAcc_labelCode to set
	 */
	public void setBankAcc_labelCode(String bankAcc_labelCode) {
		this.bankAcc_labelCode = bankAcc_labelCode;
	}

	/**
	 * @return the bankAcc_labelCode
	 */
	public String getBankAcc_labelCode() {
		return bankAcc_labelCode;
	}

	/**
	 * @param bankAcc_valueCode the bankAcc_valueCode to set
	 */
	public void setBankAcc_valueCode(String bankAcc_valueCode) {
		this.bankAcc_valueCode = bankAcc_valueCode;
	}

	/**
	 * @return the bankAcc_valueCode
	 */
	public String getBankAcc_valueCode() {
		return bankAcc_valueCode;
	}
	
	/**
	 * @param bankAcc_captionCode the bankAcc_captionCode to set
	 */
	public void setBankAcc_captionCode(String bankAcc_captionCode) {
		this.bankAcc_captionCode = bankAcc_captionCode;
	}

	/**
	 * @return the bankAcc_captionCode
	 */
	public String getBankAcc_captionCode() {
		return bankAcc_captionCode;
	}

	/**
	 * @param traceNo the traceNo to set
	 */
	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	/**
	 * @return the traceNo
	 */
	public String getTraceNo() {
		return traceNo;
	}

	/**
	 * @return the txnRefNumber
	 */
	public String getTxnRefNumber() {
		return txnRefNumber;
	}

	/**
	 * @param txnRefNumber the txnRefNumber to set
	 */
	public void setTxnRefNumber(String txnRefNumber) {
		this.txnRefNumber = txnRefNumber;
	}

	/**
	 * @return the smsMobile
	 */
	public String getSmsMobile() {
		return smsMobile;
	}

	/**
	 * @param smsMobile the smsMobile to set
	 */
	public void setSmsMobile(String smsMobile) {
		this.smsMobile = smsMobile;
	}

	/**
	 * @return the smsMessage
	 */
	public String getSmsMessage() {
		return smsMessage;
	}

	/**
	 * @param smsMessage the smsMessage to set
	 */
	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}

	/**
	 * @return the smsRemitUsrId
	 */
	public String getSmsRemitUsrId() {
		return smsRemitUsrId;
	}

	/**
	 * @param smsRemitUsrId the smsRemitUsrId to set
	 */
	public void setSmsRemitUsrId(String smsRemitUsrId) {
		this.smsRemitUsrId = smsRemitUsrId;
	}

	/**
	 * @return the smsRemitTxnId
	 */
	public String getSmsRemitTxnId() {
		return smsRemitTxnId;
	}

	/**
	 * @param smsRemitTxnId the smsRemitTxnId to set
	 */
	public void setSmsRemitTxnId(String smsRemitTxnId) {
		this.smsRemitTxnId = smsRemitTxnId;
	}

	/**
	 * @return the smsMobileCountryCode
	 */
	public String getSmsMobileCountryCode() {
		return smsMobileCountryCode;
	}

	/**
	 * @param smsMobileCountryCode the smsMobileCountryCode to set
	 */
	public void setSmsMobileCountryCode(String smsMobileCountryCode) {
		this.smsMobileCountryCode = smsMobileCountryCode;
	}

	/**
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}

	/**
	 * @param trackingId the trackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	
	/**
	 * @return the isRiskLimitCompatible
	 */
	public boolean isRiskLimitCompatible() {
		return isRiskLimitCompatible;
	}
	/**
	 * @param isRiskLimitCompatible the isRiskLimitCompatible to set
	 */
	public void setRiskLimitCompatible(boolean isRiskLimitCompatible) {
		this.isRiskLimitCompatible = isRiskLimitCompatible;
	}
	
	/**
	 * @return the sendAmtMsg
	 */
	public String getSendAmtMsg() {
		return sendAmtMsg;
	}

	/**
	 * @param sendAmtMsg the sendAmtMsg to set
	 */
	public void setSendAmtMsg(String sendAmtMsg) {
		this.sendAmtMsg = sendAmtMsg;
	}
	
	/**
	 * @return the sendAmtErrCode
	 */
	public String getSendAmtErrCode() {
		return sendAmtErrCode;
	}

	/**
	 * @param sendAmtErrCode the sendAmtErrCode to set
	 */
	public void setSendAmtErrCode(String sendAmtErrCode) {
		this.sendAmtErrCode = sendAmtErrCode;
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
	 * @return the noOfTxn
	 */
	public String getNoOfTxn() {
		return noOfTxn;
	}

	/**
	 * @param noOfTxn the noOfTxn to set
	 */
	public void setNoOfTxn(String noOfTxn) {
		this.noOfTxn = noOfTxn;
	}
	
	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	/**
	 * @return the achAccount
	 */
	public String getAchAccount() {
		return achAccount;
	}

	/**
	 * @param achAccount the achAccount to set
	 */
	public void setAchAccount(String achAccount) {
		this.achAccount = achAccount;
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
	 * @return the notificationType
	 */
	public String getNotificationType() {
		return notificationType;
	}

	/**
	 * @param notificationType the notificationType to set
	 */
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	/**
	 * @return the subNotification
	 */
	public String getSubNotification() {
		return subNotification;
	}

	/**
	 * @param subNotification the subNotification to set
	 */
	public void setSubNotification(String subNotification) {
		this.subNotification = subNotification;
	}

	/**
	 * @return the startIndex
	 */
	public String getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the hasNext
	 */
	public String getHasNext() {
		return hasNext;
	}

	/**
	 * @param hasNext the hasNext to set
	 */
	public void setHasNext(String hasNext) {
		this.hasNext = hasNext;
	}

	/**
	 * @return the docType
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * @param docType the docType to set
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * @return the docExtn
	 */
	public String getDocExtn() {
		return docExtn;
	}

	/**
	 * @param docExtn the docExtn to set
	 */
	public void setDocExtn(String docExtn) {
		this.docExtn = docExtn;
	}

	/**
	 * @return the docFileName
	 */
	public String getDocFileName() {
		return docFileName;
	}

	/**
	 * @param docFileName the docFileName to set
	 */
	public void setDocFileName(String docFileName) {
		this.docFileName = docFileName;
	}

	/**
	 * @return the docFileSize
	 */
	public String getDocFileSize() {
		return docFileSize;
	}

	/**
	 * @param docFileSize the docFileSize to set
	 */
	public void setDocFileSize(String docFileSize) {
		this.docFileSize = docFileSize;
	}

	/**
	 * @return the docFile
	 */
	public String getDocFile() {
		return docFile;
	}

	/**
	 * @param docFile the docFile to set
	 */
	public void setDocFile(String docFile) {
		this.docFile = docFile;
	}

	/**
	 * @return the trackingNumber
	 */
	public String getTrackingNumber() {
		return trackingNumber;
	}

	/**
	 * @param trackingNumber the trackingNumber to set
	 */
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	/**
	 * @return the oneCardId
	 */
	public String getOneCardId() {
		return oneCardId;
	}

	/**
	 * @param oneCardId the oneCardId to set
	 */
	public void setOneCardId(String oneCardId) {
		this.oneCardId = oneCardId;
	}

	/**
	 * @return the field1
	 */
	public String getField1() {
		return field1;
	}

	/**
	 * @param field1 the field1 to set
	 */
	public void setField1(String field1) {
		this.field1 = field1;
	}

	/**
	 * @return the field2
	 */
	public String getField2() {
		return field2;
	}

	/**
	 * @param field2 the field2 to set
	 */
	public void setField2(String field2) {
		this.field2 = field2;
	}

	/**
	 * @return the field3
	 */
	public String getField3() {
		return field3;
	}

	/**
	 * @param field3 the field3 to set
	 */
	public void setField3(String field3) {
		this.field3 = field3;
	}

	/**
	 * @return the field4
	 */
	public String getField4() {
		return field4;
	}

	/**
	 * @param field4 the field4 to set
	 */
	public void setField4(String field4) {
		this.field4 = field4;
	}

	/**
	 * @return the field5
	 */
	public String getField5() {
		return field5;
	}

	/**
	 * @param field5 the field5 to set
	 */
	public void setField5(String field5) {
		this.field5 = field5;
	}

	/**
	 * @return the sendCity
	 */
	public String getSendCity() {
		return sendCity;
	}

	/**
	 * @param sendCity the sendCity to set
	 */
	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}

	/**
	 * @return the sendState
	 */
	public String getSendState() {
		return sendState;
	}

	/**
	 * @param sendState the sendState to set
	 */
	public void setSendState(String sendState) {
		this.sendState = sendState;
	}

	/**
	 * @return the sendZipCode
	 */
	public String getSendZipCode() {
		return sendZipCode;
	}

	/**
	 * @param sendZipCode the sendZipCode to set
	 */
	public void setSendZipCode(String sendZipCode) {
		this.sendZipCode = sendZipCode;
	}

	/**
	 * @return the sendSSN
	 */
	public String getSendSSN() {
		return sendSSN;
	}

	/**
	 * @param sendSSN the sendSSN to set
	 */
	public void setSendSSN(String sendSSN) {
		this.sendSSN = sendSSN;
	}

}
