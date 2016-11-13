package hap.message;


import java.util.Arrays;

public abstract class Message {

	public enum QOS {
		AtMostOnce(0),
		AtLeastOnce(1),
		ExactlyOnce(2);

		QOS(int value) {
			myValue = value;
		}

		private final int myValue;

		public int getValue() {
			return myValue;
		}

		public static QOS fromInt(int val) {
			if (val == 0) {
				return AtMostOnce;
			} else if (val == 1) {
				return AtLeastOnce;
			} else {
				return ExactlyOnce;
			}
		}
	}

	public abstract void visit(IMessageListener listener);

	public Message(String topic, byte[] payload, QOS qos, boolean retained) {
		if (topic.startsWith(myTopicRoot)) {
			myTopic = topic;
		} else {
			myTopic = combineTopic(myTopicRoot, topic);
		}
		myPayload = payload;
		myQos = qos;
		myRetained = retained;
	}


	public byte[] getPayload() {
		return myPayload;
	}

	public String getTopic() {
		return myTopic;
	}

	public QOS getQos() {
		return myQos;
	}

	public boolean isRetained() {
		return myRetained;
	}

	@Override
	public String toString() {
		String s = getTopic();
		s += Arrays.toString(getPayload());
		return s;
	}

	final private String myTopic;
	final private byte[] myPayload;
	private QOS myQos;
	private boolean myRetained;

	public static String combineTopic(final String topicRoot, final String... topic) {
		String t = topicRoot;

		for (String curr : topic) {
			if (!curr.startsWith("/")) {
				curr = "/" + curr;
			}

			if (curr.endsWith("/")) {
				curr = curr.substring(0, curr.length() - 1);
			}

			t += curr;
		}

		return t;
	}

	private static String myTopicRoot = "";

	public static String getTopicRoot() {
		return myTopicRoot;
	}

	public static void setTopicRoot(String root) {
		myTopicRoot = root;
	}
}
