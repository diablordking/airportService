package com.example.airportservice.repository;

import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    /**
     * API to find Station by icao code
     *
     * @param icaoCode icaoCode
     * @return Station Station
     */
    Station findByIcaoCode(String icaoCode);

    /**
     * API to find station starting with provided keyword
     *
     * @param startsWith startsWith
     * @return Station list
     */
    List<Station> findByIcaoCodeStartsWith(String startsWith);

    /**
     * API to delete station by icao code
     *
     * @param icaoCode icaoCode
     * @throws MetarException MetarException
     */
    @Modifying
    @Transactional
    @Query("delete from Station s where s.icaoCode=:icaoCode")
    void delete(@Param("icaoCode") String icaoCode) throws MetarException;

}
