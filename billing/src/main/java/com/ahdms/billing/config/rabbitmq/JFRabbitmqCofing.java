package com.ahdms.billing.config.rabbitmq;

import javax.annotation.Resource;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class JFRabbitmqCofing {
	@Resource
	Environment environment;

	@Bean(name = "jfConnectionFactory")
	@Primary
	public ConnectionFactory jfConnectionFactory() {
		CachingConnectionFactory connection = new CachingConnectionFactory();
		connection.setAddresses(environment.getProperty("spring.rabbitmq.host"));
		connection.setHost(environment.getProperty("spring.rabbitmq.host"));
		connection.setPort(environment.getProperty("spring.rabbitmq.port",Integer.class));
		connection.setUsername(environment.getProperty("spring.rabbitmq.username"));
		connection.setPassword(environment.getProperty("spring.rabbitmq.password"));
		connection.setVirtualHost("/");
		return connection;
	}

	@Bean(name = "JF_LOG")
	public Queue queue001() {
		return new Queue("JF_LOG");
	}

	@Bean(name = "jfFactory")
	public SimpleRabbitListenerContainerFactory jfFirstFactory(
			@Qualifier("jfConnectionFactory") ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		 factory.setConnectionFactory(connectionFactory);
		return factory;
	}
	
	@Bean(name = "jfRabbitmqTemplate")
	public RabbitTemplate rabbitTemplate(@Qualifier("jfConnectionFactory") ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		return rabbitTemplate;
	}

}
