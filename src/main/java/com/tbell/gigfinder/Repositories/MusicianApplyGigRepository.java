package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianApplyGig;
import com.tbell.gigfinder.models.MusicianProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MusicianApplyGigRepository extends CrudRepository<MusicianApplyGig, Long> {


    Long countByGig(Gig gig);

    List<MusicianApplyGig> findAllByGigId(long id);

    Long countByGigId(long id);

    Long countMusicianApplyGigByGig_Id(long id);

    List<MusicianApplyGig> findAllByGig(Gig eachGig);


    Iterable<MusicianApplyGig> findAllByGigAndMusicianProfile(Gig gig, MusicianProfile musicianProfile);

    Iterable<MusicianApplyGig> findAllByGigIdAndMusicianProfileId(long id, long id1);
}
