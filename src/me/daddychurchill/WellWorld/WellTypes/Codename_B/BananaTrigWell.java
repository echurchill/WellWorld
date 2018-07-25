package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class BananaTrigWell extends BananaWellArchetype {

	private int surfaceAt;
	
	public BananaTrigWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		surfaceAt = random.nextInt(48) + 16;
		
	}
	
	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		//EC: use the one passed in instead
		//int chunkX = chunk.getX();
		//int chunkZ = chunk.getZ();
		
		for(int x=0; x<16; x++) {
			for(int z=0; z<16; z++) {
				int trueX = chunkX*16+x;
				int trueZ = chunkZ*16+z;

				chunk.setBlocks(x, 1, surfaceAt, z, materialLiquid);
//				chunk.setBlocks(x, 1, surfaceAt, z, 64);
//				for(int y=0; y<64; y++)
//					result[xyzToByte(x,y,z)] = (byte) Material.WATER.getId();

				double h = BananaTrigFunction.get(trueX, trueZ)*50+surfaceAt;
//				double h = BananaTrigFunction.get(trueX, trueZ)*77+50;

				boolean river = false;
				if(BananaTrigFunction.holes(trueX, trueZ)) {
					river = true;
					h = h-6;
					chunk.setBlocks(x, (int)h, (int)h+4, z, materialLiquid);
//					for(int y=(int) h; y<h+4; y++)
//						result[xyzToByte(x,y,z)] = (byte) Material.WATER.getId();
				}
				
				if (!river) {
					chunk.setBlocks(x, (int)h-1, (int)h, z, materialGrass);
					chunk.setBlocks(x, (int)h-5, (int)h-1, z, materialDirt);
				} else 
					chunk.setBlocks(x, (int)h-5, (int)h, z, materialDirt);
				chunk.setBlocks(x, 1, (int)h-5, z, materialStone);
				
//				for(int y=0; y<h; y++) {
//					byte mat = byteAir;
//					if(y>h-2 && !river)
//						mat = byteGrass;
//					else if(y>h-5)
//						mat = byteDirt;
//					else
//						mat = byteStone;
//					result[xyzToByte(x,y,z)] = mat;   
//				}
				
				// CAVES
				int highest = -1;
				for(int i=125; i>0; i--) {
					if(highest == -1)
						if(chunk.getBlockType(x, i, z) != Material.AIR)
//						if(result[xyzToByte(x,i,z)] > 0)
							highest=i;
				}
				if(highest>40) {
					double height = (Math.sin(BananaTrigFunction.normalise(trueX+trueZ))+Math.sin(BananaTrigFunction.normalise(trueX))+Math.sin(BananaTrigFunction.normalise(trueZ)))*5+5;
					if(height>5) {
						chunk.setBlocks(x, (int)(highest/2.0), (int)(highest/2.0+height), z, Material.AIR);
//						for(int y=highest/2; y<highest/2+height; y++) {
//							result[xyzToByte(x,y,z)] = 0;
//						}
					}
				}
				//This will set the floor of each chunk at bedrock level to bedrock
				//result[xyzToByte(x,0,z)] = (byte) Material.BEDROCK.getId();
			}
		}
	}
}
