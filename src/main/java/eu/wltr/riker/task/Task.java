package eu.wltr.riker.task;


import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.jongo.marshall.jackson.oid.Id;

import eu.wltr.riker.meta.token.Token;


public class Task implements UserScoped {

	@Id
	private Token token;

	private Token userToken;

	private String title;

	private String description;

	private Duration intervall;

	private LocalDate lastExecution;

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Token getUserToken() {
		return userToken;
	}

	public void setUserToken(Token userToken) {
		this.userToken = userToken;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Duration getIntervall() {
		return intervall;
	}

	public void setIntervall(Duration intervall) {
		this.intervall = intervall;
	}

	public LocalDate getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(LocalDate lastExecution) {
		this.lastExecution = lastExecution;
	}

}