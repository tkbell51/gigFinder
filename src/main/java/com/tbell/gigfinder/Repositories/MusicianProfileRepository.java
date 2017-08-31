package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicianProfileRepository extends CrudRepository<MusicianProfile, Long> {
    MusicianProfile findById(long id);

    MusicianProfile findByUser(User user);
}
