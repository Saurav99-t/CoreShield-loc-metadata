package com.asssessment.coreshield.repository;

import com.asssessment.coreshield.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,String> {

}
