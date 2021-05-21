package project4;

public class treeNode {
	
//	Variables
	private String chStr;
	private int frequency;
	private String code;
	private treeNode left,right,next;
	
//	Constructor(s)
	public treeNode() {
		this.chStr = null;
		this.frequency = 0;
		this.code = null;
		this.left = null;
		this.next = null;
		this.right = null;
	}
	
	public treeNode(String ch,int f, String c, treeNode l, treeNode r, treeNode n) {
		this.chStr = ch;
		this.frequency = f;
		this.code = c;
		this.left = l;
		this.next = n;
		this.right = r;
	}
	
//	Setters
	public void setChstr(String c) {
		this.chStr = c;
	}
	
	public void setFrequency(int f) {
		this.frequency = f;
	}
	
	public void setCode(String c) {
		this.code = c;
	}
	
	public void setLeft(treeNode l) {
		this.left = l;
	}
	
	public void setRight(treeNode r) {
		this.right = r;
	}
	
	public void setNext(treeNode n) {
		this.next = n;
	}
	
//	Getters
	public String getChstr() {
		return this.chStr; 
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public treeNode getLeft() {
		return this.left;
	}
	
	public treeNode getRight() {
		return this.right;
	}
	
	public treeNode getNext() {
		return this.next;
	}
	
//	Methods
	
/*	unEscapeChar: used to unEscape escape char such as \n, so that they can be properly printed to
	files */
	
	public String unEscapeChar(String s) 
	{
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < s.length(); i++) 
		{
			switch((int)s.charAt(i)) 
			{
			case 10: output.append("\\n"); break;
            case 13: output.append("\\r"); break;
            default: output.append(s.charAt(i));
			}
		}
		return output.toString();
	}
	
	
	public String printNode() {
		String output = "("+this.chStr + ", " + this.frequency + ", " + this.code + ", ";
		
		if(this.next != null) 
		{
			output = output + this.next.getChstr() + ", ";
		} else {
			output = output + "null, ";
		}
		
		if(this.left != null)
		{
			output = output + this.left.getChstr() + ", ";
		} else {
			output = output + "null, ";
		}
		
		if(this.right != null)
		{
			output = output + this.right.getChstr();
		} else {
			output = output + "null";
		}
		
		output = output + ");";
		output = unEscapeChar(output);
		return output;
	}
}
