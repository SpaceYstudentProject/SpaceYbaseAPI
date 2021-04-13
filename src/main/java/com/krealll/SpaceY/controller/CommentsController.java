package com.krealll.SpaceY.controller;

import com.krealll.SpaceY.controller.parameters.ResponseParameters;
import com.krealll.SpaceY.model.Launch;
import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.dto.CreateCommentDTO;
import com.krealll.SpaceY.service.impl.CommentService;
import com.krealll.SpaceY.service.impl.EntityService;
import com.krealll.SpaceY.service.impl.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${spring.data.rest.base-path}comments")
public class CommentsController {

    private final UserService userService;
    private final CommentService commentService;
    private final EntityService entityService;

    @Autowired
    public CommentsController(UserService userService, CommentService commentService, EntityService entityService) {
        this.userService = userService;
        this.commentService = commentService;
        this.entityService = entityService;
    }

    @PostMapping("/launch/{id}")
    @ApiOperation(value = "create your comment",
            notes = "Return ResponseEntitiy of result OK or error response.")
    public ResponseEntity leaveComment(@PathVariable(name="id") String id, @RequestBody CreateCommentDTO createCommentDTO){
        User user  = userService.findByLogin(createCommentDTO.getUsername());
        if( user != null){
            Map<String,Object> response;
            Optional<Launch> launchOptional = entityService.findLaunch(id);
            if(launchOptional.isEmpty()){
                Launch createdLaunch = entityService.addLaunch(id);
                if(createdLaunch == null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                launchOptional = Optional.of(createdLaunch);
            }
            response = commentService.createComment(createCommentDTO, launchOptional.get().getEntitiesId(), user);
            if(response.containsKey(ResponseParameters.ERROR)){
                return ResponseEntity.status((HttpStatus) response.get(ResponseParameters.ERROR)).build();
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "update your comment",
            notes = "Return ResponseEntitiy of result OK or error response.")
    public ResponseEntity updateComment(@PathVariable(name ="id") Integer id, @RequestBody Map<String,Object> fields){
        Map<String,Object> response;
        response = commentService.updateComment(id,fields);
        if(response.containsKey(ResponseParameters.ERROR)){
            return ResponseEntity.status((HttpStatus) response.get(ResponseParameters.ERROR)).build();
        }
        return ResponseEntity.ok(response);
    }




}
