package org.example.recipe_match_backend.recipe.domain;


import jakarta.persistence.*;
import lombok.*;
import org.example.recipe_match_backend.global.entity.BaseEntity;
import org.example.recipe_match_backend.user.domain.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecipeRating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ratingValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
