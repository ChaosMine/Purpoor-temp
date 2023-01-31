package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;

import java.io.IOException;

public class PacketPlayOutScoreboardObjective implements Packet<PacketListenerPlayOut> {

	private String a;
	private IChatBaseComponent b;
	private IScoreboardCriteria.EnumScoreboardHealthDisplay c;
	private int d;

	public PacketPlayOutScoreboardObjective() {
	}

	public PacketPlayOutScoreboardObjective(ScoreboardObjective scoreboardobjective, int i) {
		this.a = scoreboardobjective.getName();
		this.b = scoreboardobjective.getDisplayName();
		this.c = scoreboardobjective.getRenderType();
		this.d = i;
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = packetdataserializer.e(16);
		this.d = packetdataserializer.readByte();
		if (this.d == 0 || this.d == 2) {
			this.b = packetdataserializer.h();
			this.c = packetdataserializer.readEnum(IScoreboardCriteria.EnumScoreboardHealthDisplay.VALUES);
		}
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.a(this.a);
		packetdataserializer.writeByte(this.d);
		if (this.d == 0 || this.d == 2) {
			packetdataserializer.a(this.b);
			packetdataserializer.writeEnum(this.c);
		}

	}

	public void a(PacketListenerPlayOut packetlistenerplayout) {
		packetlistenerplayout.a(this);
	}
}
