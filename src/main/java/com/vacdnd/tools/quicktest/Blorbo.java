package com.vacdnd.tools.quicktest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Blorbo {
	@SequenceGenerator(name="defSequence", allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
					generator = "defSequence")
	private long id;
	
	String name;
s
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
