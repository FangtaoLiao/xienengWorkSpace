package dataIntsert;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
/**
 * 
 * Title:Server
 * Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * @author chenrl
 * 2016年1月6日下午3:29:28
 */
public class Server {

    public static final String HOST = "tcp://47.96.183.194:1883";
    public static final String TOPIC = "syn_002b00353037471831303235_sub";
    private static final String clientid = "server_taylor";

    private MqttClient client;
    private MqttTopic topic;
    private String userName = "admin";
    private String passWord = "public1";

    private MqttMessage message;

    public Server() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
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

    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        if(token.isComplete())
            token.waitForCompletion();
        	System.out.println("消息发布成功! ");
    }

    public static void main(String[] args) throws MqttException {
        Server server = new Server();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        System.out.println();// new Date()为获取当前系统时间
        String date = df.format(new Date());
        String time = df1.format(new Date());
        
		//String msg = "{\"time\":\""+System.currentTimeMillis()+"\",\"type\":\"8\"}";//数采运行状态
      //数采配置
		//String msg = "{\"time\":\""+System.currentTimeMillis()+"\",\"type\":\"9\"}";
      //数采点表
        String msg = "{\"time\":\"1521010709000\",\"type\":\"10\"}";

		//String msg = "{\"time\":\"1521010709\",\"type\":\"12\",\"data\":{\"log_time\":\"1519837200,1521082800\"}}";//数采配置
		//String msg = "{\"time\":\""+System.currentTimeMillis()+"\",\"type\":\"6\",\"data\": [{\"com\":\"com1\",\"devs\":[{\"id\":\"1\",\"coll\":[{\"1000\":\"\'3\',\'1\'\"},{\"1001\":\"\'3\',\'1\'\"}]}]}]}";
		//String msg = "{\"time\":\"1521010709000\",\"type\":\"11\",\"data\":{\"data_time\":\"\'1521079200\',\'1521080400\'\"}}";//数采配置
		System.out.println("服务器发布消息："+msg);
        server.message = new MqttMessage();
        server.message.setQos(2);
        server.message.setRetained(true);
        server.message.setPayload(msg.getBytes());
        server.publish(server.topic , server.message);        

        System.out.println(server.message.isRetained() + "------ratained状态");
    }
}