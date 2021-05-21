package project4;

import java.io.File;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		String inFile = args[0];
		String debugFile = inFile + "_Debug.txt";
		File deBug = new File(debugFile);
		
		huffmanCode h = new huffmanCode();	
		h.computeCharCounts(inFile, debugFile);
		h.printCountAry(debugFile);
		h.constructHuffmanLList(deBug);
		h.constructBinTree(deBug);
		h.constructCharCode(h.getHuffmanTreeRoot().getNext(), "");
		h.printList(deBug);
		h.printTraversals(deBug);
		h.userInterface();
		
		System.out.println("Done!");
	}
}