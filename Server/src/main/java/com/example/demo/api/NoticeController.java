package com.example.demo.api;

import com.example.demo.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/vi/notice")
@RestController
public class NoticeController {

  private final com.example.demo.service.NoticeService noticeService;

  @Autowired
  public NoticeController(NoticeService noticeService) {
    this.noticeService = noticeService;
  }

  @DeleteMapping
  public int deleteAllNotices() {
    return noticeService.clearAllNotices();
  }
}
