package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import me.daddychurchill.WellWorld.Support.ByteChunk;

public class BananaSkyWell extends BananaWellArchetype {

	OctaveGenerator l1;
	OctaveGenerator l2;
	int yOffset;
	
	private byte byteCobbleStone = (byte) Material.COBBLESTONE.getId();
	private byte byteSmoothBrick = (byte) Material.SMOOTH_BRICK.getId();
	private byte byteSandStone = (byte) Material.SANDSTONE.getId();
	private byte byteSand = (byte) Material.SAND.getId();

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
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
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
							chunk.setBlock(x, i + yOffset, z, byteGrass);
						else if(i>highest-3)
							chunk.setBlock(x, i + yOffset, z, byteDirt);
						else if(i<30)
							chunk.setBlock(x, i + yOffset, z, byteStone);
						else if(random.nextInt(10) > 6)
							chunk.setBlock(x, i + yOffset, z, byteCobbleStone);
						else
							chunk.setBlock(x, i + yOffset, z, byteStone);


					// This generates the "mushroom" heads
				} else if(y >= 0.1) {
					highest = y*10+32;
					for(int i=random.nextInt(3)+28; i<highest; i++)
						if(i<highest-1)
							chunk.setBlock(x, i + yOffset, z, byteDirt);
						else
							chunk.setBlock(x, i + yOffset, z, byteGrass);

				}

				// This generates the "dream paths"
				// These paths allow you to travel between the islands, sort of...
				if(py>-0.3 && py<0) {
					highest = 18+Math.abs(py*5);
					for(int i=18; i<highest; i++)
						if(i == 18)
							chunk.setBlock(x, i + yOffset, z, byteSmoothBrick);
						else if(random.nextInt(10) > 7)
							chunk.setBlock(x, i + yOffset, z, byteSandStone);
						else
							chunk.setBlock(x, i + yOffset, z, byteSand);

					// Are we going to do anything here?
				}
			}
		}
	}
}
