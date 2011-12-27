package me.daddychurchill.WellWorld;

import java.util.Random;

import me.daddychurchill.WellWorld.Support.ByteChunk;

import org.bukkit.Chunk;
import org.bukkit.World;

public abstract class WellArchetype {
	
	protected Random random;
	protected long randseed;
	
	//TODO getBounds to return the bounding rectangle of chunks for this region
	
	public WellArchetype(long seed) {
		// make our own random
		this.randseed = seed;
		this.random = new Random(randseed);
	}
	
	public abstract void populateChunk(World world, ByteChunk chunk);
	public abstract void populateBlocks(World world, Chunk chunk);	
}
