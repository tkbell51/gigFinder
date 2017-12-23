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

    List<Gig> findByCompanyProfileOrderByGigStartAsc (CompanyProfile companyProfile);

    Iterable<Gig> findByGigLocationOrderByGigStartAsc(String location);


    Gig findById(long id);



    Iterable<Gig> findByGigTypeContainingIgnoreCaseOrderByGigStartAsc(String type);

    Iterable<Gig> findAllByOrderByGigStartAsc();

    Iterable<Gig> findByGigLocationContainingIgnoreCaseOrderByGigStartAsc(String location);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Gig g WHERE g.gigEnd > :date")
//    int removeOlderThan(@Param("date") java.sql.Date date);



}
