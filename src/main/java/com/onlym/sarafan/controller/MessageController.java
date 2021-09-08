package com.onlym.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.onlym.sarafan.domain.Message;
import com.onlym.sarafan.domain.User;
import com.onlym.sarafan.domain.Views;
import com.onlym.sarafan.dto.EventType;
import com.onlym.sarafan.dto.ObjectType;
import com.onlym.sarafan.repo.MessageRepo;
import com.onlym.sarafan.util.WsSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal User user
    ) {
        message.setCreationDate(LocalDateTime.now());
        message.setAuthor(user);
        Message updated = messageRepo.save(message);

        wsSender.accept(EventType.CREATE, updated);
        return updated;
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDB,
            @RequestBody Message message
    ) {
        BeanUtils.copyProperties(message, messageFromDB, "id");

        Message updated = messageRepo.save(messageFromDB);
        wsSender.accept(EventType.UPDATE, updated);

        return updated;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);

    }

//    @MessageMapping("/changeMessage")
//    @SendTo("/topic/activity")
//    public Message change(Message message) {
//        return messageRepo.save(message);
//    }
}
