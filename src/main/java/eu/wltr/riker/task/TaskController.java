package eu.wltr.riker.task;


import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("tasks/")
public class TaskController { 

	@RequestMapping("test/")
	public Task hello() {
		Task t = new Task();

		t.setTitle("Blumen gießen");
		t.setDescription(".öfasdfsd");
		t.setIntervall(Duration.standardDays(7));
		t.setLastExecution(LocalDate.parse("2014-05-21"));

		return t;

    }

}
