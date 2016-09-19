package com.example.liuhailong.longviewsample.bean;

import java.util.ArrayList;

public class Cities {
	private String id;
	private String name;
	private ArrayList<Countries> cities;
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
	public ArrayList<Countries> getCities() {
		return cities;
	}
	public void setCities(ArrayList<Countries> cities) {
		this.cities = cities;
	}
	@Override
	public String toString() {
		return "Cities [id=" + id + ", name=" + name + ", cities=" + cities
				+ "]";
	}
	
	
}

