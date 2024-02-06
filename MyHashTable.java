import java.util.ArrayList;
import java.util.Objects;


class HashNode<K, V>implements Comparable<HashNode<K,V>> {
    K key;
    V value;
    final int hashCode;
 
    // Reference to next node
    HashNode<K, V> next;
	public HashNode<Character, Integer> leftchild;
	public HashNode<Character, Integer> rightchild;
	public int freq;
	public Character chara;
 
    // Constructor
    public HashNode(K key, V value, int hashCode)
    {
        this.key = key;
        this.value = value;
        this.hashCode = hashCode;
    }
    public HashNode(K key, V value)
    {
        this.key = key;
        this.value = value;
        this.hashCode = 0 ;
    }
    public boolean isLeaf() {
		return this.leftchild == null && this.rightchild == null;
	}
    public HashNode(K key) {
    	this.hashCode = 0;
		this.key = key;
    }

	@Override
	public int compareTo(HashNode<K, V> o) {
		int frequencyCompare = Integer.compare((Integer)this.value, (Integer)o.value);
		if(frequencyCompare != 0) { 
			return frequencyCompare;
		}
		return(Integer)this.value - (Integer)o.value;
	}

}
// Class to represent entire hash table
class MyHashTable<K, V> {
	
	 
    // bucketArray is used to store array of chains
    public ArrayList<HashNode<K, V> > bucketArray;
 
    // Current capacity of array list
    private int numBuckets;
 
    // Current size of array list
    public int size;
 
    // Constructor (Initializes capacity, size and
    // empty chains.
    public MyHashTable()
    {
        bucketArray = new ArrayList<>();
        numBuckets = 10;
        size = 0;
 
        // Create empty chains
        for (int i = 0; i < numBuckets; i++)
            bucketArray.add(null);
    }
 
    public int size() { return size; }
    public boolean isEmpty() { return size() == 0; }
     
      private final int hashCode (K key) {
        return Objects.hashCode(key);
    }
   
    // This implements hash function to find index
    // for a key
    private int getBucketIndex(K key)
    {
        int hashCode = hashCode(key);
        int index = hashCode % numBuckets;
        // key.hashCode() coule be negative.
        index = index < 0 ? index * -1 : index;
        return index;
    }
 
    // Method to remove a given key
    public V remove(K key)
    {
        // Apply hash function to find index for given key
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);
        // Get head of chain
        HashNode<K, V> head = bucketArray.get(bucketIndex);
 
        // Search for key in its chain
        HashNode<K, V> prev = null;
        while (head != null) {
            // If Key found
            if (head.key.equals(key) && hashCode == head.hashCode)
                break;
 
            // Else keep moving in chain
            prev = head;
            head = head.next;
        }
 
        // If key was not there
        if (head == null)
            return null;
 
        // Reduce size
        size--;
 
        // Remove key
        if (prev != null)
            prev.next = head.next;
        else
            bucketArray.set(bucketIndex, head.next);
 
        return head.value;
    }
 
    // Returns value for a key
    public V get(K key)
    {
        // Find head of chain for given key
        int bucketIndex = getBucketIndex(key);
          int hashCode = hashCode(key);
       
        HashNode<K, V> head = bucketArray.get(bucketIndex);
 
        // Search key in chain
        while (head != null) {
            if (head.key.equals(key) && head.hashCode == hashCode)
                return head.value;
            head = head.next;
        }
 
        // If key not found
        return null;
    }
 
    // Adds a key value pair to hash
    public void add(K key, V value)
    {
        // Find head of chain for given key
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);
        HashNode<K, V> head = bucketArray.get(bucketIndex);
 
        // Check if key is already present
        while (head != null) {
            if (head.key.equals(key) && head.hashCode == hashCode) {
                head.value = value;
                return;
            }
            head = head.next;
        }
 
        // Insert key in chain
        size++;
        head = bucketArray.get(bucketIndex);
        HashNode<K, V> newNode
            = new HashNode<K, V>(key, value, hashCode);
        newNode.next = head;
        bucketArray.set(bucketIndex, newNode);
 
        // If load factor goes beyond threshold, then
        // double hash table size
        if ((1.0 * size) / numBuckets >= 0.7) {
            ArrayList<HashNode<K, V> > temp = bucketArray;
            bucketArray = new ArrayList<>();
            numBuckets = 2 * numBuckets;
            size = 0;
            for (int i = 0; i < numBuckets; i++)
                bucketArray.add(null);
 
            for (HashNode<K, V> headNode : temp) {
                while (headNode != null) {
                    add(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }
    
    public synchronized boolean containsKey(Object key) {
        ArrayList<HashNode<K, V>> tab = bucketArray;

        for (int i=0; i <tab.size();i++) {
        	if(tab.get(i)!=null) {
        		if(tab.get(i).key==key) {
        			return true;
        		}
        	}
        }
        return false;
    }
    public static void main(String[]args) {
    	MyHashTable<Character,Integer>hash = new MyHashTable<Character,Integer>();
    	hash.add('c', 12);
    	hash.add('b', 22);
    	for(int i =0; i<hash.bucketArray.size();i++) {
    		if(hash.bucketArray.get(i)!=null) {
    			HashNode<Character, Integer> temp = hash.bucketArray.get(i);
    		System.out.println(hash.bucketArray.indexOf(temp));
    		System.out.println(temp.key);
    		System.out.println(temp.value);

    		}
    	}
    	System.out.println(hash.containsKey('c'));
    }
}
