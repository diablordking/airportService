
CREATE TABLE IF NOT EXISTS `subscriptions`
(
    `icao_code` VARCHAR(50) PRIMARY KEY,
    `is_active` INT,
    `created_time` TIMESTAMP,
    `last_modified_time` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `station`
(
    `icao_code` VARCHAR(50) PRIMARY KEY,
    `filename` VARCHAR(50) ,
    `created_time` TIMESTAMP,
    `size` INT
);

CREATE TABLE IF NOT EXISTS `metar_metrics`
(
    `icao_code` VARCHAR(50) PRIMARY KEY,
    `timestamp` TIMESTAMP ,
    `wind_strength` VARCHAR(50) ,
    `temperature` VARCHAR(50) ,
    `visibility` VARCHAR(50)
  );

  CREATE TABLE IF NOT EXISTS `metar`
  (
      `icao_code` VARCHAR(50) PRIMARY KEY,
      `data` VARCHAR(300) ,
      `created_time` TIMESTAMP  ,
      `last_modified_time` TIMESTAMP
);