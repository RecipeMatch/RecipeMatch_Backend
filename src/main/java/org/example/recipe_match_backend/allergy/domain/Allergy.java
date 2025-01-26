package org.example.recipe_match_backend.allergy.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.recipe_match_backend.user.domain.UserAllergy;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String allergyName;

    @OneToMany(mappedBy = "allergy", cascade = CascadeType.PERSIST)
    private List<UserAllergy> userAllergies = new ArrayList<>();

}
