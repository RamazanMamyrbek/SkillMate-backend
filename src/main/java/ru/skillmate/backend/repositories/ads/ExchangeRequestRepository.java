package ru.skillmate.backend.repositories.ads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.ExchangeRequest;
import ru.skillmate.backend.entities.users.Users;

import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    boolean existsByRequesterAndAd(Users requester, Ad ad);

    List<ExchangeRequest> findAllByRequester(Users requester);

    List<ExchangeRequest> findAllByReceiver(Users receiver);

    boolean existsByIdAndRequester(Long requestId, Users requester);
}
