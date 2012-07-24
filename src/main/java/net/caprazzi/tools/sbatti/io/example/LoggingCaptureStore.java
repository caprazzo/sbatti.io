package net.caprazzi.tools.sbatti.io.example;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import net.caprazzi.tools.sbatti.io.CaptureStore;
import net.caprazzi.tools.sbatti.io.CaptureStoreReceipt;
import net.caprazzi.tools.sbatti.io.CapturedData;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class LoggingCaptureStore<TData> implements CaptureStore<TData> {

	
	private static ListeningExecutorService service 
		= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
	private final String loggerName;
	
	public LoggingCaptureStore(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	public ListenableFuture<CaptureStoreReceipt> store(final String sender, final CapturedData<TData> capture) {

		
		return service.submit(new Callable<CaptureStoreReceipt>() {

			@Override
			public CaptureStoreReceipt call() throws Exception {
				System.out.println(loggerName + " Stored capture " + capture);
				return CaptureStoreReceipt.forSuccess(capture.getId(), Instant.now(), "Logged");
			}
		});
		
	}

	@Override
	public void confirm(CaptureStoreReceipt receipt) {
		// TODO Auto-generated method stub
		
	}

}
