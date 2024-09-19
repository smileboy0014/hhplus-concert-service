package com.hhplus.hhplusconcertservice.infra.concert;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long> {

}
