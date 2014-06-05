package eu.wltr.riker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class CommonConfig {
	
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		System.out.println("bar");
		
	}
	
	
}
