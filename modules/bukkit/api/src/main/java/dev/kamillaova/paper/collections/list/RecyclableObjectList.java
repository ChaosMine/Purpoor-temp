package dev.kamillaova.paper.collections.list;

import dev.kamillaova.paper.recycle.Recyclable;
import dev.kamillaova.paper.util.fastutil.ObjectListIterators;
import it.unimi.dsi.fastutil.objects.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface RecyclableObjectList<T> extends ObjectList<T>, Recyclable {
	@SuppressWarnings("unchecked")
	static <T> RecyclableObjectList<T> empty() {
		return _Empty.INSTANCE;
	}

	@SuppressWarnings("rawtypes")
	final class _Empty<E> extends AbstractObjectList<E> implements RecyclableObjectList<E>, RandomAccess {
		private static final _Empty INSTANCE = new _Empty();

		@Override
		public ObjectListIterator<E> iterator() {
			return listIterator();
		}

		@Override
		public ObjectListIterator<E> listIterator() {
			return ObjectListIterators.emptyListIterator();
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public void clear() { }

		@Override
		public boolean contains(Object obj) {
			return false;
		}

		@Override
		public boolean containsAll(Collection c) {
			return c.isEmpty();
		}

		@Override
		public Object[] toArray() {
			return new Object[0];
		}

		@Override
		public E get(int index) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}

		@Override
		public boolean equals(Object o) {
			return (o instanceof List) && ((List<?>) o).isEmpty();
		}

		@Override
		public int hashCode() {
			return 1;
		}

		@Override
		public boolean removeIf(Predicate filter) {
			Objects.requireNonNull(filter);
			return false;
		}

		@Override
		public void replaceAll(UnaryOperator operator) {
			Objects.requireNonNull(operator);
		}

		@Override
		public void sort(Comparator c) { }

		@Override
		public void forEach(Consumer action) {
			Objects.requireNonNull(action);
		}

		@Override
		public ObjectSpliterator<E> spliterator() {
			return ObjectSpliterators.emptySpliterator();
		}

		@Override
		public void recycle() { }
	}
}
