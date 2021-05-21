import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AStar {
	AstarNode startNode,goalNode;
	AstarNode Open;
	AstarNode Close;
	AstarNode childList;
	
	public AStar() 
	{
		Open = new AstarNode();
		Close = new AstarNode();
		childList = new AstarNode();
	}
	
//	Getters and setters
	public AstarNode getStart() 
	{
		return this.startNode;
	}
	
	public AstarNode getGoal() 
	{
		return this.goalNode;
	}
	
	public void setStart(AstarNode s)
	{
		this.startNode = s;
	}
	
	public void setGoal(AstarNode g)
	{
		this.goalNode = g;
	}
	
	public void setChildList(AstarNode list) {
		this.childList.setNext(list); 
	}
//	Methods
	public int computeGstar(AstarNode n) 
	{
		if(n.getParent() != null) 
		{
			return (n.getParent().getgStar()) + 1;
		} 
		else
		{
			return 1;
		}
	}
	
	public int computeHstar(AstarNode n) 
	{
		int moves = 0; // total moves needed to get to goals configuration
		
		for(int i = 0; i< n.getConfiguration().length;i++) 
		{
		
//			If i at n.configuration is in the wrong position
			if((n.getConfiguration()[i] != 0) && (n.getConfiguration()[i] != goalNode.getConfiguration()[i])) 
			{
				
//				Searching for value's rightPosition in the table
				int wrongPosition = i;
				int rightPosition = 0;
				while(goalNode.getConfiguration()[rightPosition] != n.getConfiguration()[i]) 
				{
					rightPosition++;
				}
				
//				When found, find how many moves will it take to put it back into its proper place by finding total difference
				
//				step1: Finding level distance
				int x = Math.floorDiv(wrongPosition,3);
				int y = Math.floorDiv(rightPosition,3);
				int z = Math.abs(x-y);
				
//				step2: Finding colummn distance 
				int q = (Math.min(wrongPosition, rightPosition)) + (3*z);
				int w = Math.abs(q - (Math.max(wrongPosition, rightPosition)));
				
//				step3: increment total moves taken
				moves += (w + z);					
			}
			
		}
		return moves;
	}

	public boolean match(AstarNode nodeA,AstarNode nodeB)
	{
		if(nodeA == null || nodeB == null) 
		{
			return false;
		}
		int[] configuration1 = nodeA.getConfiguration();
		int[] configuration2 = nodeB.getConfiguration();
		for(int i=0; i< configuration1.length; i++) 
		{
			if(configuration1[i] != configuration2[i]) 
			{
				return false;
			}
		}
		return true;
	}

	public boolean isGoalNode(AstarNode n) 
	{
		if(match(n,goalNode)) 
		{
			return true;
		}
		return false;
	}
	
	public void listInsert(AstarNode n) 
	{
		AstarNode runner = Open;
		
		if(Open.getNext() == null) 
		{
			Open.setNext(n);
			n.setNext(null);
		}
		else
		{	
			while(runner.getNext() != null && runner.getNext().getfStar() < n.getfStar()) 
			{
				runner = runner.getNext();
			}
			n.setNext(runner.getNext());
			runner.setNext(n);
		}
	}

	public AstarNode remove() 
	{
		AstarNode retNode = Open.getNext();
		
		if(retNode!=null)
		{
			Open.setNext(retNode.getNext());
		}
		return retNode;
	}

	public boolean checkAncestors(AstarNode currentNode) 
	{
		if(currentNode == startNode) 
		{
			return true;
		}
		
		AstarNode runner = currentNode.getParent();
		
		while((runner != startNode) && !(match(runner,currentNode))) 
		{
			runner = runner.getParent();
		}
		
		return match(currentNode, runner);
	}
	
	public void printList(AstarNode head,File output) 
	{
		AstarNode runner = head;
		
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			while(runner != null) 
			{
				String out = runner.printNode();
				bw.write(out);
				bw.write("\n");
				runner = runner.getNext();
			}
			
			bw.close();
			fw.close();
		} 
		catch(Exception e) 
		{
			
		};
		
		
	}
	
	public void printBoard(AstarNode node,File output) 
	{
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < node.getConfiguration().length; i++) 
			{
				if(i%3 == 0) 
				{             
					bw.write("\n|");
				}
				bw.write(node.getConfiguration()[i] + "|");
			}
			bw.write("\n\n");
			bw.close();
			fw.close();
		} 
		catch(Exception e) 
		{
			System.out.println("PrintBoard error");
		}
	}
	
	public void printSolution(AstarNode currentNode,File output)
	{
		if(currentNode == startNode) 
		{
			printBoard(startNode,output);
		}
		else 
		{
			printSolution(currentNode.getParent(),output);			
			printBoard(currentNode,output);
		}
	}
	
	public AstarNode constructChildList(AstarNode currentNode) 
	{	
		AstarNode LLstack = new AstarNode();
		
//		1st:Find empty space
		int[] currentConfig = currentNode.getConfiguration();
		int i = 0;
		while(currentNode.getConfiguration()[i] != 0) 
		{
			i++;
		}
		
//		2nd: Check around 0 and see who can move into zeros spot
//		2.1: Is there a number above zero  that can move down?
		if(Math.floorDiv(i,3) > 0) 
		{
			int[] newConfig = currentConfig.clone();
			newConfig[i] = newConfig[i-3];
			newConfig[i-3] = 0;
			
			AstarNode newNode = new AstarNode(newConfig);
			newNode.setParent(currentNode);
			
			if(!(checkAncestors(newNode))) 
			{
				newNode.setNext(LLstack.getNext());
				LLstack.setNext(newNode);
			}
		}
		
//		2.2:can a number below zero move up?
		if(Math.floorDiv(i,3) < 2) 
		{
			int[] newConfig = currentConfig.clone();
			newConfig[i] = newConfig[i+3];
			newConfig[i+3] = 0;
			
			AstarNode newNode = new AstarNode(newConfig);
			newNode.setParent(currentNode);
			
			if(!(checkAncestors(newNode))) 
			{
				newNode.setNext(LLstack.getNext());
				LLstack.setNext(newNode);
			}
		}
		
//		2.3: Can numbers on the side move in?
			if((i%3) != 0) 
			{
				int[] newConfig = currentConfig.clone();
				newConfig[i] = newConfig[i-1];
				newConfig[i-1] = 0;
				
				AstarNode newNode = new AstarNode(newConfig);
				newNode.setParent(currentNode);
				
				if(!(checkAncestors(newNode))) 
				{
					newNode.setNext(LLstack.getNext());
					LLstack.setNext(newNode);
				}
			}
			
			if((i%3) != 2) 
			{
				int[] newConfig = currentConfig.clone();
				newConfig[i] = newConfig[i+1];
				newConfig[i+1] = 0;
				
				AstarNode newNode = new AstarNode(newConfig);
				newNode.setParent(currentNode);
				
				if(!(checkAncestors(newNode))) 
				{
					newNode.setNext(LLstack.getNext());
					LLstack.setNext(newNode);
				}
			}
		return LLstack.getNext();
	}

	public AstarNode pop() {
		AstarNode retNode = this.childList.getNext();
		childList.setNext(retNode.getNext());
		retNode.setNext(null);
		return retNode;
	}

	public boolean isOnOpen(AstarNode node) {
		AstarNode runner = Open.getNext();
		while(runner != null) 
		{
			if(runner == node) 
			{
				return true;
			}
			runner = runner.getNext();
		}
		return false;
	}
	
	public boolean isOnClose(AstarNode node) {
		AstarNode runner = Close.getNext();
		while(runner != null) 
		{
			if(runner == node) 
			{
				return true;
			}
			runner = runner.getNext();
		}
		return false;
	}
}