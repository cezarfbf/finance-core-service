package com.cezarfbf.finance.core.domain.category;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> list(CategoryContext context) {
        // categories.context is a native Postgres enum; filtering in Java avoids the
        // "operator does not exist: category_context = character varying" comparison. The
        // category set is tiny, so the full scan is negligible.
        return repository.findAll().stream()
            .filter(c -> c.getArchivedAt() == null && c.getContext() == context)
            .sorted(Comparator.comparingInt(Category::getSortOrder))
            .map(CategoryDto::from)
            .toList();
    }
}
