package com.example.demo.api;

import com.example.demo.model.SenderNotice;
import com.example.demo.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/vi/sendernotice")
@RestController
public class SenderNoticeController {

    private final NoticeService noticeService;

    @Autowired
    public SenderNoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public List<SenderNotice> addNotice (@Valid @NonNull @RequestBody SenderNotice notice){
        noticeService.addSenderNotice(notice);
        return noticeService.getAllSenderNotices();
    }

    @PostMapping(path = "{id}")
    public List<SenderNotice> addNoticeWithId (@Valid @NonNull @RequestBody SenderNotice notice, @PathVariable("id") UUID id){
        noticeService.addSenderNoticeWithId(id, notice);
        return noticeService.getAllSenderNotices();
    }

    @GetMapping
    public List<SenderNotice> getAllNotices(){
        return noticeService.getAllSenderNotices();
    }

    @GetMapping(path = "{id}")
    public SenderNotice selectNoticeById(@PathVariable("id") UUID id){
        return noticeService.selectSenderNoticeById(id).orElse(null); //you could handle exceptions here like 404
        // if somebody send get request with invalid UUID int the url etc..
    }

    @DeleteMapping(path = "{id}")
    public List<SenderNotice> deleteNoticeById (@PathVariable ("id") UUID id){
        noticeService.deleteSenderNotice(id);
        return noticeService.getAllSenderNotices();
    }

    @PutMapping(path = "{id}")
    public List<SenderNotice> updateNotice (@PathVariable ("id")UUID id,@Valid @NonNull @RequestBody SenderNotice noticeToUpdate) {
        noticeService.updateSenderNotice(id, noticeToUpdate);
        return noticeService.getAllSenderNotices();
    }

}