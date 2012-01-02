package me.daddychurchill.WellWorld;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.daddychurchill.WellWorld.Support.ByteChunk;
import me.daddychurchill.WellWorld.Support.WellWall;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
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
			well.populateChunk(source);
			
			// draw the well walls
			WellWall.generateWalls(well, source, chunkX - well.getX(), chunkZ - well.getZ());
			
			return source.blocks;
		} else
			return null;
	}
}
