package eu.wltr.riker.config;


import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mongodb.DB;
import com.mongodb.MongoClient;


@Configuration
@ComponentScan("eu.wltr.riker.data")
public class DatabaseConfig {

	@Bean
	public MongoClient provideMongoClient() throws UnknownHostException {
		return new MongoClient();

	}

	@Bean
	public DB provideDb(MongoClient mc) {
		return mc.getDB("riker-spring");

	}

	@Bean
	public Jongo provideJongo(DB db) {
		return new Jongo(db);

	}

}
