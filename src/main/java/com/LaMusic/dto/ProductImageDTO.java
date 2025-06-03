package com.LaMusic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
	private String url;
	private String altText;
	private Integer sortOrder;
	private Boolean isPrimary;
}
