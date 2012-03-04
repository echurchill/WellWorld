package me.daddychurchill.WellWorld.WellTypes;

import me.daddychurchill.WellWorld.Support.ByteChunk;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.noise.NoiseGenerator;

public class SmoothSnowWell extends StandardWellArchetype {

	int intSnowBase = Material.SNOW_BLOCK.getId();
	int intSnow = Material.SNOW.getId();
	int intPumpkin = Material.PUMPKIN.getId();
	int oddsSnowMan = 1; // % of the time it shows up
	
	public SmoothSnowWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		bottomMaterial = Material.STONE;
		middleMaterial = Material.SNOW_BLOCK;
		topMaterial = middleMaterial;
		liquidMaterial = Material.ICE;
		
		vScale = calcRandomRange(3.0, 7.0);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double doubleY = getGenerator().noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, frequency, amplitude) * vScale + bottomLevel;
				int y = NoiseGenerator.floor(doubleY);
				chunk.setBlocks(x, 1, y - middleThickness, z, bottomMaterial);
				chunk.setBlocks(x, y - middleThickness, y, z, middleMaterial);
				if (y < liquidLevel) {
					chunk.setBlocks(x, y, liquidLevel, z, liquidMaterial);
				} else
					chunk.setBlock(x, y, z, topMaterial);
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		
		// smooth out the snow and add a snowman... maybe
		boolean madeSnowMan = false;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double doubleY = getGenerator().noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, frequency, amplitude) * vScale + bottomLevel;
				int y = NoiseGenerator.floor(doubleY);
				Block block = chunk.getBlock(x, y, z);
				if (block.getTypeId() == intSnowBase) {
					byte amount = (byte) NoiseGenerator.floor((doubleY - y) * 8);
					block.setTypeIdAndData(intSnow, amount, false);
					if (!madeSnowMan && amount == 0 && random.nextInt(100) < oddsSnowMan) {
						chunk.getBlock(x, y    , z).setTypeId(intSnowBase, false);
						chunk.getBlock(x, y + 1, z).setTypeId(intSnowBase, false);
						chunk.getBlock(x, y + 2, z).setTypeId(intPumpkin, true);
						madeSnowMan = true;
					}
				}
			}
		}
		
		//TODO place one tree
		
		//populateSpecials(chunk, chunkX, chunkZ);
	}
}
