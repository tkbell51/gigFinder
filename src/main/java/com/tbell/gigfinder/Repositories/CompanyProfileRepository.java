package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.Comment;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyProfileRepository extends CrudRepository<CompanyProfile, Long> {
    List<Comment>findAllByUser(User user);

    CompanyProfile findByUser(User user);

    CompanyProfile findById(long id);
}
