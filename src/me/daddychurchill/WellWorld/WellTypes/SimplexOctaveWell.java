package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class SimplexOctaveWell extends WellArchetype {

	private int octives = 3;
	private double xFactor = 1.0;
	private double zFactor = 1.0;
	private double fequency = 0.5;
	private double amplitude = 0.5;
	private double hScale = 1.0 / 64.0;
	private double vScale = 16.0;
	private int yLevel = 64;
	private byte stoneId = (byte) Material.STONE.getId();
	//private byte airId = (byte) Material.AIR.getId();
	
	private SimplexOctaveGenerator generator;
	
	public SimplexOctaveWell(long seed, int wellX, int wellZ) {
		super(seed, wellX, wellZ);
		generator = new SimplexOctaveGenerator(randseed, octives);
		generator.setScale(hScale);
	}

	@Override
	public void populateChunk(World world, ByteChunk chunk) {
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double noise = generator.noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, fequency, amplitude) * vScale;
				chunk.setBlocks(x, 1, yLevel + (int)noise, z, stoneId);
			}
		}
	}

	@Override
	public void populateBlocks(World world, Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
