package ru.skillmate.backend.repositories.ads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.UserAdView;
import ru.skillmate.backend.entities.users.Users;

import java.util.List;

@Repository
public interface UserAdViewRepository extends JpaRepository<UserAdView, Long> {
    boolean existsByUserAndAd(Users user, Ad ad);

    List<UserAdView> findAllByUser(Users user);
}
