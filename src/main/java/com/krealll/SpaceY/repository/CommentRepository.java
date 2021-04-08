package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findCommentById (Integer id);

    List<Comment> findAllByUsersId(Integer userId);

}
