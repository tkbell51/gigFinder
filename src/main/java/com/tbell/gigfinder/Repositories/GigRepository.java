package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.Gig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GigRepository extends CrudRepository<Gig, Long> {
    List<Gig> findByCompanyProfile (CompanyProfile companyProfile);

    Iterable<Gig> findByGigLocationContaining(String location);

    Gig findById(long id);

    Iterable<Gig> findByGigLocationContainingIgnoreCase(String location);

    Iterable<Gig> findByGigStartIgnoreCase(String gigStart);

    Iterable<Gig> findByGigTypeContainingIgnoreCase(String type);
}
