package org.example.recipe_match_backend.tool.repository;

import org.example.recipe_match_backend.tool.domain.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ToolRepository extends JpaRepository<Tool,Long> {
    Optional<Tool> findByToolName(String toolName);
}
