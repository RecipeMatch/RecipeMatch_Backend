package org.example.recipe_match_backend.domain.tool.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.recipe_match_backend.domain.recipe.domain.RecipeTool;
import org.example.recipe_match_backend.domain.user.domain.UserTool;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toolName;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.PERSIST)
    private List<RecipeTool> recipeTools = new ArrayList<>();

    @OneToMany(mappedBy = "tool", cascade = CascadeType.PERSIST)
    private List<UserTool> userTools = new ArrayList<>();

}
