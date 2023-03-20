package com.m2code.electronic.store.services.impl;

import com.m2code.electronic.store.dtos.CategoryDto;
import com.m2code.electronic.store.dtos.PageableResponse;
import com.m2code.electronic.store.entities.Category;
import com.m2code.electronic.store.exceptions.BadApiRequest;
import com.m2code.electronic.store.exceptions.ResourceNotFoundException;
import com.m2code.electronic.store.helpers.Helper;
import com.m2code.electronic.store.repositories.CategoryRepository;
import com.m2code.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Value("${category.image.path}")
    private String coverPhotoPath;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setId(categoryId);
        Category category = categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not available with id " + categoryId));
        category.setCoverImage(categoryDto.getCoverImage());
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());

        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not available with id " + categoryId));

        String fullCoverPhotoPath = coverPhotoPath.concat(category.getCoverImage());

        Path path = Paths.get(fullCoverPhotoPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new BadApiRequest("Cover photo not Found");
        }

        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        return Helper.getPageableResponse(page, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not available with id " + categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }
}
