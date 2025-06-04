package com.LaMusic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.stereotype.Service;

import com.LaMusic.Mappers.CategoryMapper;
import com.LaMusic.dto.CategoryDTO;
import com.LaMusic.entity.Category;
import com.LaMusic.entity.Product;
import com.LaMusic.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Category>findAll2(){
    	return categoryRepository.findAll();
    }
    
    public CategoryDTO findById(UUID id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
    
    public Category findById2(UUID id) {
    	return categoryRepository.findById(id)
    			.orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryDTO create(CategoryDTO dto) {
        Category parent = dto.getParentId() != null
                ? categoryRepository.findById(dto.getParentId()).orElse(null)
                : null;

        Category category = CategoryMapper.toEntity(dto, parent);
        category = categoryRepository.save(category);
        return CategoryMapper.toDTO(category);
    }

    public CategoryDTO update(UUID id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Category parent = dto.getParentId() != null
                ? categoryRepository.findById(dto.getParentId()).orElse(null)
                : null;

        existing.setName(dto.getName());
        existing.setSlug(dto.getSlug());
        existing.setDescription(dto.getDescription());
        existing.setParent(parent);
        existing.setSortOrder(dto.getSortOrder());
        existing.setActive(dto.isActive());

        return CategoryMapper.toDTO(categoryRepository.save(existing));
    }

    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }

	public List<Category> getCategoriesByIds(List<UUID> categoryIds) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Product> getProductsByCategoryId(UUID id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("Category not Found !"));
		return category.getProducts();
	}
}
