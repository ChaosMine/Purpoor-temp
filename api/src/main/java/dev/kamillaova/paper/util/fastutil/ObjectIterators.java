package dev.kamillaova.paper.util.fastutil;

import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.NoSuchElementException;

public final class ObjectIterators {
	private ObjectIterators() {}

	@SuppressWarnings("unchecked")
	public static <T> ObjectIterator<T> emptyIterator() {
		return EmptyObjectIterator.INSTANCE;
	}

	@SuppressWarnings("rawtypes")
	static sealed class EmptyObjectIterator<E>
		implements ObjectIterator<E>
		permits ObjectListIterators.EmptyObjectListIterator
	{
		static final EmptyObjectIterator INSTANCE = new EmptyObjectIterator();

		@Override
		public boolean hasNext() { return false; }

		@Override
		public E next() { throw new NoSuchElementException(); }

		@Override
		public void remove() { throw new UnsupportedOperationException(); }
	}
}
