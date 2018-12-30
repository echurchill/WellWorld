package me.daddychurchill.WellWorld.Support;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class InitialBlocks {
	public int chunkX;
	public int chunkZ;
	public int width;
//	public int height;
	public ChunkData chunkData;

	public static final int chunksBlockWidth = 16;

	public InitialBlocks(World aWorld, ChunkData data, int aChunkX, int aChunkZ) {
		super();
		chunkX = aChunkX;
		chunkZ = aChunkZ;
		width = chunksBlockWidth;
//		height = aWorld.getMaxHeight();
		chunkData = data;
	}

	public int getBlockX(int x) {
		return chunkX * width + x;
	}

	public int getBlockZ(int z) {
		return chunkZ * width + z;
	}

	public Material getBlockType(int x, int y, int z) {
		return chunkData.getType(x, y, z);
	}

	public boolean isBlock(int x, int y, int z, Material material) {
		return chunkData.getType(x, y, z) == material;
	}

	public void setBlock(int x, int y, int z, Material material) {
		chunkData.setBlock(x, y, z, material);
	}

	public void setBlockIfAir(int x, int y, int z, Material material) {
		if (chunkData.getType(x, y, z) == Material.AIR && chunkData.getType(x, y - 1, z) != Material.AIR)
			chunkData.setBlock(x, y, z, material);
	}

	public void setBlocks(int x, int y1, int y2, int z, Material material) {
		for (int y = y1; y < y2; y++)
			chunkData.setBlock(x, y, z, material);
	}

	public void setBlocks(int x1, int x2, int y, int z1, int z2, Material material) {
		for (int x = x1; x < x2; x++) {
			for (int z = z1; z < z2; z++) {
				chunkData.setBlock(x, y, z, material);
			}
		}
	}

	public void setBlocks(int x1, int x2, int y1, int y2, int z1, int z2, Material material) {
		for (int x = x1; x < x2; x++) {
			for (int z = z1; z < z2; z++) {
				for (int y = y1; y < y2; y++)
					chunkData.setBlock(x, y, z, material);
			}
		}
	}

	public void setBlocksAt(int y, Material material) {
		setBlocks(0, width, y, 0, width, material);
	}

	public void setBlocksAt(int y1, int y2, Material material) {
		for (int y = y1; y < y2; y++)
			setBlocks(0, width, y, 0, width, material);
	}

//	public void setAllBlocks(byte materialID) {
//		// shortcut if we are simply clearing everything
//		if (materialID == airId) {
//			for (int c = 0; c < sectionsPerChunk; c++) {
//				blocks[c] = null;
//			}
//		
//		// fine.. do it the hard way
//		} else {
//			for (int c = 0; c < sectionsPerChunk; c++) {
//				if (blocks[c] == null)
//					blocks[c] = new byte[bytesPerSection];
//				Arrays.fill(blocks[c], 0, bytesPerSection, materialID);
//			}
//		}	
//	}
//
//	public void setAllBlocks(Material material) {
//		setAllBlocks((byte) material.getId());
//	}
//	
//	public void replaceBlocks(byte fromId, byte toId) {
//		// if we are replacing air we might need to do this the hard way
//		if (fromId == airId) {
//			for (int c = 0; c < sectionsPerChunk; c++) {
//				if (blocks[c] == null)
//					blocks[c] = new byte[bytesPerSection];
//				for (int i = 0; i < bytesPerSection; i++) {
//					if (blocks[c][i] == fromId)
//						blocks[c][i] = toId;
//				}
//			}
//		
//		// if not then take a short cut if we can
//		} else {
//			for (int c = 0; c < sectionsPerChunk; c++) {
//				if (blocks[c] != null) {
//					for (int i = 0; i < bytesPerSection; i++) {
//						if (blocks[c][i] == fromId)
//							blocks[c][i] = toId;
//					}
//				}
//			}
//		}
//	}
//
//	public void replaceBlocks(Material fromMaterial, Material toMaterial) {
//		replaceBlocks((byte) fromMaterial.getId(), (byte) toMaterial.getId());
//	}

	public int setLayer(int blocky, Material material) {
		setBlocks(0, width, blocky, blocky + 1, 0, width, material);
		return blocky + 1;
	}

	public int setLayer(int blocky, int height, Material material) {
		setBlocks(0, width, blocky, blocky + height, 0, width, material);
		return blocky + height;
	}

	public int setLayer(int blocky, int height, int inset, Material material) {
		setBlocks(inset, width - inset, blocky, blocky + height, inset, width - inset, material);
		return blocky + height;
	}

	public void setArcNorthWest(int inset, int y1, int y2, Material primary, boolean fill) {
		setArcNorthWest(inset, y1, y2, primary, primary, fill);
	}

	public void setArcSouthWest(int inset, int y1, int y2, Material primary, boolean fill) {
		setArcSouthWest(inset, y1, y2, primary, primary, fill);
	}

	public void setArcNorthEast(int inset, int y1, int y2, Material primary, boolean fill) {
		setArcNorthEast(inset, y1, y2, primary, primary, fill);
	}

	public void setArcSouthEast(int inset, int y1, int y2, Material primary, boolean fill) {
		setArcSouthEast(inset, y1, y2, primary, primary, fill);
	}

	protected void setArcNorthWest(int inset, int y1, int y2, Material primary, Material secondary, boolean fill) {
		// Ref: Notes/BCircle.PDF
		int cx = inset;
		int cz = inset;
		int r = width - inset * 2 - 1;
		int x = r;
		int z = 0;
		int xChange = 1 - 2 * r;
		int zChange = 1;
		int rError = 0;

		while (x >= z) {
			if (fill) {
				setBlocks(cx, cx + x + 1, y1, y2, cz + z, cz + z + 1, primary); // point in octant 1 ENE
				setBlocks(cx, cx + z + 1, y1, y2, cz + x, cz + x + 1, primary); // point in octant 2 NNE
			} else {
				setBlock(cx + x, y1, cz + z, primary); // point in octant 1 ENE
				setBlocks(cx + x, y1 + 1, y2, cz + z, secondary); // point in octant 1 ENE
				setBlock(cx + z, y1, cz + x, primary); // point in octant 2 NNE
				setBlocks(cx + z, y1 + 1, y2, cz + x, secondary); // point in octant 2 NNE
			}

			z++;
			rError += zChange;
			zChange += 2;
			if (2 * rError + xChange > 0) {
				x--;
				rError += xChange;
				xChange += 2;
			}
		}
	}

	protected void setArcSouthWest(int inset, int y1, int y2, Material primary, Material secondary, boolean fill) {
		// Ref: Notes/BCircle.PDF
		int cx = inset;
		int cz = width - inset;
		int r = width - inset * 2 - 1;
		int x = r;
		int z = 0;
		int xChange = 1 - 2 * r;
		int zChange = 1;
		int rError = 0;

		while (x >= z) {
			if (fill) {
				setBlocks(cx, cx + z + 1, y1, y2, cz - x - 1, cz - x, primary); // point in octant 7 WNW
				setBlocks(cx, cx + x + 1, y1, y2, cz - z - 1, cz - z, primary); // point in octant 8 NNW
			} else {
				setBlock(cx + z, y1, cz - x - 1, primary); // point in octant 7 WNW
				setBlocks(cx + z, y1 + 1, y2, cz - x - 1, secondary); // point in octant 7 WNW
				setBlock(cx + x, y1, cz - z - 1, primary); // point in octant 8 NNW
				setBlocks(cx + x, y1 + 1, y2, cz - z - 1, secondary); // point in octant 8 NNW
			}

			z++;
			rError += zChange;
			zChange += 2;
			if (2 * rError + xChange > 0) {
				x--;
				rError += xChange;
				xChange += 2;
			}
		}
	}

	protected void setArcNorthEast(int inset, int y1, int y2, Material primary, Material secondary, boolean fill) {
		// Ref: Notes/BCircle.PDF
		int cx = width - inset;
		int cz = inset;
		int r = width - inset * 2 - 1;
		int x = r;
		int z = 0;
		int xChange = 1 - 2 * r;
		int zChange = 1;
		int rError = 0;

		while (x >= z) {
			if (fill) {
				setBlocks(cx - z - 1, cx, y1, y2, cz + x, cz + x + 1, primary); // point in octant 3 ESE
				setBlocks(cx - x - 1, cx, y1, y2, cz + z, cz + z + 1, primary); // point in octant 4 SSE
			} else {
				setBlock(cx - z - 1, y1, cz + x, primary); // point in octant 3 ESE
				setBlocks(cx - z - 1, y1 + 1, y2, cz + x, secondary); // point in octant 3 ESE
				setBlock(cx - x - 1, y1, cz + z, primary); // point in octant 4 SSE
				setBlocks(cx - x - 1, y1 + 1, y2, cz + z, secondary); // point in octant 4 SSE
			}

			z++;
			rError += zChange;
			zChange += 2;
			if (2 * rError + xChange > 0) {
				x--;
				rError += xChange;
				xChange += 2;
			}
		}
	}

	protected void setArcSouthEast(int inset, int y1, int y2, Material primary, Material secondary, boolean fill) {
		// Ref: Notes/BCircle.PDF
		int cx = width - inset;
		int cz = width - inset;
		int r = width - inset * 2 - 1;
		int x = r;
		int z = 0;
		int xChange = 1 - 2 * r;
		int zChange = 1;
		int rError = 0;

		while (x >= z) {
			if (fill) {
				setBlocks(cx - x - 1, cx, y1, y2, cz - z - 1, cz - z, primary); // point in octant 5 SSW
				setBlocks(cx - z - 1, cx, y1, y2, cz - x - 1, cz - x, primary); // point in octant 6 WSW
			} else {
				setBlock(cx - x - 1, y1, cz - z - 1, primary); // point in octant 5 SSW
				setBlocks(cx - x - 1, y1 + 1, y2, cz - z - 1, secondary); // point in octant 5 SSW
				setBlock(cx - z - 1, y1, cz - x - 1, primary); // point in octant 6 WSW
				setBlocks(cx - z - 1, y1 + 1, y2, cz - x - 1, secondary); // point in octant 6 WSW
			}

			z++;
			rError += zChange;
			zChange += 2;
			if (2 * rError + xChange > 0) {
				x--;
				rError += xChange;
				xChange += 2;
			}
		}
	}

	private void drawCircleBlocks(int cx, int cz, int x, int z, int y, Material material) {
		// Ref: Notes/BCircle.PDF
		setBlock(cx + x, y, cz + z, material); // point in octant 1
		setBlock(cx + z, y, cz + x, material); // point in octant 2
		setBlock(cx - z - 1, y, cz + x, material); // point in octant 3
		setBlock(cx - x - 1, y, cz + z, material); // point in octant 4
		setBlock(cx - x - 1, y, cz - z - 1, material); // point in octant 5
		setBlock(cx - z - 1, y, cz - x - 1, material); // point in octant 6
		setBlock(cx + z, y, cz - x - 1, material); // point in octant 7
		setBlock(cx + x, y, cz - z - 1, material); // point in octant 8
	}

	private void drawCircleBlocks(int cx, int cz, int x, int z, int y1, int y2, Material material) {
		for (int y = y1; y < y2; y++) {
			drawCircleBlocks(cx, cz, x, z, y, material);
		}
	}

	private void fillCircleBlocks(int cx, int cz, int x, int z, int y, Material material) {
		// Ref: Notes/BCircle.PDF
		setBlocks(cx - x - 1, cx - x, y, cz - z - 1, cz + z + 1, material); // point in octant 5
		setBlocks(cx - z - 1, cx - z, y, cz - x - 1, cz + x + 1, material); // point in octant 6
		setBlocks(cx + z, cx + z + 1, y, cz - x - 1, cz + x + 1, material); // point in octant 7
		setBlocks(cx + x, cx + x + 1, y, cz - z - 1, cz + z + 1, material); // point in octant 8
	}

	private void fillCircleBlocks(int cx, int cz, int x, int z, int y1, int y2, Material material) {
		for (int y = y1; y < y2; y++) {
			fillCircleBlocks(cx, cz, x, z, y, material);
		}
	}

	public void setCircle(int cx, int cz, int r, int y, Material material, boolean fill) {
		setCircle(cx, cz, r, y, y + 1, material, fill);
	}

	public void setCircle(int cx, int cz, int r, int y1, int y2, Material material, boolean fill) {
		// Ref: Notes/BCircle.PDF
		int x = r;
		int z = 0;
		int xChange = 1 - 2 * r;
		int zChange = 1;
		int rError = 0;

		while (x >= z) {
			if (fill)
				fillCircleBlocks(cx, cz, x, z, y1, y2, material);
			else
				drawCircleBlocks(cx, cz, x, z, y1, y2, material);
			z++;
			rError += zChange;
			zChange += 2;
			if (2 * rError + xChange > 0) {
				x--;
				rError += xChange;
				xChange += 2;
			}
		}
	}
}
