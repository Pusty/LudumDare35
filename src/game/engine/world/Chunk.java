package game.engine.world;

public class Chunk {
	int[] idarray;
	int sizex;
	int sizey;
	int chunkx;
	int chunky;
		public Chunk(int cx,int cy,int sx,int sy){
			idarray = new int[sx * sy];
			sizex = sx;
			sizey = sy;
			chunkx=cx;
			chunky=cy;
			for(int i=0;i<idarray.length;i++){
				idarray[i]=-1;
			}
			
		}
		public int getChunkX(){return chunkx;}
		public int getChunkY(){return chunky;}
		
		public int getBlockID(int x, int y) {
			if (x >= sizex || y >= sizey || x < 0 || y < 0)
				return 0;
			return idarray[y * sizex + x];
		}
	
		
		boolean emptyWorld=true;
		
		public boolean isEmptyWorld() {
			return emptyWorld;
		}
		
	

		public int getSizeX() {
			return sizex;
		}

		public int getSizeY() {
			return sizey;
		}

		public int[] getBlockArray() {
			return idarray;
		}
		public void setBlockID(int x, int y,int id) {
			if (x >= sizex || y >= sizey || x < 0 || y < 0)
				return;
			if(id!=-1 && emptyWorld)
				emptyWorld=false;
			idarray[y * sizex + x]=id;
		}
		
	
}
