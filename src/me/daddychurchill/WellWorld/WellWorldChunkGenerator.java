package me.daddychurchill.WellWorld;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.daddychurchill.WellWorld.Support.ByteChunk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class WellWorldChunkGenerator extends ChunkGenerator {

	private WellWorld plugin;
	public String worldname;
	public String worldstyle;
	public static final Material wallMaterial = Material.BEDROCK;
	
	public WellWorldChunkGenerator(WellWorld instance, String name, String style){
		this.plugin = instance;
		this.worldname = name;
		this.worldstyle = style;
	}
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		// see if this works any better (loosely based on ExpansiveTerrain)
		int x = random.nextInt(100) + 16;
		int z = random.nextInt(100) + 16;
		int y = world.getHighestBlockYAt(x, z);
		return new Location(world, x, y, z);
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new WellWorldBlockPopulator(plugin, worldname, worldstyle));
	}

	@Override
	public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
		
		// figure out what everything looks like
		WellArchetype well = plugin.getWellManager(world, random, chunkX, chunkZ);
		if (well != null) {
			
			// let the well do it's stuff
			ByteChunk source = new ByteChunk(chunkX, chunkZ);
			
			// let the chunk do it's stuff
			well.populateChunk(world, source);
			
			// add walls?
			int testX = chunkX - well.getX();
			int testZ = chunkZ - well.getZ();
			drawWellBounds(source, random, 
					testX == 0, testX == WellWorld.wellWidthInChunks - 1,
					testZ == 0, testZ == WellWorld.wellWidthInChunks - 1);
			
			return source.blocks;
		} else
			return null;
	}
	
	//TODO railroad along the wall ridge?
	private void drawWellBounds(ByteChunk source, Random random, boolean wallNorth, boolean wallSouth, boolean wallWest, boolean wallEast) {
		if (wallNorth || wallSouth || wallEast || wallWest) {
			
			// place the walls themselves
			for (int i = 0; i < 8; i++) {
				int y1 = i * 16;
				int y2 = (i + 1) * 16;
				
				// the walls themselves
				if (wallNorth)
					source.setBlocks(0, WellWorld.wallThicknessInBlocks - i, y1, y2, 0, 16, wallMaterial);
				if (wallSouth)
					source.setBlocks(16 - WellWorld.wallThicknessInBlocks + i, 16, y1, y2, 0, 16, wallMaterial);
				if (wallWest)
					source.setBlocks(0, 16, y1, y2, 0, WellWorld.wallThicknessInBlocks - i, wallMaterial);
				if (wallEast)
					source.setBlocks(0, 16, y1, y2, 16 - WellWorld.wallThicknessInBlocks + i, 16, wallMaterial);
			}
			
			//TODO add between wells portals
		}
		
		// add a floor
		source.setBlocksAt(0, wallMaterial);
	}
}
