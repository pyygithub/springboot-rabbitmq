package com.pyy.springboot;

import com.pyy.springboot.entity.Order;
import com.pyy.springboot.producer.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringbootApplicationTests {

	@Autowired
	private RabbitSender rabbitSender;

	@Test
	public void testSender1() throws Exception {
		Map<String, Object> properties = new HashMap<>();
		properties.put("number", "12345");
		properties.put("send_time", LocalDate.now());

		rabbitSender.send("hello rabbitmq for springBoot", properties);
	}


	@Test
	public void testSenderOrder() throws Exception {
		Order order = new Order();
		order.setId("111");
		order.setName("订单详情xxx");
		rabbitSender.sendOrder(order);
	}

}
