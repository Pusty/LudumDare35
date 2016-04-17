package game.engine.world;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class ChunkGenerator {

	HashMap<Structure,Integer> structures;
	final Random random = new Random();
	public ChunkGenerator() {
		structures = new HashMap<Structure,Integer>();
		
		//HOLE
		structures.put(new Structure() {
			int size = 1;
			@Override
			public void init() {
				size = random.nextInt(2)+1;
			}
			@Override
			public int getSize() {
				return size+3;
			}
			@Override
			public void build(World w,Chunk c,int x) {			
				for(int ex=1;ex<=size+1;ex++) {
					c.setBlockID(x+ex, 0, -1);
					c.setBlockID(x+ex, 1, -1);
					c.setBlockID(x+ex, 2, -1);
					c.setBlockID(x+ex, 3, -1);
					c.setBlockID(x+ex, 4, -1);
					c.setBlockID(x+ex, 5, -1);
				}
				c.setBlockID(x, 2, 6);
				c.setBlockID(x+getSize()-2, 2, 4);
				c.setBlockID(x+getSize()-2, 1, 3);
				c.setBlockID(x+getSize()-2, 0, 3);	
			}
		},20);
		
		structures.put(new Structure() {
			@Override
			public void init() { }
			
			@Override
			public int getSize() {
				return 3;
			}
			@Override
			public void build(World w,Chunk c,int x) {			
				c.setBlockID(x+1, 0, 1);
				c.setBlockID(x+1, 1, 1);
				c.setBlockID(x+1, 2, 1);
				c.setBlockID(x+1, 3, 6);
				c.setBlockID(x, 3, 4);
				c.setBlockID(x, 2, 8);
			}
		},20);
		
		
		structures.put(new Structure() {
			@Override
			public void init() { }
			
			@Override
			public int getSize() {
				return 3;
			}
			@Override
			public void build(World w,Chunk c,int x) {			
				c.setBlockID(x+1, 0, 1);
				c.setBlockID(x+1, 1, 1);
				c.setBlockID(x+1, 2, 5);
				c.setBlockID(x+1, 3, 1);
				c.setBlockID(x+1, 4, 6);
				c.setBlockID(x, 4, 4);
				c.setBlockID(x, 3, 7);
				c.setBlockID(x+1, 5, -1);
			}
		},5);
		
		structures.put(new Structure() {
			int size = 1;
			@Override
			public void init() {
				size = random.nextInt(3)+1;
			}
			@Override
			public int getSize() {
				return size;
			}
			@Override
			public void build(World w,Chunk c,int x) {	}
		},60);
		
		
	}
	
	public Structure getStructure() {
		int max = 0;
		for(int extra:structures.values()) {
			max = max + extra;
		}		
		int index = random.nextInt(max);
		int min = 0;
		for(Entry<Structure, Integer> en:structures.entrySet()) {
			if(index >= min && index < min + en.getValue()) {
				return en.getKey();
			}
			min = min + en.getValue();
		}
		return null;
	}
	
	int generatedChunks = 0;
	public void generate(World world) {
		Chunk newChunk = new Chunk(2, 0, 16, 16);
		for(int x=0;x<16;x++) {
			newChunk.setBlockID(x, 0, 1);
			newChunk.setBlockID(x, 1, 1);
			newChunk.setBlockID(x, 2, 5);
		}
		
		if(generatedChunks<10) {
			for(int x=0;x<16;x++) {
				Structure struc = getStructure();
				if(x+struc.getSize() < 16) {
					struc.build(world, newChunk, x);
					x=x+struc.getSize();
				}
			}
		}else {
			newChunk.setBlockID(5, 2, 9);
			newChunk.setBlockID(6, 2, 10);
			newChunk.setBlockID(5, 3, 11);
			newChunk.setBlockID(6, 3, 12);
		}
		
		generatedChunks++;
		
		
		world.getChunkArray()[2].fill(newChunk);
		
	}

}

abstract class Structure {
	public Structure() { init(); }
	public abstract void init();
	public abstract void build(World w,Chunk c,int x);
	public abstract int getSize();
}