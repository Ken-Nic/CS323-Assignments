import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
//		Step0:
		File inFile1 = new File(args[0]);
		File inFile2 = new File(args[1]);
		File outFile1 = new File(args[2]);
		File outFile2 = new File(args[3]);
		
		int[] initialConfiguration = new int[9];
		int[] goalConfiguration = new int[9];
		int counter = 0;
		
		try
		{
			Scanner sc = new Scanner(inFile1);
			for(int i=0; i < 9; i++) 
			{
				initialConfiguration[i] = sc.nextInt();
			}
			sc.close();
			sc = new Scanner(inFile2);
			for(int i = 0; i < 9; i++) 
			{
				goalConfiguration[i] = sc.nextInt();
			}	
			sc.close();
		}
		catch(Exception e)
		{}
		
		AstarNode startNode = new AstarNode(initialConfiguration);
		AstarNode goalNode = new AstarNode(goalConfiguration);
		AStar A = new AStar();
		A.setStart(startNode);
		A.setGoal(goalNode);
		
		
//		Step1:
		startNode.setgStar(0);
		startNode.sethStar(A.computeHstar(startNode));
		startNode.setfStar(startNode.getgStar() + startNode.gethStar());
		A.listInsert(startNode);
		
		AstarNode currentNode = null;
		
//		Step11:
		while(currentNode != A.getGoal() || A.childList.getNext() == null) 
		{
	//		Step2:
			currentNode = A.remove();
			
	//		Step3:
			if(A.isGoalNode(currentNode)) 
			{
				try 
				{
					FileWriter fw = new FileWriter(outFile2,true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("Initial Node\n---------------------");
					bw.close();
					A.printSolution(currentNode, outFile2);
					return;
				}
				catch(Exception e)
				{
					
				}
			}
			
	//		Step4:
			A.setChildList(A.constructChildList(currentNode));
			AstarNode child;
			
	//		Step8: repeat 5 - 7
			while(A.childList.getNext() != null) 
			{
				
		//		Step5:
				child = A.pop();
				
		//		Step6:
				int gstar = A.computeGstar(child);
				int hstar = A.computeHstar(child);
				child.setgStar(gstar);
				child.sethStar(hstar);
				child.setfStar(child.getgStar() + child.gethStar());
				
		//		Step7:
				if(!(A.isOnOpen(child)) && !(A.isOnClose(child))) 
				{
					A.listInsert(child);
					child.setParent(currentNode);
				}
				else if(A.isOnOpen(child) && child.getfStar() < A.Open.getNext().getfStar()) 
				{
					child = A.Open.getNext();
					A.listInsert(child);
				}
				else if(A.isOnClose(child) && child.getfStar() < A.Close.getNext().getfStar())
				{
					child = A.Close.getNext();
					A.listInsert(child);
					child.setParent(currentNode);
				}
			}
			
	//		Step9:
			currentNode.setNext(A.Close.getNext());
			A.Close.setNext(currentNode);
			
	//		Step10:
			try 
			{
				FileWriter fw = new FileWriter(outFile1,true);
				BufferedWriter bw = new BufferedWriter(fw);

//				Limiting prints to 30 loops
				if(counter < 30) 
				{
					bw.write("This is OpenList: \n");
					bw.close();
					fw.close();
					A.printList(A.Open,outFile1);
					
					fw = new FileWriter(outFile1,true);
					bw = new BufferedWriter(fw);
					bw.write("\nThis is CLOSE list: \n");
					bw.close();
					fw.close();
					
					A.printList(A.Close,outFile1);
					counter++;
				}
			}
			catch(Exception e) 
			{
				
			}
		}
		
		
//		Step12:
		if(A.Open.getNext() == null && currentNode != A.getGoal()) 
		{
			try 
			{
				FileWriter fw = new FileWriter(outFile1,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("No solution can be found in the search");
				bw.close();
				fw.close();
			} 
			catch(Exception e) 
			{
				
			}
		}		
	}

}
