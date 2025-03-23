package com.asssessment.coreshield.repository;

import com.asssessment.coreshield.model.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata,String> {
}
