package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{
}
