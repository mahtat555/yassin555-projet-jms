package server;

import java.io.FileOutputStream;
import java.io.IOException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.StreamMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageReceiver {

	// URL of the JMS server
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	// default broker URL is : tcp://localhost:61616"

	// Name of the queue we will receive messages from
	private static String subject = "JCG_QUEUE";

	public static void main(String[] args) throws JMSException, IOException {
		// Getting JMS connection from the server
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();

		// Creating session for seding messages
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// Getting the queue 'JCG_QUEUE'
		Destination destination = session.createQueue(subject);

		// MessageConsumer is used for receiving (consuming) messages
		MessageConsumer consumer = session.createConsumer(destination);

		// Here we receive the message.
		Message message = consumer.receive();

		// We will be using StreamMessage.
		if (message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            int size = streamMessage.readInt();
            byte[] data = new byte[size];
            streamMessage.readBytes(data);
            FileOutputStream out = new FileOutputStream("happy-receiver.png");
            out.write(data);
            out.close();
            System.out.println("Image recut !!");

        }
		connection.close();
	}
}
