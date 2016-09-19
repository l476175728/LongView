package com.example.liuhailong.longviewsample.bean;

import java.util.ArrayList;

public class Provinces {
	private String id;
	private String name;
	private ArrayList<Cities> provinces;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Cities> getProvinces() {
		return provinces;
	}
	public void setProvinces(ArrayList<Cities> provinces) {
		this.provinces = provinces;
	}
	@Override
	public String toString() {
		return "Provinces [id=" + id + ", name=" + name + ", provinces="
				+ provinces + "]";
	}
	

}