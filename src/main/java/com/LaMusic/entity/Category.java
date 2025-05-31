package com.LaMusic.entity;

import java.util.List;
import java.util.UUID;

import com.LaMusic.util.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "tb_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category extends Auditable{

	@Id
	@GeneratedValue
	private UUID id;
	
	private String name;
	
	private String slug;
	
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	private Integer sortOrder;
	
	private boolean isActive;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "categories")
	private List<Product> products;
	
}
