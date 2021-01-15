package com.ahdms.ctidservice.config;

import javax.annotation.Resource;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitConfig {
	
	@Value("${rabbitmq.queue.authRecord}")
	private String queueName;
	
	@Value("${rabbitmq.fanoutName.websocket:websocket}")
	private String fanoutName;
	
	@Value("${rabbitmq.fanoutexchange.websocket}")
	private String fanoutexchange;
	
	@Value("${rabbitmq.queue.queueBtou}")
	private String btQueueName;
	
	@Resource
	Environment environment;

	@Bean(name = "ctidConnectionFactory")
	@Primary
	public ConnectionFactory jfConnectionFactory() {
		CachingConnectionFactory connection = new CachingConnectionFactory();
		connection.setHost(environment.getProperty("spring.rabbitmq.host"));
		connection.setPort(environment.getProperty("spring.rabbitmq.port",Integer.class));
		connection.setUsername(environment.getProperty("spring.rabbitmq.username"));
		connection.setPassword(environment.getProperty("spring.rabbitmq.password"));
		connection.setVirtualHost("/");
		return connection;
	}


	@Bean(name = "ctidFactory")
	public SimpleRabbitListenerContainerFactory jfFirstFactory(
			@Qualifier("ctidConnectionFactory") ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		 factory.setConnectionFactory(connectionFactory);
		return factory;
	}
	
	@Bean(name = "ctidRabbitmqTemplate")
	public RabbitTemplate rabbitTemplate(@Qualifier("ctidConnectionFactory") ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		return rabbitTemplate;
	}
	
    @Bean
    public Queue queue(){
    	return new Queue(queueName); 
    }
    
    @Bean(name = "${rabbitmq.queue.queueBtou}")
    public Queue btQueue(){
    	return new Queue(btQueueName); 
    }
    
    @Bean(name = "${rabbitmq.fanoutName.websocket:websocket}")
    public Queue wsQueue(){
    	return new Queue(fanoutName); 
    }
    
    /**
     * 这里的exchange是交换机的名称字符串和发送消息时的名称必须相同
     * this.rabbitTemplate.convertAndSend("exchange", "topic.1", context);
     */
    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(fanoutexchange);
    }

    /**
     * @param queueMessage 队列
     * @param exchange     交换机
     *                     bindings 绑定交换机队列信息
     */
    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(wsQueue()).to(exchange());
    }
 
}
