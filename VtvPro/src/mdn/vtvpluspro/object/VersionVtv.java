package mdn.vtvpluspro.object;

public class VersionVtv {
	private boolean status;
	private String appUrl;
	private String appProUrl;
	private String logoApp;
	private String purchase;
	private String policy;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAppProUrl() {
		return appProUrl;
	}

	public void setAppProUrl(String appProUrl) {
		this.appProUrl = appProUrl;
	}

	public String getLogoApp() {
		return logoApp;
	}

	public void setLogoApp(String logoApp) {
		this.logoApp = logoApp;
	}

	/**
	 * @return the purchase
	 */
	public String getPurchase() {
		return purchase;
	}

	/**
	 * @param purchase
	 *            the purchase to set
	 */
	public void setPurchase(String purchase) {
		this.purchase = purchase;
	}

	/**
	 * @return the policy
	 */
	public String getPolicy() {
		return policy;
	}

	/**
	 * @param policy
	 *            the policy to set
	 */
	public void setPolicy(String policy) {
		this.policy = policy;
	}

}
