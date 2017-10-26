package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicianProfileRepository extends CrudRepository<MusicianProfile, Long> {
    MusicianProfile findById(long id);

    MusicianProfile findByUser(User user);


    Iterable<MusicianProfile> findByMusicianInstrumentsContainingIgnoreCase(String instrument);

    Iterable<MusicianProfile> findByLocationIgnoreCase(String location);


    MusicianProfile findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    Iterable<MusicianProfile> findMusicianProfileByLocationContainingIgnoreCase(String location);


}
