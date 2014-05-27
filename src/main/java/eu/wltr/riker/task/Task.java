package eu.wltr.riker.task;


import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

import eu.wltr.riker.meta.token.Token;


public class Task {

	private Token _id;

	private Token userToken;

	private String title;

	private String description;

	private Duration intervall;

	private LocalDateTime lastExecution;

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

	public LocalDateTime getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(LocalDateTime lastExecution) {
		this.lastExecution = lastExecution;
	}

}