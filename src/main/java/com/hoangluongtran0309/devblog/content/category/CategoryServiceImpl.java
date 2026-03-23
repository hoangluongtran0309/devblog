package com.hoangluongtran0309.devblog.content.category;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.shared.Slug;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(Slug categorySlug) {
        return categoryRepository.existsByCategorySlug(categorySlug);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(CategoryName categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getBySlug(Slug categorySlug) {
        return categoryRepository.findByCategorySlug(categorySlug);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getById(CategoryId categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category create(CategoryParameters parameters) {
        logger.info("Creating category with name: {}", parameters.getCategoryName());

        validateForCreate(parameters);

        Category category = new Category(
                categoryRepository.nextId(),
                parameters.getCategoryName(),
                parameters.getCategoryIcon(),
                parameters.getCategorySlug());

        logger.info("Category created with ID: {}", category.getId().asString());

        return categoryRepository.save(category);
    }

    @Override
    public Category update(CategoryId categoryId, CategoryParameters parameters) {
        logger.info("Updating category with ID: {}", categoryId.asString());

        Category category = getById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        validateForUpdate(category, parameters);

        category.setCategoryName(parameters.getCategoryName());
        category.setCategoryIcon(parameters.getCategoryIcon());
        category.setCategorySlug(parameters.getCategorySlug());

        logger.info("Category updated with ID: {}", categoryId.asString());

        return categoryRepository.save(category);
    }

    @Override
    public void delete(CategoryId categoryId) {
        logger.info("Deleting category with ID: {}", categoryId.asString());

        Category category = getById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        categoryRepository.delete(category);

        logger.info("Category deleted with ID: {}", categoryId.asString());
    }

    private void validateForCreate(CategoryParameters parameters) {
        if (existsBySlug(parameters.getCategorySlug())) {
            throw new CategoryAlreadyExistsException(parameters.getCategorySlug());
        }

        if (existsByName(parameters.getCategoryName())) {
            throw new CategoryAlreadyExistsException(parameters.getCategoryName());
        }
    }

    private void validateForUpdate(Category category, CategoryParameters parameters) {
        if (!category.getCategorySlug().equals(parameters.getCategorySlug())
                && existsBySlug(parameters.getCategorySlug())) {
            throw new CategoryAlreadyExistsException(parameters.getCategorySlug());
        }

        if (!category.getCategoryName().equals(parameters.getCategoryName())
                && existsByName(parameters.getCategoryName())) {
            throw new CategoryAlreadyExistsException(parameters.getCategoryName());
        }
    }
}