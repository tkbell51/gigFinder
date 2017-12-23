package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianApplyGig;
import com.tbell.gigfinder.models.MusicianProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MusicianApplyGigRepository extends CrudRepository<MusicianApplyGig, Long> {



    List<MusicianApplyGig> findAllByGigIdOrderByDateAppliedAsc(long id);


    List<MusicianApplyGig> findAllByGigOrderByDateAppliedAsc(Gig eachGig);


    Iterable<MusicianApplyGig> findAllByGigAndMusicianProfileOrderByDateAppliedAsc(Gig gig, MusicianProfile musicianProfile);


    Iterable<MusicianApplyGig> findAllByMusicianProfileOrderByDateAppliedAsc(MusicianProfile musicianProfile);

    MusicianApplyGig findByGigAndMusicianProfile(Gig gigApply, MusicianProfile musicianHired);

    Iterable<MusicianApplyGig> findAllByMusicianProfileAndHiredOrderByDateAppliedAsc(MusicianProfile musicianProfile, Boolean hired);

    Iterable<MusicianApplyGig> findByGigAndHiredOrderByDateAppliedAsc(Gig gig, Boolean hired);

}
