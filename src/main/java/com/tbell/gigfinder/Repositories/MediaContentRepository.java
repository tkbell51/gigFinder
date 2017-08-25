package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.MediaContent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaContentRepository extends CrudRepository<MediaContent, Long> {
}
