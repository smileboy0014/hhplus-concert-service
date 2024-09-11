USE concert;

-- Create Place
INSERT INTO place (name, total_seat, created_at, updated_at)
VALUES ('Seoul Big Park', 50, NOW(), NOW());
SET @last_place_id = LAST_INSERT_ID();

-- Create Concert
INSERT INTO concert (name, created_at, updated_at)
VALUES ('Psy concert', NOW(), NOW());
SET @last_concert_id = LAST_INSERT_ID();

-- Create Concert Dates
INSERT INTO concert_date (concert_id, place_id, concert_date, created_at, updated_at)
VALUES (@last_concert_id, @last_place_id, '2024-10-25', NOW(), NOW()),
       (@last_concert_id, @last_place_id, '2024-10-26', NOW(), NOW());
SET @last_concert_date_id = LAST_INSERT_ID();

-- Create Seats
INSERT INTO seat (concert_date_id, seat_number, price, ticket_class, status, created_at, updated_at)
VALUES
    -- C class seats
    (@last_concert_date_id, 1, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 2, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 3, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 4, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 5, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 6, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 7, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 8, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 9, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 10, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 11, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 12, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 13, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 14, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 15, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 16, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 17, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 18, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 19, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 20, 120000, 'C', 'UNAVAILABLE', NOW(), NOW()),
    -- B class seats
    (@last_concert_date_id, 21, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 22, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 23, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 24, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 25, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 26, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 27, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 28, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 29, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 30, 150000, 'B', 'UNAVAILABLE', NOW(), NOW()),
    -- A class seats
    (@last_concert_date_id, 31, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 32, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 33, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 34, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 35, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 36, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 37, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 38, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 39, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 40, 170000, 'A', 'UNAVAILABLE', NOW(), NOW()),
    -- S class seats
    (@last_concert_date_id, 41, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 42, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 43, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 44, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 45, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 46, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 47, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 48, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 49, 190000, 'S', 'AVAILABLE', NOW(), NOW()),
    (@last_concert_date_id, 50, 190000, 'S', 'AVAILABLE', NOW(), NOW());
