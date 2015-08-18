package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import me.daddychurchill.WellWorld.Support.InitialBlocks;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.noise.*;

public class BananaForestWell extends BananaWellArchetype {

	private OctaveGenerator l1;
	private OctaveGenerator l2;

	public BananaForestWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		l1 = new SimplexOctaveGenerator(random, 2);
		l1.setScale(1 / 64.0);
		l2 = new SimplexOctaveGenerator(random, 2);
		l2.setScale(1 / 640.0);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		// pretty much a direct port of codename_B's PM on DevBukkit
		// http://dev.bukkit.org/home/private-messages/29092-well-world/#m10

		// The main "lump"
		double y;
		double sh;
		int highest;

		for (int x = 0; x < 16; x++) {
			int noiseX = getNoiseValue(chunkX, x);
			for (int z = 0; z < 16; z++) {
				int noiseZ = getNoiseValue(chunkZ, z);
					
				y = l1.noise(noiseX, noiseZ, 7, 1);
				double my = l1.noise(noiseX / 2.0, noiseZ / 2.0, 7, 1);
				sh = l2.noise(noiseX, noiseZ, 7, 1);
				double ry = 1 - my;
				int ht = (int) (ry * 4 + 64);

				chunk.setBlocks(x, 1, ht - 10, z, materialStone);
				chunk.setBlocks(x, ht - 10, ht, z, materialStone);
				chunk.setBlock(x, ht, z, materialGrass);
//				for (int i = 1; i < ht; i++) {
//					if (i < ht - 10)
//						data[GenUtil.xyzToByte(x, i, z)] = (byte) Material.STONE.getId();
//					else
//						data[GenUtil.xyzToByte(x, i, z)] = (byte) Material.DIRT.getId();
//				}
//				data[GenUtil.xyzToByte(x, ht, z)] = (byte) Material.GRASS.getId();

				int dx = (int) (y * 100) / 20;
				int dsh = (int) (sh * 100) / 10;
				highest = (int) (dx + dsh * 2 + 80);

				if (y > 0.6) {
					chunk.setBlocks(x, ht, highest, z, materialLog);
					chunk.setBlocks(x, highest, highest + 5, z, materialLeaves);
//						for (int i = ht; i < highest; i++)
//  						data[GenUtil.xyzToByte(x, i, z)] = (byte) Material.LOG.getId();
//						for (int i = highest; i < highest + 5; i++)
//  						data[GenUtil.xyzToByte(x, i, z)] = (byte) Material.LEAVES.getId();

				} else if (y >= 0) {
					double u = l1.noise(noiseX + 1, noiseZ, 7, 1);
					double d = l1.noise(noiseX - 1, noiseZ, 7, 1);
					double l = l1.noise(noiseX, noiseZ + 1, 7, 1);
					double r = l1.noise(noiseX, noiseZ - 1, 7, 1);

					double ul = l1.noise(noiseX + 1, noiseZ + 1, 7, 1);
					double ur = l1.noise(noiseX + 1, noiseZ - 1, 7, 1);
					double dl = l1.noise(noiseX - 1, noiseZ + 1, 7, 1);
					double dr = l1.noise(noiseX - 1, noiseZ - 1, 7, 1);

					double uu = l1.noise(noiseX + 2, noiseZ, 7, 1);
					double dd = l1.noise(noiseX - 2, noiseZ, 7, 1);
					double ll = l1.noise(noiseX, noiseZ + 2, 7, 1);
					double rr = l1.noise(noiseX, noiseZ - 2, 7, 1);
//					double u = l1.noise(getBlockLocation(chunkX, x + 1),
//							getBlockLocation(chunkZ, z), 7, 1);
//					double d = l1.noise(getBlockLocation(chunkX, x - 1),
//							getBlockLocation(chunkZ, z), 7, 1);
//					double l = l1.noise(getBlockLocation(chunkX, x),
//							getBlockLocation(chunkZ, z + 1), 7, 1);
//					double r = l1.noise(getBlockLocation(chunkX, x),
//							getBlockLocation(chunkZ, z - 1), 7, 1);
//
//					double ul = l1.noise(getBlockLocation(chunkX, x + 1),
//							getBlockLocation(chunkZ, z + 1), 7, 1);
//					double ur = l1.noise(getBlockLocation(chunkX, x + 1),
//							getBlockLocation(chunkZ, z - 1), 7, 1);
//					double dl = l1.noise(getBlockLocation(chunkX, x - 1),
//							getBlockLocation(chunkZ, z + 1), 7, 1);
//					double dr = l1.noise(getBlockLocation(chunkX, x - 1),
//							getBlockLocation(chunkZ, z - 1), 7, 1);
//
//					double uu = l1.noise(getBlockLocation(chunkX, x + 2),
//							getBlockLocation(chunkZ, z), 7, 1);
//					double dd = l1.noise(getBlockLocation(chunkX, x - 2),
//							getBlockLocation(chunkZ, z), 7, 1);
//					double ll = l1.noise(getBlockLocation(chunkX, x),
//							getBlockLocation(chunkZ, z + 2), 7, 1);
//					double rr = l1.noise(getBlockLocation(chunkX, x),
//							getBlockLocation(chunkZ, z - 2), 7, 1);

					if (u > 0.6 || d > 0.6 || l > 0.6 || r > 0.6 || ul > 0.6
							|| ur > 0.6 || dl > 0.6 || dr > 0.6 || uu > 0.6
							|| dd > 0.6 || ll > 0.6 || rr > 0.6) {
						for (int i = 80 - Math.abs(80 - highest) / 2; i < highest; i++)
							chunk.setBlock(x, i, z, materialLeaves);
//							data[GenUtil.xyzToByte(x, i, z)] = (byte) Material.LEAVES.getId();

						for (int i = highest; i < highest + 5; i++)
							chunk.setBlock(x, i, z, materialLeaves);
//							data[GenUtil.xyzToByte(x, i, z)] = (byte) Material.LEAVES.getId();

					}
				}
				// data[GenUtil.xyzToByte(x, 0, z)] = (byte) Material.BEDROCK.getId();
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// trees are already here, so just do ores
		populateOres(chunk);
	}

	@Override
	protected void calculateMaterials() {
		// no randomize please
	}

}
