package eu.wltr.riker.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.wltr.riker.auth.AuthContext;
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

}
