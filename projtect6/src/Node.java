
public class Node {
//	Variables
	private int jobID,jobTime;
	private Node next;
	
	
//	Constructor(s)
	public Node()
	{
		this.jobID = -99999;
	}
	
	public Node(int j, int jTime, Node n)
	{
		this.jobID = j;
		this.jobTime = jTime;
		this.next = n;
	}
	
//	Setters & Getters
	public void setJobID(int i) 
	{
		this.jobID = i;
	}
	
	public void setJobTime(int t)
	{
		this.jobTime = t;
	}
	
	public void setNext(Node n) 
	{
		this.next = n;
	}
	
	
	public int getJobID() 
	{
		return this.jobID;
	}
	
	public int getJobTime()
	{
		return this.jobTime;
	}
	
	public Node getNext() 
	{
		return this.next;
	}

}
