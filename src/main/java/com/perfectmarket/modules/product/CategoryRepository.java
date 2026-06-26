package com.perfectmarket.modules.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(value = """
    select c from Category c
    where c.parent is null
    """)
    List<Category> findAllRootCategories();

    @Query("""
    SELECT child.id FROM Category c 
    JOIN c.children child 
    WHERE c.id = :id
""")
    List<UUID> findImmediateChildIds(@Param("id") UUID id);
}
