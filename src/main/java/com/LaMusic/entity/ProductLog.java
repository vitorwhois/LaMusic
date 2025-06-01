package com.LaMusic.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLog {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private String action;

//    @Column(name = "old_values", columnDefinition = "jsonb")
//    private String oldValues;
//
//    @Column(name = "new_values", columnDefinition = "jsonb")
//    private String newValues;

    @ManyToOne
    @JoinColumn(name = "responsible_user_id")
    private User responsibleUser;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;
}
