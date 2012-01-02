package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class DinnerboneMoonWell extends WellArchetype {

	// Port of Dinnerbone's BukkitFullOfMoon for testing purposes with some WellWorld added goodness
    private static final int CRATER_CHANCE = 30; // down from 45 // Out of 100
    private static final int MIN_CRATER_SIZE = 3;
    private static final int SMALL_CRATER_SIZE = 6; // down from 8
    private static final int BIG_CRATER_SIZE = 14; // down from 16
    private static final int BIG_CRATER_CHANCE = 10; // Out of 100
    private static final int FLAG_CHANCE = 1; // Out of 200
    private static final int FLAG_HEIGHT = 3;
    
    //EC: moved these to constants to make things look "prettier" to me
    private static final Material negativeMaterial = Material.AIR;
    private static final Material surfaceMaterial = Material.SPONGE;

	public DinnerboneMoonWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
	}

	//EC: simplified this code a bit
    private NoiseGenerator generator;
    private int getHeight(double x, double z, double variance) {
        if (generator == null)
            generator = new SimplexNoiseGenerator(randseed);

        double result = generator.noise(x, z);
        result *= variance;
        return NoiseGenerator.floor(result);
    }
   
	@Override
	public void populateChunk(ByteChunk chunk) {
		//EC: we don't need to additional bounds checking since the caller will overwrite what it needs to
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int height = getHeight(chunkX + x * 0.0625, chunkZ + z * 0.0625, 2) + 60;
                chunk.setBlocks(x, 1, height, z, surfaceMaterial);
            }
        }
	}
    
	@Override
	public void populateBlocks(Chunk chunk) {
		//EC: in this simplified block populators we use "chunk" instead of the original code's "source"...
		//    and, more importantly, shows that we need to unify multiple block populators into a single one
		
		// populate craters
        if (random.nextInt(100) <= CRATER_CHANCE) {
        	
        	//EC: I had to move things around and bounds check a few variables to make it WellWorld happy
            int centerX = (chunk.getX() << 4) + random.nextInt(16);
            int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
            
            //EC: moved this a little earlier in the code
            int radius = 0;
            if (random.nextInt(100) <= BIG_CRATER_CHANCE) {
                radius = random.nextInt(BIG_CRATER_SIZE - MIN_CRATER_SIZE + 1) + MIN_CRATER_SIZE;
            } else {
                radius = random.nextInt(SMALL_CRATER_SIZE - MIN_CRATER_SIZE + 1) + MIN_CRATER_SIZE;
            }
            
            //EC: bounds checking to make more WellWorld happy
            centerX = nudgeToBounds(centerX, radius, minBlock.getBlockX(), maxBlock.getBlockX());
            centerZ = nudgeToBounds(centerZ, radius, minBlock.getBlockZ(), maxBlock.getBlockZ());
            
            //EC: now we are ready to figure out the center vector
            int centerY = world.getHighestBlockYAt(centerX, centerZ);
            centerY = nudgeToBounds(centerY, radius, minBlock.getBlockY(), maxBlock.getBlockY());
            
            //EC: call the (possibly) optimized filled sphere code instead
            this.drawSolidSphere(world, chunk, centerX, centerY, centerZ, radius, negativeMaterial);
            
//			for (int x = -radius; x <= radius; x++) {
//			    for (int y = -radius; y <= radius; y++) {
//			        for (int z = -radius; z <= radius; z++) {
//			            Vector position = center.clone().add(new Vector(x, y, z));
//			
//			            if (center.distance(position) <= radius + 0.5) {
//			            	Location loc = position.toLocation(world);
//			           		world.getBlockAt(loc).setTypeId(negativeId, false);
//			            }
//			        }
//			    }
//			}
        }
        
        // populate flags
        if (random.nextInt(200) <= FLAG_CHANCE) {

        	//EC: I had to move things around and bounds check a few variables to make it WellWorld happy
            int centerX = (chunk.getX() << 4) + random.nextInt(16);
            int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
            
            //EC: bounds checking to make more WellWorld happy
            centerX = nudgeToBounds(centerX, minBlock.getBlockX(), maxBlock.getBlockX());
            centerZ = nudgeToBounds(centerZ, minBlock.getBlockZ(), maxBlock.getBlockZ());
            
            //EC: now we are ready to figure out the height
            int centerY = world.getHighestBlockYAt(centerX, centerZ);
            centerY = nudgeToBounds(centerY, minBlock.getBlockY(), maxBlock.getBlockY());
            
            BlockFace direction = null;
            Block top = null;
            int dir = random.nextInt(4);

            if (dir == 0) {
                direction = BlockFace.NORTH;
            } else if (dir == 1) {
                direction = BlockFace.EAST;
            } else if (dir == 2) {
                direction = BlockFace.SOUTH;
            } else {
                direction = BlockFace.WEST;
            }

            for (int y = centerY; y < centerY + FLAG_HEIGHT; y++) {
                top = world.getBlockAt(centerX, y, centerZ);
                top.setType(Material.FENCE);
            }

            //EC: replaced the deprecated getFace with the new getRelative
            //EC: for some reason, sometimes the relative block is not a fence post... maybe it is a Minecraft/Bukkit issue
			Block signBlock = top.getRelative(direction);
			if (signBlock.getType() == Material.FENCE) {
				signBlock.setType(Material.WALL_SIGN);
	            BlockState state = signBlock.getState();
	
	            if (state instanceof Sign) {
	                Sign sign = (Sign)state;
	                org.bukkit.material.Sign data = (org.bukkit.material.Sign)state.getData();
	
	                data.setFacingDirection(direction);
	                sign.setLine(0, "---------|*****");
	                sign.setLine(1, "---------|*****");
	                sign.setLine(2, "-------------");
	                sign.setLine(3, "-------------");
	                sign.update(true);
	            }
			}
        }
	}
}
