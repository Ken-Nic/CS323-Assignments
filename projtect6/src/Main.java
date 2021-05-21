import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
//		File from CLI
		File inFile1 = new File(args[0]);
		File inFile2 = new File(args[1]);
		int numProcs = Integer.parseInt(args[2]);
		File outFile1 = new File(args[3]);
		File outFile2 = new File(args[4]);
		Schedule sch;
		
//		Variables
		int jobID;
		int availProc;
		int numNodes = 0;
//		Getting number of nodes from file, creating scheduling object
		try 
		{
			Scanner read = new Scanner(inFile1);
			numNodes = Integer.parseInt(read.next());
			read.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Error getting number of nodes on graph");
		}
		
//		Checking for (un)limited processors
		if(numProcs <= 0) 
		{
			System.out.println("Error: Need 1 or more processors");
			System.exit(-1);
		} else if(numProcs > numNodes) 
		{
			numProcs = numNodes;
		}
		
//		Creating Schedule object
		sch = new Schedule(numNodes,numProcs);
		sch.loadMatrix(inFile1);
		sch.loadJobTimeAry(inFile2);
		sch.setMatrix();
		sch.printMatrix(outFile2);
		
		while(!(sch.isGraphEmpty()))
		{
			jobID = sch.findOrphan();
			
			while(jobID > 0)
			{
				Node newNode = new Node(jobID,sch.getJobTime(jobID),null);
				sch.openInsert(newNode);
				sch.printOPEN(outFile2);
				jobID = sch.findOrphan();
			}
			
			availProc = sch.getNextProc();
			while(availProc >= 0 && !(sch.isOPENEmpty()) && sch.getProcsUsed() < numProcs)
			{
				Node newJob = sch.getNewJob();
				sch.putJobOnTable(availProc,newJob.getJobID(),newJob.getJobTime());
				if(availProc > sch.getProcsUsed()) 
				{
					sch.setProcsUsed(sch.getProcsUsed() + 1);
				}
				availProc = sch.getNextProc();
			}
			
			sch.printTable(outFile2); 
			boolean hasCycle = sch.checkCycle();
			
			if(hasCycle) 
			{
				System.out.println("There is a cycle in the graph");
				System.exit(-1);			
			}
			
			sch.incrementTime();
			int proc = 0;
			
			while(proc <= sch.getProcsUsed()) 
			{
				jobID = sch.checkJob(proc);
				if(jobID > 0) 
				{
					sch.deleteJob(jobID);
				}
				
				sch.printMatrix(outFile2);
				proc++;
			}
		}
		
		sch.printTable(outFile1);
	}
}
