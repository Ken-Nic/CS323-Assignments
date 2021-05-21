import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Schedule {
	private int numNodes;
	private int numProcs;
	private int procsUsed = 0;
	private int currentTime = 0;
	private int totalJobTime;
	private int[] jobTimeAry;
	private int[][] adjMatrix;
	private int[][] table;
	private Node OPEN;
	
//	Methods
	public Schedule(int n, int p) 
	{
		this.numNodes = n;
		this.numProcs = p;
		OPEN = new Node();
		
//		Allocating space
		this.jobTimeAry = new int[numNodes+1];
		Arrays.fill(this.jobTimeAry,0);
		this.adjMatrix = new int[numNodes+1][numNodes+1];
		this.adjMatrix[0][0] = numNodes;
	}
	
//	Setters & Getters
	public void setProcessors(int p) 
	{
		this.numProcs = p;
	}
	
	public void setCurrentTime(int t) 
	{
		this.currentTime = t;
	}
	
	public void setProcsUsed(int proc) 
	{
		this.procsUsed = proc;
	}
	
	public int getCurrentTime() 
	{
		return this.currentTime;
	}
	
	public int getJobTime(int id) 
	{
		return this.jobTimeAry[id];
	}
	
	public int getProcsUsed() 
	{
		return this.procsUsed;
	}

//	Methods
	public void incrementTime() 
	{
		this.currentTime++;
	}
	
	
//	Step 12 condition check & returns the finished jobID
	public int checkJob(int proc) 
	{
		if(table[proc][currentTime] <=0 && table[proc][currentTime-1] > 0) 
		{
			return table[proc][currentTime-1];
		}
		return -1;
	}
	
	public void loadMatrix(File inFile) 
	{
		try
		{
			Scanner read = new Scanner(inFile);
			read.nextLine();  //Burn a line
			String[] inNodes; //Array to hold node i and node j
			String inLine; //Data from file in string format
			while(read.hasNextLine())
			{
//				Getting edge data
				inLine = read.nextLine();
				inNodes = inLine.split(" ",2);
				
//				Updating matrix based on edge data
//				inNodes[0] == i
//				inNodes[1] == j
				this.adjMatrix[Integer.parseInt(inNodes[0])][Integer.parseInt(inNodes[1])] = 3;
				this.adjMatrix[0][Integer.parseInt(inNodes[1])]++;
				this.adjMatrix[Integer.parseInt(inNodes[0])][0]++;
			}
			read.close();
		}
		catch(Exception e)
		{}
	}
	
	public int loadJobTimeAry(File infile) 
	{
		try 
		{
			Scanner read = new Scanner(infile);
			read.nextLine(); //Burn a line
			String[] jobTime; //Array to hold node i and job time
			String data; //Data from file in string format
			
			while(read.hasNextLine()) 
			{
				data = read.nextLine();
				jobTime = data.split(" ",2);
				
				this.totalJobTime += Integer.parseInt(jobTime[1]); 
				this.jobTimeAry[Integer.parseInt(jobTime[0])] = Integer.parseInt(jobTime[1]); 	
			}
			read.close();
			table = new int[numProcs][totalJobTime];
		}
		catch(Exception e)
		{}
		
		return this.totalJobTime;
	}
	
	public void setMatrix() 
	{
		for(int i=1; i < this.numNodes+1; i++) 
		{
			this.adjMatrix[i][i] = 1;
		}
	}
	
	public void printMatrix(File out) 
	{
		try
		{
			FileWriter fw = new FileWriter(out,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("   ");
			for(int x = 0; x < numNodes + 1; x++) 
			{
				String colN = " " + x + " ";
				bw.write(colN);
			}
			
			bw.write("\n");
			for(int i = 0; i < numNodes+1; i++) 
			{
				String rowN = " " + i + " ";
				bw.write(rowN);
				
				for(int j = 0; j < numNodes+1; j++) 
				{
					String value = "|" + this.adjMatrix[j][i] +"|";
					bw.write(value);
				}
				bw.write("\n");
			}
			
			
			bw.close();
			fw.close();
			
		} catch(Exception e) {}
	}
	
	public int findOrphan() 
	{
		for(int j = 0; j < numNodes+1; j++) 
		{
			if(adjMatrix[0][j] == 0 && adjMatrix[j][j] == 1) 
			{
				adjMatrix[j][j] = 2;
				return j;
			}
		}
		return -1;
	}

	public boolean isOPENEmpty() 
	{
		if(OPEN.getNext() == null) 
		{
			return true;
		}
		return false;
	}
	
	public void openInsert(Node n) 
	{
		if(OPEN.getNext() == null || adjMatrix[n.getJobID()][0] >= adjMatrix[OPEN.getNext().getJobID()][0]) 
		{
			n.setNext(OPEN.getNext());
			OPEN.setNext(n);
		}
		else
		{
			Node runner = this.OPEN.getNext();
			while(runner.getNext() != null && adjMatrix[runner.getNext().getJobID()][0] >= adjMatrix[n.getJobID()][0])
			{
				runner = runner.getNext();
			}
        n.setNext(runner.getNext());
        runner.setNext(n);
		}
	}
	
	public void printOPEN(File out) 
	{
		try 
		{
		FileWriter fw = new FileWriter(out,true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write("\nOPEN");
		
		Node runner = OPEN.getNext();
		while(runner != null)
		{
			String nodeOutput = "" + runner.getJobID();
			bw.write("->"+nodeOutput);
			runner = runner.getNext();
		}
		
		bw.write("\n");
		bw.close();
		fw.close();
		}
		catch(Exception e) 
		{
			
		}
	}
	
	public int getNextProc() 
	{
		for(int i = 0; i < numProcs; i++) 
		{
			if(table[i][currentTime] == 0) 
			{
				return i;
			}
		}
		return -1;
	}
	
	public Node getNewJob() 
	{
		Node out = OPEN.getNext();
		OPEN.setNext(out.getNext());
		return out;
	}
	
	public void putJobOnTable(int process,int job, int jobTime) 
	{
		int time = currentTime;
		int endTime = time + jobTime;
		
		while(time < endTime)
		{
		table[process][time] = job;
		time++;
		}
	}
	
	public void printTable(File output)
	{
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			

			bw.write("\n===============================\n");
			String out;
			out ="ProcUsed:" + (this.procsUsed + 1)+ " CurrentTime:" + this.currentTime + "\n      ";
			bw.write(out);
			for(int i = 0; i < this.totalJobTime; i++) 
			{
				out = i + " ";
				bw.write(out);
			}
			bw.write("\n");

			for(int i = 0; i < numProcs; i++) 
			{
				bw.write("P(" + (i+1)+"):|");
				for(int j = 0; j < totalJobTime; j++ ) 
				{
					out = table[i][j] + "|";
					bw.write(out);
				}
				bw.write("\n");
			}
			bw.close();
			fw.close();
		} 
		catch(Exception e)
		{
			
		}
		
	}
	
	public boolean isGraphEmpty() 
	{
		
		if(adjMatrix[0][0] == 0) 
		{
		return true;
		}	
	return false;
	}
	
	public boolean isProcessorEmpty()
	{
		for(int i = 0; i < numProcs; i++) 
		{
			if (table[i][currentTime] > 0) 
			{
				return false;
			}
		}
		return true;
	}
		
	public boolean checkCycle() 
	{
		if(isOPENEmpty() && !(isGraphEmpty()) && isProcessorEmpty())
		{	
			return true;
		}
		return false;
	}

	public void deleteJob(int job) 
	{
		adjMatrix[job][job] = 0;
		adjMatrix[0][0]--;
		
		for(int j = 1; j <=numNodes; j++) 
		{
			if(adjMatrix[job][j]>0) 
			{
				adjMatrix[0][j]--;
			}
		}
	}
}
