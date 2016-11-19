// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package onewire.state;

import chainedfsm.EnterChain;
import chainedfsm.LeaveChain;
import hap.communication.Communicator;
import hap.message.Message;
import hap.message.general.UnclassifiedMessage;
import jowshell.Network;
import jowshell.items.OwData;
import jowshell.items.OwDevice;
import onewire.OneWire;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ReadValueState extends OwBaseState {
	public ReadValueState(Communicator com, OneWire oneWire) {
		super(com, oneWire);
		myNet = myOw.getDiscovery().getNetwork();
		myDevice = myNet.getAllDevices();
		new EnterChain<>(this, this::enter);
		new LeaveChain<>(this, this::leave);
	}

	public void enter() {
		myWorker = new Thread(() -> {
			while (!myShouldStop) {
				for (String name : myDevice.keySet()) {
					if (myShouldStop) {
						return;
					}

					OwDevice d = myDevice.get(name);
					HashMap<String, OwData> data = d.getData();
					for (String dataName : data.keySet()) {
						if (myShouldStop) {
							return;
						}

						OwData dataItem = data.get(dataName);
						if (dataItem.isReadable(myOw)) {
							String readData = dataItem.read(myOw);
							String topic = Message.combineTopic(myOw.getOwTopic(), d.getName());
							topic = Message.combineTopic(topic, dataItem.getFullPropertyName());
							Message m = new UnclassifiedMessage(topic, readData.getBytes(), Message.QOS.AtMostOnce, false);

							// Transfer to module's main thread
							myOutgoing.add(m);
						}
					}
				}

			}
		});

		myWorker.start();
	}

	public void leave() {
		myShouldStop = true;
		try {
			myWorker.wait();
		} catch (InterruptedException e) {
			// Ignore
		}
	}

	@Override
	public void tick() {
		while (!myOutgoing.isEmpty()) {
			Message m = myOutgoing.pollFirst();
			myCom.publish(m);
		}
	}

	private final ConcurrentLinkedDeque<Message> myOutgoing = new ConcurrentLinkedDeque<>();
	private boolean myShouldStop = false;
	private Thread myWorker;
	private final Network myNet;
	private final HashMap<String, OwDevice> myDevice;
}
