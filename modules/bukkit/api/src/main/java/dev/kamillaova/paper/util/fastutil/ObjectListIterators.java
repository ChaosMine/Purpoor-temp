package dev.kamillaova.paper.util.fastutil;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import java.util.NoSuchElementException;

public final class ObjectListIterators {
	private ObjectListIterators() {}

	@SuppressWarnings("unchecked")
	public static <T> ObjectListIterator<T> emptyListIterator() {
		return EmptyObjectListIterator.INSTANCE;
	}

	@SuppressWarnings("rawtypes")
	static final class EmptyObjectListIterator<E>
		extends ObjectIterators.EmptyObjectIterator<E>
		implements ObjectListIterator<E>
	{
		static final EmptyObjectListIterator INSTANCE = new EmptyObjectListIterator();

		@Override
		public boolean hasPrevious() { return false; }

		@Override
		public E previous() { throw new NoSuchElementException(); }

		@Override
		public int nextIndex() { return 0; }

		@Override
		public int previousIndex() { return -1; }
	}
}
