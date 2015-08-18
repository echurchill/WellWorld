package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class MineralPlatWell extends WellArchetype {

	private int chunkHeight = 127;
	private int streetLevel;
	private double xMineralFactor = 8.0;
	private double yMineralFactor = 5.0;
	private double zMineralFactor = 8.0;
	private double threshholdMineral = 0.90;
	private SimplexNoiseGenerator noiseMineral;
	
	//private byte byteMineral = Material.STONE;
	
	public MineralPlatWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		noiseMineral = new SimplexNoiseGenerator(seed);
		
		streetLevel = 64;
		
		int mineralStratas = streetLevel / 5;
		yCoal = (chunkHeight - streetLevel / 2) + streetLevel;
		yIron = mineralStratas * 5;
		yGold = mineralStratas * 4;
		yLapis = mineralStratas * 3;
		yRedstone = mineralStratas * 2;
		yDiamond = mineralStratas * 1;
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int blockX = chunkX * 16 + x;
				int blockZ = chunkZ * 16 + z;
				for (int blockY = 0; blockY <= streetLevel; blockY++) {
					double mineralLevel = noiseMineral.noise(blockX / xMineralFactor, blockY / yMineralFactor, blockZ / zMineralFactor);
					if (mineralLevel > threshholdMineral)
						chunk.setBlock(x, blockY, z, RandomMineralAt(blockY));
				}
			}
		}
	}
	
	private int yCoal;
	private int yIron;
	private int yGold;
	private int yLapis;
	private int yRedstone;
	private int yDiamond;
	
	protected Material RandomMineralAt(int y) {
		if (y < yDiamond)
			return Material.DIAMOND_ORE;
		else if (y < yRedstone)
			return Material.REDSTONE_ORE;
		else if (y < yLapis)
			return Material.LAPIS_ORE;
		else if (y < yGold)
			return Material.GOLD_ORE;
		else if (y < yIron)
			return Material.IRON_ORE;
		else if (y < yCoal)
			return Material.COAL_ORE;
		else
			return Material.STONE;
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
