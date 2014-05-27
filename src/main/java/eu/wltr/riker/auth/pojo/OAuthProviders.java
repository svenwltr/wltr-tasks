package eu.wltr.riker.auth.pojo;


import java.util.HashMap;
import java.util.Map;


public class OAuthProviders {

	private Map<String, Provider> providers = new HashMap<>();

	public Map<String, Provider> getProviders() {
		return providers;

	}

	public static class Provider {

		public String clientId;

		public String clientSecret;

		public String tokenEndpoint;

		public String authorizationEndpoint;
	
	}

}
