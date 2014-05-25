package eu.wltr.riker.task;


import org.joda.time.Duration;
import org.joda.time.LocalDate;


public class Task {

	private String title;

	private String description;

	private Duration intervall;

	private LocalDate lastExecution;

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