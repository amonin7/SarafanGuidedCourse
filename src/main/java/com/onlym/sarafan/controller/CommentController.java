package com.onlym.sarafan.controller;

import com.onlym.sarafan.domain.Comment;
import com.onlym.sarafan.domain.User;
import com.onlym.sarafan.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal User user
    ) {
        return commentService.create(comment, user);
    }
}
