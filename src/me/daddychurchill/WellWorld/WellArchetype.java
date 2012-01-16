package me.daddychurchill.WellWorld;

import java.util.Random;

import me.daddychurchill.WellWorld.Support.ByteChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
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
		int x1 = wellX * 16;
		int x2 = (wellX + WellWorld.wellWidthInChunks) * 16; 
		int z1 = wellZ * 16;
		int z2 = (wellZ + WellWorld.wellWidthInChunks) * 16; 
		this.minBlock = new Vector(x1 + WellWorldChunkGenerator.wallThicknessInBlocks, 1, z1 + WellWorldChunkGenerator.wallThicknessInBlocks);
		this.maxBlock = new Vector(x2 - WellWorldChunkGenerator.wallThicknessInBlocks, 127, z2 - WellWorldChunkGenerator.wallThicknessInBlocks);
	}
	
	// override these to make something really happen!
	public abstract void generateChunk(ByteChunk chunk, int chunkX, int chunkZ);
	public abstract void populateBlocks(Chunk chunk, int chunkX, int chunkZ);	
	
	// gives you the well's origin chunk position
	public int getX() {
		return wellX;
	}
	
	public int getZ() {
		return wellZ;
	}
	
	public Biome getBiome() {
		return world.getBiome(wellX * ByteChunk.Width + 8, wellZ * ByteChunk.Width + 8);
	}
	
	protected int getBlockX(ByteChunk chunk, int x) {
		return (chunk.getX() - wellX) * ByteChunk.Width + x;
	}
	
	protected int getBlockZ(ByteChunk chunk, int z) {
		return (chunk.getZ() - wellZ) * ByteChunk.Width + z;
	}
	
	protected int getNoiseValue(int chunkAt, int at) {
		return chunkAt * ByteChunk.Width + at;
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
        int materialId = material.getId();
        
        for (int x = 0; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                for (int z = 0; z <= radius; z++) {
                	Vector ray = new Vector(centerX + x, centerY + y, centerZ + z);
                	if (center.distance(ray) <= radius + 0.5) {
                		world.getBlockAt(centerX + x, centerY + y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX + x, centerY + y, centerZ - z).setTypeId(materialId, false);
               			world.getBlockAt(centerX + x, centerY - y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX + x, centerY - y, centerZ - z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ - z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ - z).setTypeId(materialId, false);
                	}
                }
            }
        }
	}

	protected void drawSolidSphere(World world, Chunk chunk, int centerX, int centerY, int centerZ, int radius, Material fillMaterial, int floodY, Material floodMaterial) {
        Vector center = new Vector(centerX, centerY, centerZ);
        int materialId;
        int fillMaterialId = fillMaterial.getId();
        int floodMaterialId = floodMaterial.getId();
        
        for (int x = 0; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                for (int z = 0; z <= radius; z++) {
                	Vector ray = new Vector(centerX + x, centerY + y, centerZ + z);
                	if (center.distance(ray) <= radius + 0.5) {
            			materialId = fillMaterialId;
                		
                		// upper portion
                		world.getBlockAt(centerX + x, centerY + y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX + x, centerY + y, centerZ - z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY + y, centerZ - z).setTypeId(materialId, false);
               			
               			// lower portion
                		if (centerY - y <= floodY)
                			materialId = floodMaterialId;
               			world.getBlockAt(centerX + x, centerY - y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX + x, centerY - y, centerZ - z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ + z).setTypeId(materialId, false);
               			world.getBlockAt(centerX - x, centerY - y, centerZ - z).setTypeId(materialId, false);
                	}
                }
            }
        }
	}
	
	private int grassId = Material.GRASS.getId();
	private int stoneId = Material.STONE.getId();
	private int noFoliageId = Material.CAKE.getId();
	protected void sprinkleGoodness(Chunk chunk, 
			int specialBlockOdds, int specialsPerLayer, 
			int transmutableId, int plantableId,
			int flowerOdds, Material flowerMaterial, Material bladesMaterial) {
		
		// sprinkle minerals for each y layer, one of millions of ways to do this!
		for (int y = 1; y < 127; y++) {
			if (random.nextInt(specialBlockOdds) == 0) {
				for (int i = 0; i < specialsPerLayer; i++) {
					Block block = chunk.getBlock(random.nextInt(16), y, random.nextInt(16));
					int id = block.getTypeId();
					
					// Transmutation?
					if (id == transmutableId)
						block.setTypeId(pickRandomMineralAt(y).getId(), false);
					
					// If the block supports foliage, then what type will go here?
					else if (id == plantableId) {
						Location foliageAt = block.getLocation().add(0, 1, 0);
						if (random.nextInt(flowerOdds) == 0)
							world.getBlockAt(foliageAt).setType(flowerMaterial);
						else {
							Block grass = world.getBlockAt(foliageAt);
							grass.setType(bladesMaterial);
							grass.setData((byte)1);
						}
					}
				}
			}
		}
	}
	
	protected void sprinkleGoodness(Chunk chunk, 
			int specialBlockOdds, int specialsPerLayer, 
			int flowerOdds, Material flowerMaterial, Material bladesMaterial) {
		sprinkleGoodness(chunk, specialBlockOdds, specialsPerLayer, stoneId, grassId, flowerOdds, flowerMaterial, bladesMaterial);
	}
	
	protected void sprinkleGoodness(Chunk chunk, 
			int specialBlockOdds, int specialsPerLayer) {
		sprinkleGoodness(chunk, specialBlockOdds, specialsPerLayer, stoneId, noFoliageId, 1000, Material.AIR, Material.AIR);
	}
	
	protected void sprinkleTrees(Chunk chunk, int treesPerChunk, TreeType treeType) {

		// plant lots of trees
		int worldX = chunk.getX() * 16; // these use the chunk's world relative location instead..
		int worldZ = chunk.getZ() * 16; // ..of well's as generateTree needs world coordinates
		for (int t = 0; t < treesPerChunk; t++) {
			int centerX = worldX + random.nextInt(16);
			int centerZ = worldZ + random.nextInt(16);
			int centerY = world.getHighestBlockYAt(centerX, centerZ);
			Block rootBlock = world.getBlockAt(centerX, centerY - 1, centerZ);
			if (rootBlock.getTypeId() == grassId) {
				Location treeAt = rootBlock.getLocation().add(0, 1, 0);
				world.generateTree(treeAt, treeType);
			}
		}
	}
	
	private int intAir = Material.AIR.getId();
	protected void setBlock(int x, int y, int z, int blockId, byte blockData) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, blockData, false);
	}
	
	protected void setBlock(int x, int y, int z, int blockId) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, (byte) 0, false);
	}
	
	protected void setBlock(int x, int y, int z, int blockId, byte blockData, boolean blockPhysics) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, blockData, blockPhysics);
	}
	
	protected void setBlock(int x, int y, int z, int blockId, boolean blockPhysics) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, (byte) 0, blockPhysics);
	}
	
	protected void clearBlock(int x, int y, int z) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() != intAir)
			block.setTypeIdAndData(intAir, (byte) 0, false);
	}
}
