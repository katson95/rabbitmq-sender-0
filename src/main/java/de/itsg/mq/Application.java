
package de.itsg.mq;

import java.util.Date;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application implements CommandLineRunner {

	final static String queueName0 = "i360-msg-queue-0";

	final static String queueName1 = "i360-msg-queue-1";
	
	final static String exchange = "i360-msg-exchange";

	@Autowired
	AnnotationConfigApplicationContext context;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	Queue queue0() {
		return new Queue(queueName0, false);
	}

	@Bean
	Queue queue1() {
		return new Queue(queueName1, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	Binding binding0(Queue queue0, TopicExchange exchange) {
		return BindingBuilder.bind(queue0).to(exchange).with(queueName0);
	}

	@Bean
	Binding binding1(Queue queue1, TopicExchange exchange) {
		return BindingBuilder.bind(queue1).to(exchange).with(queueName1);
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Waiting five seconds...");
		Thread.sleep(5000);
		System.out.println("Sending message...");
		rabbitTemplate.convertAndSend(queueName0,
				"Hello from ITSG Rabbit to queue 0 !, Sent at " + new Date().toString());
		rabbitTemplate.convertAndSend(queueName1,
				"Hello from ITSG Rabbit to queue 1!, Sent at " + new Date().toString());
		context.close();
	}
}