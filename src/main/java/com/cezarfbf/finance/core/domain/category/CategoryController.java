package com.cezarfbf.finance.core.domain.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Category lookups")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "List categories for a context",
        description = "Active (non-archived) categories for the given context, ordered by sort order."
    )
    public ResponseEntity<List<CategoryDto>> list(
        @Parameter(description = "Context: PERSONAL or BUSINESS") @RequestParam CategoryContext context
    ) {
        return ResponseEntity.ok(service.list(context));
    }
}
