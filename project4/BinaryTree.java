package project4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class BinaryTree {
	private treeNode root;
	
//	constructor
	BinaryTree(){
		
	}
	
//	Setters
	public void setRoot(treeNode r) 
	{
		this.root = r;
	}
	
//	Getters
	public treeNode getRoot()
	{
		return this.root;
	}
//	Methods
	public void preOrderTraversal(treeNode n, File output)  {
		if(isLeaf(n)) {
			try 
			{
				FileWriter fw = new FileWriter(output,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(n.printNode() + "\n");
				bw.close();
				fw.close();
			} catch (Exception e){}
		} 
		else
		{
			
			
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(n.printNode() + "\n");
			bw.close();
			fw.close();
		} catch (Exception e){}
		
		preOrderTraversal(n.getLeft(), output);
		preOrderTraversal(n.getRight(), output);
		}
	}
	
	public void inOrderTraversal(treeNode n, File output) {
		if(isLeaf(n)) {
			try 
			{
				FileWriter fw = new FileWriter(output,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(n.printNode() + "\n");
				bw.close();
				fw.close();
			} catch (Exception e){}
		} 
		else
		{
			
		inOrderTraversal(n.getLeft(), output);
		
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(n.printNode() + "\n");
			bw.close();
			fw.close();
		} catch (Exception e){}

		inOrderTraversal(n.getRight(), output);
		}
	}
	
	public void postOrderTraversal(treeNode n, File output)  {
		if(isLeaf(n)) {
			try 
			{
				FileWriter fw = new FileWriter(output,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(n.printNode() + "\n");
				bw.close();
				fw.close();
			} catch (Exception e){}
		} 
		else
		{
			
		postOrderTraversal(n.getLeft(), output);
		postOrderTraversal(n.getRight(), output);
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(n.printNode() + "\n");
			bw.close();
			fw.close();
		} catch (Exception e){}
		}
	}
	
	public Boolean isLeaf(treeNode n) {
		if(n.getLeft() == null && n.getRight() == null) {
			return true;
		}
		return false;
	}
}

