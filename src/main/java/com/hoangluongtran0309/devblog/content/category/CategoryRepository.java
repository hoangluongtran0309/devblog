package com.hoangluongtran0309.devblog.content.category;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.shared.Slug;

@Repository
@Transactional(readOnly = true)
public interface CategoryRepository extends CrudRepository<Category, CategoryId>, CategoryRepositoryCustom,
        PagingAndSortingRepository<Category, CategoryId> {

    boolean existsByCategorySlug(Slug categorySlug);

    boolean existsByCategoryName(CategoryName categoryName);

    Optional<Category> findByCategorySlug(Slug categorySlug);
}
