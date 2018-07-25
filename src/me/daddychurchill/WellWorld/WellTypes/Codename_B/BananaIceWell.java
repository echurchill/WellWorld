package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import me.daddychurchill.WellWorld.Support.InitialBlocks;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class BananaIceWell extends BananaWellArchetype {

	private static final int oddsSnowmen = 30; // n out of 100
	
	private SimplexOctaveGenerator noisegen;
	private SimplexOctaveGenerator layergen;
	private SimplexOctaveGenerator cliffgen;
	
	public BananaIceWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		noisegen = new SimplexOctaveGenerator(seed, 4);
		layergen = new SimplexOctaveGenerator(seed+1, 5);
		cliffgen = new SimplexOctaveGenerator(seed+1, 6);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {	
		// pretty much a direct port of codename_B's PM on DevBukkit
		// http://dev.bukkit.org/home/private-messages/29092-well-world/#m10
		
		chunk.setBlocksAt(1, 60, Material.WATER);
		chunk.setBlocksAt(60, 64, Material.ICE);
		
		// Layers?
		for(int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++) {
				double cake = layergen.noise((x+chunkX*16)/100.0f, (z+chunkZ*16)/100.0f, 0.6, 0.4)*16;
				if(cake>=0)
					for(int i = 0; (i < cake && i < 32); i++) {
						int y = 63 - i;
						chunk.setBlock(x, y, z, Material.SNOW_BLOCK);
					}
			}
		
		// Peaks?
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double noiz = 58+noisegen.noise((x+chunkX*16)/100.0f, (z+chunkZ*16)/100.0f, 0.5, 0.5)*16;
                for (int y = 0; y < noiz; y++) {
                    if(y >= noiz-1 && y>=64)
                    	chunk.setBlock(x, y, z, Material.SNOW);
                    else if (y >= noiz-2 && y>=64)
                    	chunk.setBlock(x, y, z, Material.GRASS);
                    else if(y >= noiz-4 && y>=64)
                    	chunk.setBlock(x, y, z, Material.DIRT);
                    else if(y >= noiz-5 && y>=63)
                    	chunk.setBlock(x, y, z, Material.SNOW_BLOCK);
                    else
                    	chunk.setBlock(x, y, z, Material.STONE);
                }
            }
        }
        
		// Bowls?
		for(int x =0; x < 16; x++)
			for(int z=0; z < 16; z++) {
				int multiple = 32;
				double shimmy = cliffgen.noise((x+chunkX*16)/100.0f, (z+chunkZ*16)/100.0f, 0.6, 0.4)*multiple;
				
				if(shimmy > multiple/2+4) {
					for(int i=32; i<64+(multiple/2.0-shimmy/2.0); i++) {
                    	chunk.setBlock(x, i, z, Material.SNOW_BLOCK);
					}
 				}
			}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		if (random.nextInt(100) < oddsSnowmen) {
			int centerX = (chunk.getX() << 4) + 8;
			int centerZ = (chunk.getZ() << 4) + 8;
			int centerY = world.getHighestBlockYAt(centerX, centerZ);
			setBlock(centerX, centerY + 1, centerZ, Material.SNOW_BLOCK);
			setBlock(centerX, centerY + 2, centerZ, Material.SNOW_BLOCK);
			setBlock(centerX, centerY + 3, centerZ, Material.PUMPKIN, true);
		} //else 
		//TODO: add a way to decorate the trees with snow
		//	populateFoliage(chunk);
	}
}
