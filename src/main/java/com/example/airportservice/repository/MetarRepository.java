package com.example.airportservice.repository;

import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Metar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MetarRepository extends JpaRepository<Metar, Long> {

    /**
     * API to find Metar data by icaoCode
     *
     * @param icaoCode icaoCode
     * @return Metar Metar
     */
    Metar findByIcaoCode(String icaoCode);

    /**
     * API to unsubscribe station metar data
     *
     * @param icaoCode icaoCode
     * @throws MetarException MetarException
     */
    @Modifying
    @Transactional
    @Query("delete from Metar m where m.icaoCode=:icaoCode")
    void unsubscribe(@Param("icaoCode") String icaoCode) throws MetarException;
}

