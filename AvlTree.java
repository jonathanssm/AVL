package com.avl;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AvlTree<K extends Comparable<K>, V> implements Iterable<V> {

	private No<K, V> root;
	private int lenght;
	
	public AvlTree() {
		this.root = null;
		this.lenght = 0;
	}

	public void insert(K key, V value) {
		No<K, V> n = new No<K, V>(key, value);
		insertAVL(this.root, n);
	}

	private void insertAVL(No<K, V> toCompare, No<K, V> toInsert) {

		if (toCompare == null) {
			this.root = toInsert;

		} else {

			if (toInsert.getKey().compareTo(toCompare.getKey()) < 0) {

				if (toCompare.getLeft() == null) {
					toCompare.setLeft(toInsert);
					toInsert.setFather(toCompare);
					verifBalance(toCompare);

				} else {
					insertAVL(toCompare.getLeft(), toInsert);
				}

			} else if (toInsert.getKey().compareTo(toCompare.getKey()) > 0) {

				if (toCompare.getRight() == null) {
					toCompare.setRight(toInsert);
					toInsert.setFather(toCompare);
					verifBalance(toCompare);

				} else {
					insertAVL(toCompare.getRight(), toInsert);
				}

			} else {
				// The node exist
			}
		}
		
		lenght++;
		
	}

	private void verifBalance(No<K, V> actual) {
		setBalance(actual);
		int balance = actual.getBalance();

		if (balance == -2) {

			if (height(actual.getLeft().getLeft()) >= height(actual.getLeft().getRight())) {
				actual = rightRotation(actual);

			} else {
				actual = doubleRotationLeftRight(actual);
			}

		} else if (balance == 2) {

			if (height(actual.getRight().getRight()) >= height(actual.getRight().getLeft())) {
				actual = leftRotation(actual);

			} else {
				actual = doubleRotationRightLeft(actual);
			}
		}

		if (actual.getFather() != null) {
			verifBalance(actual.getFather());
		} else {
			this.root = actual;
		}
	}

	public void remove(K k) {
		removeAVL(this.root, k);
	}

	private void removeAVL(No<K, V> actual, K k) {
		if (actual == null) {
			return;

		} else {

			if (actual.getKey().compareTo(k) > 0) {
				removeAVL(actual.getLeft(), k);

			} else if (actual.getKey().compareTo(k) < 0) {
				removeAVL(actual.getRight(), k);

			} else if (actual.getKey() == k) {
				removeFoundNode(actual);
			}
		}
		
		lenght--;
		
	}

	private void removeFoundNode(No<K, V> toRemove) {
		No<K, V> r;

		if (toRemove.getLeft() == null || toRemove.getRight() == null) {

			if (toRemove.getFather() == null) {
				this.root = null;
				toRemove = null;
				return;
			}
			r = toRemove;

		} else {
			r = sucessor(toRemove);
			toRemove.setKey(r.getKey());
			toRemove.setValue(r.getValue());
		}

		No<K, V> p;
		if (r.getLeft() != null) {
			p = r.getLeft();
		} else {
			p = r.getRight();
		}

		if (p != null) {
			p.setFather(r.getFather());
		}

		if (r.getFather() == null) {
			this.root = p;
		} else {
			if (r == r.getFather().getLeft()) {
				r.getFather().setLeft(p);
			} else {
				r.getFather().setRight(p);
			}
			verifBalance(r.getFather());
		}
		r = null;
	}

	private No<K, V> leftRotation(No<K, V> initial) {

		No<K, V> right = initial.getRight();
		right.setFather(initial.getFather());

		initial.setRight(right.getLeft());

		if (initial.getRight() != null) {
			initial.getRight().setFather(initial);
		}

		right.setLeft(initial);
		initial.setFather(right);

		if (right.getFather() != null) {

			if (right.getFather().getRight() == initial) {
				right.getFather().setRight(right);
			
			} else if (right.getFather().getLeft() == initial) {
				right.getFather().setLeft(right);
			}
		}

		setBalance(initial);
		setBalance(right);

		return right;
	}

	private No<K, V> rightRotation(No<K, V> initial) {

		No<K, V> left = initial.getLeft();
		left.setFather(initial.getFather());

		initial.setLeft(left.getRight());

		if (initial.getLeft() != null) {
			initial.getLeft().setFather(initial);
		}

		left.setRight(initial);
		initial.setFather(left);

		if (left.getFather() != null) {

			if (left.getFather().getRight() == initial) {
				left.getFather().setRight(left);
			
			} else if (left.getFather().getLeft() == initial) {
				left.getFather().setLeft(left);
			}
		}

		setBalance(initial);
		setBalance(left);

		return left;
	}

	private No<K, V> doubleRotationLeftRight(No<K, V> initial) {
		initial.setLeft(leftRotation(initial.getLeft()));
		return rightRotation(initial);
	}

	private No<K, V> doubleRotationRightLeft(No<K, V> inicial) {
		inicial.setRight(rightRotation(inicial.getRight()));
		return leftRotation(inicial);
	}

	private No<K, V> sucessor(No<K, V> q) {
		if (q.getRight() != null) {
			No<K, V> r = q.getRight();
			while (r.getLeft() != null) {
				r = r.getLeft();
			}
			return r;
		} else {
			No<K, V> p = q.getFather();
			while (p != null && q == p.getRight()) {
				q = p;
				p = q.getFather();
			}
			return p;
		}
	}

	private int height(No<K, V> actual) {
		if (actual == null) {
			return -1;
		}

		if (actual.getLeft() == null && actual.getRight() == null) {
			return 0;
		
		} else if (actual.getLeft() == null) {
			return 1 + height(actual.getRight());
		
		} else if (actual.getRight() == null) {
			return 1 + height(actual.getLeft());
		
		} else {
			return 1 + Math.max(height(actual.getLeft()), height(actual.getRight()));
		}
	}

	private void setBalance(No<K, V> no) {
		no.setBalance(height(no.getRight()) - height(no.getLeft()));
	}

	private class TreeIterator implements Iterator<V> {

		private Queue<No<K, V>> queue;

		public TreeIterator() {
			queue = new LinkedList<>();
			queue.add(root);
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public V next() {

			No<K, V> node = queue.poll();

			if (node.getLeft() != null) {
				queue.add(node.getLeft());
			}
			if (node.getRight() != null) {
				queue.add(node.getRight());
			}

			return node.getValue();
		}
	}

	@Override
	public Iterator<V> iterator() {

		return new TreeIterator();
	}

	public void clear() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public boolean isLeaf(No<K, V> node) {
		return node.getLeft() == null && node.getRight() == null;
	}

	public boolean isRoot(No<K, V> node) {
		return node.getFather() == null;
	}

	public int getQtdNode() {
		return getQtdNode(root, 0);
	}

	private int getQtdNode(No<K, V> root, int qtd) {
		if (root == null) {
			return qtd;
		}
		int leftQtdNode = getQtdNode(root.getLeft(), qtd + 1);
		int rightQtdNode = getQtdNode(root.getRight(), qtd + 1);
		return leftQtdNode + rightQtdNode;
	}
	
	public No<K, V> getRootNode() {
		return root;
	}
	
	public V search(K el) {
		No<K, V> node = search(root, el);
		return node != null ? node.getValue() : null;
	}

	private No<K, V> search(No<K, V> p, K el) {
		while (p != null) {

			if (el.compareTo(p.getKey()) == 0)
				return p;

			else if (el.compareTo(p.getKey()) < 0)
				return this.search(p.getLeft(), el);

			else
				return this.search(p.getRight(), el);
		}

		return null;
	}
	
	public boolean contais(V value) {

		if (lenght == 0) {
			throw new EmptyStackException();
		}

		Queue<No<K, V>> queue = new LinkedList<>();
		queue.add(root);
		No<K, V> node = root;

		while (!queue.isEmpty() && !node.getValue().equals(value)) {

			node = queue.poll();

			if (node.getLeft() != null) {
				queue.add(node.getLeft());
			}
			if (node.getRight() != null) {
				queue.add(node.getRight());
			}
		}

		return node.getValue().equals(value);
	}
	
	public void inWidth() {

		Queue<No<K, V>> queue = new LinkedList<No<K, V>>();
		
		if (root == null) {
			System.out.println("Empity Tree");
		} else {
			queue.clear();
			queue.add(root);
			queue.add(null);

			while (!queue.isEmpty()) {

				No<K, V> node = queue.remove();

				if (node == null) {
					if (queue.size() == 0) {
						return;
					}
					queue.add(null);
					System.out.println();
					continue;
				}

				System.out.print(node.getValue() + " ");

				if (node.getLeft() != null) {
					queue.add(node.getLeft());
				}
				if (node.getRight() != null) {
					queue.add(node.getRight());
				}
			}
		}
	}

	public void inorder() {
		inorder(root);
	}

	private void inorder(No<K, V> p) {
		if (p != null) {
			inorder(p.getLeft());
			System.out.print(p.getKey() + " ");
			inorder(p.getRight());
		}
	}
	
	public void preorder() {
		preorder(root);
	}

	private void preorder(No<K, V> p) {
		if (p != null) {
			System.out.print(p.getKey() + " ");
			preorder(p.getLeft());
			preorder(p.getRight());
		}
	}

	public void postorder() {
		postorder(root);
	}

	private void postorder(No<K, V> p) {
		if (p != null) {
			postorder(p.getLeft());
			postorder(p.getRight());
			System.out.print(p.getKey() + " ");
		}
	}

	public List<V> symmetrical() {
		List<V> list = new ArrayList<>();
		symmetrical(root, list);
		return list;
	}

	private void symmetrical(No<K, V> node, List<V> list) {
		if (node != null) {
			symmetrical(node.getLeft(), list);
			list.add(node.getValue());
			symmetrical(node.getRight(), list);
		}
	}

}
