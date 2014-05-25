package eu.wltr.riker.auth.pojo;

public class Login {

	private Provider provider;

	private String subject;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public enum Provider {
		Google;

	}

}
