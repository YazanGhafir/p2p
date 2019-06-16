package com.example.demo.dao;

import com.example.demo.model.SenderNotice;
import com.example.demo.model.TransporterNotice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoticeDao {

    int insertSenderNotice (UUID id, SenderNotice notice);

    default int insertSenderNotice (SenderNotice notice) {
        UUID id = UUID.randomUUID();
        return insertSenderNotice(id, notice);
    }

    int insertTransporterNotice (UUID id, TransporterNotice notice);

    default int insertTransporterNotice (TransporterNotice notice) {
        UUID id = UUID.randomUUID();
        return insertTransporterNotice(id, notice);
    }

    List<SenderNotice> selectAllSenderNotices();

    List<TransporterNotice> selectAllTransporterNotices();

    Optional<SenderNotice> selectSenderNoticeById(UUID id);

    Optional<TransporterNotice> selectTransporterNoticeById(UUID id);

    int deleteSenderNoticeById(UUID id);

    int deleteTransporterNoticeById(UUID id);

    int updateSenderNoticeById(UUID id, SenderNotice update);

    int updateTransporterNoticeById(UUID id, TransporterNotice update);

    int clearAllNotices();

}
