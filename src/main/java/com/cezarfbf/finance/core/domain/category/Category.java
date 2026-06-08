package com.cezarfbf.finance.core.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryContext context;

    private String code;

    @Column(nullable = false)
    private String name;

    private String icon;

    private String color;

    @Column(nullable = false)
    private Integer sortOrder;

    private UUID parentId;

    private OffsetDateTime archivedAt;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    private Long version;
}
