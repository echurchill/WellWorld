package me.daddychurchill.WellWorld.Support;

import java.util.Arrays;

import org.bukkit.Material;

public class ByteChunk {
	public final static int Width = 16;
	public final static int Height = 128;
	
	private int chunkX;
	private int chunkZ;
	public byte[] blocks;
		
	public ByteChunk (int chunkX, int chunkZ) {
		super();
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		blocks = new byte[Width * Width * Height];
	}
	
	public int getX() {
		return chunkX;
	}
	
	public int getZ() {
		return chunkZ;
	}
	
	public int getBlockX(int x) {
		return chunkX * Width + x;
	}
	
	public int getBlockZ(int z) {
		return chunkZ * Width + z;
	}
	
	public byte getBlock(int x, int y, int z) {
		return blocks[(x * Width + z) * Height + y];
	}
	
	public void setBlock(int x, int y, int z, byte materialId) {
		blocks[(x * Width + z) * Height + y] = materialId;
	}
	
	public void setBlock(int x, int y, int z, Material material) {
		setBlock(x, y, z, (byte) material.getId());
	}
	
	public void setBlocks(int x, int y1, int y2, int z, byte materialId) {
		if (y1 < y2) {
			int xz = (x * Width + z) * Height;
			Arrays.fill(blocks, xz + y1, xz + y2, materialId);
		}
	}
	
	public void setBlocks(int x, int y1, int y2, int z, Material material) {
		setBlocks(x, y1, y2, z, (byte) material.getId());
	}
	
	public void setBlocks(int x1, int x2, int y1, int y2, int z1, int z2, byte materialId) {
		if (y1 < y2) {
			for (int x = x1; x < x2; x++) {
				int xw = x * Width;
				for (int z = z1; z < z2; z++) {
					int xz = (xw + z) * Height;
					Arrays.fill(blocks, xz + y1, xz + y2, materialId);
				}
			}
		}
	}
	
	public void setBlocks(int x1, int x2, int y1, int y2, int z1, int z2, Material material) {
		setBlocks(x1, x2, y1, y2, z1, z2, (byte) material.getId());
	}
	
	public void setBlocksAt(int y, byte materialId) {
		for (int x = 0; x < Width; x++) {
			int xw = x * Width;
			for (int z = 0; z < Width; z++) {
				blocks[(xw + z) * Height + y] = materialId;
			}
		}
	}
	
	public void setBlocksAt(int y, Material material) {
		setBlocksAt(y, (byte) material.getId());
	}
	
	public void setBlocksAt(int y1, int y2, byte materialId) {
		if (y1 < y2) {
			for (int x = 0; x < Width; x++) {
				int xw = x * Width;
				for (int z = 0; z < Width; z++) {
					int xz = (xw + z) * Height;
					Arrays.fill(blocks, xz + y1, xz + y2, materialId);
				}
			}
		}
	}
	
	public void setBlocksAt(int y1, int y2, Material material) {
		setBlocksAt(y1, y2, (byte) material.getId());
	}
	
	public void setAllBlocks(byte materialID) {
		Arrays.fill(blocks, 0, Width * Width * Height, materialID);
	}

	public void setAllBlocks(Material material) {
		setAllBlocks((byte) material.getId());
	}
	
	public void replaceBlocks(byte fromId, byte toId) {
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == fromId)
				blocks[i] = toId;
		}
	}

	public void replaceBlocks(Material fromMaterial, Material toMaterial) {
		replaceBlocks((byte) fromMaterial.getId(), (byte) toMaterial.getId());
	}
}
