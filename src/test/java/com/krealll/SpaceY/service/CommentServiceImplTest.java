package com.krealll.SpaceY.service;

import com.krealll.SpaceY.controller.parameters.RequestParameters;
import com.krealll.SpaceY.controller.parameters.ResponseParameters;
import com.krealll.SpaceY.model.Comment;
import com.krealll.SpaceY.model.Permission;
import com.krealll.SpaceY.model.Role;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.CommentDTO;
import com.krealll.SpaceY.model.dto.CreateCommentDTO;
import com.krealll.SpaceY.model.type.UserStatus;
import com.krealll.SpaceY.repository.CommentRepository;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@PrepareForTest(CommentServiceImpl.class)
public class CommentServiceImplTest {

    private CommentRepository commentRepository;
    private CommentServiceImpl commentService;
    private Comment comment;
    private CommentDTO commentDTO;
    private CreateCommentDTO createCommentDTO;
    private User user;
    private Optional<Comment> optionalComment;
    private Map<String, Object> testFields;

    @BeforeMethod
    public void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentService = new CommentServiceImpl(commentRepository);

        Set<Permission> permissions = new HashSet<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        role.setPermissions(permissions);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user = new User();
        user.setLogin("Daetwen");
        user.setEmail("123vlad.@gmail.com");
        user.setPassword(encoder.encode("123456"));
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(roles);

        comment = new Comment();
        comment.setContent("Hello my friend");
        comment.setCreated(LocalDateTime.now());
        comment.setUpdated(LocalDateTime.now());
        comment.setUsersId(user.getId());
        comment.setId(0);
        comment.setParentId(0);
        comment.setDepth((short)0);
        comment.setEntitiesId(0);

        commentDTO = new CommentDTO();
        commentDTO = commentDTO.fromComment(comment);
        optionalComment = Optional.of(comment);
        testFields = new HashMap<>();

        createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setContent("Hello my friend");
        createCommentDTO.setParentId(0);
        createCommentDTO.setUsername(user.getLogin());
    }

    @Test
    public void createCommentTrueOne() {
        Map<String, Object> expected = new HashMap<>();
        comment.setParentId(2);
        comment.setDepth((short)1);
        optionalComment = Optional.of(comment);
        when(commentRepository.findCommentById(anyInt())).thenReturn(optionalComment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        expected.put(ResponseParameters.COMMENT, comment);
        createCommentDTO.setParentId(2);
        Map<String, Object> actual = commentService.createComment(createCommentDTO, 0, user);
        assertEquals(expected, actual);
    }

    @Test
    public void createCommentTrueTwo() {
        Map<String, Object> expected = new HashMap<>();
        comment.setParentId(0);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        expected.put(ResponseParameters.COMMENT, comment);
        Map<String, Object> actual = commentService.createComment(createCommentDTO, 0, user);
        assertEquals(expected, actual);
    }

    @Test
    public void createCommentFalse() {
        Map<String, Object> expected = new HashMap<>();
        comment.setParentId(2);
        optionalComment = Optional.of(comment);
        when(commentRepository.findCommentById(anyInt())).thenReturn(Optional.empty());
        expected.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
        createCommentDTO.setParentId(2);
        Map<String, Object> actual = commentService.createComment(createCommentDTO, 0, user);
        assertEquals(expected, actual);
    }

    @Test
    public void updateCommentTrue() {
        Map<String, Object> expected = new HashMap<>();
        expected.put(ResponseParameters.COMMENT, commentDTO);
        when(commentRepository.findById(anyInt())).thenReturn(optionalComment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        testFields.put(RequestParameters.CONTENT, "Hello my friend");
        Map<String, Object> actual = commentService.updateComment(0, testFields);
        assertEquals(actual, expected);
    }

    @Test
    public void updateCommentFalseOne() {
        Map<String, Object> expected = new HashMap<>();
        expected.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
        comment.setId(null);
        optionalComment = Optional.of(comment);
        when(commentRepository.findById(anyInt())).thenReturn(optionalComment);
        //testFields.put(null, "Hello my friend");
        Map<String, Object> actual = commentService.updateComment(0, testFields);
        assertEquals(actual, expected);
    }

    @Test
    public void updateCommentFalseTwo() {
        Map<String, Object> expected = new HashMap<>();
        expected.put(ResponseParameters.ERROR, HttpStatus.BAD_REQUEST);
        when(commentRepository.findById(anyInt())).thenReturn(Optional.empty());
        Map<String, Object> actual = commentService.updateComment(0, testFields);
        assertEquals(actual, expected);
    }

}