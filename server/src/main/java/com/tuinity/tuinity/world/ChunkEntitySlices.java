package com.tuinity.tuinity.world;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.EntityComplexPart;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.phys.AxisAlignedBB;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public final class ChunkEntitySlices {
	private final int minSection;
	private final int maxSection;

	private final EntityCollectionBySection allEntities;
	private final EntityCollectionBySection hardCollidingEntities;
	private final Reference2ObjectOpenHashMap<Class<? extends Entity>, EntityCollectionBySection> entitiesByClass;

	public ChunkEntitySlices(int minSection, int maxSection) { // inclusive, inclusive
		this.minSection = minSection;
		this.maxSection = maxSection;

		this.allEntities = new EntityCollectionBySection(this);
		this.hardCollidingEntities = new EntityCollectionBySection(this);
		this.entitiesByClass = new Reference2ObjectOpenHashMap<>();
	}

	// synchronized is used in this class for write protection, thank you dumbass mods for doing dumb
	// shit async.

	public synchronized void addEntity(Entity entity, int chunkSection) {
		var sectionIndex = chunkSection - this.minSection;

		this.allEntities.addEntity(entity, sectionIndex);

		if (entity.hardCollides()) {
			this.hardCollidingEntities.addEntity(entity, sectionIndex);
		}

		for (Iterator<Reference2ObjectMap.Entry<Class<? extends Entity>, EntityCollectionBySection>> iterator =
				 this.entitiesByClass.reference2ObjectEntrySet().fastIterator(); iterator.hasNext(); ) {
			var entry = iterator.next();

			if (entry.getKey().isInstance(entity)) {
				entry.getValue().addEntity(entity, sectionIndex);
			}
		}
	}

	public synchronized void removeEntity(Entity entity, int chunkSection) {
		var sectionIndex = chunkSection - this.minSection;

		this.allEntities.removeEntity(entity, sectionIndex);

		if (entity.hardCollides()) {
			this.hardCollidingEntities.removeEntity(entity, sectionIndex);
		}

		for (Iterator<Reference2ObjectMap.Entry<Class<? extends Entity>, EntityCollectionBySection>> iterator =
				 this.entitiesByClass.reference2ObjectEntrySet().fastIterator(); iterator.hasNext(); ) {
			var entry = iterator.next();

			if (entry.getKey().isInstance(entity)) {
				entry.getValue().removeEntity(entity, sectionIndex);
			}
		}
	}

	public void getHardCollidingEntities(Entity except, AxisAlignedBB box, List<Entity> into, Predicate<? super Entity> predicate) {
		this.hardCollidingEntities.getEntities(except, box, into, predicate);
	}

	public void getEntities(Entity except, AxisAlignedBB box, List<Entity> into, Predicate<? super Entity> predicate) {
		this.allEntities.getEntitiesWithEnderDragonParts(except, box, into, predicate);
	}

	public <T extends Entity> void getEntities(EntityTypes<?> type, AxisAlignedBB box, List<? super T> into,
																						 Predicate<? super T> predicate) {
		this.allEntities.getEntities(type, box, into, predicate);
	}

	private EntityCollectionBySection initClass(Class<? extends Entity> clazz) {
		var ret = new EntityCollectionBySection(this);

		for (var sectionIndex = 0; sectionIndex < this.allEntities.entitiesBySection.length; ++sectionIndex) {
			var sectionEntities = this.allEntities.entitiesBySection[sectionIndex];
			if (sectionEntities == null) {
				continue;
			}

			var storage = sectionEntities.storage;

			for (int i = 0, len = Math.min(storage.length, sectionEntities.size()); i < len; ++i) {
				var entity = storage[i];

				if (clazz.isInstance(entity)) {
					ret.addEntity(entity, sectionIndex);
				}
			}
		}

		return ret;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T extends Entity> void getEntities(Class<? extends T> clazz, Entity except, AxisAlignedBB box, List<? super T> into,
																						 Predicate<? super T> predicate) {
		var collection = this.entitiesByClass.get(clazz);
		if (collection != null) {
			collection.getEntities(except, box, (List) into, (Predicate) predicate);
		} else {
			synchronized (this) {
				this.entitiesByClass.putIfAbsent(clazz, collection = this.initClass(clazz));
			}
			collection.getEntities(except, box, (List) into, (Predicate) predicate);
		}
	}

	protected static final class BasicEntityList<E extends Entity> {
		private static final Entity[] EMPTY = new Entity[0];
		private static final int DEFAULT_CAPACITY = 4;

		private E[] storage;
		private int size;

		public BasicEntityList() {
			this(0);
		}

		@SuppressWarnings("unchecked")
		public BasicEntityList(int cap) {
			this.storage = (E[]) (cap <= 0 ? EMPTY : new Entity[cap]);
		}

		public boolean isEmpty() {
			return this.size == 0;
		}

		public int size() {
			return this.size;
		}

		@SuppressWarnings("unchecked")
		private void resize() {
			if (this.storage == EMPTY) {
				this.storage = (E[]) new Entity[DEFAULT_CAPACITY];
			} else {
				this.storage = Arrays.copyOf(this.storage, this.storage.length * 2);
			}
		}

		public void add(E entity) {
			var idx = this.size++;
			if (idx >= this.storage.length) {
				this.resize();
				this.storage[idx] = entity;
			} else {
				this.storage[idx] = entity;
			}
		}

		public int indexOf(E entity) {
			var storage = this.storage;

			for (int i = 0, len = Math.min(this.storage.length, this.size); i < len; ++i) {
				if (storage[i] == entity) {
					return i;
				}
			}

			return -1;
		}

		public boolean remove(E entity) {
			var idx = this.indexOf(entity);
			if (idx == -1) {
				return false;
			}

			var size = --this.size;
			var storage = this.storage;
			if (idx != size) {
				System.arraycopy(storage, idx + 1, storage, idx, size - idx);
			}

			storage[size] = null;

			return true;
		}

		public boolean has(E entity) {
			return this.indexOf(entity) != -1;
		}
	}

	protected static final class EntityCollectionBySection {
		private final ChunkEntitySlices manager;
		private final BasicEntityList<Entity>[] entitiesBySection;
		private int count;

		@SuppressWarnings("unchecked")
		public EntityCollectionBySection(ChunkEntitySlices manager) {
			this.manager = manager;

			var sectionCount = manager.maxSection - manager.minSection + 1;

			this.entitiesBySection = new BasicEntityList[sectionCount];
		}

		public void addEntity(Entity entity, int sectionIndex) {
			var list = this.entitiesBySection[sectionIndex];

			if (list != null && list.has(entity)) {
				return;
			}

			if (list == null) {
				this.entitiesBySection[sectionIndex] = list = new BasicEntityList<>();
			}

			list.add(entity);
			++this.count;
		}

		public void removeEntity(Entity entity, int sectionIndex) {
			var list = this.entitiesBySection[sectionIndex];

			if (list == null || !list.remove(entity)) {
				return;
			}

			--this.count;

			if (list.isEmpty()) {
				this.entitiesBySection[sectionIndex] = null;
			}
		}

		public void getEntities(Entity except, AxisAlignedBB box, List<Entity> into, Predicate<? super Entity> predicate) {
			if (this.count == 0) {
				return;
			}

			var minSection = this.manager.minSection;
			var maxSection = this.manager.maxSection;

			var min = MathHelper.clamp(MathHelper.floor(box.minY - 2.0) >> 4, minSection, maxSection);
			var max = MathHelper.clamp(MathHelper.floor(box.maxY + 2.0) >> 4, minSection, maxSection);

			// TODO use the bitset

			var entitiesBySection = this.entitiesBySection;

			for (var section = min; section <= max; ++section) {
				var list = entitiesBySection[section - minSection];

				if (list == null) {
					continue;
				}

				var storage = list.storage;

				for (int i = 0, len = Math.min(storage.length, list.size()); i < len; ++i) {
					var entity = storage[i];

					if (entity == null || entity == except || !entity.getBoundingBox().intersects(box)) {
						continue;
					}

					if (predicate != null && !predicate.test(entity)) {
						continue;
					}

					into.add(entity);
				}
			}
		}

		public void getEntitiesWithEnderDragonParts(Entity except, AxisAlignedBB box, List<Entity> into,
																								Predicate<? super Entity> predicate) {
			if (this.count == 0) {
				return;
			}

			var minSection = this.manager.minSection;
			var maxSection = this.manager.maxSection;

			var min = MathHelper.clamp(MathHelper.floor(box.minY - 2.0) >> 4, minSection, maxSection);
			var max = MathHelper.clamp(MathHelper.floor(box.maxY + 2.0) >> 4, minSection, maxSection);

			// TODO use the bitset

			var entitiesBySection = this.entitiesBySection;

			for (var section = min; section <= max; ++section) {
				var list = entitiesBySection[section - minSection];

				if (list == null) {
					continue;
				}

				var storage = list.storage;

				for (int i = 0, len = Math.min(storage.length, list.size()); i < len; ++i) {
					var entity = storage[i];

					if (entity == null || entity == except || !entity.getBoundingBox().intersects(box)) {
						continue;
					}

					if (predicate == null || predicate.test(entity)) {
						into.add(entity);
					} // else: continue to test the ender dragon parts

					if (entity instanceof EntityEnderDragon) {
						for (var part : ((EntityEnderDragon) entity).children) {
							if (part == except || !part.getBoundingBox().intersects(box)) {
								continue;
							}

							if (predicate != null && !predicate.test(part)) {
								continue;
							}

							into.add(part);
						}
					}
				}
			}
		}

		public <T extends Entity> void getEntities(EntityTypes<?> type, AxisAlignedBB box, List<? super T> into,
																							 Predicate<? super T> predicate) {
			if (this.count == 0) {
				return;
			}

			var minSection = this.manager.minSection;
			var maxSection = this.manager.maxSection;

			var min = MathHelper.clamp(MathHelper.floor(box.minY - 2.0) >> 4, minSection, maxSection);
			var max = MathHelper.clamp(MathHelper.floor(box.maxY + 2.0) >> 4, minSection, maxSection);

			// TODO use the bitset

			var entitiesBySection = this.entitiesBySection;

			for (var section = min; section <= max; ++section) {
				var list = entitiesBySection[section - minSection];

				if (list == null) {
					continue;
				}

				var storage = list.storage;

				for (int i = 0, len = Math.min(storage.length, list.size()); i < len; ++i) {
					var entity = storage[i];

					if (entity == null || (type != null && entity.getEntityType() != type) || !entity.getBoundingBox().intersects(box)) {
						continue;
					}

					if (predicate != null && !predicate.test((T) entity)) {
						continue;
					}

					into.add((T) entity);
				}
			}
		}
	}
}
