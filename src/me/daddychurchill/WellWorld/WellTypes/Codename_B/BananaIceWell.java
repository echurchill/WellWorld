package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import me.daddychurchill.WellWorld.Support.ByteChunk;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class BananaIceWell extends BananaWellArchetype {

	private byte byteWater = (byte) Material.STATIONARY_WATER.getId();
	private byte byteIce = (byte) Material.ICE.getId();
	private byte byteSnowBlock = (byte) Material.SNOW_BLOCK.getId();
	private byte byteSnow = (byte) Material.SNOW.getId();
	private byte byteGrass = (byte) Material.GRASS.getId();
	private byte byteDirt = (byte) Material.DIRT.getId();
	private byte byteStone = (byte) Material.STONE.getId();
	
	private int intSnowBlock = byteSnowBlock;
	private int intPumpkin = Material.PUMPKIN.getId();
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
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {	
		// pretty much a direct port of codename_B's PM on DevBukkit
		// http://dev.bukkit.org/home/private-messages/29092-well-world/#m10
		
//		for(int i=1; i<60; i++)
//			setLayer(getByte(Material.STATIONARY_WATER), i);
//		for(int i=60; i<64; i++)
//			setLayer(getByte(Material.ICE),i);
		chunk.setBlocksAt(1, 60, byteWater);
		chunk.setBlocksAt(60, 64, byteIce);
		
		// Layers?
		for(int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++) {
				double cake = layergen.noise((x+chunkX*16)/100.0f, (z+chunkZ*16)/100.0f, 0.6, 0.4)*16;
				if(cake>=0)
					for(int i = 0; (i < cake && i < 32); i++) {
						int y = 63 - i;
//						set(Material.SNOW_BLOCK, x, y, z);
						chunk.setBlock(x, y, z, byteSnowBlock);
					}
			}
		
		// Peaks?
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double noiz = 58+noisegen.noise((x+chunkX*16)/100.0f, (z+chunkZ*16)/100.0f, 0.5, 0.5)*16;
                for (int y = 0; y < noiz; y++) {
                    if(y >= noiz-1 && y>=64)
//                    	set(Material.SNOW, x, y, z);
                    	chunk.setBlock(x, y, z, byteSnow);
                    else if (y >= noiz-2 && y>=64)
//                    	set(Material.GRASS, x, y, z);
                    	chunk.setBlock(x, y, z, byteGrass);
                    else if(y >= noiz-4 && y>=64)
//                    	set(Material.DIRT, x, y, z);
                    	chunk.setBlock(x, y, z, byteDirt);
                    else if(y >= noiz-5 && y>=63)
//                    	set(Material.SNOW_BLOCK, x, y, z);
                    	chunk.setBlock(x, y, z, byteSnowBlock);
                    else
//                        set(Material.STONE, x, y, z);
                    	chunk.setBlock(x, y, z, byteStone);
                }
            }
        }
        
		// Bowls?
		for(int x =0; x < 16; x++)
			for(int z=0; z < 16; z++) {
				int multiple = 32;
				double shimmy = cliffgen.noise((x+chunkX*16)/100.0f, (z+chunkZ*16)/100.0f, 0.6, 0.4)*multiple;
				
				if(shimmy > multiple/2+4) {
					for(int i=32; i<64+(multiple/2-shimmy/2); i++) {
//						set(Material.SNOW_BLOCK, x, i, z);
                    	chunk.setBlock(x, i, z, byteSnowBlock);
					}

//EC: Replace the fake snowmen with real ones :-)
//					int y = (int) (64+(multiple/2-shimmy/2));
//					if(random.nextInt(64)==16 && x>1 && x<15 && z>1 && z<15) {
//	//					set(Material.SNOW_BLOCK, x, y-1, z);
//	//					set(Material.SNOW_BLOCK, x, y, z);
//	//					set(Material.SNOW_BLOCK, x, y+1, z);
//	//					set(Material.SNOW_BLOCK, x, y+2, z);
//	//					set(Material.LEVER, x+1, y+2, z);
//	//					set(Material.LEVER, x-1, y+2, z);
//	//					set(Material.PUMPKIN, x, y+3, z);
//	//					set(Material.SNOW, x, y+4, z);
//					}
 				}
			}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		if (random.nextInt(100) < oddsSnowmen) {
			int centerX = (chunk.getX() << 4) + 8;
			int centerZ = (chunk.getZ() << 4) + 8;
			int centerY = world.getHighestBlockYAt(centerX, centerZ);
			setBlock(centerX, centerY + 1, centerZ, intSnowBlock);
			setBlock(centerX, centerY + 2, centerZ, intSnowBlock);
			setBlock(centerX, centerY + 3, centerZ, intPumpkin, true);
		} //else 
		//TODO: add a way to decorate the trees with snow
		//	populateFoliage(chunk);
	}
}
