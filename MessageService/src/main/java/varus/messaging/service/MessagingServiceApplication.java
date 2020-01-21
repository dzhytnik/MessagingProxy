package varus.messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import varus.messaging.service.dao.ConfigRepository;

import javax.persistence.MappedSuperclass;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {"varus.messaging.dao.bean"})
public class MessagingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessagingServiceApplication.class, args);
	}


}
