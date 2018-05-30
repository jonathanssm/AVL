package com.avl;

public class No<K, V> {

	private No<K, V> left;
	private No<K, V> right;
	private No<K, V> father;
	private K key;
	private V value;
	private int balance;

	public No(K key, V value) {
		setLeft(setRight(setFather(null)));
		setBalance(0);
		setKey(key);
		setValue(value);
	}

	@Override
	public String toString() {
		return "No [key=" + key + ", value=" + value + "]";
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balanceamento) {
		this.balance = balanceamento;
	}

	public No<K, V> getFather() {
		return father;
	}

	public No<K, V> setFather(No<K, V> father) {
		this.father = father;
		return father;
	}

	public No<K, V> getRight() {
		return right;
	}

	public No<K ,V> setRight(No<K, V> right) {
		this.right = right;
		return right;
	}

	public No<K, V> getLeft() {
		return left;
	}

	public void setLeft(No<K, V> left) {
		this.left = left;
	}
	
}
