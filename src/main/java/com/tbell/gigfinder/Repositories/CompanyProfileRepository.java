package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.CompanyProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileRepository extends CrudRepository<CompanyProfile, Long> {
}
