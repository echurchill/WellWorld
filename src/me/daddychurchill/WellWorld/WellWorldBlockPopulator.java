package me.daddychurchill.WellWorld;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;

public class WellWorldBlockPopulator extends BlockPopulator {

	private WellWorldChunkGenerator chunkGen;

	public WellWorldBlockPopulator(WellWorldChunkGenerator chunkGen) {
		this.chunkGen = chunkGen;
	}

	@Override
	public void populate(World world, Random random, Chunk source) {
		int chunkX = source.getX();
		int chunkZ = source.getZ();

		// figure out what everything looks like
		WellArchetype well = chunkGen.getWellManager(world, random, chunkX, chunkZ);
		if (well != null) {

			// well centric chunkX/Z
			int adjustedX = chunkX - well.getX();
			int adjustedZ = chunkZ - well.getZ();

			// populate the chunk
			well.populateBlocks(source, adjustedX, adjustedZ);

			// draw the well walls
			populateWalls(well, source, adjustedX, adjustedZ);
		}
	}

	private void populateWalls(WellArchetype well, Chunk source, int wellChunkX, int wellChunkZ) {
		if (chunkGen.wellMarkers) {

			// sides?
			boolean wallNorth = wellChunkX == 0;
			boolean wallSouth = wellChunkX == WellWorld.wellWidthInChunks - 1;
			boolean wallWest = wellChunkZ == 0;
			boolean wallEast = wellChunkZ == WellWorld.wellWidthInChunks - 1;

			if (wallNorth || wallSouth || wallEast || wallWest) {

				// title
				if (wallNorth && wallWest) {
					int x = 0;
					int y = WellWorldChunkGenerator.wallHeightInBlocks;
					int z = 0;
					Block block = source.getBlock(x, y, z);
					block.setType(Material.SIGN);

					BlockData data = block.getBlockData();
					if (data instanceof org.bukkit.block.data.type.Sign) {
						org.bukkit.block.data.type.Sign sign = (org.bukkit.block.data.type.Sign) data;
						sign.setRotation(BlockFace.SOUTH_EAST);
						block.setBlockData(sign);
					}

					BlockState state = block.getState();
					if (state instanceof org.bukkit.block.Sign) {

						org.bukkit.block.Sign sign = (org.bukkit.block.Sign) state;
						String text = well.getClass().getSimpleName();

						if (text.length() > 16) {
							sign.setLine(0, text.substring(0, 16));
							if (text.length() > 32) {
								sign.setLine(1, text.substring(15, 32));
								if (text.length() > 64) {
									sign.setLine(2, text.substring(31, 64));
									if (text.length() > 128) {
										sign.setLine(3, text.substring(63, 128));
									} else
										sign.setLine(3, text.substring(63, text.length()));
								} else
									sign.setLine(2, text.substring(31, text.length()));
							} else
								sign.setLine(1, text.substring(15, text.length()));
						} else
							sign.setLine(0, text.substring(0, text.length()));

						state.update();
					}
				}
			}
		}
	}
}
