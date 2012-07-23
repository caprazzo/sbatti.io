package net.caprazzi.tools.sbatti.io;

public interface AsyncStore<T> {

	void Store(StoreRequest<T> request);
	
}
