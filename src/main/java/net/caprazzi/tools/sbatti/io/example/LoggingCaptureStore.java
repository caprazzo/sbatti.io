package net.caprazzi.tools.sbatti.io.example;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageReceipt;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageStore;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class LoggingCaptureStore<TData> implements DataMessageStore<TData> {

	
	private static ListeningExecutorService service 
		= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
	private final String loggerName;
	
	public LoggingCaptureStore(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	public ListenableFuture<DataMessageReceipt> store(final String sender, final DataMessage<TData> capture) {

		
		return service.submit(new Callable<DataMessageReceipt>() {

			@Override
			public DataMessageReceipt call() throws Exception {
				System.out.println(loggerName + " Stored capture " + capture);
				return DataMessageReceipt.forSuccess(capture.getId(), Instant.now(), "Logged");
			}
		});
		
	}

	@Override
	public void confirm(DataMessageReceipt receipt) {
		// TODO Auto-generated method stub
		
	}

}
