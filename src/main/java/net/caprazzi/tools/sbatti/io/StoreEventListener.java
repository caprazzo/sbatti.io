package net.caprazzi.tools.sbatti.io;

import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageReceipt;

public interface StoreEventListener {

	void onComplete(DataMessageReceipt receipt);
	
}
