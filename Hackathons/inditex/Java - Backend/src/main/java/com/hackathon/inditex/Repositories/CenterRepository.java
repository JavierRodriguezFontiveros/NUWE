package com.hackathon.inditex.Repositories;

import com.hackathon.inditex.Entities.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {
    boolean existsByCoordinates_LatitudeAndCoordinates_Longitude(double latitude, double longitude);
}
