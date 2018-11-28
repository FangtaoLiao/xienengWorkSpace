package sendmqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MQTT {

	public static final String HOST = "tcp://192.168.1.199:1883";
	public static final String TOPIC = "ncyt_pub_yc_ym_yx";
	private static final String clientid = "server";

	private MqttClient client;
	private MqttTopic topic;
	private String userName = "admin";
	private String passWord = "public";

	private MqttMessage message;

	public MQTT() {
		// MemoryPersistence设置clientid的保存形式，默认为以内存保存

		try {
			client = new MqttClient(HOST, clientid, new MemoryPersistence());
		} catch (MqttException e) {
			e.printStackTrace();
		}

		connect();
	}

	public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException,

			MqttException {

		MqttDeliveryToken token = topic.publish(message);

		token.waitForCompletion();

		System.out.println("message is published completely! "

				+ token.isComplete());

	}

	public static void main(String[] args) throws MqttPersistenceException, MqttException, InterruptedException {
		// 一次
		//once();
		// 多次
		more();
	}

	private static void more() throws InterruptedException, MqttPersistenceException, MqttException {
		MQTT mqtt = new MQTT();

		mqtt.message = new MqttMessage();

		mqtt.message.setQos(1);

		mqtt.message.setRetained(false);
		

		while (true) {

			String msg = getMQtt().toString();
			mqtt.message.setPayload(msg.getBytes());
			System.out.println(msg);
			mqtt.publish(mqtt.topic, mqtt.message);

			Thread.sleep(1000 * 60 * 5);

			if (false) {
				break;
			}
			
		}

		System.out.println(mqtt.message.isRetained() + "------ratained状态");
		
	}

	/**
	 * 发送一次
	 * 
	 * @author ybj
	 * @Description
	 * @Date 2018年8月7日
	 */
	private static void once() {
		System.out.println(getMQtt());
	}

	private void connect() {

		MqttConnectOptions options = new MqttConnectOptions();

		options.setCleanSession(true);

		options.setUserName(userName);

		options.setPassword(passWord.toCharArray());

		// 设置超时时间

		options.setConnectionTimeout(10);

		// 设置会话心跳时间

		options.setKeepAliveInterval(20);

		try {

			client.setCallback(new PushCallback());

			client.connect(options);

			topic = client.getTopic(TOPIC);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private static StringBuilder getMQtt() {
		int count = 43;
		int min = 0;
		int max = 50;
		String collId = "S101H36CjUKV65";
		StringBuilder sb = new StringBuilder();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<mqttData>\n");
		sb.append("\t<DataCollector id=\"" + collId + "\">\n");
		sb.append("\t\t<time>");
		sb.append(df.format(new Date()).toString());
		sb.append("</time>\n");
		sb.append("\t\t<type Type=\"Yc\">\n");

		for (int i = 0; i < count; i++) {
			sb.append("\t\t\t<yc pId=\"" + i + "\" d=\"F\" q=\"0\">" + getRandom(min, max) + "</yc>\n");
		}
		sb.append("\t\t</type>\n");
		sb.append("\t</DataCollector>\n");
		sb.append("</mqttData>");
		return sb;
	}

	private static Integer getRandom(int min, int max) {
		int a = (int) (Math.random() * (max - min + 1)) + min;
		return a;
	}
}
