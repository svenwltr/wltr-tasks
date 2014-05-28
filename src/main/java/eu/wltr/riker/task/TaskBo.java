package eu.wltr.riker.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.wltr.riker.auth.AuthContext;
import eu.wltr.riker.meta.token.Token;
import eu.wltr.riker.meta.token.TokenBo;


@Service
public class TaskBo {

	@Autowired
	private TaskDto taskDto;

	@Autowired
	private TokenBo tokenBo;

	public Task create(Task task, AuthContext ctx) {
		task.setId(tokenBo.next());
		task.setUserToken(ctx.getUser().getToken());

		taskDto.save(task);

		return task;

	}

	public Iterable<Task> getAll(AuthContext ctx) {
		return taskDto.findAllByUserToken(ctx);

	}

	public void update(Task task, AuthContext ctx) {
		Task old = taskDto.findOne(task.getId());

		if (old == null)
			throw new RuntimeException();

		Token requestUser = ctx.getUser().getToken();
		Token taskUser = old.getUserToken();

		if (!requestUser.equals(taskUser))
			throw new SecurityException();

		task.setUserToken(old.getUserToken());

		taskDto.save(task);

	}

}
