package project4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BST<E extends Comparable<E>> implements Iterable<E>, Cloneable {
	
	Node<E> root;
	int size;
	
	public BST() {
		this.root = null;
		this.size = 0;
	}
	
	public BST(E[] collection) {
		this.root = null;
		this.size = 0;
		for (E e : collection) {
			this.add(e);
		}
	}
	
	
	
	
	/**
	 * Adds the specified element to this set if it is not already present. 
	 * More formally, adds the specified element e to this tree if the set 
	 * contains no element e2 such that Objects.equals(e, e2). 
	 * If this set already contains the element, the call leaves the set unchanged and returns false. 
	 * This operation should be O(H).
	 * 
	 * @param e - element to be added to the set
	 * @return true if this set did not already contain the specified element
	 * @throws NullPointerException - if the specified element is null and this set uses natural ordering, 
	 * or its comparator does not permit null elements 
	 */
	
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException("Element is null");
		}
		
		if (this.contains(e)) {
			return false;
		}
		
		Node<E> node = new Node<E>(e);
		if (size == 0) {
			root = node;
			size++;
			return true;
		}
		Node<E> n = root;
		while (!n.isLeaf) {
			if (e.compareTo(n.data) < 0) {
				if (n.left == null) {
					break;
				}
				if (n.right == null || n.left.height > n.right.height) {
					n.height = height(n);
				}
				n = n.left;
			}
			else {
				if (n.right == null) {
					break;
				}
				if (n.left == null || n.right.height > n.left.height) {
					n.height = height(n);
				}
				n = n.right;
			}
		}
		if (e.compareTo(n.data) < 0) {
			n.left = node;
			if (n.right == null || n.left.height > n.right.height) {
				n.height = height(n);
			}
			node.parent = n;
			node.depth = depth(node.parent);
			n.isLeaf = false;
			size++;
			return true;
		}
		n.right = node;
		if (n.left == null || n.right.height > n.left.height) {
			n.height = height(n);
		}
		node.parent = n;
		node.depth = depth(node.parent);
		n.isLeaf = false;
		size++;
		return true;
	}
	
	
	
	/**
	 * Adds all of the elements in the specified collection to this tree.
	 * This operation should be O(MH) where M is the size of the collection 
	 * and H is the height of the current tree.
	 * 
	 * @param collection - collection containing elements to be added to this set
	 * @return true if this set changed as a result of the call
	 * @throws NullPointerException - if the specified collection is null or if any element of the collection is null
	 */
	
	public boolean addAll(Collection<? extends E> collection) {
		if (collection == null) {
			throw new NullPointerException("Null collection");
		}
		for (E e : collection) {
			if (e == null) {
				throw new NullPointerException("Null element in collection");
			}
			this.add(e);
		}
		return true;
	}
	
	
	
	/**
	 * Removes the specified element from this tree if it is present. 
	 * More formally, removes an element e such that Objects.equals(o, e), 
	 * if this tree contains such an element. Returns true if this tree 
	 * contained the element (or equivalently, if this tree changed as a 
	 * result of the call). (This tree will not contain the element once the call returns.)
	 * This operation should be O(H).
	 * 
	 * @param o - object to be removed from this set, if present
	 * @return true if this set contained the specified element
	 * @throws ClassCastException - if the specified object cannot be compared with the 
	 * elements currently in this tree
	 * @throws NullPointerException - if the specified element is null
	 */
	
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		if (o == null) {
			throw new NullPointerException("Object is null");
		}
		if (!this.contains(o)) {
			return false;
		}
		Node<E> node = root;
		Node<E> replaceNode = root;
		while (node != null) {
			if (node.data.equals(o)) {
				if (node.isLeaf) {
					if (node.equals(root)) {
						root = null;
					}
					else if (node.equals(node.parent.left)) {
						node.parent.left = null;
					}
					else if (node.equals(node.parent.right)){
						node.parent.right = null;
					}
					node.parent = null;
					node.isLeaf = false;
					node.depth = 0;
					node.data = null;
					node = null;
					size--;
					return true;
				}
				else {
					replaceNode = node;
					if (replaceNode.right != null) {
						replaceNode = replaceNode.right;
						while (replaceNode.left != null) {
							replaceNode = replaceNode.left;
						}
					}
					else {
						replaceNode = replaceNode.left;
						while (replaceNode.right != null) {
							replaceNode = replaceNode.right;
						}
					}
					if (!replaceNode.parent.equals(node)) {
						if (replaceNode.right != null) {
							replaceNode.parent.left = replaceNode.right;
							replaceNode.right.parent = replaceNode.parent;
						}
						else if (replaceNode.equals(replaceNode.parent.left)) {
							replaceNode.parent.left = null;
						}
						else if (replaceNode.equals(replaceNode.parent.right)) {
							replaceNode.parent.right = null;
						}
					}
					replaceNode.parent = node.parent;
					replaceNode.isLeaf = node.isLeaf;
					replaceNode.depth = node.depth;
					if (node.parent != null) {
						if (node.equals(node.parent.left)) {
							node.parent.left = replaceNode;
						}
						else if (node.equals(node.parent.right)) {
							node.parent.right = replaceNode;
						}
					}
					if (!replaceNode.equals(node.left)) {
						replaceNode.left = node.left;
						if (node.left != null) {
							node.left.parent = replaceNode;
						}
					}
					if (!replaceNode.equals(node.right)) {
						replaceNode.right = node.right;
						if (node.right != null) {
							node.right.parent = replaceNode;
						}
					}
					if (node.equals(root)) {
						root = replaceNode;
						if (replaceNode.parent != null) {
							if (replaceNode.equals(replaceNode.parent.left)) {
								replaceNode.parent.left = null;
							}
							if (replaceNode.equals(replaceNode.parent.right)) {
								replaceNode.parent.right = null;
							}
							replaceNode.parent = null;
						}
					}
					if (replaceNode.left == null && replaceNode.right == null) {
						replaceNode.isLeaf = true;
					}
					node.left = null;
					node.right = null;
					node.parent = null;
					node.isLeaf = false;
					node.depth = 0;
					node.data = null;
					node = null;
					size--;
					return true;
				}
			}
			else if (node.data.compareTo((E) o) > 0) {
				node = node.left;
			}
			else {
				node = node.right;
			}
		}
		return false;
	}
	
	
	
	/**
	 * Removes all of the elements from this set. The set will be empty after this call returns.
	 * This operation should be O(1).
	 */
	
	public void clear() {
		root = null;
	}
	
	
	
	/**
	 * Returns true if this set contains the specified element. 
	 * More formally, returns true if and only if this set contains 
	 * an element e such that Objects.equals(o, e).
	 * This operation should be O(H).
	 * 
	 * @param o - object to be checked for containment in this set
	 * @return true if this set contains the specified element
	 * @throws ClassCastException - if the specified object cannot be compared 
	 * with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null and this 
	 * set uses natural ordering, or its comparator does not permit null elements
	 */
	
	@SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		if (root == null) {
            return false;
		}
		Node<E> node = root;
		while (node != null) {	
			if (o.equals(node.data)) {
				return true;
			}
			if (node.data.compareTo((E) o) > 0) {
				node = node.left;
			}
			else {
				node = node.right;
			}
		}
        return false;
	}
	
	
	
	/**
	 * Returns true if this collection contains all of the elements in 
	 * the specified collection. This implementation iterates over the specified 
	 * collection, checking each element returned by the iterator in turn to see 
	 * if it's contained in this tree. If all elements are so contained true is 
	 * returned, otherwise false.
	 * This operation should be O(MH) where M is the size of the collection and 
	 * H is the height of the current tree.
	 * 
	 * @param c - collection to be checked for containment in this tree
	 * @return true if this tree contains all of the elements in the specified collection
	 * @throws NullPointerException - if the specified collection contains one or more null 
	 * elements and this collection does not permit null elements, or if the specified 
	 * collection is null.
	 */
	
	public boolean containsAll(Collection<?> c) {
		if (c == null) {
			throw new NullPointerException("Collection is null");
		}
		for (Object o : c) {
			if (o == null) {
				throw new NullPointerException("Object in collection is null");
			}
			if (!this.contains(o)) {
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * Returns the number of elements in this tree.
	 * This operation should be O(1).
	 * 
	 * @return number of elements in this tree
	 */
	
	public int size() {
		return size;
	}
	
	
	
	/**
	 * Returns true if this set contains no elements.
	 * This operation should be O(1).
	 * 
	 * @return true if this set contains no elements
	 */
	
	public boolean isEmpty() {
		return root == null;
	}
	
	
	
	/**
	 * Returns the height of this tree. The height of a leaf is 0. 
	 * The height of the tree is the height of its root node.
	 * This operation should be O(1).
	 * 
	 * @return the height of this tree or zero if the tree is empty
	 */
	
	public int height() {
		return height(root);
	}
	
	
	
	/**
	 * Returns an iterator over the elements in this tree in ascending order.
	 * This operation should be O(N).
	 * 
	 * Specified by iterator in interface Iterable<E extends Comparable<E>>
	 * 
	 * @return an iterator over the elements in this set in ascending order
	 */
	
	public Iterator<E> iterator() {
		return new Iter();
	}
	
	
	
	/**
	 * Returns an iterator over the elements in this tree in order of the preorder traversal.
	 * This operation should be O(N).
	 * 
	 * @return an iterator over the elements in this tree in order of the preorder traversal
	 */
	
	public Iterator<E> preorderIterator() {
		return new PreorderIter();
	}
	
	
	
	/**
	 * Returns an iterator over the elements in this tree in order of the postorder traversal.
	 * This operation should be O(N).
	 * 
	 * @return an iterator over the elements in this tree in order of the postorder traversal
	 */
	
	public Iterator<E> postorderIterator() {
		return new PostorderIter();
	}
	
	
	
	/**
	 * Returns the element at the specified position in this tree. The order of the indexed 
	 * elements is the same as provided by this tree's iterator. The indexing is zero based 
	 * (i.e., the smallest element in this tree is at index 0 and the largest one is at index size()-1).
	 * This operation should be O(H).
	 * 
	 * @param index - index of the element to return
	 * @return the element at the specified position in this tree
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	
	public E get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index is out of bounds");
		}
		if (index == 0) {
			return this.first();
		}
		Iterator<E> iter = this.iterator();
		E answer = null;
		int i = 1;
		while (i <= index) {
			answer = iter.next();
			i++;
		}
		return answer;
	}
	
	
	
	/**
	 * Returns a collection whose elements range from fromElement, inclusive, to toElement, inclusive. 
	 * The returned collection/list is backed by this tree, so changes in the returned list are 
	 * reflected in this tree, and vice-versa (i.e., the two structures share elements. 
	 * The returned collection should be organized according to the natural ordering of the elements 
	 * (i.e., it should be sorted).
	 * This operation should be O(M) where M is the number of elements in the returned list.
	 * 
	 * @param fromElement - low endpoint (inclusive) of the returned collection
	 * @param toElement - high endpoint (inclusive) of the returned collection
	 * @return a collection containing a portion of this tree whose elements range from fromElement, 
	 * inclusive, to toElement, inclusive
	 * @throws NullPointerException - if fromElement or toElement is null
	 * @throws IllegalArgumentException - if fromElement is greater than toElement
	 */
	
	public ArrayList<E> getRange(E fromElement, E toElement) {
		if (fromElement == null || toElement == null) {
			throw new NullPointerException("Null bound");
		}
		if (fromElement.compareTo(toElement) > 0) {
			throw new IllegalArgumentException("Starting element is greater than ending element");
		}
		ArrayList<E> rangeList = new ArrayList<>();
		Iterator<E> iter = this.iterator();
		E answer = this.first();
		while (answer.compareTo(fromElement) < 0) {
			answer = iter.next();
		}
		while (answer.compareTo(toElement) <= 0) {
			rangeList.add(answer);
			answer = iter.next();
		}
		return rangeList;
	}
	
	
	
	/**
	 * Returns the least element in this tree greater than or equal to the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * 
	 * @param e - the value to match
	 * @return the least element greater than or equal to e, or null if there is no such element
	 * @throws ClassCastException - if the specified element cannot be compared with the elements 
	 * currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	
	public E ceiling(E e) {
		if (e == null) {
			throw new NullPointerException("Element is null");
		}
		Iterator<E> iter = this.iterator();
		E answer = this.first();
		while (iter.hasNext()) {
			answer = iter.next();
			if (!(answer instanceof E)) {
				throw new ClassCastException("Element is not of correct type");
			}
			if (answer.equals(e) || answer.compareTo(e) > 0) {
				return answer;
			}
		}
		return null;
	}
	
	
	
	/**
	 * Returns the greatest element in this set less than or equal to the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * 
	 * @param e - the value to match
	 * @return the greatest element less than or equal to e, or null if there is no such element
	 * @throws ClassCastException - if the specified element cannot be compared with the elements 
	 * currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	
	public E floor(E e) {
		if (e == null) {
			throw new NullPointerException("Element is null");
		}
		Iterator<E> iter = this.iterator();
		E answer = this.first();
		E prev = null;
		while (answer.compareTo(e) < 0) {
			prev = answer;
			answer = iter.next();
			if (!(answer instanceof E)) {
				throw new ClassCastException("Element cannot be cast to type E");
			}
			if (answer.equals(e)) {
				return answer;
			}
			if (answer.compareTo(e) > 0) {
				return prev;
			}
		}
		return null;
	}
	
	
	
	/**
	 * Returns the first (lowest) element currently in this tree.
	 * This operation should be O(H).
	 * 
	 * @return the first (lowest) element currently in this tree
	 * @throws NoSuchElementException - if this set is empty
	 */
	
	public E first() {
		Node<E> node = root;
		if (node == null) {
			throw new NoSuchElementException("No such element exists");
		}
		while (!node.isLeaf && node.left != null) {
			node = node.left;
		}
		return node.data;
	}

	
	
	/**
	 * Returns the last (highest) element currently in this tree.
	 * This operation should be O(H).
	 * 
	 * @return the last (highest) element currently in this tree
	 * @throws NoSuchElementException - if this set is empty
	 */
	
	public E last() {
		Node<E> node = root;
		if (node == null) {
			throw new NoSuchElementException("No such element exists");
		}
		while (node.right != null) {
			node = node.right;
		}
		return node.data;
	}
	
	
	
	/**
	 * Returns the greatest element in this set strictly less than the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * 
	 * @param e - the value to match
	 * @return the greatest element less than e, or null if there is no such element
	 * @throws ClassCastException - if the specified element cannot be compared with the 
	 * elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	
	public E lower(E e) {
		if (e == null) {
			throw new NullPointerException("Element is null");
		}
		Iterator<E> iter = this.iterator();
		E answer = this.first();
		E prev = null;
		while (answer.compareTo(e) < 0) {
			prev = answer;
			answer = iter.next();
			if (!(answer instanceof E)) {
				throw new ClassCastException("Element is not of correct type");
			}
			if (answer.equals(e) || answer.compareTo(e) > 0) {
				return prev;
			}
		}
		return null;
	}
	
	
	
	/**
	 * Returns the least element in this tree strictly greater than the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * 
	 * @param e - the value to match
	 * @return the least element greater than e, or null if there is no such element
	 * @throws ClassCastException - if the specified element cannot be compared with 
	 * the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	
	public E higher(E e) {
		if (e == null) {
			throw new NullPointerException("Element is null");
		}
		Iterator<E> iter = this.iterator();
		E answer = this.first();
		while (iter.hasNext()) {
			answer = iter.next();
			if (!(answer instanceof E)) {
				throw new ClassCastException("Element is not of correct type");
			}
			if (answer.compareTo(e) > 0) {
				return answer;
			}
		}
		return null;
	}
	
	
	
	/**
	 * Returns the height of a node. It does so recursively, with the base case of a
	 * null node returning -1. The height of a node is the greatest of the heights of 
	 * its two children plus 1.
	 * 
	 * @param node - the node to find the height of
	 * @return the height of the node
	 */
	public int height(Node<E> node) {
		if (node == null) {
			return -1;
		}
		return Math.max(height(node.left), height(node.right)) + 1;
	}
	
	
	
	/**
	 * Returns the depth of a node. It does so recursively, with the base of the root 
	 * returning 0. The depth of the node it one greater than the depth of its parent.
	 * 
	 * @param node - node to find the depth of
	 * @return the depth of a node
	 */
	public int depth(Node<E> node) {
		if (node == null) {
			return 0;
		}
		if (node.equals(root)) {
			return 0;
		}
		return depth(node.parent) + 1;
	}
	
	
	
	/**
	 * Returns a shallow copy of this tree instance 
	 * (i.e., the elements themselves are not cloned but the nodes are).
	 * This operation should be O(N).
	 * 
	 * @returns a shallow copy of this tree
	 */
	
	public BST<E> clone() {
		BST<E> cloneTree = new BST<E>();
		Iterator<E> iter = this.iterator();
		cloneTree.add(this.first());
		while (iter.hasNext()) {
			cloneTree.add(iter.next());		
		}
		return cloneTree;
	}
	
	
	
	/**
	 * Compares the specified object with this tree for equality. 
	 * Returns true if the given object is also a tree, the two trees have the same size, 
	 * and every member of the given tree is contained in this tree.
	 * This operation should be O(N).
	 * 
	 * Overrides equals in class Object
	 * 
	 * @param obj - object to be compared for equality with this tree
	 * @return true if the specified object is equal to this tree
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BST)) {
			return false;
		}
		if (size != ((BST<E>) obj).size()) {
			return false;
		}
		for (E e : (BST<E>)obj) {
			if (!this.contains(e)) {
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * Returns a string representation of this tree. The string representation consists of 
	 * a list of the tree's elements in the order they are returned by its iterator 
	 * (inorder traversal), enclosed in square brackets ("[]"). Adjacent elements are separated 
	 * by the characters ", " (comma and space). Elements are converted to strings as by 
	 * String.valueOf(Object).
	 * This operation should be O(N).
	 * 
	 * Overrides toString in class Object
	 * 
	 * @return a string representation of this collection
	 */
	
	@Override
	public String toString() {
		if (root == null) {
			return "[]";
		}
		String treeString = "[" + this.first();
		Iterator<E> iter = this.iterator();
		while (iter.hasNext()) {
			treeString += ", " + iter.next();
		}
		treeString += "]";
		return treeString;
	}
	
	
	
	/**
	 * This function returns an array containing all the elements returned by this tree's iterator, 
	 * in the same order, stored in consecutive elements of the array, starting with index 0. 
	 * The length of the returned array is equal to the number of elements returned by the iterator.
	 * This operation should be O(N).
	 * 
	 * @return an array, whose runtime component type is Object, containing all of the elements 
	 * in this tree
	 */
	public Object[] toArray() {
		Object a [] = new Object[size];
		if (root == null) {
			return a;
		}
		Iterator<E> iter = this.iterator();
		a[0] = this.first();
		int i = 1;
		while (iter.hasNext()){
			a[i] = iter.next();
			i++;
		}
		return a;
	}
	
	
	
	/**
	 * Produces tree like string representation of this tree. 
	 * Returns a string representation of this tree in a tree-like format. 
	 * The string representation consists of a tree-like representation of this tree. 
	 * Each node is shown in its own line with the indentation showing the depth of 
	 * the node in this tree. The root is printed on the first line, followed by its 
	 * left subtree, followed by its right subtree.
	 * This operation should be O(N)
	 * 
	 * @return string containing tree-like representation of this tree.
	 */
	public String toStringTreeFormat() {
		Node<E> node = root;
		Node<E> prev = root;
		String treeString = "" + node.data + "\n";
		node = node.left;
		int counter = 0;
		boolean skip = false;
		while (counter < size * 2) {
			if (node == null) {
				for (int i = 0; i < depth(prev) * 3; i ++) {
					treeString += " ";
				}
				treeString += "|--" + null + "\n";
				if (prev.isLeaf && !skip || prev.right != null) {
					node = prev.right;
					skip = true;
				}
				else {
					while (prev.parent != null) {
						if (prev.equals(prev.parent.right)) {
							if (prev.parent.equals(root)) {
								return treeString;
							}
							prev = prev.parent;
							//skip = false;
						}
						else {
							node = prev.parent.right;
							prev = prev.parent;
							break;
						}
					}
				}
			}
			if (node != null) { 
				for (int i = 0; i < (depth(node) - 1) * 3; i++) {
					treeString += " ";
				}
				treeString += "|--" + node.data + "\n";
				prev = node;
				node = node.left;
				skip = false;
			}
			counter++;
		}
		return treeString;
	}
	
	
	
	
	public static class Node<E extends Comparable<E>> {    
		Node<E> left = null;
		Node<E> right = null;
		Node<E> parent = null;
		E data;
		boolean isLeaf;
		int height;
		int depth;
		
		Node ( E data ) {
            this.data = data;
            this.isLeaf = true;
    		this.height = 0;
    		this.depth = 0;
        }
	}
	
	
	
	
	private class Iter implements Iterator<E> { 
		Node<E> node = root;
		int nextIndex = 1;    				// next index after first node would be 1
	
		

		/**
		 * Returns true if the tree has a next value and false otherwise. The nextIndex
		 * is increased for each iteration in the next() method. 
		 * 
		 * @return true if the tree has a next value and false otherwise
		 */
		@Override
		public boolean hasNext() {
			return nextIndex < size;       // the greatest index is size - 1
		}

		/**
		 * Returns the next value in this tree. The next value for this iterator will
		 * be the next highest value. If there is no next value, null is returned.
		 * 
		 * @return the object of the node at the next index
		 */
		@Override
		public E next() {
			if (root == null) {
				return null;
			}
			if (node.equals(root) && nextIndex == 1) {   // traverses to the first node (least node) to begin the iterator
				while (node.left != null) {
					node = node.left;
				}
			}
			if (!this.hasNext()) {   // checks that the node has a next node
				return null;         // returns null if it does not have a next node
			}
			if (node.right != null) {   // moves right, then as far left as it can
				node = node.right;
				while(node.left != null) {
					node = node.left;
				}
				nextIndex++;
				return node.data;
			}
			if (node.right == null) {
				if (node.parent != null) {
					if (node.equals(node.parent.left)) {   // node is a left child, so the next node is its parent
						node = node.parent;
						nextIndex++;
						return node.data;
					}
					if (node.equals(node.parent.right)) {    // node is a right child
						while (node.equals(node.parent.right)) {    // move up until node is a left child
							node = node.parent;
							}
						if (node.parent != null) {              // we have reached the end of the subtree and have returned to the root
							node = node.parent;
							nextIndex++;
							return node.data;
						}
					}
				}
			}
			return null;
	    }
	}
	
	private class PreorderIter implements Iterator<E> {
		Node<E> node = root;
		int nextIndex = 1;    					 // next index after first node would be 1
		
		/**
		 * Returns true if the tree has a next value and false otherwise. The nextIndex
		 * is increased for each iteration in the next() method. 
		 * 
		 * @return true if the tree has a next value and false otherwise
		 */
		@Override
		public boolean hasNext() {
			return nextIndex < size; 			// the greatest index is size - 1
		}

		/**
		 * Returns the next value in this tree. This iterator visits the root first, 
		 * then the left subtree, then the left subtree. If there is no next value, \
		 * null is returned.
		 * 
		 * @return the object of the node at the next index
		 */
		@Override
		public E next() {
			if (root == null) {
				return null;
			}
			if (!this.hasNext()) {   // checks that the node has a next node
				return null;         // returns null if it does not have a next node
			}
			if (node.left != null) {       // if there is a left child, it is the next node
				node = node.left;
				nextIndex++;
				return node.data;
			}
			if (node.right != null) {      // if there is no left child, then if there is a right child, it is the next node
				node = node.right;
				nextIndex++;
				return node.data;
			}
			if (node.equals(node.parent.left)) {     // node is a leaf. if it is a left child, we move up until the parent has a right child
				while (node.parent != null) {
					if (node.parent.right != null) {
						node = node.parent.right;
						nextIndex++;
						return node.data;
					}
					node = node.parent;
				}
			}
			else {   									// leaf is a right child
				while (node.parent != null) {			// we are at the root, so we move to the right of the root if it exists
					if (node.parent.equals(root)) {
						if (root.right != null) {
							node = root.right;
							nextIndex++;
							return node.data;
						}
					}
					while (node.equals(node.parent.right)) {   // move up until node is not a right child
						node = node.parent;
						if (node.equals(node.parent.left)) {   // node is a left child
							if (node.parent.right != null) {   // if parent has a right child, move there 
								node = node.parent.right;
								nextIndex++;
								return node.data;
							}
						}
					}
					node = node.parent;         // this node's parent does not have a right child, so we move up
				}
			}
			return null;
		}
	}
	
	private class PostorderIter implements Iterator<E> {
		Node<E> node = root;
		int nextIndex = 1;    					 // next index after first node would be 1
		
		
		/**
		 * Returns true if the tree has a next value and false otherwise. The nextIndex
		 * is increased for each iteration in the next() method. 
		 * 
		 * @return true if the tree has a next value and false otherwise
		 */
		@Override
		public boolean hasNext() {
			return nextIndex < size;			// the greatest index is size - 1
		}	

		/**
		 * Returns the next value in this tree. The iterator visits a node after it has
		 * visited all of its children. If there is no next value, null is returned.
		 * 
		 * @return the object of the node at the next index
		 */
		@Override
		public E next() {
			if (root == null) {
				return null;
			}
			if (node.equals(root) && nextIndex == 1) {    // moves to the node farthest left
				while (node.left != null) {
					node = node.left;
				}
				if (node.right != null) {             // if farthest left node has a right child, move there
					node = node.right;
				}
			}
			if (!this.hasNext()) {   // checks that the node has a next node
				return null;         // returns null if it does not have a next node
			}
			if (node.equals(node.parent.right)) {        // if node is right child, move to its parent (traversal of that subtree is complete)
				nextIndex++;
				node = node.parent;
				return node.data;
			}
			if (node.equals(node.parent.left)) {         // if node is left child, check if parent has a right subtree, then go as far left as possible
				if (node.parent.right == null) {
					node = node.parent;
					nextIndex++;
					return node.data;
				}
				node = node.parent.right;
				while (node.left != null) {
					node = node.left;
				}
				if (node.right != null) {
					node = node.right;
				}
				nextIndex++;
				return node.data;
			}
			return null;
	    }	
		
	}
}
