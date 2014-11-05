package mdn.vtvpluspro.object;

public class ChargingSMSInfo {

	private String title;
	private String body;
	private String type; // GPC, VMS, VTL
	private String status;
	private String form_sms;
	private String return_mt;
	private String message;
	private String service;

	public ChargingSMSInfo(String title, String body, String type,
			String status, String form_sms, String return_mt, String message,
			String service) {
		super();
		this.title = title;
		this.body = body;
		this.type = type;
		this.status = status;
		this.form_sms = form_sms;
		this.return_mt = return_mt;
		this.message = message;
		this.service = service;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getForm_sms() {
		return form_sms;
	}

	public void setForm_sms(String form_sms) {
		this.form_sms = form_sms;
	}

	public String getReturn_mt() {
		return return_mt;
	}

	public void setReturn_mt(String return_mt) {
		this.return_mt = return_mt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

}
