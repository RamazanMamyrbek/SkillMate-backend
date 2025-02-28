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
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.exceptions.ResourcesNotMatchingException;
import ru.skillmate.backend.mappers.skills.SkillMapper;
import ru.skillmate.backend.repositories.skills.SkillRepository;
import ru.skillmate.backend.services.resources.ResourceService;
import ru.skillmate.backend.services.skills.SkillService;
import ru.skillmate.backend.services.users.UsersService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final ResourceService resourceService;
    private final UsersService usersService;
    private final SkillMapper skillMapper;
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
    public SkillResponseDto createSkill(Long userId, String name, String description, String level, List<MultipartFile> achievements, String email) {
        Users user = (Users) usersService.getUserByUsername(email);
        Skill skill = Skill
                .builder()
                .name(name)
                .description(description)
                .level(SkillLevel.valueOf(level))
                .user(user)
                .build();
        if(achievements != null && !achievements.isEmpty()) {
            skill.setAchievements(saveAchievements(achievements));
        }
        return skillMapper.mapToResponseDto(skillRepository.save(skill));
    }

    @Override
    @Transactional
    public SkillResponseDto editSkill(Long userId, Long skillId, String name, String description, String level, List<MultipartFile> achievements, String email) {
        Skill skill = getSkillById(skillId);
        Users user = (Users) usersService.getUserByUsername(email);
        checkSkillBelongsToUser(skill, user);
        skill.setName(name);
        skill.setDescription(description);
        skill.setLevel(SkillLevel.valueOf(level));
        skill.getAchievements().forEach(achievement -> resourceService.deleteResource(achievement.getId()));
        if(achievements != null && !achievements.isEmpty()) {
            skill.setAchievements(saveAchievements(achievements));
        } else {
            skill.setAchievements(new ArrayList<>());
        }
        return skillMapper.mapToResponseDto(skillRepository.save(skill));
    }

    @Override
    @Transactional
    public void deleteSkillById(Long skillId, String email) {
        Skill skill = getSkillById(skillId);
        checkSkillBelongsToUser(skill, (Users) usersService.getUserByUsername(email));
        skill.getAchievements().forEach(achievement -> resourceService.deleteResource(achievement.getId()));
        skillRepository.deleteById(skillId);
    }

    private void checkSkillBelongsToUser(Skill skill, Users user) {
        if(!skill.getUser().equals(user)) {
            throw ResourcesNotMatchingException.userIdAndSkillIdNotMathing(user.getId(), skill.getId());
        }
    }

    private List<Resource> saveAchievements(List<MultipartFile> achievements) {
        return achievements
                .stream()
                .map(achievement -> {
                            resourceService.checkFile(achievement);
                            return resourceService.saveResource(achievement, folderSkillAchievements);
                        }

                )
                .collect(Collectors.toList());
    }
}