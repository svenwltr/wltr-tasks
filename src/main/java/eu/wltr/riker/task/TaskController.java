package eu.wltr.riker.task;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.wltr.riker.auth.AuthContext;


@RestController
@RequestMapping(
		value = "tasks/",
		consumes = "application/json",
		produces = "application/json")
public class TaskController {

	@Autowired
	private TaskBo taskBo;

	@RequestMapping("test/")
	public Task hello() {
		Task t = new Task();

		t.setTitle("Blumen gießen");
		t.setDescription(".öfasdfsd");
		t.setIntervall(Duration.standardDays(7));
		t.setLastExecution(LocalDate.parse("2014-05-21"));

		return t;

	}

	@RequestMapping(method = RequestMethod.POST)
	public Task createTask(
			@RequestBody Task task,
			HttpServletRequest request,
			HttpServletResponse response,
			AuthContext ctx) {

		taskBo.save(task);

		return task;

	}

}
