package dev.kamillaova.paper.recycle;

public interface Recyclable extends AutoCloseable {
	void recycle();

	default void close() {
		this.recycle();
	}
}
