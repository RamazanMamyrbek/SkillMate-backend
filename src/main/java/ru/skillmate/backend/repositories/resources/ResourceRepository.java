package ru.skillmate.backend.repositories.resources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.resources.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
