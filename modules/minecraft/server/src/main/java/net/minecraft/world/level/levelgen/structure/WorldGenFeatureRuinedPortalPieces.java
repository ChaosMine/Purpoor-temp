package net.minecraft.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BaseBlockPosition;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureStructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class WorldGenFeatureRuinedPortalPieces extends DefinedStructurePiece {
	private static final Logger LOGGER = LogManager.getLogger();
	private final MinecraftKey e;
	private final EnumBlockRotation f;
	private final EnumBlockMirror g;
	private final Position h;
	private final a_ i;

	public WorldGenFeatureRuinedPortalPieces(BlockPosition var0, Position var1, a_ var2, MinecraftKey var3, DefinedStructure var4, EnumBlockRotation var5, EnumBlockMirror var6, BlockPosition var7) {
		super(WorldGenFeatureStructurePieceType.J, 0);
		this.c = var0;
		this.e = var3;
		this.f = var5;
		this.g = var6;
		this.h = var1;
		this.i = var2;
		this.a(var4, var7);
	}

	public WorldGenFeatureRuinedPortalPieces(DefinedStructureManager var0, NBTTagCompound var1) {
		super(WorldGenFeatureStructurePieceType.J, var1);
		this.e = new MinecraftKey(var1.getString("Template"));
		this.f = EnumBlockRotation.valueOf(var1.getString("Rotation"));
		this.g = EnumBlockMirror.valueOf(var1.getString("Mirror"));
		this.h = Position.a(var1.getString("VerticalPlacement"));
		this.i = WorldGenFeatureRuinedPortalPieces.a_.a.parse(new Dynamic<>(DynamicOpsNBT.a, var1.get("Properties"))).getOrThrow(true, LOGGER::error);
		DefinedStructure var2 = var0.a(this.e);
		this.a(var2, new BlockPosition(var2.a().getX() / 2, 0, var2.a().getZ() / 2));
	}

	private static int a(GeneratorAccess var0, int var1, int var2, Position var3) {
		return var0.a(WorldGenFeatureRuinedPortalPieces.a(var3), var1, var2) - 1;
	}

	public static HeightMap.Type a(Position var0) {
		return var0 == Position.ON_OCEAN_FLOOR ? HeightMap.Type.OCEAN_FLOOR_WG : HeightMap.Type.WORLD_SURFACE_WG;
	}

	private static DefinedStructureProcessorPredicates a(Block var0, float var1, Block var2) {
		return new DefinedStructureProcessorPredicates(new DefinedStructureTestRandomBlock(var0, var1), DefinedStructureTestTrue.b, var2.getBlockData());
	}

	private static DefinedStructureProcessorPredicates a(Block var1) {
		return new DefinedStructureProcessorPredicates(new DefinedStructureTestBlock(Blocks.LAVA), DefinedStructureTestTrue.b, var1.getBlockData());
	}

	@Override
	protected void a(NBTTagCompound var0) {
		super.a(var0);
		var0.setString("Template", this.e.toString());
		var0.setString("Rotation", this.f.name());
		var0.setString("Mirror", this.g.name());
		var0.setString("VerticalPlacement", this.h.a());
		WorldGenFeatureRuinedPortalPieces.a_.a.encodeStart(DynamicOpsNBT.a, this.i).resultOrPartial(LOGGER::error).ifPresent(var1 -> var0.set("Properties", var1));
	}

	private void a(DefinedStructure var0, BlockPosition var1) {
		DefinedStructureProcessorBlockIgnore var2 = this.i.d ? DefinedStructureProcessorBlockIgnore.b : DefinedStructureProcessorBlockIgnore.d;
		var var3 = new ArrayList<DefinedStructureProcessorPredicates>();
		var3.add(WorldGenFeatureRuinedPortalPieces.a(Blocks.GOLD_BLOCK, 0.3f, Blocks.AIR));
		var3.add(this.c());
		if (!this.i.b) {
			var3.add(WorldGenFeatureRuinedPortalPieces.a(Blocks.NETHERRACK, 0.07f, Blocks.MAGMA_BLOCK));
		}
		DefinedStructureInfo var4 = new DefinedStructureInfo().a(this.f).a(this.g).a(var1).a(var2).a(new DefinedStructureProcessorRule(var3)).a(new DefinedStructureProcessorBlockAge(this.i.c)).a(new DefinedStructureProcessorLavaSubmergedBlock());
		if (this.i.g) {
			var4.a(DefinedStructureProcessorBlackstoneReplace.b);
		}
		this.a(var0, this.c, var4);
	}

	private DefinedStructureProcessorPredicates c() {
		if (this.h == Position.ON_OCEAN_FLOOR) {
			return WorldGenFeatureRuinedPortalPieces.a(Blocks.MAGMA_BLOCK);
		}
		if (this.i.b) {
			return WorldGenFeatureRuinedPortalPieces.a(Blocks.NETHERRACK);
		}
		return WorldGenFeatureRuinedPortalPieces.a(Blocks.LAVA, 0.2f, Blocks.MAGMA_BLOCK);
	}

	@Override
	public boolean a(GeneratorAccessSeed var0, StructureManager var1, ChunkGenerator var22, Random var3, StructureBoundingBox var4, ChunkCoordIntPair var5, BlockPosition var6) {
		if (!var4.b(this.c)) {
			return true;
		}
		var4.c(this.a.b(this.b, this.c));
		boolean var7 = super.a(var0, var1, var22, var3, var4, var5, var6);
		this.b(var3, var0);
		this.a(var3, var0);
		if (this.i.f || this.i.e) {
			BlockPosition.a(this.g()).forEach(var2 -> {
				if (this.i.f) {
					this.a(var3, var0, var2);
				}
				if (this.i.e) {
					this.b(var3, var0, var2);
				}
			});
		}
		return var7;
	}

	@Override
	protected void a(String var0, BlockPosition var1, WorldAccess var2, Random var3, StructureBoundingBox var4) {
	}

	private void a(Random var0, GeneratorAccess var1, BlockPosition var2) {
		IBlockData var3 = var1.getType(var2);
		if (var3.isAir() || var3.a(Blocks.VINE)) {
			return;
		}
		EnumDirection var4 = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(var0);
		BlockPosition var5 = var2.shift(var4);
		IBlockData var6 = var1.getType(var5);
		if (!var6.isAir()) {
			return;
		}
		if (!Block.a(var3.getCollisionShape(var1, var2), var4)) {
			return;
		}
		BlockStateBoolean var7 = BlockVine.getDirection(var4.opposite());
		var1.setTypeAndData(var5, Blocks.VINE.getBlockData().set(var7, true), 3);
	}

	private void b(Random var0, GeneratorAccess var1, BlockPosition var2) {
		if (var0.nextFloat() < 0.5f && var1.getType(var2).a(Blocks.NETHERRACK) && var1.getType(var2.up()).isAir()) {
			var1.setTypeAndData(var2.up(), Blocks.JUNGLE_LEAVES.getBlockData().set(BlockLeaves.PERSISTENT, true), 3);
		}
	}

	private void a(Random var0, GeneratorAccess var1) {
		for (int var2 = this.n.a + 1; var2 < this.n.d; ++var2) {
			for (int var3 = this.n.c + 1; var3 < this.n.f; ++var3) {
				BlockPosition var4 = new BlockPosition(var2, this.n.b, var3);
				if (!var1.getType(var4).a(Blocks.NETHERRACK)) continue;
				this.c(var0, var1, var4.down());
			}
		}
	}

	private void c(Random var0, GeneratorAccess var1, BlockPosition var2) {
		BlockPosition.MutableBlockPosition var3 = var2.i();
		this.d(var0, var1, var3);
		for (int var4 = 8; var4 > 0 && var0.nextFloat() < 0.5f; --var4) {
			var3.c(EnumDirection.DOWN);
			this.d(var0, var1, var3);
		}
	}

	private void b(Random var0, GeneratorAccess var1) {
		boolean var2 = this.h == Position.ON_LAND_SURFACE || this.h == Position.ON_OCEAN_FLOOR;
		BaseBlockPosition var3 = this.n.g();
		int var4 = var3.getX();
		int var5 = var3.getZ();
		float[] var6 = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.9f, 0.9f, 0.8f, 0.7f, 0.6f, 0.4f, 0.2f};
		int var7 = var6.length;
		int var8 = (this.n.d() + this.n.f()) / 2;
		int var9 = var0.nextInt(Math.max(1, 8 - var8 / 2));
		BlockPosition.MutableBlockPosition var11 = BlockPosition.ZERO.i();
		for (int var12 = var4 - var7; var12 <= var4 + var7; ++var12) {
			for (int var13 = var5 - var7; var13 <= var5 + var7; ++var13) {
				int var14 = Math.abs(var12 - var4) + Math.abs(var13 - var5);
				int var15 = Math.max(0, var14 + var9);
				if (var15 >= var7) continue;
				float var16 = var6[var15];
				if (!(var0.nextDouble() < (double) var16)) continue;
				int var17 = WorldGenFeatureRuinedPortalPieces.a(var1, var12, var13, this.h);
				int var18 = var2 ? var17 : Math.min(this.n.b, var17);
				var11.d(var12, var18, var13);
				if (Math.abs(var18 - this.n.b) > 3 || !this.a(var1, var11)) continue;
				this.d(var0, var1, var11);
				if (this.i.e) {
					this.b(var0, var1, var11);
				}
				this.c(var0, var1, var11.down());
			}
		}
	}

	private boolean a(GeneratorAccess var0, BlockPosition var1) {
		IBlockData var2 = var0.getType(var1);
		return !var2.a(Blocks.AIR) && !var2.a(Blocks.OBSIDIAN) && !var2.a(Blocks.CHEST) && (this.h == Position.IN_NETHER || !var2.a(Blocks.LAVA));
	}

	private void d(Random var0, GeneratorAccess var1, BlockPosition var2) {
		if (!this.i.b && var0.nextFloat() < 0.07f) {
			var1.setTypeAndData(var2, Blocks.MAGMA_BLOCK.getBlockData(), 3);
		} else {
			var1.setTypeAndData(var2, Blocks.NETHERRACK.getBlockData(), 3);
		}
	}

	public enum Position {
		ON_LAND_SURFACE("on_land_surface"),
		PARTLY_BURIED("partly_buried"),
		ON_OCEAN_FLOOR("on_ocean_floor"),
		IN_MOUNTAIN("in_mountain"),
		UNDERGROUND("underground"),
		IN_NETHER("in_nether");

		private static final Map<String, Position> g;

		static {
			g = Arrays.stream(Position.values()).collect(Collectors.toMap(Position::a, var0 -> var0));
		}

		private final String h;

		Position(String var2) {
			this.h = var2;
		}

		public static Position a(String var0) {
			return g.get(var0);
		}

		public String a() {
			return this.h;
		}
	}

	public static class a_ {
		public static final Codec<a_> a = RecordCodecBuilder.create(builder ->
			builder.group(
				Codec.BOOL.fieldOf("cold").forGetter(v -> v.b),
				Codec.FLOAT.fieldOf("mossiness").forGetter(v -> v.c),
				Codec.BOOL.fieldOf("air_pocket").forGetter(v -> v.d),
				Codec.BOOL.fieldOf("overgrown").forGetter(v -> v.e),
				Codec.BOOL.fieldOf("vines").forGetter(v -> v.f),
				Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(v -> v.g)
			).apply(builder, a_::new)
		);

		public boolean b;
		public float c = 0.2f;
		public boolean d;
		public boolean e;
		public boolean f;
		public boolean g;

		public a_() {
		}

		public a_(boolean var0, float var1, boolean var2, boolean var3, boolean var4, boolean var5) {
			this.b = var0;
			this.c = var1;
			this.d = var2;
			this.e = var3;
			this.f = var4;
			this.g = var5;
		}
	}
}
