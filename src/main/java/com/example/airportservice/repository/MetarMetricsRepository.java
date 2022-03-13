package com.example.airportservice.repository;

import com.example.airportservice.models.MetarMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetarMetricsRepository extends JpaRepository<MetarMetrics, Long> {

    /**
     * API to find MetarMetrics data by icaoCode
     *
     * @param icaoCode icaoCode
     * @return MetarMetrics MetarMetrics
     */
    MetarMetrics findByIcaoCode(String icaoCode);

}
