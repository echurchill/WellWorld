package me.daddychurchill.WellWorld.WellTypes;

import me.daddychurchill.WellWorld.Support.InitialBlocks;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;
import org.bukkit.util.noise.NoiseGenerator;

public class SmoothSnowWell extends StandardWellArchetype {

	private static Material materialSnowBase = Material.SNOW_BLOCK;
	private static Material materialSnow = Material.SNOW;
	private static Material materialPumpkin = Material.JACK_O_LANTERN;
	int oddsSnowMan = 1; // % of the time it shows up
	
	public SmoothSnowWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		materialBottom = Material.STONE;
		materialMiddle = Material.SNOW_BLOCK;
		materialTop = materialMiddle;
		materialLiquid = Material.ICE;
		
		vScale = calcRandomRange(3.0, 7.0);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double doubleY = getGenerator().noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, frequency, amplitude) * vScale + bottomLevel;
				int y = NoiseGenerator.floor(doubleY);
				chunk.setBlocks(x, 1, y - middleThickness, z, materialBottom);
				chunk.setBlocks(x, y - middleThickness, y, z, materialMiddle);
				if (y < liquidLevel) {
					chunk.setBlocks(x, y, liquidLevel, z, materialLiquid);
				} else
					chunk.setBlock(x, y, z, materialTop);
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		
		//1060, 43
		
		// smooth out the snow and add a snowman... maybe
		boolean madeSnowMan = false;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double doubleY = getGenerator().noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, frequency, amplitude) * vScale + bottomLevel;
				int y = NoiseGenerator.floor(doubleY);
				Block block = chunk.getBlock(x, y, z);
				if (block.getType() == materialSnowBase) {
					
					// to make a snowman or not?
					int amount = NoiseGenerator.floor((doubleY - y) * 8);
					if (!madeSnowMan && amount == 0 && random.nextInt(100) < oddsSnowMan) {
						chunk.getBlock(x, y + 1  , z).setType(materialSnowBase, false);
						chunk.getBlock(x, y + 2, z).setType(materialSnowBase, false);
						chunk.getBlock(x, y + 3, z).setType(materialPumpkin, true);
						madeSnowMan = true;
					} else {
						block.setType(materialSnow);
						BlockData data = block.getBlockData();
						if (data instanceof Snow) {
							Snow snow = (Snow)data;
							snow.setLayers(amount + 1);
							block.setBlockData(snow);
						}
					}
				}
			}
		}
		
		//TODO place one tree
		
		//populateSpecials(chunk, chunkX, chunkZ);
	}
}
