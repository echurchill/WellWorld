package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class BasaltFieldWell extends WellArchetype {

	private int octives = 3;
	private double xFactor = 1.0;
	private double zFactor = 1.0;
	private double fequency = 0.5;
	private double amplitude = 0.5;
	private double hScale = 1.0 / 64.0;
	private double vScale = 16.0;
	private int solidLevel;
	private int liquidLevel;
	private Material solid = Material.OBSIDIAN;
	private Material liquid = Material.LAVA;
	private Material backfill;
	
	private SimplexOctaveGenerator generator;
	
	public BasaltFieldWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generator = new SimplexOctaveGenerator(randseed, octives);
		generator.setScale(hScale);
		
		solidLevel = random.nextInt(64) + 32;
		liquidLevel = solidLevel - random.nextInt(32);
		vScale = calcRandomRange(20.0, 30.0);
		if (random.nextBoolean()) {
			liquid = Material.LAVA;
			backfill = random.nextBoolean() ? liquid : Material.GLASS;
		} else {
			liquid = Material.WATER;
			backfill = random.nextBoolean() ? liquid : Material.ICE;
		}
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x += 4) {
			for (int z = 0; z < 16; z += 4) {
				double noise = generator.noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, fequency, amplitude) * vScale;
				int y1 = 1;
				int y2 = solidLevel + (int)noise;
				chunk.setBlocks(x, x + 3, y1, y2, z, z + 3, solid);
				if (y2 < liquidLevel)
					chunk.setBlocks(x, x + 3, y2, liquidLevel, z, z + 3, backfill);
				chunk.setBlocks(x + 3, x + 4, y1, liquidLevel, z, z + 4, liquid);
				chunk.setBlocks(x, x + 3, y1, liquidLevel, z + 3, z + 4, liquid);
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
