package ru.skillmate.backend.services.skills;

import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.skills.response.SkillResponseDto;
import ru.skillmate.backend.entities.skills.Skill;

import java.util.List;

public interface SkillService {
    List<SkillResponseDto> getAllSkillsByUserId(Long userId);

    SkillResponseDto getSkillResponseById(Long skillId);

    Skill getSkillById(Long skillId);

    SkillResponseDto createSkill(Long userId, String name, String description, String level, List<MultipartFile> achievements, String email);

    SkillResponseDto editSkill(Long userId, Long skillId, String name, String description, String level, List<MultipartFile> achievements, String email);

    void deleteSkillById(Long skillId, String email);
}
