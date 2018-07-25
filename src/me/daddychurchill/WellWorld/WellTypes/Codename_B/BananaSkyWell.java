package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class BananaSkyWell extends BananaWellArchetype {

	protected OctaveGenerator l1;
	protected OctaveGenerator l2;
	protected int yOffset;
	
	public BananaSkyWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		l1 = new SimplexOctaveGenerator(new Random(randseed), 2);
		l1.setScale(1/138.0);
		l2 = new PerlinOctaveGenerator(new Random(randseed), 2);
		l2.setScale(1/512.0);
		
		yOffset = random.nextInt(50);
	}

	@Override
	public boolean includeBottom() {
		return false;
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		double y;
		double py;
		double highest;

		for(int x=0; x<16; x++) {
			for(int z=0; z<16; z++) {
				y = l1.noise(getNoiseValue(chunkX, x), getNoiseValue(chunkZ, z), 7, 1);
				py = l2.noise(getNoiseValue(chunkX, x), getNoiseValue(chunkZ, z), 7, 1);

				// This generates the "mushroom" stalk
				if(y > 0.5) {
					highest = y*18+26;
					for(int i=random.nextInt(3)+20; i<highest; i++)
						if(i>highest-1)
							chunk.setBlock(x, i + yOffset, z, materialGrass);
						else if(i>highest-3)
							chunk.setBlock(x, i + yOffset, z, materialDirt);
						else if(i<30)
							chunk.setBlock(x, i + yOffset, z, materialStone);
						else if(random.nextInt(10) > 6)
							chunk.setBlock(x, i + yOffset, z, Material.COBBLESTONE);
						else
							chunk.setBlock(x, i + yOffset, z, materialStone);


					// This generates the "mushroom" heads
				} else if(y >= 0.1) {
					highest = y*10+32;
					for(int i=random.nextInt(3)+28; i<highest; i++)
						if(i<highest-1)
							chunk.setBlock(x, i + yOffset, z, materialDirt);
						else
							chunk.setBlock(x, i + yOffset, z, materialGrass);

				}

				// This generates the "dream paths"
				// These paths allow you to travel between the islands, sort of...
				if(py>-0.3 && py<0) {
					highest = 18+Math.abs(py*5);
					for(int i=18; i<highest; i++)
						if(i == 18)
							chunk.setBlock(x, i + yOffset, z, Material.STONE_BRICKS);
						else if(random.nextInt(10) > 7)
							chunk.setBlock(x, i + yOffset, z, Material.SANDSTONE);
						else
							chunk.setBlock(x, i + yOffset, z, Material.SAND);

					// Are we going to do anything here?
				}
			}
		}
	}
}
