package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class CodenameBWell extends WellArchetype {

	private byte stoneId = (byte) Material.STONE.getId();
	//private byte airId = (byte) Material.AIR.getId();
	
	public CodenameBWell(long seed, int wellX, int wellZ) {
		super(seed, wellX, wellZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void populateChunk(World world, ByteChunk chunk) {
		
		// pretty much a direct copy of codename_B's post for testing purposes
		// http://forums.bukkit.org/threads/intermediate-infinite-terrain-generation-using-simplexoctaves.28855/
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		
		// This generates an "octave generator" from the random - the same one each time
		SimplexOctaveGenerator gen =  new SimplexOctaveGenerator(randseed, 8);
		
		// This set's the "scale" ie how spread out it is - 64 is a good number for hilly terrain
		gen.setScale(1 / 64.0);
		
		// This loops through the chunk
		for (int x = 0; x < 16; x++){
			for (int z = 0; z < 16; z++){
				
				// This generates noise based on the absolute x and z (between -1 and 1) 
				// - it will be smooth noise if all goes well 
				// - multiply it by the desired value 
				// - 16 is good for hilly terrain.
				double noise = gen.noise(x + chunkX * 16, z + chunkZ * 16, 0.5, 0.5) * 16;
				
				//EC: optimized this based on how WellWorld chunk class works
				//for(int y = 0; y < 32 + noise; y++){
				//	
				//	// Just some checks I use 
				//	// - you don't need this.
				//	if(chunk.getBlock(x, y, z) == airId)
				//		
				//		// Obviously sets that byte[] corresponding to the chunk x,y,z to stone 
				//		// - you can use your preferred way of doing this.
				//		chunk.setBlock(x, y, z, stoneId);
				//}
				chunk.setBlocks(x, 1, (int) (32 + noise), z, stoneId);
			}
		}
	}

	@Override
	public void populateBlocks(World world, Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
