package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.MusicianProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicianProfileRepository extends CrudRepository<MusicianProfile, Long> {


}
