package org.example.recipe_match_backend.tool.repository;

import org.example.recipe_match_backend.tool.domain.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool,Long> {
    Tool findByToolName(String toolName);
}
