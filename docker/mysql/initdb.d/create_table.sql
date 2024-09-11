CREATE DATABASE IF NOT EXISTS concert DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;;
ALTER DATABASE concert CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

USE concert;

-- place 테이블 생성
CREATE TABLE IF NOT EXISTS place
(
    place_id   BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    total_seat INT    NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (place_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- concert 테이블 생성
CREATE TABLE IF NOT EXISTS concert
(
    concert_id BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
#     place_id   BIGINT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (concert_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


-- concert_date 테이블 생성
CREATE TABLE IF NOT EXISTS concert_date
(
    concert_date_id BIGINT NOT NULL AUTO_INCREMENT,
    concert_id      BIGINT,
    place_id        BIGINT,
    concert_date    VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    PRIMARY KEY (concert_date_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE INDEX IDX_CONCERT_DATE ON concert_date (concert_id, concert_date);

-- seat 테이블 생성
CREATE TABLE IF NOT EXISTS seat
(
    seat_id         BIGINT NOT NULL AUTO_INCREMENT,
    concert_date_id BIGINT,
    seat_number     INT    NOT NULL,
    price           DECIMAL(38, 2),
    status          ENUM ('AVAILABLE','UNAVAILABLE'),
    version         INT    NOT NULL DEFAULT 1,
    ticket_class    ENUM ('S','A','B','C'),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    PRIMARY KEY (seat_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE INDEX IDX_SEAT_STATUS ON seat (concert_date_id, status); # 성능 향상을 위한 복합 인덱스 추가
CREATE INDEX IDX_SEAT_COVERING ON seat (concert_date_id, status, seat_id, seat_number, price, created_at, updated_at,
                                        ticket_class, version);
# 성능 향상을 위한 커버링 인덱스 추가

-- reservation 테이블 생성
CREATE TABLE IF NOT EXISTS reservation
(
    reservation_id  BIGINT NOT NULL AUTO_INCREMENT,
    concert_id      BIGINT,
    concert_date_id BIGINT,
    seat_id         BIGINT,
    user_id         BIGINT,
    concert_name    VARCHAR(255),
    concert_date    VARCHAR(255),
    seat_number     INT    NOT NULL,
    seat_price      DECIMAL(38, 2),
    status          ENUM ('CANCEL','TEMPORARY_RESERVED','COMPLETED'),
    reserved_at     DATETIME(6),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    PRIMARY KEY (reservation_id),
    UNIQUE KEY unique_reservation (concert_id, concert_date_id, seat_number)
) ENGINE = InnoDB;
