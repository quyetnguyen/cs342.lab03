import java.io.*;
import java.util.*;

public class BTree
{
	int nodeSize; // in bytes
	int numNodes; // number of nodes
	int t; // degree of the tree
	BTreeNode root;
	String fileName;
	RandomAccessFile btreeFile;

	public BTree (String fileName) throws FileNotFoundException
	{
		this.fileName = fileName;
		btreeFile = new RandomAccessFile(fileName, "rw");
	}


	public void bTreeCreate (int t)
	{
		nodeSize = 9 + (2 * t - 1) * 12 + (2 * t) * 4;
		this.t = t;
		numNodes = 0;

		BTreeNode x = allocateNode();
		x.leaf = true;
		x.n = 0;
		diskWrite(x);
		this.root = x;
	}


	public void readBTreeFromDisk()
	{
		btreeFile
	}

	public void writeBTreeToDisk()
	{
		
	}

	public TreeObject bTreeSearch(BTreeNode x, TreeObject k)
	{
		int i = 0;
		while((i < x.n) && (k.getKey() > x.k_objects[i].getKey()))
		{
			i++;
		}
		if ((i < x.n) && (k.getKey() == x.k_objects[i].getKey()))
			return x.k_objects[i];
		else
			return (bTreeSearch(diskRead(x.c_pointers[i]), k));
	}

	// y is node to be split, x is parent which will get the middle element
	// y = C_i[x], middle child of x
	public void bTreeSplitChild (BTreeNode x, int i, BTreeNode y)
	{
		BTreeNode z = allocateNode();
		z.leaf = y.leaf;
		for (int j = 0; j < (t - 1); j++)
			z.k_objects[j] = y.k_objects[j+t];
		z.n = t - 1;
		if (!y.leaf)
		{
			for (int j = 0; j < t; j++)
				z.c_pointers[j] = y.c_pointers[j+t];
		}
		y.n = t - 1;
		for (int j = (x.n +1); j > i + 1; j--)
			x.c_pointers[j+1] = x.c_pointers[j];
		y.c_pointers[i+1] = z.offset;
		for (int j = y.n; j > i; j--)
			x.k_objects[j+1] = x.k_objects[j];
		x.k_objects[i] = y.k_objects[t];
		x.n++;
		diskWrite(y);
		diskWrite(z);
		diskWrite(x);
	}

	// BTree t, key k
	public void bTreeInsert (TreeObject k)
	{
		BTreeNode r = this.root;
		if (r.n == (2*t -1))
		{
			BTreeNode s = allocateNode();
			// Move r to the end of the file 
			r.offset = s.offset;
			// Move s (new root) to the root file location
			s.offset = nodeSize;
			this.root = s;
			s.leaf = false;
			s.n = 0;
			s.c_pointers[0] = r.offset;
			bTreeSplitChild(s, 0, r);
			bTreeInsertNonfull(s, k);
		}
		else
		{
			bTreeInsertNonfull(r, k);
		}
	}

	public void bTreeInsertNonfull (BTreeNode x, TreeObject k)
	{
		int i = x.n;
		if (x.leaf)
		{
			while ( i >= 0 && k.getKey() < x.k_objects[i].getKey())
			{
				x.k_objects[i+1] = x.k_objects[i];
				i--;
			}			
			x.k_objects[i+1] = k;
			x.n++;
			diskWrite(x);
		}
		else
		{
			while ( i >= 0 && k.getKey() < x.k_objects[i].getKey())
				i--;
			i++;			
		BTreeNode c_i = diskRead(x.c_pointers[i]);
			if (c_i.n == (2*t-1))
			{
				bTreeSplitChild	(x, i, c_i);
				
				if (k.getKey() > x.k_objects[i].getKey())
					i++;
				bTreeInsertNonfull(c_i, k);	
			}				
		}
	}

	private void diskWrite (BTreeNode x)
	{
		
	}

	public BTreeNode diskRead (int nodePtr)
	{

		return null;
	}

/*
	private makeRoot(Node x)
	{
		this.root = x;
		x.offset = nodeSize;
	}
*/

	public BTreeNode allocateNode()
	{
		numNodes++;
		return new BTreeNode();
	}

	private class BTreeNode
	{
		// is it a leaf?
		boolean leaf;
		// number of objects
		int n;
		// array of tree objects
		TreeObject[] k_objects;
		// pointers to child nodes;
		int[] c_pointers;
		// file offset
		int offset;

		BTreeNode()
		{
			leaf = false;
			n = 0;
			k_objects = new TreeObject[2*t - 1];
			c_pointers = new int[2*t];
			offset = nodeSize * numNodes; // this is more a guess than anything
		}
	}
}
