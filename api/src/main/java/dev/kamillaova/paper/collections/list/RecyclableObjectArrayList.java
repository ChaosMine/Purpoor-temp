package dev.kamillaova.paper.collections.list;

import io.netty.util.internal.ObjectPool;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class RecyclableObjectArrayList<T> extends ObjectArrayList<T> implements RecyclableObjectList<T> {
	private static final ObjectPool<RecyclableObjectArrayList> RECYCLER = ObjectPool.newPool(RecyclableObjectArrayList::new);

	private final ObjectPool.Handle<RecyclableObjectArrayList> handle;

	private RecyclableObjectArrayList(ObjectPool.Handle<RecyclableObjectArrayList> handle) {
		this.handle = handle;
	}

	public static <T> RecyclableObjectArrayList<T> newInstance() {
		return (RecyclableObjectArrayList) RECYCLER.get();
	}

	@Override
	public void recycle() {
		if (handle != null) {
			this.clear();
			handle.recycle(this);
		}
	}
}
