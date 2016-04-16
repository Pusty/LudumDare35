package game.engine.world;

import me.pusty.game.main.GameClass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class WorldLoader {
	
	public static World loadWorldComplete(GameClass g, String name,String folder) {
		World world = loadWorld(g,Gdx.files.internal("resources/"+folder+"/tilemap_"+name+"_World.csv"));
		return world;
	}
	public static World loadWorld(GameClass g, FileHandle handle) {
		try {
		String text = handle.readString();
		String lines[] = text.split("\n");
		
		int count = 0;
		for(char c:lines[0].toCharArray())
			if(c==',')
				count++;
		
		int sizeX = count+1;
		int sizeY = lines.length;
		
		
		World world = new World(g,sizeX,sizeY);
		for(int y=0;y<sizeY;y++) {
			String[] numbers = GameClass.splitNonRegex(lines[y].trim() , ",");
			for(int x=0;x<sizeX;x++) {
				int number = Integer.parseInt(numbers[x].trim());
				world.setBlockID(x,(sizeY-1)-y, number);
			}
		}
		return world;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
