package com.labc.Airport.Utilities;

public class PaymentIdentifier {
	private boolean success;
	private String message;
	private String id;
	private String code;
	private String reference;
	private String voucher;
	private String ordernumber;
	private String sequence;
	private String approval;
	private String lote;
	private String responsecode;
	private boolean deferred;
	private String datetime;
	private String amount;
	
	public PaymentIdentifier(boolean success, String message, String id, String code, String reference, String voucher,
			String ordernumber, String sequence, String approval, String lote, String responsecode, boolean deferred,
			String datetime, String amount) {
		super();
		this.success = success;
		this.message = message;
		this.id = id;
		this.code = code;
		this.reference = reference;
		this.voucher = voucher;
		this.ordernumber = ordernumber;
		this.sequence = sequence;
		this.approval = approval;
		this.lote = lote;
		this.responsecode = responsecode;
		this.deferred = deferred;
		this.datetime = datetime;
		this.amount = amount;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public String getOrdernumber() {
		return ordernumber;
	}
	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getResponsecode() {
		return responsecode;
	}
	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}
	public boolean isDeferred() {
		return deferred;
	}
	public void setDeferred(boolean deferred) {
		this.deferred = deferred;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	

}
