package project4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
public class LList {

	private treeNode listHead;
	
//	Constructors
	public LList() {
		this.listHead = new treeNode();
		this.listHead.setChstr("dummy");
		this.listHead.setFrequency(0);
	}
	
//	Setters
//	Getters
	public treeNode getHead() 
	{
		return this.listHead;
	}
//	Methods
	public void insertNewNode(treeNode n) {
        treeNode runner = this.listHead;
        while(runner.getNext() != null && runner.getNext().getFrequency() < n.getFrequency()) {
        	runner = runner.getNext();
        }
        n.setNext(runner.getNext());
        runner.setNext(n);
	}
	
	public void printList(File outFile) {
		try 
		{
			FileWriter fw = new FileWriter(outFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			treeNode runner = this.listHead;
			while(runner != null) {
				bw.write(runner.printNode() + "\n");
				runner = runner.getNext();
			}
			
			bw.write("\n");
			bw.close();
			fw.close();
		}
		catch(Exception e)
		{}
	}	
}