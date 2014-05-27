package eu.wltr.riker.task;


import org.joda.time.Duration;
import org.joda.time.LocalDate;

import eu.wltr.riker.meta.token.Token;


public class Task implements UserScoped {

	private Token _id;

	private Token userToken;

	private String title;

	private String description;

	private Duration intervall;

	private LocalDate lastExecution;

	public Token getId() {
		return _id;
	}

	public void setId(Token token) {
		this._id = token;
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