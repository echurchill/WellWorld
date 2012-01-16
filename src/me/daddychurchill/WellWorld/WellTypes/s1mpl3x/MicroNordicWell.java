package me.daddychurchill.WellWorld.WellTypes.s1mpl3x;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class MicroNordicWell extends WellArchetype {

	// Port of s1mpl3x's 50 line WGEN for testing purposes
	// http://forums.bukkit.org/threads/codename_bs-list-of-plugins-under-50-lines-of-code-aka-the-under-50-challenge.33264/
	private byte byteStone = (byte) Material.STONE.getId();
	private byte byteGrass = (byte) Material.GRASS.getId();
	private int baseHeight;
	private int baseVariability;
	
    private SimplexOctaveGenerator noisegen;
    
	public MicroNordicWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
        noisegen = new SimplexOctaveGenerator(randseed, 4);
        baseHeight = calcRandomRange(20, 60); //EC: make the general height more random
        baseVariability = calcRandomRange(15, 30); //EC: make the variability more random
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
            	//double noiz = 25+noisegen.noise((x+x_chunk*16)/100.0f, (z+z_chunk*16)/100.0f, 0.5, 0.5)*15;
                double noiz = baseHeight+noisegen.noise((x+chunkX*16)/100.0f, (z+chunkZ*16)/100.0f, 0.5, 0.5)*baseVariability;
                for (int y = 0; y < noiz; y++) {
//                    if (y == 0)
//                        ret[(x * 16 + z) * 128 + y] = 7;
//                    else if (y >= noiz-1)
//                        ret[(x * 16 + z) * 128 + y] = 2;
//                    else
//                        ret[(x * 16 + z) * 128 + y] = 1;
                    if (y >= noiz-1)
                    	chunk.setBlock(x, y, z, byteGrass);
                    else
                    	chunk.setBlock(x, y, z, byteStone);
                }
            }
        }
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
//        int x = r.nextInt(16)+s.getX()*16;
//        int z = r.nextInt(16)+s.getZ()*16;
        int x = random.nextInt(16)+chunk.getX()*16;
        int z = random.nextInt(16)+chunk.getZ()*16;
        world.generateTree(new Location(world, x, world.getHighestBlockYAt(x, z), z), TreeType.BIG_TREE);
	}

}
