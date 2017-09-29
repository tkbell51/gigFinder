package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianApplyGig;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MusicianApplyGigRepository extends CrudRepository<MusicianApplyGig, Long> {


    Long countByGig(Gig gig);

    List<MusicianApplyGig> findAllByGigId(long id);

    Long countByGigId(long id);

    Long countMusicianApplyGigByGig_Id(long id);
}
