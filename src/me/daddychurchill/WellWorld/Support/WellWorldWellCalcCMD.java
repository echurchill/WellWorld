package me.daddychurchill.WellWorld.Support;

import me.daddychurchill.WellWorld.WellWorld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WellWorldWellCalcCMD implements CommandExecutor {
//    private final WellWorld plugin;

    public WellWorldWellCalcCMD(WellWorld plugin)
    {
//        this.plugin = plugin;
    }

	int wellWidthInChunks = 4;
	int halfWellWidth = wellWidthInChunks / 2;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		for (int chunkX = -wellWidthInChunks * 2; chunkX < wellWidthInChunks * 2; chunkX++) {
			int wellX = calcOrigin(chunkX);

			String contentZ = "";
			for (int chunkZ = -wellWidthInChunks * 2; chunkZ < wellWidthInChunks * 2; chunkZ++) {
				int wellZ = calcOrigin(chunkZ);
				
				// shift it?
				if ((Math.abs(wellX) / wellWidthInChunks) % 2 == 1)
					if (wellZ + halfWellWidth > chunkZ)
						wellZ -= halfWellWidth;
					else
						wellZ += halfWellWidth;
//					wellZ += wellWidthInChunks / 2;
				
				int adjustedX = chunkX - wellX;
				int adjustedZ = chunkZ - wellZ;
				
				//contentZ = contentZ + " (" + wellZ + ")" + chunkZ;
				contentZ = contentZ + " " + chunkZ + "[" + wellZ + "," + adjustedZ + "]" + generateWalls(adjustedX, adjustedZ);
			}
			
			WellWorld.log.info("" + wellX + "->" + contentZ);
		}
		
		return true;
	}

	private int calcOrigin(int i) {
		if (i >= 0) {
			return i / wellWidthInChunks * wellWidthInChunks;
		} else {
			return -((Math.abs(i + 1) / wellWidthInChunks * wellWidthInChunks) + wellWidthInChunks);
		}
	}
	
	public String generateWalls(int wellChunkX, int wellChunkZ) {
		
		// sides?
		boolean wallNorth = wellChunkX == 0;
		boolean wallSouth = wellChunkX == wellWidthInChunks - 1;
		boolean wallWest = wellChunkZ == 0;
		boolean wallEast = wellChunkZ == wellWidthInChunks - 1;
		
		return (wallNorth ? "N" : "-") + (wallSouth ? "S" : "-") +
			   (wallWest ? "W" : "-") + (wallEast ? "E" : "-");
	}
}
