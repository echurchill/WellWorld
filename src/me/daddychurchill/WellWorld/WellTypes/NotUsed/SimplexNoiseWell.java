package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class SimplexNoiseWell extends WellArchetype {

	private float xFactor = 50.0f;
	private float yFactor = 25.0f;
	private float zFactor = 50.0f;
	private SimplexNoiseGenerator generator;

	public SimplexNoiseWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		generator = new SimplexNoiseGenerator(randseed);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 128; y++) {
					double noise = generator.noise((chunkX * 16 + x) / xFactor, y / yFactor,
							(chunkZ * 16 + z) / zFactor);
					noise += (64 - y) * (1 / 32f);
					chunk.setBlock(x, y, z, noise > 0 ? Material.STONE : Material.AIR);
				}
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
