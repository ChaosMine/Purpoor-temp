package org.bukkit.craftbukkit.map;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.map.MapCursor;

import java.util.ArrayList;

public class RenderData {

    public byte[] buffer; // Paper
    public final ObjectArrayList<MapCursor> cursors;

    public RenderData() {
        this.buffer = new byte[128 * 128];
        this.cursors = new ObjectArrayList<MapCursor>();
    }

}
