package com.api.remitGuru.component.util;

import java.util.ArrayList;

public class PromoDataObj  implements java.io.Serializable {



	private String sendGroupId = null;
	private String sendUserId = null;
	private String sendLoginId = null;
	private String promoEvent = null;
	
	private String rgtn = null;
	private String promoCode = null;

	private String sendCountryCode = null;
	private String sendCurrencyCode = null;
	private String recvCountryCode = null;
	private String recvCurrencyCode = null;
	private String sendModeCode = null;
	private String receiveModeCode = null;
	private String programCode = null;
	private String sendRoleId = null;
	
	private String bookingDate = null;
	private String registrationDate = null;
	private String senderDob = null;
	private String marketingRef = null;
	private String recvBankId = null;
	private String nearestLogisticCity = null;
	private String transactionAmount = "0";
		
	private String  promoStatusCode = null; 
	private String  promoMsg = null;
	public ArrayList errorData = new ArrayList();
	public ArrayList promoResult = new ArrayList();
	/**
	 * @return the sendGroupId
	 */
	public String getSendGroupId() {
		return sendGroupId;
	}
	/**
	 * @param sendGroupId the sendGroupId to set
	 */
	public void setSendGroupId(String sendGroupId) {
		this.sendGroupId = sendGroupId;
	}
	/**
	 * @return the sendUserId
	 */
	public String getSendUserId() {
		return sendUserId;
	}
	/**
	 * @param sendUserId the sendUserId to set
	 */
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	/**
	 * @return the sendLoginId
	 */
	public String getSendLoginId() {
		return sendLoginId;
	}
	/**
	 * @param sendLoginId the sendLoginId to set
	 */
	public void setSendLoginId(String sendLoginId) {
		this.sendLoginId = sendLoginId;
	}
	/**
	 * @return the promoEvent
	 */
	public String getPromoEvent() {
		return promoEvent;
	}
	/**
	 * @param promoEvent the promoEvent to set
	 */
	public void setPromoEvent(String promoEvent) {
		this.promoEvent = promoEvent;
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
	 * @return the sendModeCode
	 */
	public String getSendModeCode() {
		return sendModeCode;
	}
	/**
	 * @param sendModeCode the sendModeCode to set
	 */
	public void setSendModeCode(String sendModeCode) {
		this.sendModeCode = sendModeCode;
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
	 * @return the programCode
	 */
	public String getProgramCode() {
		return programCode;
	}
	/**
	 * @param programCode the programCode to set
	 */
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
	/**
	 * @return the sendRoleId
	 */
	public String getSendRoleId() {
		return sendRoleId;
	}
	/**
	 * @param sendRoleId the sendRoleId to set
	 */
	public void setSendRoleId(String sendRoleId) {
		this.sendRoleId = sendRoleId;
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
	 * @return the registrationDate
	 */
	public String getRegistrationDate() {
		return registrationDate;
	}
	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	/**
	 * @return the senderDob
	 */
	public String getSenderDob() {
		return senderDob;
	}
	/**
	 * @param senderDob the senderDob to set
	 */
	public void setSenderDob(String senderDob) {
		this.senderDob = senderDob;
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
	 * @return the recvBankId
	 */
	public String getRecvBankId() {
		return recvBankId;
	}
	/**
	 * @param recvBankId the recvBankId to set
	 */
	public void setRecvBankId(String recvBankId) {
		this.recvBankId = recvBankId;
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
	 * @return the transactionAmount
	 */
	public String getTransactionAmount() {
		return transactionAmount;
	}
	/**
	 * @param transactionAmount the transactionAmount to set
	 */
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	/**
	 * @return the promoStatusCode
	 */
	public String getPromoStatusCode() {
		return promoStatusCode;
	}
	/**
	 * @param promoStatusCode the promoStatusCode to set
	 */
	public void setPromoStatusCode(String promoStatusCode) {
		this.promoStatusCode = promoStatusCode;
	}
	/**
	 * @return the promoMsg
	 */
	public String getPromoMsg() {
		return promoMsg;
	}
	/**
	 * @param promoMsg the promoMsg to set
	 */
	public void setPromoMsg(String promoMsg) {
		this.promoMsg = promoMsg;
	}
	/**
	 * @return the promoResult
	 */
	public ArrayList getPromoResult() {
		return promoResult;
	}
	/**
	 * @param promoResult the promoResult to set
	 */
	public void setPromoResult(ArrayList promoResult) {
		this.promoResult = promoResult;
	}


	/**
	 * @return the errorData
	 */
	public ArrayList getErrorData() {
		return errorData;
	}
	/**
	 * @param errorData the errorData to set
	 */
	public void setErrorData(ArrayList errorData) {
		this.errorData = errorData;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "PromoDataObj ["
				+ (sendGroupId != null ? "sendGroupId=" + sendGroupId + ", "
						: "")
				+ (sendUserId != null ? "sendUserId=" + sendUserId + ", " : "")
				+ (sendLoginId != null ? "sendLoginId=" + sendLoginId + ", "
						: "")
				+ (promoEvent != null ? "promoEvent=" + promoEvent + ", " : "")
				+ (rgtn != null ? "rgtn=" + rgtn + ", " : "")
				+ (promoCode != null ? "promoCode=" + promoCode + ", " : "")
				+ (sendCountryCode != null ? "sendCountryCode="
						+ sendCountryCode + ", " : "")
				+ (sendCurrencyCode != null ? "sendCurrencyCode="
						+ sendCurrencyCode + ", " : "")
				+ (recvCountryCode != null ? "recvCountryCode="
						+ recvCountryCode + ", " : "")
				+ (recvCurrencyCode != null ? "recvCurrencyCode="
						+ recvCurrencyCode + ", " : "")
				+ (sendModeCode != null ? "sendModeCode=" + sendModeCode + ", "
						: "")
				+ (receiveModeCode != null ? "receiveModeCode="
						+ receiveModeCode + ", " : "")
				+ (programCode != null ? "programCode=" + programCode + ", "
						: "")
				+ (sendRoleId != null ? "sendRoleId=" + sendRoleId + ", " : "")
				+ (bookingDate != null ? "bookingDate=" + bookingDate + ", "
						: "")
				+ (registrationDate != null ? "registrationDate="
						+ registrationDate + ", " : "")
				+ (senderDob != null ? "senderDob=" + senderDob + ", " : "")
				+ (marketingRef != null ? "marketingRef=" + marketingRef + ", "
						: "")
				+ (recvBankId != null ? "recvBankId=" + recvBankId + ", " : "")
				+ (nearestLogisticCity != null ? "nearestLogisticCity="
						+ nearestLogisticCity + ", " : "")
				+ (promoStatusCode != null ? "promoStatusCode="
						+ promoStatusCode + ", " : "")
				+ (promoMsg != null ? "promoMsg=" + promoMsg + ", " : "")
				+ (promoResult != null ? "promoResult="
						+ promoResult.subList(0,
								Math.min(promoResult.size(), maxLen)) : "")
				+ "]";
	}	

}
