package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nullable;
import java.io.IOException;

public class PacketPlayOutTitle implements Packet<PacketListenerPlayOut> {

	private PacketPlayOutTitle.EnumTitleAction a;
	private IChatBaseComponent b;
	public net.kyori.adventure.text.Component adventure$text; // Paper
	private int c;
	private int d;
	private int e;

	public PacketPlayOutTitle() {
	}

	public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction, IChatBaseComponent ichatbasecomponent) {
		this(packetplayouttitle_enumtitleaction, ichatbasecomponent, -1, -1, -1);
	}

	public PacketPlayOutTitle(int i, int j, int k) {
		this(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent) null, i, j, k);
	}

	public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction, @Nullable IChatBaseComponent ichatbasecomponent, int i, int j, int k) {
		this.a = packetplayouttitle_enumtitleaction;
		this.b = ichatbasecomponent;
		this.c = i;
		this.d = j;
		this.e = k;
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = packetdataserializer.readEnum(EnumTitleAction.VALUES);
		if (this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE || this.a == PacketPlayOutTitle.EnumTitleAction.ACTIONBAR) {
			this.b = packetdataserializer.h();
		}

		if (this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
			this.c = packetdataserializer.readInt();
			this.d = packetdataserializer.readInt();
			this.e = packetdataserializer.readInt();
		}

	}

	// Paper start
	public net.md_5.bungee.api.chat.BaseComponent[] components;

	public PacketPlayOutTitle(EnumTitleAction action, net.md_5.bungee.api.chat.BaseComponent[] components, int fadeIn, int stay, int fadeOut) {
		this.a = action;
		this.components = components;
		this.c = fadeIn;
		this.d = stay;
		this.e = fadeOut;
	}
	// Paper end

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.writeEnum(this.a);
		if (this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE || this.a == PacketPlayOutTitle.EnumTitleAction.ACTIONBAR) {
			// Paper start
			if (this.adventure$text != null) {
				packetdataserializer.writeComponent(this.adventure$text);
			} else if (this.components != null) {
				packetdataserializer.writeComponent(this.components);
			} else
				// Paper end
				packetdataserializer.a(this.b);
		}

		if (this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
			packetdataserializer.writeInt(this.c);
			packetdataserializer.writeInt(this.d);
			packetdataserializer.writeInt(this.e);
		}

	}

	public void a(PacketListenerPlayOut packetlistenerplayout) {
		packetlistenerplayout.a(this);
	}

	public enum EnumTitleAction {
		TITLE,
		SUBTITLE,
		ACTIONBAR,
		TIMES,
		CLEAR,
		RESET,
		;

		public static final EnumTitleAction @Unmodifiable [] VALUES = values();
	}
}
