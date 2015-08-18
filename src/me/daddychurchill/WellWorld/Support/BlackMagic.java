package me.daddychurchill.WellWorld.Support;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

@SuppressWarnings("deprecation")
public abstract class BlackMagic {

	public static final void setBlock(InitialBlocks chunk, int x, int y, int z, Material material, byte data) {
		chunk.chunkData.setBlock(x, y, z, material.getId(), data);
	}

	public static final void setBlock(World world, int x, int y, int z, Material material, byte data) {
		setBlock(world.getBlockAt(x, y, z), material, data, false);
	}

	public static final void setBlock(World world, int x, int y, int z, Material material, byte data, boolean physics) {
		setBlock(world.getBlockAt(x, y, z), material, data, physics);
	}

	public static final void setBlock(Chunk chunk, int x, int y, int z, Material material, byte data) {
		setBlock(chunk.getBlock(x, y, z), material, data, false);
	}

	public static final void setBlock(Chunk chunk, int x, int y, int z, Material material, byte data, boolean physics) {
		setBlock(chunk.getBlock(x, y, z), material, data, physics);
	}

	public static final void setBlock(Block block, Material material, byte data) {
		setBlock(block, material, data, false);
	}

	public static final void setBlock(Block block, Material material, byte data, boolean physics) {
		block.setTypeIdAndData(material.getId(), data, physics);
	}
}
