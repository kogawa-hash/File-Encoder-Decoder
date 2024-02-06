import java.util.Iterator;  
import java.util.Map;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;

// Import any package as required

public class HuffmanSubmit<E> implements Huffman {
	// Feel free to add more methods and variables as required. 
	private static final int Alphabet_size= 256; 


	public void encode(String inputFile, String outputFile, String freqFile) {
		// TODO: Your code here
		BinaryIn binaryin = new BinaryIn(inputFile);
		MyHashTable<Character, Integer> hash = new MyHashTable<Character, Integer>();
		
		while(!binaryin.isEmpty()) {
			
			Character chara = binaryin.readChar();

			if(hash.containsKey(chara)) {
				hash.add(chara, hash.get(chara) + 1);
			}
			
			else {
				hash.add(chara, 1);
			}
		}
		MyHashTable<Character,String> table = HuffmanTree(hash);
		
		String x = new BinaryIn(inputFile).readString();
		StringBuilder s = new StringBuilder();
		for (int i =0; i<x.length();i++) {
			if(table.containsKey(x.charAt(i))) {
				s.append(table.get(x.charAt(i)));
			}
		}
		
		BinaryOut y = new BinaryOut(outputFile);
		y.write(s.toString()); //use binaryout to write out a file
		y.flush();
		y.close();
		
		
		MyHashTable<Character, Integer> mapp = new MyHashTable<Character, Integer>();
		BinaryIn bi  = new BinaryIn(inputFile);
		String stri = bi.readString();
		char[] strArray = stri.toCharArray();
		for(char c: strArray) {
			if(mapp.containsKey(c)) {
				mapp.add(c, mapp.get(c) + 1);
			} else {
				mapp.add(c, 1);
			}
		}
		String towrite = " ";
		StringBuilder result = new StringBuilder();
		for(HashNode<Character,Integer> entry : mapp.bucketArray) {
			if(entry!=null) {
			System.out.println(entry.key + "::: " + entry.value);
			towrite =String.format("%8s", Integer.toBinaryString(entry.key)).replace(' ', '0');
			result.append(towrite + ":" + entry.value + "\n");
			
			BinaryOut outt = new BinaryOut(freqFile);
			outt.write(result.toString());
			outt.flush();
			outt.close();
			}
		}
   }
	
	public class Node implements Comparable<Node>{ //want to compare based on frequency
		Character character;
		int frequency;
		HashNode<Character, Integer> leftchild;
		HashNode<Character, Integer> rightchild;
		
		public Node(Character character, int frequency) {
			this.character = character;
			this.frequency = frequency;
			this.leftchild = this.rightchild = null;
		}
		
		public Node(int frequency) {
			this.character=null;
			this.frequency=frequency;
		}

		public boolean isLeaf() {
			return this.leftchild == null && this.rightchild == null;
		}
		
		@Override
		public int compareTo(HuffmanSubmit.Node o) { //compare nodes
			// TODO Auto-generated method stub
			
			int frequencyCompare = Integer.compare(this.frequency, o.frequency);
			if(frequencyCompare != 0) { 
				return frequencyCompare;
			}
			return this.frequency - o.frequency;
		}
	}
	
	 public MyHashTable<Character,String> HuffmanTree(MyHashTable<Character, Integer> hash) {
		MinPQ<HashNode<Character, Integer>> pq = new MinPQ<HashNode<Character, Integer>>(hash.size(), new Comparator<HashNode<Character, Integer>>() {

			@Override
			public int compare(HashNode<Character, Integer> o1, HashNode<Character, Integer> o2) {
				// TODO Auto-generated method stub
				return o1.value - o2.value;
			}
			});
		
		Iterator<HashNode<Character, Integer>> it = hash.bucketArray.iterator();

		while(it.hasNext()) {
			
			HashNode<Character, Integer> entry = it.next();
			if(entry!=null) {
				HashNode<Character, Integer> node = new HashNode<Character, Integer>(entry.key, entry.value);
				pq.insert(node);
				System.out.println("ENTRY KEY: " + entry.key);
			}
		}
		
		for(Iterator<HashNode<Character, Integer>> i = pq.iterator();i.hasNext();) {
			HashNode<Character, Integer> n = i.next(); 
			String str = String.format("%8s", Integer.toBinaryString(n.key)).replace(' ', '0');
			System.out.println("KEY: " + str + " VAL: " + n.value);
		}
		
		HashNode<Character, Integer> root = null;
		while(pq.size()>1) {
			HashNode<Character, Integer> x = pq.min();
			pq.delMin();
			
			HashNode<Character, Integer> y = pq.min();
			pq.delMin();
			int freq = x.value + y.value;
			HashNode<Character, Integer> sum = new HashNode<Character, Integer>(null, freq);

			sum.leftchild = x;
			sum.rightchild = y;
			root = sum;
			pq.insert(sum);	
		}
		
		MyHashTable<Character,String> map = LookupTable(root);
		System.out.println("the size of the table is: "+ map.size()); //check the size
		for(Iterator<HashNode<Character, String>> iter = map.bucketArray.iterator();iter.hasNext();) {
			HashNode<Character,String> en = iter.next();
			if(en!=null) {
				System.out.println("Key: " + en.key + " Value:"+ en.value); //check every character and its frequency
			} 
		}
		return map;
	} 
	 
	MyHashTable<Character, String> LookupTable(HashNode<Character, Integer> root) {//map each character to a binary encoding 
		//length of the binary encoding for each character depend on how frequently that character occurs
		MyHashTable<Character, String> lookup = new MyHashTable<>();
		prefixCode(root, "", lookup);
		return lookup;	
	}

   private void prefixCode(HashNode<Character, Integer> node, String prefix, MyHashTable<Character, String>  lookup) {
	   //HashNode<Character, Integer> 
	   											
		// TODO Auto-generated method stub
	   
	   if(!node.isLeaf()) {
		   prefixCode(node.leftchild, prefix + '0', lookup); //when traversing leftside of binary tree, we encode an O
		   prefixCode(node.rightchild, prefix +'1', lookup); //encode 1
	   } else {
		   lookup.add(node.key, prefix);
	   }
	}
   
   //find n.character to see if a is printed
   	MyHashTable<Character, Integer> readfreqtable(String freqFile) { //read hashtable
	   Scanner scan = null;
	try {
		scan = new Scanner(new File (freqFile));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   MyHashTable<Character, Integer> table = new MyHashTable<Character, Integer>();
	   while(scan.hasNext()) {
		   String key = "";
		   String line = scan.nextLine();
		   int i = 0;
		   if(line.isEmpty()) {
			   break;
		   }
		   while(line.charAt(i) != ':') {
			   key+=line.charAt(i);  
			   i++;
		   }
		   
		   i++; //skip column
		   String frequency = "";
		   while(i<line.length()) {  
			   frequency+=line.charAt(i);
			   i++; 
		   }
		   int intfre = Integer.parseInt(frequency);
		   int intkey = Integer.parseInt(key,2); //number of base 2, so its binary number
		   table.add((char) intkey, intfre);
	   }
	return table;
  	}
   
   private HashNode<Character,Integer> readTrie(MyHashTable<Character, Integer> hash) {    //reconstruct a trie from the preorder bitstring representation
		//priority queue, store nodes, go through hastbale, for each entry in hashtable, we crate a node from that entry
			//and put that node inside the pq. then go though pq, take first two nodes from pq, and create a new node(parent) parent.left = 1st node you get
			//put parent back into pq. keep doing until only one node is left. last node = root.
	  // PriorityQueue<HashNode<Character,Integer>> pq = new PriorityQueue<HashNode<Character,Integer>>(); 
	   MinPQ<HashNode<Character,Integer>> pq = new MinPQ<HashNode<Character,Integer>>(); 
	   for(HashNode<Character, Integer> x : hash.bucketArray) {
		   if(x!=null) {
		   HashNode<Character,Integer> n = new HashNode<Character,Integer>(x.key,x.value);
		   pq.insert(n);
		   }
	   }
	   while(pq.size() > 1) {
		   HashNode<Character, Integer> n1 = pq.delMin();
		   HashNode<Character, Integer> n2 = pq.delMin();
		   HashNode<Character, Integer> parent = new HashNode<Character,Integer>(null, n1.value + n2.value);
		   parent.leftchild = n1;
		   parent.rightchild = n2;
		   pq.insert(parent);
		   
	   }
	   HashNode<Character, Integer> root = pq.delMin();
	   return root;
	}

   public void decode(String inputFile, String outputFile, String freqFile){
		// TODO: Your code here
	
	  String in = new BinaryIn(inputFile).readString();
	  StringBuilder sb = new StringBuilder();
	  MyHashTable<Character, Integer> table = readfreqtable(freqFile);
	  HashNode<Character, Integer> root = readTrie(table);
	  if(root == null) {
		   return;
	   }
	   char[] chars = in.toCharArray();
	   int index = 0;
	   HashNode<Character, Integer> x = root;
	   while(index<chars.length) {
		   char cc = chars[index];
		   if(cc == '0' && x.leftchild != null) {
			   x = x.leftchild;
		   }
		   else if (cc== '1' && x.rightchild != null) {
			   x = x.rightchild;
		   }
		   if(x.leftchild == null && x.rightchild == null) {
			   sb.append(x.key);
			   x = root;
		   }
		   index++;
		   
		   if(in.isEmpty()) {
			   break;
		   }
	   }
	   
	   BinaryOut out = new BinaryOut(outputFile);
		out.write(sb.toString());
		out.flush();
		out.close();
	}

public static void main(String[] args) {

	   	Huffman huffman = new HuffmanSubmit();
	    
//	    huffman.encode("ur.jpg", "urdecode.txt", "freq_jpg.txt");
	    
//	    huffman.decode("urdecode.txt", "urdecode.jpg", "freq_jpg.txt");
	   
	   huffman.encode("alice30.txt", "alice30_encode.txt", "freq_alice.txt");
	    
	   huffman.decode("alice30_encode.txt", "alice30_decode.txt", "freq_alice.txt");

   } 
} 
