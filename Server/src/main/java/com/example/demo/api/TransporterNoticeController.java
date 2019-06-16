package com.example.demo.api;

import com.example.demo.model.TransporterNotice;
import com.example.demo.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/vi/transporternotice")
@RestController
public class TransporterNoticeController {

    private final NoticeService NoticeService;

    @Autowired
    public TransporterNoticeController(NoticeService NoticeService) {
        this.NoticeService = NoticeService;
    }

    @PostMapping
    public List<TransporterNotice> addNotice (@Valid @NonNull @RequestBody TransporterNotice notice){
        NoticeService.addTransporterNotice(notice);
        return NoticeService.getAllTransporterNotices();
    }

    @PostMapping(path = "{id}")
    public List<TransporterNotice> addNoticeWithId (@Valid @NonNull @RequestBody TransporterNotice notice, @PathVariable("id") UUID id){
        NoticeService.addTransporterNoticeWithId(id, notice);
        return NoticeService.getAllTransporterNotices();
    }

    @GetMapping
    public List<TransporterNotice> getAllNotices(){
        return NoticeService.getAllTransporterNotices();
    }

    @GetMapping(path = "{id}")
    public TransporterNotice selectNoticeById(@PathVariable("id") UUID id){
        return NoticeService.selectTransporterNoticeById(id).orElse(null); //you could handle exceptions here like 404
        // if somebody send get request with invalid UUID int the url etc..
    }

    @DeleteMapping(path = "{id}")
    public List<TransporterNotice> deleteNoticeById (@PathVariable ("id") UUID id){
        NoticeService.deleteTransporterNotice(id);
        return NoticeService.getAllTransporterNotices();
    }

    @PutMapping(path = "{id}")
    public List<TransporterNotice> updateNotice (@PathVariable ("id")UUID id,@Valid @NonNull @RequestBody TransporterNotice noticeToUpdate) {
        NoticeService.updateTransporterNotice(id, noticeToUpdate);
        return NoticeService.getAllTransporterNotices();
    }

}
