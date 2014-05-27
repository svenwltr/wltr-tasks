package eu.wltr.riker.task;


import eu.wltr.riker.meta.token.Token;


public interface UserScoped {

	public Token getToken();

	public Token getUserToken();

}
