package eu.wltr.riker.task;


import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.wltr.riker.auth.AuthContext;
import eu.wltr.riker.meta.token.Token;


@RestController
@RequestMapping(
		value = "tasks/",
		produces = "application/json")
public class TaskController {

	@Autowired
	private TaskBo taskBo;

	@RequestMapping("example/")
	public Task hello() {
		Task t = new Task();

		t.setId(new Token("foo"));
		t.setUserToken(new Token("userid"));
		t.setTitle("Blumen gießen");
		t.setDescription(".öfasdfsd");
		t.setInterval(Duration.standardDays(7));
		t.setLastExecution(DateTime.parse("2014-05-21"));

		return t;

	}

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Task> listAll(AuthContext ctx) {
		return taskBo.getAll(ctx);

	}

	@RequestMapping(method = RequestMethod.POST)
	public Task create(
			@RequestBody Task task,
			HttpServletResponse response,
			AuthContext ctx) {
		taskBo.create(task, ctx);

		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		return task;

	}


}
