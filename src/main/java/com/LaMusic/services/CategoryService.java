package com.LaMusic.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.LaMusic.controllers.dto.CategoryDto;
import com.LaMusic.entity.Category;
import com.LaMusic.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryService {

	CategoryRepository categoryRepository;
	
	public List<Category> listCategories(){
		return categoryRepository.findAll();
	}
	
	public Category findCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("Category not found!")) ;	
	}
	
	public Category createCategory(CategoryDto categoryDto) {
		return categoryRepository.save(categoryDto.toCategory());
	}
	
	public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = findCategoryById(id);
        existingCategory.setName(updatedCategory.getName());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

	public List<Category> getCategoriesByIds(List<Long> ids ) {
		return categoryRepository.findAllById(ids);
	}
    
	
}
