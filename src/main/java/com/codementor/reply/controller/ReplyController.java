package com.codementor.reply.controller;


import com.codementor.reply.dto.ReplyDto;
import com.codementor.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reply")
public class ReplyController {

    public final ReplyService replyService;

    @PostMapping("/create/{parentId}")
    public ResponseEntity createReply(@PathVariable Long parentId, @RequestBody ReplyDto dto){
        replyService.create(dto);

        return new ResponseEntity("답글이 작성 됐습니다.", HttpStatus.OK);
    }

}
