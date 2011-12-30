package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class RealisticMoonWell extends WellArchetype {

	// based on loose extension of DinnerboneMoonWell
	
	// original options
    //private static final int CRATER_CHANCE = 30; // down from 45 // Out of 100
    private static final int MIN_CRATER_SIZE = 3;
    private static final int SMALL_CRATER_SIZE = 6; // down from 8
    private static final int BIG_CRATER_SIZE = 14; // down from 16
    private static final int BIG_CRATER_CHANCE = 10; // Out of 100
    
    // wellworld additions
    private static final int MIN_SURFACE_HEIGHT = 40;
    private static final int MAX_SURFACE_HEIGHT = 80;
    private static final double MIN_SURFACE_VARIANCE = 1.5;
    private static final double MAX_SURFACE_VARIANCE = 4;
	private static final int WATER_CHANCE = 30; // out of 100... the rest of the time it is lava
    private static final int FLOOD_CHANCE = 50; // out of 100
    private static final int METEORITE_RATIO = 3; // 1/n the size of the crater
    private static final int MIN_METEORITE_SIZE = 1;
    private static final int MIN_CRATER_CHANCE = 20;
    private static final int MAX_CRATER_CHANCE = 50;
    private static final Material negativeMaterial = Material.AIR;
    
    private Material interiorMaterial;
    private Material surfaceMaterial;
    private Material floodMaterial;
    private int surfaceAt;
    private int craterChance;
    private double surfaceVariance;

	public RealisticMoonWell(long seed, int wellX, int wellZ) {
		super(seed, wellX, wellZ);
		
		// other private bits
		surfaceAt = calcRandomRange(MIN_SURFACE_HEIGHT, MAX_SURFACE_HEIGHT);
		surfaceVariance = calcRandomRange(MIN_SURFACE_VARIANCE, MAX_SURFACE_VARIANCE);
		craterChance = calcRandomRange(MIN_CRATER_CHANCE, MAX_CRATER_CHANCE);
		
		// material
		switch (random.nextInt(10)) {
		case 0: // cheese
			interiorMaterial = Material.SPONGE;
			surfaceMaterial = Material.SPONGE;
			floodMaterial = negativeMaterial;
			break;
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
			surfaceVariance = MIN_SURFACE_VARIANCE;
			craterChance = MAX_CRATER_CHANCE;
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
	public void populateChunk(World world, ByteChunk chunk) {
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = getHeight(world, chunkX + x * 0.0625, chunkZ + z * 0.0625, surfaceVariance) + surfaceAt;
                chunk.setBlocks(x, 1, height, z, interiorMaterial);
                chunk.setBlock(x, height, z, surfaceMaterial);
            }
        }
	}
    
	@Override
	public void populateBlocks(World world, Chunk chunk) {
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

//            // make it a thing
//            Vector center = new BlockVector(centerX, centerY, centerZ);
//            
//            // clear the sphere
//            for (int x = -radius; x <= radius; x++) {
//                for (int y = -radius; y <= radius; y++) {
//                    for (int z = -radius; z <= radius; z++) {
//                        Vector position = center.clone().add(new Vector(x, y, z));
//                        
//                        // within the sphere's zone
//                        if (center.distance(position) <= radius + 0.5) {
//                        	Location loc = position.toLocation(world);
//                        	if (floodIt && loc.getBlockY() <= floodY)
//                        		world.getBlockAt(loc).setType(floodMaterial);
//                        	else
//                        		world.getBlockAt(loc).setType(negativeMaterial);
//                        }
//                    }
//                }
//            }
//            
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
//            		Vector meteoriteCenter = new BlockVector(meteoriteX, meteoriteY, meteoriteZ);
//                    for (int x = -meteoriteRadius; x <= meteoriteRadius; x++) {
//                        for (int y = -meteoriteRadius; y <= meteoriteRadius; y++) {
//                            for (int z = -meteoriteRadius; z <= meteoriteRadius; z++) {
//                                Vector meteoritePosition = meteoriteCenter.clone().add(new Vector(x, y, z));
//                                
//                                // within the sphere's zone
//                                if (meteoriteCenter.distance(meteoritePosition) <= meteoriteRadius + 0.5) {
//                                	Location loc = meteoritePosition.toLocation(world);
//                               		world.getBlockAt(loc).setType(meteoriteMaterial);
//                                }
//                            }
//                        }
//                    }
            	}
            }
        }
	}
}
