package netCar;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartDeviceLogic {
private static Logger logger = LoggerFactory.getLogger(StartDeviceLogic.class);
	
	public static void main(String[] args) {
		try {
			String contextData = "spring-application.xml";
			String[] contexts = new String[] { contextData };
			
			final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contexts);
//			ActiveMQQueue mq=(ActiveMQQueue)context.getBean("uploadNetCar");
//			logger.info("Device Logic listen mq:"+mq.getQueueName());
		} catch (Exception e) {
			logger.info("",e);
		}
	}
}
