package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

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
	private byte solidId = (byte) Material.OBSIDIAN.getId();
	private byte liquidId = (byte) Material.STATIONARY_LAVA.getId();
	private byte backfillId;
	
	private SimplexOctaveGenerator generator;
	
	public BasaltFieldWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generator = new SimplexOctaveGenerator(randseed, octives);
		generator.setScale(hScale);
		
		solidLevel = random.nextInt(64) + 32;
		liquidLevel = solidLevel - random.nextInt(32);
		if (random.nextBoolean()) {
			liquidId = (byte) Material.STATIONARY_LAVA.getId();
			backfillId = random.nextBoolean() ? liquidId : (byte) Material.GLASS.getId();
		} else {
			liquidId = (byte) Material.STATIONARY_WATER.getId();
			backfillId = random.nextBoolean() ? liquidId : (byte) Material.ICE.getId();
		}
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x += 4) {
			for (int z = 0; z < 16; z += 4) {
				double noise = generator.noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, fequency, amplitude) * vScale;
				int y1 = 1;
				int y2 = solidLevel + (int)noise;
				chunk.setBlocks(x, x + 3, y1, y2, z, z + 3, solidId);
				if (y2 < liquidLevel)
					chunk.setBlocks(x, x + 3, y2, liquidLevel, z, z + 3, backfillId);
				chunk.setBlocks(x + 3, x + 4, y1, liquidLevel, z, z + 4, liquidId);
				chunk.setBlocks(x, x + 3, y1, liquidLevel, z + 3, z + 4, liquidId);
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
