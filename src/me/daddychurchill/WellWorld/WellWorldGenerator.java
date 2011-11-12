package me.daddychurchill.WellWorld;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class WellWorldGenerator extends ChunkGenerator {

	@SuppressWarnings("unused")
	private WellWorld plugin;
	private int WellSize = 16;
	
	public WellWorldGenerator(WellWorld instance){
		this.plugin = instance;
		
	}
	
	protected int coordsToByte(int x, int y, int z){
		return (x * 16 + z) * 128 + y;
	}
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		// TODO do something smarter!
		return new Location(world, 32, 18, 32);
	}

	@Override
	public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
		byte[] blocks = new byte[32768];
		int x, y, z;
		
		for (x = 0; x < 16; x++){
			for (z = 0; z < 16; z++){
				blocks[this.coordsToByte(x, 0, z)] = (byte) Material.BEDROCK.getId();
				
				if (chunkX % WellSize == 0 || chunkZ % WellSize == 0){
					for (y = 1; y < 120; y++) {
						if (x == 0 || z == 0 || x == 15 || z == 15) {
							blocks[this.coordsToByte(x, y, z)] = (byte) Material.OBSIDIAN.getId();
						}
					}
				}
				else {
					for (y = 1; y < 16; y++){
						blocks[this.coordsToByte(x, y, z)] = (byte) Material.GRASS.getId();
					}
				}
			}
		}
		
		return blocks;
	}

}
