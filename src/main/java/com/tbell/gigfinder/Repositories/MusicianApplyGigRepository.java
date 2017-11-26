package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianApplyGig;
import com.tbell.gigfinder.models.MusicianProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MusicianApplyGigRepository extends CrudRepository<MusicianApplyGig, Long> {



    List<MusicianApplyGig> findAllByGigId(long id);


    List<MusicianApplyGig> findAllByGig(Gig eachGig);


    Iterable<MusicianApplyGig> findAllByGigAndMusicianProfile(Gig gig, MusicianProfile musicianProfile);


    Iterable<MusicianApplyGig> findAllByMusicianProfile(MusicianProfile musicianProfile);

    MusicianApplyGig findByGigAndMusicianProfile(Gig gigApply, MusicianProfile musicianHired);

    Iterable<MusicianApplyGig> findAllByMusicianProfileAndHired(MusicianProfile musicianProfile, Boolean hired);

    Iterable<MusicianApplyGig> findByGigAndHired(Gig gig, Boolean hired);
}
