package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.Gig;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface GigRepository extends CrudRepository<Gig, Long> {

    List<Gig> findByCompanyProfile (CompanyProfile companyProfile);

    Iterable<Gig> findByGigLocationContaining(String location);

    Gig findById(long id);

    Iterable<Gig> findByGigLocationContainingIgnoreCase(String location);


    Iterable<Gig> findByGigTypeContainingIgnoreCase(String type);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Gig g WHERE g.gigEnd > :date")
//    int removeOlderThan(@Param("date") java.sql.Date date);



}
