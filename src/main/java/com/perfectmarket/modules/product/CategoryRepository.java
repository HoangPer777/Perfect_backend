package com.perfectmarket.modules.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("""
    select c from Category c 
    where not exists (
        select 1 from Category child where child.parent = c
    )
""")
    List<Category> findAllLeafCategories();
}
