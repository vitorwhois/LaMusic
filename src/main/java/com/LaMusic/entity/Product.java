package com.LaMusic.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.LaMusic.util.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product extends Auditable{

	@Id
	@GeneratedValue
	private UUID id;
	
	private String name;
	private String description;
	private BigDecimal price;
	private Integer stock;	
	private String imageUrl;
	
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "product_category",
			joinColumns = @JoinColumn(name = "product_id"),
			inverseJoinColumns = @JoinColumn (name = "category_id")			
			)
	private List<Category> categories;
		
	
}
