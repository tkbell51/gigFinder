package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.MediaContent;
import com.tbell.gigfinder.models.MusicianProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaContentRepository extends CrudRepository<MediaContent, Long> {
    List<MediaContent> findByMusicianProfile(MusicianProfile musicianProfile);
}
