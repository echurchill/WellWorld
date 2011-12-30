package me.daddychurchill.WellWorld;

import java.util.Random;

import me.daddychurchill.WellWorld.Support.ByteChunk;
import me.daddychurchill.WellWorld.Support.WellWall;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public abstract class WellArchetype {
	
	public Random random;
	protected long randseed;
	protected Vector minBlock;
	protected Vector maxBlock;
	protected int wellX;
	protected int wellZ;
	
	public WellArchetype(long seed, int wellX, int wellZ) {
		// make our own random
		this.randseed = seed;
		this.random = new Random(randseed);
		
		// where are we?
		this.wellX = wellX;
		this.wellZ = wellZ;
		
		// calculate the well's block bounds
		int x1 = wellX * 16;
		int x2 = (wellX + WellWorld.wellWidthInChunks) * 16; 
		int z1 = wellZ * 16;
		int z2 = (wellZ + WellWorld.wellWidthInChunks) * 16; 
		this.minBlock = new Vector(x1 + WellWall.wallThicknessInBlocks, 1, z1 + WellWall.wallThicknessInBlocks);
		this.maxBlock = new Vector(x2 - WellWall.wallThicknessInBlocks, 127, z2 - WellWall.wallThicknessInBlocks);
	}
	
	// override these to make something really happen!
	public abstract void populateChunk(World world, ByteChunk chunk);
	public abstract void populateBlocks(World world, Chunk chunk);	
	
	// gives you the well's origin chunk position
	public int getX() {
		return wellX;
	}
	
	public int getZ() {
		return wellZ;
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
}
