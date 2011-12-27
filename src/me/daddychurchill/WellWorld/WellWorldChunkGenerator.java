package me.daddychurchill.WellWorld;

import java.util.Random;

import me.daddychurchill.WellWorld.Support.ByteChunk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class WellWorldChunkGenerator extends ChunkGenerator {

	private WellWorld plugin;
	public String worldname;
	public String worldstyle;
	
	public WellWorldChunkGenerator(WellWorld instance, String name, String style){
		this.plugin = instance;
		this.worldname = name;
		this.worldstyle = style;
	}
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		// see if this works any better (loosely based on ExpansiveTerrain)
		int x = random.nextInt(16) - 8;
		int z = random.nextInt(16) - 8;
		int y = world.getHighestBlockYAt(x, z);
		return new Location(world, x, y, z);
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
			int testX = chunkX % WellWorld.Width;
			int testZ = chunkZ % WellWorld.Width;
			addWalls(source, testX == 0, testX == WellWorld.Width - 1 || chunkX == -1,
							 testZ == 0, testZ == WellWorld.Width - 1 || chunkZ == -1);
			
			// add a floor
			source.setBlocksAt(0, Material.BEDROCK);
			
			return source.blocks;
		} else
			return null;
	}
	
	//TODO railroad along the wall ridge?
	private void addWalls(ByteChunk source, boolean wallNorth, boolean wallSouth, boolean wallWest, boolean wallEast) {
		if (wallNorth || wallSouth || wallEast || wallWest) {
			
			// place the walls themselves
			for (int i = 0; i < 8; i++) {
				int y1 = i * 16;
				int y2 = (i + 1) * 16;
				if (wallNorth)
					source.setBlocks(0, 8 - i, y1, y2, 0, 16, Material.BEDROCK);
				if (wallSouth)
					source.setBlocks(16 - 8 + i, 16, y1, y2, 0, 16, Material.BEDROCK);
				if (wallWest)
					source.setBlocks(0, 16, y1, y2, 0, 8 - i, Material.BEDROCK);
				if (wallEast)
					source.setBlocks(0, 16, y1, y2, 16 - 8 + i, 16, Material.BEDROCK);
			}
			
			//TODO add between wells portals
			//TODO prevent people from climbing over the walls
		}
	}
}
