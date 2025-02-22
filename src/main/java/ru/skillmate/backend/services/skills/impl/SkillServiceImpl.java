package ru.skillmate.backend.services.skills.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.skills.response.SkillResponseDto;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.skills.Skill;
import ru.skillmate.backend.entities.skills.enums.SkillLevel;
import ru.skillmate.backend.exceptions.FileException;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.mappers.skills.SkillMapper;
import ru.skillmate.backend.repositories.skills.SkillRepository;
import ru.skillmate.backend.services.resources.ResourceService;
import ru.skillmate.backend.services.skills.SkillService;
import ru.skillmate.backend.services.users.UsersService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final ResourceService resourceService;
    private final UsersService usersService;
    private final SkillMapper skillMapper;
    @Value("${minio.allowedTypes.allowedAchievementTypes}")
    private Set<String> allowedAchievementTypes;
    @Value("${minio.folders.skillAchievements}")
    private String folderSkillAchievements;
    @Override
    public List<SkillResponseDto> getAllSkillsByUserId(Long userId) {
        List<Skill> skillList;
        if(userId != null) {
            skillList = skillRepository.findAllByUserId(userId);
        } else {
            skillList = skillRepository.findAll();
        }
        return skillMapper.mapToResponseDtoList(skillList);
    }

    @Override
    public SkillResponseDto getSkillResponseById(Long skillId) {
        Skill skill = getSkillById(skillId);
        return skillMapper.mapToResponseDto(skill);
    }

    @Override
    public Skill getSkillById(Long skillId) {
        return skillRepository.findById(skillId).orElseThrow(() -> ResourceNotFoundException.skillNotFound(skillId));
    }

    @Override
    @Transactional
    public SkillResponseDto createSkill(Long userId, String name, String description, String level, List<MultipartFile> achievements) {
        Skill skill = Skill
                .builder()
                .name(name)
                .description(description)
                .level(SkillLevel.valueOf(level))
                .user(usersService.getUserById(userId))
                .build();
        if(achievements != null && !achievements.isEmpty()) {
            skill.setAchievements(saveAchievements(achievements));
        }
        return skillMapper.mapToResponseDto(skillRepository.save(skill));
    }

    private List<Resource> saveAchievements(List<MultipartFile> achievements) {
        return achievements
                .stream()
                .map(achievement -> {
                            checkFile(achievement);
                            return resourceService.saveResource(achievement, folderSkillAchievements);
                        }

                )
                .toList();
    }

    private void checkFile(MultipartFile file) {
        if (file == null) {
            log.error("File is null");
            throw new FileException("File is null");
        }
        if (file.isEmpty()) {
            log.error("File is empty");
            throw new FileException("File is empty");
        }
        if (file.getOriginalFilename() == null) {
            log.error("File has no original filename");
            throw new FileException("File has no original filename");
        }
        if (file.getContentType() == null) {
            log.error("File has no content type");
            throw new FileException("File has no content type");
        }
        if (!allowedAchievementTypes.contains(file.getContentType())) {
            log.error("Invalid file type: {}", file.getContentType());
            throw new FileException("Invalid file type. Submit only image/png,image/jpeg,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/msword files");
        }
    }


}