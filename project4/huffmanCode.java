package project4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Scanner;

public class huffmanCode {
//  Variables
	private int[] charCountAry = new int[256];
	private String[] charCode = new String[256];
	
// Inner class objects
	LList huffmanList;
	BinaryTree huffmanTree;
	
//	Constructor
	public huffmanCode()
	{
		this.huffmanList = new LList();
		this.huffmanTree = new BinaryTree();	
	}
	
//	Getters
	public treeNode getHuffmanTreeRoot() 
	{
		return this.huffmanTree.getRoot();
	}
//	Methods
	public void printTraversals(File output) 
	{
		
		//Segmenting print for readability in deBug file
//		preOrder
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("----Printing Tree Traversals---- \n ----preOrder----\n");
			bw.close();
			fw.close();
		} catch (Exception e){}
		this.huffmanTree.preOrderTraversal(getHuffmanTreeRoot().getNext(), output);
		
//		inOrder
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("----inOrder---- \n");
			bw.close();
			fw.close();
		} catch (Exception e){}
		this.huffmanTree.inOrderTraversal(getHuffmanTreeRoot().getNext(), output);
		
//		postOrder
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("----postOrder---- \n");
			bw.close();
			fw.close();
		} catch (Exception e){}
		this.huffmanTree.postOrderTraversal(getHuffmanTreeRoot().getNext(), output);
	}
	
	public void printList(File out) 
	{
	//Segmenting print for readability in deBug file
		try 
		{
		 FileWriter fw = new FileWriter(out,true);
		 BufferedWriter bw = new BufferedWriter(fw);
		 bw.write("----Printing huffman List---- \n");
		 bw.close();
		 fw.close();
		} catch (Exception e){}
		
		this.huffmanList.printList(out);
	}
	
	public void computeCharCounts(String in, String out) 
	{
		try 
		{
			File input = new File(in);
			FileInputStream reader = new FileInputStream(input);
			int index;
			
			while((index = reader.read()) != -1) 
			{
				charCountAry[index]++;
			}			
			reader.close();		
		} catch(Exception e) {
			System.out.println("File reading error");
		}
	}
	
	public void printCountAry(String out) 
	{
		File outFile = new File(out);
		try 
		{
			FileWriter fw = new FileWriter(outFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("Printing charCountAry:\n");
			for(int i = 0; i < this.charCountAry.length; i++)
			{
				if(charCountAry[i] > 0)
				{
					bw.write("Char: "+ i + " \t" + charCountAry[i] + "\n");
				}
			}
			
			bw.write("\n");
			bw.close();
			fw.close();
		}
		catch(Exception e)
		{}
	}
		
	public void constructHuffmanLList(File out)
	{
		
		
		//Segmenting print for readability in deBug file
		try 
			{
				FileWriter fw = new FileWriter(out,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("----Printing HuffmanList---- \n");
				bw.close();
				fw.close();
				} catch (Exception e){}
				
		for(int i = 0; i < 256; i++) 
		{
			if(charCountAry[i] > 0) 
			{
				String chr = "" + (char)i;
				int prob = this.charCountAry[i];
				treeNode newNode = new treeNode(chr,prob,"",null,null,null);
				huffmanList.insertNewNode(newNode);
				huffmanList.printList(out);
			}			
		}
	}
	
	public void constructBinTree(File output)
	{
		treeNode runner = this.huffmanList.getHead();
		
		int newProb;
		String newChStr;
		
		//Segmenting print for readability in deBug file
		try 
		{
			FileWriter fw = new FileWriter(output,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("----Printing Binary Tree---- \n");
			bw.close();
			fw.close();
		} catch (Exception e)
		{
			
		}
		
		
		
		while(runner.getNext().getNext() != null) 
		{
			treeNode firstChild = runner.getNext();
			treeNode secondChild = runner.getNext().getNext();
			
			newProb = (firstChild.getFrequency() + secondChild.getFrequency());
			newChStr = (firstChild.getChstr() + secondChild.getChstr());
			treeNode newNode = new treeNode(newChStr,newProb,"",firstChild,secondChild,null);
			this.huffmanList.insertNewNode(newNode);
			this.huffmanList.getHead().setNext(this.huffmanList.getHead().getNext().getNext().getNext());
			this.huffmanList.printList(output);
		}
		
		this.huffmanTree.setRoot(this.huffmanList.getHead());
	}
	
	public void constructCharCode(treeNode n,String code)
	{
		if(this.huffmanTree.isLeaf(n)) 
		{
			n.setCode(code);
			int index = (int)n.getChstr().charAt(0);
			charCode[index] = code;
		}
		else
		{
			String newLeftCode = code + "0";
			String newRightCode = code + "1";
			constructCharCode(n.getLeft(),newLeftCode);
			constructCharCode(n.getRight(),newRightCode);
		}
	}
	
	public void Encode(File in,File out)
	{
		try
		{
//			Opening inputFile & OutputFile
			FileInputStream reader = new FileInputStream(in);
			FileWriter fw = new FileWriter(out,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			int index;
			String code;
			
			while((index = reader.read())!=-1)
			{
				code = charCode[index];
				bw.write(code);			
			}
			
			bw.close();
			fw.close();
			reader.close();
			
		} 
		catch(Exception e)
		{
			System.out.println("Encode read in error");
		}
	}
	
	public void Decode(File in, File out)
	{
		treeNode spot = this.huffmanTree.getRoot().getNext();
		
		try
		{
//			Opening inputFile & OutputFile
			FileInputStream reader = new FileInputStream(in);
			FileWriter fw = new FileWriter(out,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			int bit = 0;
			
			while(bit != -1) 
			{
				
				if(this.huffmanTree.isLeaf(spot))
				{
					bw.write(spot.getChstr());
					spot = this.huffmanTree.getRoot().getNext();
				}
				
				bit = reader.read();
				if(bit == 48) 
				{
					spot = spot.getLeft();
				} else if (bit == 49) 
				{
					spot = spot.getRight();
				}
				
				else if(bit == -1)
				{
					
				}
				else
				{
					System.out.println("Error! The compress file contains invalid character!");
					bw.close();
					fw.close();
					reader.close();
					System.exit(0);
				}				
				
			}
			
			if(spot != this.huffmanTree.getRoot().getNext()) 
			{
				System.out.println("Error: The compress file is corrupted!");
			}
			
			bw.close();
			fw.close();
			reader.close();		
		}
		catch(Exception e)
		{
			System.out.println("Decode error");
		}
	}
	
	public void userInterface()
	{
		String nameOrg;
		String nameCompress;
		String nameDeCompress;
		char yesNo;
		Scanner answer = new Scanner(System.in);
		
		do 
		{
			System.out.println("Hello user, do you want to encode a file(Y/N)?");
			yesNo = answer.next().charAt(0);
			
			if(yesNo == 'N')
			{
				System.out.println("Ending program");
				System.exit(0);
			}
			else
			{
				answer.nextLine();
				System.out.println("Enter the name of the file (without .txt) that you wish to compress");
				nameOrg = answer.nextLine();
				nameCompress = nameOrg + "_Compressed.txt";
				nameDeCompress = nameOrg + "_DeCompress.txt";
				nameOrg = nameOrg + ".txt";
			
				File inputFile = new File(nameOrg);
				File outputFileCompress = new File(nameCompress);
				File outputFileDeCompress = new File(nameDeCompress);
			
				Encode(inputFile,outputFileCompress);
				Decode(outputFileCompress,outputFileDeCompress);
				
				System.out.println("File encoded\n");
			}
		} while(yesNo != 'N');	
		
		answer.close();
		System.exit(0);	
	}

}