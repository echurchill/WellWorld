package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class RealisticMoonWell extends WellArchetype {

	// loosely based on Dinnerbone's Cheese Moon World Generator
	
	// original options
    //private static final int CRATER_CHANCE = 30; // down from 45 // Out of 100
    private static final int MIN_CRATER_SIZE = 3;
    private static final int SMALL_CRATER_SIZE = 6; // down from 8
    private static final int BIG_CRATER_SIZE = 14; // down from 16
    private static final int BIG_CRATER_CHANCE = 10; // Out of 100
    
    // wellworld additions
	private static final int WATER_CHANCE = 30; // out of 100... the rest of the time it is lava
    private static final int FLOOD_CHANCE = 50; // out of 100
    private static final int METEORITE_RATIO = 3; // 1/n the size of the crater
    private static final int MIN_METEORITE_SIZE = 1;
    private static final Material negativeMaterial = Material.AIR;
    
    private Material interiorMaterial;
    private Material surfaceMaterial;
    private Material floodMaterial;
    private int surfaceAt;
    private int surfaceThickness;
    private int craterChance;
    private double surfaceVariance;

	public RealisticMoonWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		// other private bits
		surfaceAt = calcRandomRange(40, 80);
		surfaceThickness = random.nextInt(3) + 1;
		surfaceVariance = calcRandomRange(1.5, 3.0);
		craterChance = calcRandomRange(20, 50);
		
		// material
		switch (random.nextInt(5)) {
		case 1: // mars 
			interiorMaterial = Material.SOUL_SAND;
			surfaceMaterial = Material.NETHERRACK;
			if (random.nextInt(100) <= WATER_CHANCE)
				floodMaterial = Material.ICE;
			else
				floodMaterial = Material.LAVA;
			break;
		case 2: // callisto
			interiorMaterial = Material.ENDER_STONE;
			surfaceMaterial = Material.OBSIDIAN;
			if (random.nextInt(100) <= WATER_CHANCE)
				floodMaterial = Material.ICE;
			else
				floodMaterial = negativeMaterial;
			break;
		case 3: // tethys
			interiorMaterial = Material.SAND;
			surfaceMaterial = Material.ICE;
			if (random.nextInt(100) <= WATER_CHANCE)
				floodMaterial = Material.ICE;
			else
				floodMaterial = negativeMaterial;
			surfaceVariance = 1.5;
			craterChance = 50;
			break;
		case 4: // rhea
			interiorMaterial = Material.COBBLESTONE;
			surfaceMaterial = Material.SAND;
			if (random.nextInt(100) <= WATER_CHANCE)
				floodMaterial = Material.ICE;
			else
				floodMaterial = negativeMaterial;
			break;
		default: // moon
			interiorMaterial = Material.COBBLESTONE;
			surfaceMaterial = Material.STONE;
			if (random.nextInt(100) <= WATER_CHANCE)
				floodMaterial = Material.ICE;
			else
				floodMaterial = Material.LAVA;
			break;
		}
	}

    private NoiseGenerator generator;

    private NoiseGenerator getGenerator(World world) {
        if (generator == null) {
            generator = new SimplexNoiseGenerator(randseed);
        }

        return generator;
    }

    private int getHeight(World world, double x, double z, double variance) {
        NoiseGenerator gen = getGenerator(world);

        double result = gen.noise(x, z);
        result *= variance;
        return NoiseGenerator.floor(result);
    }
   
	@Override
	public void populateChunk(ByteChunk chunk) {
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = getHeight(world, chunkX + x * 0.0625, chunkZ + z * 0.0625, surfaceVariance) + surfaceAt;
                chunk.setBlocks(x, 1, height - surfaceThickness, z, interiorMaterial);
                chunk.setBlocks(x, height - surfaceThickness, height, z, surfaceMaterial);
            }
        }
	}
    
	@Override
	public void populateBlocks(Chunk chunk) {
        if (random.nextInt(100) <= craterChance) {
        	
        	// location
            int centerX = (chunk.getX() << 4) + random.nextInt(16);
            int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
            
            // how big
            int radius = 0;
            if (random.nextInt(100) <= BIG_CRATER_CHANCE) {
                radius = random.nextInt(BIG_CRATER_SIZE - MIN_CRATER_SIZE + 1) + MIN_CRATER_SIZE;
            } else {
                radius = random.nextInt(SMALL_CRATER_SIZE - MIN_CRATER_SIZE + 1) + MIN_CRATER_SIZE;
            }
            
            // nudge to fit inside the well (WellWorld addition)
            centerX = nudgeToBounds(centerX, radius, minBlock.getBlockX(), maxBlock.getBlockX());
            centerZ = nudgeToBounds(centerZ, radius, minBlock.getBlockZ(), maxBlock.getBlockZ());
            
            // how far up? (WellWorld change)
            int centerY = world.getHighestBlockYAt(centerX, centerZ);
            
            // nudge to fit inside the well (WellWorld addition)
            centerY = nudgeToBounds(centerY, radius, minBlock.getBlockY(), maxBlock.getBlockY());
            
            // flood? (WellWorld addition)
            int floodY = random.nextInt(100) <= FLOOD_CHANCE ? centerY - radius / 2 : 0;
            
            // clear the sphere
            drawSolidSphere(world, chunk, centerX, centerY, centerZ, radius, negativeMaterial, floodY, floodMaterial);

            // place meteorite
            int meteoriteRadius = Math.max(MIN_METEORITE_SIZE, radius / METEORITE_RATIO);
            if (meteoriteRadius > 0) {
            	
            	// offset a bit
            	int meteoriteX = centerX;// + random.nextInt(radius * 2) - radius;
            	int meteoriteZ = centerZ;// + random.nextInt(radius * 2) - radius;
            	
            	// move down quite a bit
            	int meteoriteY = centerY - random.nextInt(radius * 4) - radius - meteoriteRadius;
            	
            	// make sure it is in bounds
            	meteoriteX = nudgeToBounds(meteoriteX, meteoriteRadius, minBlock.getBlockX(), maxBlock.getBlockX());
            	meteoriteZ = nudgeToBounds(meteoriteZ, meteoriteRadius, minBlock.getBlockZ(), maxBlock.getBlockZ());
            	meteoriteY = nudgeToBounds(meteoriteY, meteoriteRadius, minBlock.getBlockY(), maxBlock.getBlockY());
                
            	// what is it made of?
            	Material meteoriteMaterial = pickRandomMineralAt(meteoriteY);
            	
            	// draw it
            	if (meteoriteRadius == 1) {
            		world.getBlockAt(meteoriteX, meteoriteY, meteoriteZ).setType(meteoriteMaterial);
            	} else {
                    drawSolidSphere(world, chunk, meteoriteX, meteoriteY, meteoriteZ, meteoriteRadius, meteoriteMaterial);
            	}
            }
        }
	}
}
