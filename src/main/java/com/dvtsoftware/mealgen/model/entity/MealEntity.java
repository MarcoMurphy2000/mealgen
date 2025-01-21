package com.dvtsoftware.mealgen.model.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "meals",
//        uniqueConstraints = {
//                @UniqueConstraint(name = "uk_recipe_name", columnNames = {"recipe_name"})
//        },
        indexes = {
                @Index(name = "idx_category", columnList = "category")
        })
@NamedEntityGraphs({
        @NamedEntityGraph(name = "MealDefault",
                attributeNodes = {
                        @NamedAttributeNode("calories"),
                        @NamedAttributeNode("category")
                }),
        @NamedEntityGraph(name = "MealWithAllDetails",
                attributeNodes = {
                        @NamedAttributeNode("calories"),
                        @NamedAttributeNode("category"),
                        @NamedAttributeNode("protein"),
                        @NamedAttributeNode("carbohydrates"),
                        @NamedAttributeNode("fat")
                })
})
@NoArgsConstructor
@AllArgsConstructor
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ElementCollection
    private List<String> ingredients;

    @Column(name = "protein", nullable = false)
    private double protein;

    @Column(name = "carbohydrates", nullable = false)
    private double carbohydrates;

    @Column(name = "fat", nullable = false)
    private double fat;

    @Column(name = "calories", nullable = false)
    private double calories;

    @Column(name = "category", nullable = false)
    private String category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeEntity recipe;
}