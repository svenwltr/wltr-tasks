package eu.wltr.riker.task;


import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.wltr.riker.auth.AuthContext;


@Service
public class TaskDto {

	private final static String NAME = "tasks";

	private final MongoCollection collection;

	@Autowired
	protected TaskDto(Jongo jongo) {
		this.collection = jongo.getCollection(NAME);

	}

	public Iterable<Task> findAllByUserToken(AuthContext ctx) {
		return collection.find("{userToken: #}",
				ctx.getUser().getToken().toString()).as(
				Task.class);
	}

	public void save(Task task) {
		collection.save(task);

	}

}
