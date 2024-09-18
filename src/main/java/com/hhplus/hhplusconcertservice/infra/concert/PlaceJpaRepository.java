package com.hhplus.hhplusconcertservice.infra.concert;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, Long> {

    void deleteAllInBatch();
}
