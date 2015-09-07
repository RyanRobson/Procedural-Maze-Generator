
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator {
	 
	
	static int r=25;
	static int c=25;
	static char[][] maz = new char[r][c];
	static int enRand;
	    public static void main() throws IOException
	    {
	    	BufferedWriter writer;
	    	File file = new File("assets/curMaze.txt");
            file.createNewFile();   
	        writer = new BufferedWriter(new FileWriter(file));

	    	// build maze and initialize with only walls
	        StringBuilder s = new StringBuilder(c);
	        for(int x=0;x<c;x++)
	        	s.append('*');
	        
	        for(int x=0;x<r;x++) maz[x] = s.toString().toCharArray();
	      //draws walls around the border of the mazes. 
	        
	        
	        
	        // select random point and open as start node
	        Point st = new Point((int)(Math.random()*r),(int)(Math.random()*c),null);
	        
	        maz[st.r][st.c] = 'S';
	        
	       // Point ex = new Point((int)(Math.random()*r),(int)(Math.random()*c),null);
	        //maz[ex.r][ex.c] = 'E';
	        // add random number of treasure to maze
	        int trRand = randInt(1,10);
	        for (int i=1; i<trRand; i++){
	        Point tr = new Point ((int )( Math.random()*r),(int)(Math.random()*c),null);
	        maz[tr.r][tr.c] = 'T';
	        }
	        
	        // add random number of enemies 
	        enRand = randInt(2,7);
	        for (int i=1; i<enRand; i++){
	        Point en = new Point ((int )( Math.random()*r),(int)(Math.random()*c),null);
	        maz[en.r][en.c] = 'M';
	        }
	        
	        // iterate through direct neighbors of node
	        ArrayList<Point> frontier = new ArrayList<Point>();
	        for(int x=-1;x<=1;x++)
	        	for(int y=-1;y<=1;y++){
	        		if(x==0&&y==0||x!=0&&y!=0)
	        			continue;
	        		try{
	        			if(maz[st.r+x][st.c+y]=='.') continue;
	        		}catch(Exception e){ // ignore ArrayIndexOutOfBounds
	        			continue;
	        		}
	        		// add eligible points to frontier
	        		frontier.add(new Point(st.r+x,st.c+y,st));
	        	}
	 
	        Point last=null;
	        while(!frontier.isEmpty()){
	 
	        	// pick current node at random
	        	Point cu = frontier.remove((int)(Math.random()*frontier.size()));
	        	Point op = cu.opposite();
	        	try{
	        		// if both node and its opposite are walls
	        		if(maz[cu.r][cu.c]=='*'){
	        			if(maz[op.r][op.c]=='*'){
	 
	        				// open path between the nodes
	        				maz[cu.r][cu.c]='.';
	        				maz[op.r][op.c]='.';
	 
	        				// store last node in order to mark it later
	        				last = op;
	 
	        				// iterate through direct neighbors of node, same as earlier
	        				for(int x=-1;x<=1;x++)
					        	for(int y=-1;y<=1;y++){
					        		if(x==0&&y==0||x!=0&&y!=0)
					        			continue;
					        		try{
					        			if(maz[op.r+x][op.c+y]=='.') continue;
					        		}catch(Exception e){
					        			continue;
					        		}
					        		frontier.add(new Point(op.r+x,op.c+y,op));
					        	}
	        			}
	        		}
	        	}catch(Exception e){ // ignore NullPointer and ArrayIndexOutOfBounds
	        	}
	 
	        	// if algorithm has resolved, mark end node
	        	if(frontier.isEmpty())
	        		maz[last.r][last.c]='E';
	        }
	        
	        for (int i=0; i<r;i++){
	        	if(maz[i][0] != 'E'){
	        		if (maz[0][i] !='E'){
		        		maz[i][0] = '*';
			        	maz[0][i] = '*';
	        		}
	        	}
	        }
	        for (int i=0; i<c; i++){
	        	if(maz[i][0] !='E'){
	        		if (maz[0][i] !='E'){
			          	maz[c-1][i] = '*';
			        	maz[i][c-1] = '*';
	        		}
	        	}
	        }
	        checkForExit();
	        	try
	            {
	            for(int i=0;i<r;i++){
					for(int j=0;j<c;j++){
						writer.write(maz[i][j]);
						}
					writer.newLine();	
				 }
	            		writer.close();
	            }
	            catch(FileNotFoundException e)
	            {
	                System.out.println("File Not Found");
	                System.exit( 1 );
	            }
	            catch(IOException e)
	            {
	                System.out.println("something messed up");
	                System.exit( 1 );
	            }
	        }

	    static class Point{
	    	int r;
	    	int c;
	    	
	    	Point parent;
	    	public Point(int x, int y, Point p){
	    		r=x;c=y;parent=p;
	    	}
	    	// compute opposite node given that it is in the other direction from the parent
	    	public Point opposite(){
	    		if(this.r != parent.r)
	    			return new Point(this.r + (new Integer(r).compareTo(parent.r)),this.c,this);
	    		if(this.c!= parent.c)
	    			return new Point(this.r,this.c+ (new Integer(c).compareTo(parent.c)),this);
	    		return null;
	    	}
	    }
	    
	    public static boolean checkForExit() throws IOException{
	    	
	    	boolean exitSpawned = false;
	    	for (int rows=0; rows<r; rows++){
	    		for (int columns=0; columns<c;columns++){
	    			if (maz[rows][columns]== 'E' ){
	    				exitSpawned = true;
	    				//continue;
	    				}
	    			else{
	    				exitSpawned = false;

	    			}
	    		}
	    	}
	    	
	    	if (exitSpawned = false)
	    		MazeGenerator.main();
	    	
	    	return exitSpawned; 
	    	
	    }
	    public static int randInt(int min, int max) {
	        Random rand = new Random();
	        int randomNum = rand.nextInt((max - min) + 1) + min;
	        return randomNum;
	    }
	    public static int getNoOfEnemies(){
	    	return enRand;
	    	
	    }
	}