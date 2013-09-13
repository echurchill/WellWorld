package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import me.daddychurchill.WellWorld.WellWorldChunkGenerator;
import me.daddychurchill.WellWorld.Support.ByteChunk;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class BananaVoidWell extends BananaWellArchetype {

	private byte byteObsidian = (byte) Material.OBSIDIAN.getId();
	private SimplexOctaveGenerator noisegen;
	private int maxHeight;

	public BananaVoidWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

//		SimplexOctaveGenerator noisegen = new SimplexOctaveGenerator(world.getSeed(), 4);
		noisegen = new SimplexOctaveGenerator(seed, 4);
		maxHeight = calcRandomRange(64, WellWorldChunkGenerator.lowestDoors);
	}
	
	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		// pretty much a direct port of codename_B's PM on DevBukkit
		// http://dev.bukkit.org/home/private-messages/29092-well-world/#m10
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
//				double cake = noisegen.noise((x + chunkX * 16) / 100.0f, (z + chunkZ * 16) / 100.0f, 0.6, 0.4) * 127;
				double cake = noisegen.noise((x + chunkX * 16) / (double) maxHeight * 2.0, (z + chunkZ * 16) / (double) maxHeight * 2.0, 0.6, 0.4) * 255.0;//127.0;
				int y = maxHeight - (int) cake;
				if (y >= 0 && y < maxHeight)
					chunk.setBlocks(x, y, maxHeight, z, byteObsidian);
//				if (cake < 40) {
//					for (int y = 100; y > 100 - cake; y--) {
//					set(Material.OBSIDIAN, x, y, z);
//				}
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// nothing for this one
	}

	@Override
	public boolean includeBottom() {
		return false;
	}
}
