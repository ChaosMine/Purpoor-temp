package net.minecraft.world.level.levelgen.structure;

import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.EnumBlockRotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureStructurePieceType;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructureJigsawJunction;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructurePoolEmpty;
import net.minecraft.world.level.levelgen.feature.structures.WorldGenFeatureDefinedStructurePoolStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class WorldGenFeaturePillagerOutpostPoolPiece extends StructurePiece {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final WorldGenFeatureDefinedStructurePoolStructure a;
	protected final EnumBlockRotation c;
	private final int e;
	private final List<WorldGenFeatureDefinedStructureJigsawJunction> f = Lists.newArrayList();
	private final DefinedStructureManager g;
	protected BlockPosition b;

	public WorldGenFeaturePillagerOutpostPoolPiece(DefinedStructureManager definedstructuremanager, WorldGenFeatureDefinedStructurePoolStructure worldgenfeaturedefinedstructurepoolstructure, BlockPosition blockposition, int i, EnumBlockRotation enumblockrotation, StructureBoundingBox structureboundingbox) {
		super(WorldGenFeatureStructurePieceType.ad, 0);
		this.g = definedstructuremanager;
		this.a = worldgenfeaturedefinedstructurepoolstructure;
		this.b = blockposition;
		this.e = i;
		this.c = enumblockrotation;
		this.n = structureboundingbox;
	}

	public WorldGenFeaturePillagerOutpostPoolPiece(DefinedStructureManager definedstructuremanager, NBTTagCompound nbttagcompound) {
		super(WorldGenFeatureStructurePieceType.ad, nbttagcompound);
		this.g = definedstructuremanager;
		this.b = new BlockPosition(nbttagcompound.getInt("PosX"), nbttagcompound.getInt("PosY"), nbttagcompound.getInt("PosZ"));
		this.e = nbttagcompound.getInt("ground_level_delta");
		var dataresult = WorldGenFeatureDefinedStructurePoolStructure.e.parse(DynamicOpsNBT.a, nbttagcompound.getCompound("pool_element"));
		this.a = dataresult.resultOrPartial(WorldGenFeaturePillagerOutpostPoolPiece.LOGGER::error).orElse(WorldGenFeatureDefinedStructurePoolEmpty.b);
		this.c = EnumBlockRotation.valueOf(nbttagcompound.getString("rotation"));
		this.n = this.a.a(definedstructuremanager, this.b, this.c);
		NBTTagList nbttaglist = nbttagcompound.getList("junctions", 10);

		this.f.clear();
		nbttaglist.forEach((nbtbase) -> {
			this.f.add(WorldGenFeatureDefinedStructureJigsawJunction.a(new Dynamic<>(DynamicOpsNBT.a, nbtbase)));
		});
	}

	@Override
	protected void a(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInt("PosX", this.b.getX());
		nbttagcompound.setInt("PosY", this.b.getY());
		nbttagcompound.setInt("PosZ", this.b.getZ());
		nbttagcompound.setInt("ground_level_delta", this.e);
		var dataresult = WorldGenFeatureDefinedStructurePoolStructure.e.encodeStart(DynamicOpsNBT.a, this.a);

		dataresult.resultOrPartial(WorldGenFeaturePillagerOutpostPoolPiece.LOGGER::error).ifPresent((nbtbase) -> {
			nbttagcompound.set("pool_element", nbtbase);
		});
		nbttagcompound.setString("rotation", this.c.name());
		NBTTagList nbttaglist = new NBTTagList();

		for (WorldGenFeatureDefinedStructureJigsawJunction worldgenfeaturedefinedstructurejigsawjunction : this.f) {
			nbttaglist.add(worldgenfeaturedefinedstructurejigsawjunction.a(DynamicOpsNBT.a).getValue());
		}

		nbttagcompound.set("junctions", nbttaglist);
	}

	@Override
	public boolean a(GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager, ChunkGenerator chunkgenerator, Random random, StructureBoundingBox structureboundingbox, ChunkCoordIntPair chunkcoordintpair, BlockPosition blockposition) {
		return this.a(generatoraccessseed, structuremanager, chunkgenerator, random, structureboundingbox, blockposition, false);
	}

	public boolean a(GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager, ChunkGenerator chunkgenerator, Random random, StructureBoundingBox structureboundingbox, BlockPosition blockposition, boolean flag) {
		return this.a.a(this.g, generatoraccessseed, structuremanager, chunkgenerator, this.b, blockposition, this.c, structureboundingbox, random, flag);
	}

	@Override
	public void a(int i, int j, int k) {
		super.a(i, j, k);
		this.b = this.b.b(i, j, k);
	}

	@Override
	public EnumBlockRotation ap_() {
		return this.c;
	}

	public String toString() {
		return String.format("<%s | %s | %s | %s>", this.getClass().getSimpleName(), this.b, this.c, this.a);
	}

	public WorldGenFeatureDefinedStructurePoolStructure b() {
		return this.a;
	}

	public BlockPosition c() {
		return this.b;
	}

	public int d() {
		return this.e;
	}

	public void a(WorldGenFeatureDefinedStructureJigsawJunction worldgenfeaturedefinedstructurejigsawjunction) {
		this.f.add(worldgenfeaturedefinedstructurejigsawjunction);
	}

	public List<WorldGenFeatureDefinedStructureJigsawJunction> e() {
		return this.f;
	}
}
