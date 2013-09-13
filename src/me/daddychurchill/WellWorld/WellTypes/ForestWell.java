package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.TreeType;
import org.bukkit.World;

public class ForestWell extends StandardWellArchetype {

	public ForestWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		// no water please
		liquidLevel = -1;

		// how about those trees?
		if (random.nextBoolean()) {
			treeType = TreeType.BIG_TREE;
			vScale = calcRandomRange(5.0, 10.0);
			treesPerChunk = 10;
		} else {
			treeType = TreeType.TALL_REDWOOD;
			vScale = calcRandomRange(25.0, 30.0);
			treesPerChunk = 20;
		}
	}
}
