package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class RoadPlatWell extends WellArchetype {

	private double xFactor = 25.0;
	private double zFactor = 25.0;
	private double chanceOfCompleteOuterRoad = 0.50;
	private double chanceOfCompleteInnerRoad = 0.33;
	
	private SimplexNoiseGenerator generatorRoad;
	
	public RoadPlatWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generatorRoad = new SimplexNoiseGenerator(randseed);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		
		// general fill
		Material materialGround = Material.GRASS_BLOCK;
		if ((chunkX % 2 == 0 && chunkZ % 2 != 0) || (chunkX % 2 != 0 && chunkZ % 2 == 0))
			materialGround = Material.NETHERRACK;
		chunk.setBlocks(0, 16, 100, 102, 0, 16, materialGround);

		// always there intersections
		drawIntersection(chunk, 0, 0, true);
		drawIntersection(chunk, 8, 0, true);
		drawIntersection(chunk, 0, 8, true);
		
		double roadOdds;
		
		// outer roads
		roadOdds = checkRoadOdds(chunkX, chunkZ, 0);
		drawEWRoad(chunk, 2, 0, true);
		if (roadOdds > chanceOfCompleteOuterRoad)
			drawEWRoad(chunk, 10, 0, true);
		roadOdds = checkRoadOdds(chunkX, chunkZ, 1);
		drawNSRoad(chunk, 0, 2, true);
		if (roadOdds > chanceOfCompleteOuterRoad)
			drawNSRoad(chunk, 0, 10, true);
//		if (roadOdds < 0.5 + chanceOfCompleteOuterRoad / 2.0)
//			drawEWRoad(chunk, 2, 0, true);
//		if (roadOdds > 0.5 - chanceOfCompleteOuterRoad / 2.0)
//			drawEWRoad(chunk, 10, 0, true);
//		roadOdds = checkRoadOdds(chunkX, chunkZ, 1);
//		if (roadOdds < 0.5 + chanceOfCompleteOuterRoad / 2.0)
//			drawNSRoad(chunk, 0, 2, true);
//		if (roadOdds > 0.5 - chanceOfCompleteOuterRoad / 2.0)
//			drawNSRoad(chunk, 0, 10, true);
//		drawOtherRoad = drawEWRoad(chunk, 2, 0, checkRoad(chunkX, chunkZ, 0));
//		drawEWRoad(chunk, 10, 0, !drawOtherRoad || checkRoad(chunkX, chunkZ, 1));
//		drawOtherRoad = drawNSRoad(chunk, 0, 2, checkRoad(chunkX, chunkZ, 2));
//		drawNSRoad(chunk, 0, 10, !drawOtherRoad || checkRoad(chunkX, chunkZ, 3));
		
		// inner roads
		roadOdds = checkRoadOdds(chunkX, chunkZ, 2);
		if (roadOdds < 0.5 + chanceOfCompleteInnerRoad / 2.0)
			drawEWRoad(chunk, 2, 8, true);
		if (roadOdds > 0.5 - chanceOfCompleteInnerRoad / 2.0)
			drawEWRoad(chunk, 10, 8, true);
		roadOdds = checkRoadOdds(chunkX, chunkZ, 3);
		if (roadOdds < 0.5 + chanceOfCompleteInnerRoad / 2.0)
			drawNSRoad(chunk, 8, 2, true);
		if (roadOdds > 0.5 - chanceOfCompleteInnerRoad / 2.0)
			drawNSRoad(chunk, 8, 10, true);
		drawIntersection(chunk, 8, 8, true);
		
//		boolean drawCenter = false;
//		drawCenter = drawNSRoad(chunk, 8, 2, checkInnerRoad(chunkX, chunkZ, 2)) || drawCenter;
//		drawCenter = drawEWRoad(chunk, 2, 8, checkInnerRoad(chunkX, chunkZ, 3)) || drawCenter;
//		drawCenter = drawNSRoad(chunk, 8, 10, checkInnerRoad(chunkX, chunkZ, 4)) || drawCenter;
//		drawCenter = drawEWRoad(chunk, 10, 8, checkInnerRoad(chunkX, chunkZ, 5)) || drawCenter;
//		drawIntersection(chunk, 8, 8, drawCenter);
	}
	
	private double checkRoadOdds(int chunkX, int chunkZ, int n) {
		return (generatorRoad.noise((chunkX * 16 + n) / xFactor, (chunkZ * 16 + n) / zFactor) + 1) / 2;
	}
	
	private void drawIntersection(InitialBlocks chunk, int x, int z, boolean drawRoad) {
		if (drawRoad)
			chunk.setBlocks(x, x + 2, 100, 102, z, z + 2, Material.STONE);
	}

	private boolean drawNSRoad(InitialBlocks chunk, int x, int z, boolean drawRoad) {
		if (drawRoad)
			chunk.setBlocks(x, x + 2, 100, 102, z, z + 6, Material.STONE);
		return drawRoad;
	}

	private boolean drawEWRoad(InitialBlocks chunk, int x, int z, boolean drawRoad) {
		if (drawRoad)
			chunk.setBlocks(x, x + 6, 100, 102, z, z + 2, Material.STONE);
		return drawRoad;
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
