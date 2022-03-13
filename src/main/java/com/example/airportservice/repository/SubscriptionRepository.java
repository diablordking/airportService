package com.example.airportservice.repository;

import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * API to delete subscription by icao code
     *
     * @param icaoCode icaoCode
     * @throws MetarException MetarException
     */
    @Modifying
    @Transactional
    @Query("delete from Subscription s where s.icaoCode=:icaoCode")
    void unsubscribe(@Param("icaoCode") String icaoCode) throws MetarException;

    public List<Subscription> findByActive(Boolean isActive);

}
