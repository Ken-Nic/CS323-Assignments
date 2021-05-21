

public class AstarNode {
	private int[] configuration = new int[9];
	private int gStar;
	private int hStar;
	private int fStar;
	private AstarNode parent;
	private AstarNode next;
	
//	Methods
//	Dummy constructor
	public AstarNode() 
	{
		fStar = -9999;
	}
	
	
	public AstarNode(int[] newConfig) {
		this.configuration = newConfig;
		gStar = 0;
		hStar = 0;
		fStar = 0;
		parent = null;
		next = null;
	}


	public String printNode() 
	{
		String output = "<"+String.valueOf(fStar) + "::";
		
		for(int i = 0; i < getConfiguration().length; i++) 
		{
			output += " " + getConfiguration()[i];
		}
		
		if(parent!=null) 
		{
		output += " ::";
		
		for(int i = 0; i < parent.getConfiguration().length; i++) 
		{
			output += " " + getConfiguration()[i];
		}
		
		}
		output += ">";
		return output;
	}


//	Getters and setters
	public int[] getConfiguration() {
		return this.configuration;
	}


	public int getfStar() {
		return this.fStar;
	}
	
	public int getgStar() {
		return gStar;
	}
	
	public int gethStar() {
		return hStar;
	}

	public AstarNode getParent() {
		return parent;
	}


	public AstarNode getNext() {
		return this.next;
	}


	public void setNext(AstarNode n) {
		this.next = n;
	}


	public void setConfiguration(int[] configuration) {
		this.configuration = configuration;
	}


	public void setParent(AstarNode node) {
		this.parent = node;
		
	}


	public void sethStar(int hStar) {
		this.hStar = hStar;
	}
	
	public void setgStar(int gStar) 
	{
		this.gStar = gStar;
	}
	
	public void setfStar(int fStar) 
	{
		this.fStar = fStar;
	}
}
