package net.caprazzi.tools.sbatti.io.netty;

import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;

public interface NettyCaptureStoreClientReceiptListener {

	void onReceipt(CapturedReceipt sendCapture);

}
