package me.daddychurchill.WellWorld;

import java.util.Random;

import me.daddychurchill.WellWorld.Support.InitialBlocks;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public abstract class WellArchetype {
	
	public Random random;
	protected World world;
	protected long randseed;
	protected Vector minBlock;
	protected Vector maxBlock;
	protected int wellX;
	protected int wellZ;
	
	public WellArchetype(World world, long seed, int wellX, int wellZ) {
		
		// make our own random
		this.randseed = seed;
		this.random = new Random(randseed);
		
		// where are we?
		this.world = world;
		this.wellX = wellX;
		this.wellZ = wellZ;
		
		// calculate the well's block bounds
		int x1 = wellX * InitialBlocks.chunksBlockWidth;
		int x2 = (wellX + WellWorld.wellWidthInChunks) * InitialBlocks.chunksBlockWidth; 
		int z1 = wellZ * InitialBlocks.chunksBlockWidth;
		int z2 = (wellZ + WellWorld.wellWidthInChunks) * InitialBlocks.chunksBlockWidth; 
		this.minBlock = new Vector(x1 + WellWorldChunkGenerator.wallThicknessInBlocks, 1, z1 + WellWorldChunkGenerator.wallThicknessInBlocks);
		this.maxBlock = new Vector(x2 - WellWorldChunkGenerator.wallThicknessInBlocks, 127/*world.getMaxHeight() - 1*/, z2 - WellWorldChunkGenerator.wallThicknessInBlocks);
	}
	
	// override these to make something really happen!
	public abstract void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ);
	public abstract void populateBlocks(Chunk chunk, int chunkX, int chunkZ);	
	
	// gives you the well's origin chunk position
	public int getX() {
		return wellX;
	}
	
	public int getZ() {
		return wellZ;
	}
	
	public Biome getBiome() {
		return world.getBiome(wellX * InitialBlocks.chunksBlockWidth + 8, wellZ * InitialBlocks.chunksBlockWidth + 8);
	}
	
	protected int getBlockX(InitialBlocks chunk, int x) {
		return (chunk.chunkX - wellX) * InitialBlocks.chunksBlockWidth + x;
	}
	
	protected int getBlockZ(InitialBlocks chunk, int z) {
		return (chunk.chunkZ - wellZ) * InitialBlocks.chunksBlockWidth + z;
	}
	
	protected int getNoiseValue(int chunkAt, int at) {
		return chunkAt * InitialBlocks.chunksBlockWidth + at;
	}
	
	// override these to auto-draw the top and bottom of the well
	public boolean includeTop() {
		return false;
	}
	
	public boolean includeBottom() {
		return true;
	}
	
	protected Material pickRandomMineralAt(int y) {
		// a VERY VERY rough version of http://www.minecraftwiki.net/wiki/Ore
		if (y <= 16)
			return pickRandomMineral(6);
		else if (y <= 34)
			return pickRandomMineral(4);
		else if (y <= 69)
			return pickRandomMineral(2);
		else
			return pickRandomMineral(1);
	}
	
	protected Material pickRandomMineral(int max) {
		switch (random.nextInt(max)) {
		case 1:
			return Material.IRON_ORE;
		case 2:
			return Material.GOLD_ORE;
		case 3:
			return Material.LAPIS_ORE;
		case 4:
			return Material.REDSTONE_ORE;
		case 5:
			return Material.DIAMOND_ORE;
		default:
			return Material.COAL_ORE;
		}
	}
	
	protected int nudgeToBounds(int at, int margin, int min, int max) {
        return Math.min(Math.max(at - margin, min) + margin + margin, max) - margin;
	}
	
	protected int nudgeToBounds(int at, int min, int max) {
        return Math.min(Math.max(at, min), max);
	}
	
	protected boolean inBounds(int x, int z) {
		return inXBounds(x) && inZBounds(z);
	}
	
	protected boolean inXBounds(int x) {
		return x >= minBlock.getBlockX() && x < maxBlock.getBlockX();
	}
	
	protected boolean inZBounds(int z) {
		return z >= minBlock.getBlockZ() && z < maxBlock.getBlockZ();
	}
	
	protected boolean calcOdds(int percentage) {
		return random.nextInt(100) < percentage;
	}
	
	protected int calcRandomRange(int min, int max) {
		return min + random.nextInt(max - min);
	}
	
	protected double calcRandomRange(double min, double max) {
		return min + random.nextDouble() * (max - min);
	}
	
	protected void drawSolidSphere(World world, Chunk chunk, int centerX, int centerY, int centerZ, int radius, Material material) {
        Vector center = new Vector(centerX, centerY, centerZ);
        
        for (int x = 0; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                for (int z = 0; z <= radius; z++) {
                	Vector ray = new Vector(centerX + x, centerY + y, centerZ + z);
                	if (center.distance(ray) <= radius + 0.5) {
                		world.getBlockAt(centerX + x, centerY + y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX + x, centerY + y, centerZ - z).setType(material, false);
               			world.getBlockAt(centerX + x, centerY - y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX + x, centerY - y, centerZ - z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ - z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ - z).setType(material, false);
                	}
                }
            }
        }
	}

	protected void drawSolidSphere(World world, Chunk chunk, int centerX, int centerY, int centerZ, int radius, Material fillMaterial, int floodY, Material floodMaterial) {
        Vector center = new Vector(centerX, centerY, centerZ);
        Material material;
        
        for (int x = 0; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                for (int z = 0; z <= radius; z++) {
                	Vector ray = new Vector(centerX + x, centerY + y, centerZ + z);
                	if (center.distance(ray) <= radius + 0.5) {
            			material = fillMaterial;
                		
                		// upper portion
                		world.getBlockAt(centerX + x, centerY + y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX + x, centerY + y, centerZ - z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ - z).setType(material, false);
               			
               			// lower portion
                		if (centerY - y <= floodY)
                			material = floodMaterial;
               			world.getBlockAt(centerX + x, centerY - y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX + x, centerY - y, centerZ - z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ + z).setType(material, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ - z).setType(material, false);
                	}
                }
            }
        }
	}
	
//	protected void sprinkleGoodness(Chunk chunk, 
//			int specialBlockOdds, int specialsPerLayer, 
//			Material transmutable, Material plantable,
//			int flowerOdds, Material flowerMaterial, Material bladesMaterial) {
//		
//		// sprinkle minerals for each y layer, one of millions of ways to do this!
//		for (int y = 1; y < 127; y++) {
//			if (random.nextInt(specialBlockOdds) == 0) {
//				for (int i = 0; i < specialsPerLayer; i++) {
//					Block block = chunk.getBlock(random.nextInt(16), y, random.nextInt(16));
//					Material blockMaterial = block.getType();
//					
//					// Transmutation?
//					if (blockMaterial == transmutable)
//						block.setType(pickRandomMineralAt(y), false);
//					
//					// If the block supports foliage, then what type will go here?
//					else if (blockMaterial == plantable) {
//						Location foliageAt = block.getLocation().add(0, 1, 0);
//						if (random.nextInt(flowerOdds) == 0)
//							world.getBlockAt(foliageAt).setType(flowerMaterial);
//						else {
//							BlackMagic.setBlock(world.getBlockAt(foliageAt), bladesMaterial, (byte) 1);
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	protected void sprinkleGoodness(Chunk chunk, 
//			int specialBlockOdds, int specialsPerLayer, 
//			int flowerOdds, Material flowerMaterial, Material bladesMaterial) {
//		sprinkleGoodness(chunk, specialBlockOdds, specialsPerLayer, Material.STONE, Material.GRASS, flowerOdds, flowerMaterial, bladesMaterial);
//	}
//	
//	protected void sprinkleGoodness(Chunk chunk, 
//			int specialBlockOdds, int specialsPerLayer) {
//		sprinkleGoodness(chunk, specialBlockOdds, specialsPerLayer, Material.STONE, Material.CAKE, 1000, Material.AIR, Material.AIR);
//	}
	
	protected void sprinkleTrees(Chunk chunk, int treesPerChunk, TreeType treeType) {

		// plant lots of trees
		int worldX = chunk.getX() * 16; // these use the chunk's world relative location instead..
		int worldZ = chunk.getZ() * 16; // ..of well's as generateTree needs world coordinates
		for (int t = 0; t < treesPerChunk; t++) {
			int centerX = worldX + random.nextInt(16);
			int centerZ = worldZ + random.nextInt(16);
			int centerY = world.getHighestBlockYAt(centerX, centerZ);
			Block rootBlock = world.getBlockAt(centerX, centerY - 1, centerZ);
			if (rootBlock.getType() == Material.GRASS) {
				Location treeAt = rootBlock.getLocation().add(0, 1, 0);
				world.generateTree(treeAt, treeType);
			}
		}
	}
	
//	protected void setBlock(int x, int y, int z, Material material, byte data) {
//		Block block = world.getBlockAt(x, y, z);
//		if (block.getType() == Material.AIR)
//			BlackMagic.setBlock(block, material, data);
//	}
	
	protected BlockData setBlock(int x, int y, int z, Material material) {
		return setBlock(x, y, z, material, false);
	}
	
	protected BlockData setBlock(int x, int y, int z, Material material, boolean physics) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getType() == Material.AIR) {
			block.setType(material, physics);
			return block.getBlockData();
		} else
			return null;
	}
	
//	protected void setBlock(int x, int y, int z, int blockId, byte blockData, boolean blockPhysics) {
//		Block block = world.getBlockAt(x, y, z);
//		if (block.getTypeId() == intAir)
//			block.setTypeIdAndData(blockId, blockData, blockPhysics);
//	}
	
//	protected void setBlock(int x, int y, int z, Material material, boolean blockPhysics) {
//		Block block = world.getBlockAt(x, y, z);
//		if (block.getType() == Material.AIR)
//			block.setType(material, blockPhysics);
//	}
	
	protected void clearBlock(int x, int y, int z) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getType() != Material.AIR)
			block.setType(Material.AIR, false);
	}
}
