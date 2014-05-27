package eu.wltr.riker.config;


import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import eu.wltr.riker.meta.token.Token;
import eu.wltr.riker.meta.token.jackson.TokenDeserializer;
import eu.wltr.riker.meta.token.jackson.TokenSerializer;


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
		Mapper mapper = new JacksonMapper.Builder()
				.addSerializer(Token.class, new TokenSerializer())
				.addDeserializer(Token.class, new TokenDeserializer()).build();

		return new Jongo(db, mapper);

	}
}
