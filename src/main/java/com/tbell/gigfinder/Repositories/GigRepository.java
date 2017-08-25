package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.Gig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GigRepository extends CrudRepository<Gig, Long> {


}
