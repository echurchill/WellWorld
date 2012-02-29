package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class MineralPlatWell extends WellArchetype {

	private int chunkHeight = 127;
	private int streetLevel;
	private double xMineralFactor = 8.0;
	private double yMineralFactor = 5.0;
	private double zMineralFactor = 8.0;
	private double threshholdMineral = 0.90;
	private SimplexNoiseGenerator noiseMineral;
	
	//private byte byteMineral = (byte) Material.STONE.getId();
	
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
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int blockX = chunkX * 16 + x;
				int blockZ = chunkZ * 16 + z;
				for (int blockY = 0; blockY <= streetLevel; blockY++) {
					double mineralLevel = noiseMineral.noise(blockX / xMineralFactor, blockY / yMineralFactor, blockZ / zMineralFactor);
					if (mineralLevel > threshholdMineral)
						chunk.setBlock(x, blockY, z, byteRandomMineralAt(blockY));
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
	
	private byte byteCoal = (byte) Material.COAL_ORE.getId();
	private byte byteIron = (byte) Material.IRON_ORE.getId();
	private byte byteGold = (byte) Material.GOLD_ORE.getId();
	private byte byteLapis = (byte) Material.LAPIS_ORE.getId();
	private byte byteRedstone = (byte) Material.REDSTONE_ORE.getId();
	private byte byteDiamond = (byte) Material.DIAMOND_ORE.getId();
	private byte byteStone = (byte) Material.STONE.getId();
	
	protected byte byteRandomMineralAt(int y) {
		if (y < yDiamond)
			return byteDiamond;
		else if (y < yRedstone)
			return byteRedstone;
		else if (y < yLapis)
			return byteLapis;
		else if (y < yGold)
			return byteGold;
		else if (y < yIron)
			return byteIron;
		else if (y < yCoal)
			return byteCoal;
		else
			return byteStone;
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
