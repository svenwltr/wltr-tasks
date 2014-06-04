package eu.wltr.riker.task;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.wltr.riker.auth.AuthContext;
import eu.wltr.riker.meta.token.Token;
import eu.wltr.riker.utils.httperror.Http400BadRequest;

@RestController
@RequestMapping(value = "tasks/", produces = "application/json")
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
	public Task upsert(@RequestBody Task task, HttpServletResponse response,
			AuthContext ctx) {

		if (task.getUserToken() != null) {
			response.addHeader("X-400-REASON", "'userToken' is forbidden");
			throw new Http400BadRequest();

		}

		if (task.getId() == null) {
			if (task.getTitle() == null || task.getTitle().isEmpty()) {
				response.addHeader("X-400-REASON", "'title' is required");
				throw new Http400BadRequest();
			}

			if (task.getInterval() == null) {
				response.addHeader("X-400-REASON", "'interval' is required");
				throw new Http400BadRequest();
			}

			taskBo.create(task, ctx);

		}

		else
			taskBo.update(task, ctx);

		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		return task;

	}

	@RequestMapping(value = "{id}/", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") String id,
			HttpServletResponse response, AuthContext ctx) {
		taskBo.delete(new Token(id), ctx);

	}

}
