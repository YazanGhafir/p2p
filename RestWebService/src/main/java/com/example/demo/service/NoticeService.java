package com.example.demo.service;
import com.example.demo.dao.NoticeDao;
import com.example.demo.model.SenderNotice;
import com.example.demo.model.TransporterNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoticeService {
    private final NoticeDao noticeDao;

    @Autowired
    public NoticeService(@Qualifier("fakeDao") NoticeDao noticeDao) {
        this.noticeDao = noticeDao;
    }

    public int addSenderNotice(SenderNotice notice) {
        return noticeDao.insertSenderNotice(notice); //here would be all logic required
    }

    public int addSenderNoticeWithId(UUID id, SenderNotice notice) {
        return noticeDao.insertSenderNotice(id, notice); //here would be all logic required
    }

    public int addTransporterNotice(TransporterNotice notice) {
        return noticeDao.insertTransporterNotice(notice); //here would be all logic required
    }

    public int addTransporterNoticeWithId(UUID id, TransporterNotice notice) {
        return noticeDao.insertTransporterNotice(id, notice); //here would be all logic required
    }

    public List<SenderNotice> getAllSenderNotices() {
        return noticeDao.selectAllSenderNotices(); //here would be all logic required
    }

    public List<TransporterNotice> getAllTransporterNotices() {
        return noticeDao.selectAllTransporterNotices(); //here would be all logic required
    }

    public Optional<SenderNotice> selectSenderNoticeById(UUID id) {
        return noticeDao.selectSenderNoticeById(id); //here would be all logic required
    }

    public Optional<TransporterNotice> selectTransporterNoticeById(UUID id) {
        return noticeDao.selectTransporterNoticeById(id); //here would be all logic required
    }

    public int deleteSenderNotice (UUID id) {
        return noticeDao.deleteSenderNoticeById(id); //here would be all logic required
    }

    public int deleteTransporterNotice (UUID id) {
        return noticeDao.deleteTransporterNoticeById(id); //here would be all logic required
    }

    public int updateSenderNotice (UUID id, SenderNotice newNotice) {
        return noticeDao.updateSenderNoticeById(id, newNotice); //here would be all logic required
    }

    public int updateTransporterNotice (UUID id, TransporterNotice newNotice) {
        return noticeDao.updateTransporterNoticeById(id, newNotice); //here would be all logic required
    }

    public int clearAllNotices() {
        return noticeDao.clearAllNotices();
    }

}
