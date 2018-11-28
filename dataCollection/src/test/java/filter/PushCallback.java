package filter;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 发布消息的回调类
 * 
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 * 
 * public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 * 
 * public void connectionLost(Throwable cause)在断开连接时调用。
 * 
 * public void deliveryComplete(MqttDeliveryToken token)) 接收到已经发布的 QoS 1 或 QoS 2
 * 消息的传递令牌时调用。 由 MqttClient.connect 激活此回调。
 * 
 */
public class PushCallback implements MqttCallback {
	public static int count = 0;

	public void connectionLost(Throwable cause) {
		// 连接丢失后，一般在这里面进行重连
		System.out.println("连接断开，可以做重连");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete---------" + token.isComplete());
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// subscribe后得到的消息会执行到这里面
		// System.out.println("消息计数:" + count++);
		/*
		 * System.out.println("接收消息主题:" + topic); System.out.println("接收消息Qos:" +
		 * message.getQos()); System.out.println(new
		 * String(message.getPayload(),"UTF-8"));
		 */
        if(new String(message.getPayload()).contains("S011H20A9HRMCL")){
        	writeLog(new String(message.getPayload()));
        	System.out.println("消息计数:" + count++);  
            System.out.println("接收消息主题:" + topic);  
            System.out.println("接收消息Qos:" + message.getQos());  
        	System.out.println(new String(message.getPayload()));	
        }
//		final MqttMessage messages = message;
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				String content = null;
//				try {
//					content = new String(messages.getPayload(), "UTF-8");
//				} catch (UnsupportedEncodingException e1) {
//					e1.printStackTrace();
//				}
//				Document doc = null;
//				try {
//					doc = DocumentHelper.parseText(content);
//				} catch (DocumentException e) {
//					e.printStackTrace();
//				}
//				Element mqttData = doc.getRootElement();
//				Element DataCollector = mqttData.element("DataCollector");
//				String collector_id = DataCollector.attributeValue("id");
//				Element time = DataCollector.element("time");
//				String xmlTimeStr = time.getText();
//				System.out.println(collector_id + "\t" + xmlTimeStr);
//				writeLog(collector_id + "\t" + xmlTimeStr+"\r\n");
//			}
//		}).start();

	}

	private void writeLog(String sb) {
		try {
			FileWriter fw = new FileWriter("C:\\Users\\SP0012\\Desktop\\mqttBW.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb);
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}