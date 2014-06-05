package eu.wltr.riker.auth.dto;

import org.joda.time.DateTime;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.wltr.riker.auth.pojo.Login;
import eu.wltr.riker.auth.pojo.Login.Provider;
import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;
import eu.wltr.riker.meta.token.Token;

@Service
public class UserDto {

	public static final String NAME = "users";

	private final MongoCollection collection;

	@Autowired
	protected UserDto(Jongo jongo) {
		this.collection = jongo.getCollection(NAME);
		this.collection.ensureIndex("{'sessions.token': 1}");

	}

	@Scheduled(cron = "0 * * * * ?")
	public void cleanupSessions() {
		long now = DateTime.now().getMillis();
		
		collection
				.update("{'sessions.$.expires' : {$exists : false}}")
				.multi()
				.with("{$pull : {'sessions' : {'expires' : {$exists : false}}}}");

		collection
				.update("{'sessions.expires' : {$lt : #}}", now)
				.multi()
				.with("{$pull : {'sessions' : {'expires' : {$lt : #}}}}", now);

	}

	public User findOneByLogin(Provider google, String subject) {
		return collection.findOne(
				"{logins: {$elemMatch : {provider: '#', subject: '#'}}}",
				google, subject).as(User.class);

	}

	public User findOneBySessionId(String sid) {
		return collection.findOne("{sessions: {$elemMatch : {token: #}}}", sid)
				.as(User.class);

	}

	public void save(User user) {
		collection.save(user);

	}

	public void addLogin(Token token, Login login) {
		collection.update("{_id:'#'}", token).with("{$push: {'logins': #}}",
				login);

	}

	public void addSession(Token token, Session session) {
		collection.update("{_id:'#'}", token).with("{$push: {'sessions': #}}",
				session);

	}

}
