package ru.skillmate.backend.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.skills.enums.SkillLevel;

import java.util.ArrayList;
import java.util.List;

public class AdSpecifications {
    public static Specification<Ad> bySearchFilters(String searchValue, List<String> countries, List<String> cities, List<String> levels) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchValue != null && !searchValue.isEmpty()) {
                String lowered = searchValue.toLowerCase();
                Predicate skillNameContains = cb.like(cb.lower(root.get("skillName")), "%" + lowered + "%");
                Predicate descriptionContains = cb.like(cb.lower(root.get("description")), "%" + lowered + "%");

                // Объединяем в приоритетном порядке
                Predicate searchPredicate = cb.or(
                        skillNameContains,
                        descriptionContains
                );

                predicates.add(searchPredicate);
            }

            if (countries != null && !countries.isEmpty()) {
                predicates.add(root.get("country").in(countries));
            }
            if (cities != null && !cities.isEmpty()) {
                predicates.add(root.get("city").in(cities));
            }
            if (levels != null && !levels.isEmpty()) {
                predicates.add(root.get("level").in(levels.stream().map(SkillLevel::valueOf).toList()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
