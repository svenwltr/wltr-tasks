package eu.wltr.riker.task;


import org.joda.time.DateTime;
import org.joda.time.Duration;

import eu.wltr.riker.meta.token.Token;


public class Task {

	private Token _id;

	private Token userToken;

	private String title;

	private String description;

	private Duration interval;

	private DateTime lastExecution;

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

	public Duration getInterval() {
		return interval;
	}

	public void setInterval(Duration intervall) {
		this.interval = intervall;
	}

	public DateTime getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(DateTime lastExecution) {
		this.lastExecution = lastExecution;
	}

}