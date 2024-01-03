
package com.capgemini.ocean.institute.training.entity;
 
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
 
@Entity

public class Place {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long placeID;

	private String name;

	private String address;

	private String area;

	private double distance;

	private String description;

	private String tag;

	private String image;

	public Place(Long placeID, String name, String address, String area, double distance, String description,

			String tag,String image) {

		super();

		this.placeID = placeID;

		this.name = name;

		this.address = address;

		this.area = area;

		this.distance = distance;

		this.description = description;

		this.tag = tag;

		this.image=image;

	}

	public Place() {

		super();

	}

	public Long getPlaceID() {

		return placeID;

	}

	public void setPlaceID(Long placeID) {

		this.placeID = placeID;

	}

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public String getAddress() {

		return address;

	}

	public void setAddress(String address) {

		this.address = address;

	}

	public String getArea() {

		return area;

	}

	public void setArea(String area) {

		this.area = area;

	}

	public double getDistance() {

		return distance;

	}

	public void setDistance(double distance) {

		this.distance = distance;

	}

	public String getDescription() {

		return description;

	}

	public void setDescription(String description) {

		this.description = description;

	}

	public String getTag() {

		return tag;

	}

	public void setTag(String tag) {

		this.tag = tag;

	}

	public String getImage() {

		return image;

	}

	public void setImage(String image) {

		this.image = image;

	}

	@Override

	public String toString() {

		return "Place [placeID=" + placeID + ", name=" + name + ", address=" + address + ", area=" + area

				+ ", distance=" + distance + ", description=" + description + ", tag=" + tag + ", image=" + image + "]";

	}




 
}
