package com.example.demo.dao;

import com.example.demo.model.SenderNotice;
import com.example.demo.model.TransporterNotice;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakeNoticeDataAccessService implements NoticeDao {

    private static ReaderWriter DB = new ReaderWriter();
    private static List<SenderNotice> senderNotices = new ArrayList<SenderNotice>(){};
    private static List<TransporterNotice> transporterNotices = new ArrayList<TransporterNotice>(){};

    @Override
    public int insertSenderNotice(UUID id, SenderNotice notice) {
        try {
            senderNotices = DB.readSenderNoticesFromSFile();
            senderNotices.add(new SenderNotice(notice.getDeviceID(), id, notice.getFrom(), notice.getTo(), notice.getPhone(), notice.getPrice(),
                    notice.getDeliveryDate(),notice.getQuantity(), notice.getCapacity()));
            DB.writeToSFile(senderNotices);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public int insertTransporterNotice(UUID id, TransporterNotice notice) {
        try {
            transporterNotices = DB.readTransporterNoticesFromSFile();
            transporterNotices.add(new TransporterNotice(notice.getDeviceID(), id, notice.getFrom(), notice.getTo(), notice.getPhone(), notice.getPrice(),
                    notice.getDeliveryDate(), notice.getMeansoftransport(), notice.getCapacity()));
            DB.writeToTFile(transporterNotices);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public List<SenderNotice> selectAllSenderNotices() {
        try {
            senderNotices = DB.readSenderNoticesFromSFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return senderNotices;
    }

    @Override
    public List<TransporterNotice> selectAllTransporterNotices() {
        try {
            transporterNotices = DB.readTransporterNoticesFromSFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return transporterNotices;
    }

    @Override
    public Optional<SenderNotice> selectSenderNoticeById(UUID id) {
        try {
            senderNotices = DB.readSenderNoticesFromSFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return senderNotices.stream()
                .filter(notice -> notice.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<TransporterNotice> selectTransporterNoticeById(UUID id) {
        try {
            transporterNotices = DB.readTransporterNoticesFromSFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return transporterNotices.stream()
                .filter(notice -> notice.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deleteSenderNoticeById(UUID id) {
        Optional<SenderNotice> NoticeMaybe = selectSenderNoticeById(id);
        if (!NoticeMaybe.isPresent()) {
            return 0;
        } else {

            try {
                senderNotices.remove(NoticeMaybe.get());
                DB.writeToSFile(senderNotices);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    @Override
    public int deleteTransporterNoticeById(UUID id) {
        Optional<TransporterNotice> NoticeMaybe = selectTransporterNoticeById(id);
        if (!NoticeMaybe.isPresent()) {
            return 0;
        } else {

            try {
                transporterNotices.remove(NoticeMaybe.get());
                DB.writeToTFile(transporterNotices);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    @Override
    public int updateSenderNoticeById(UUID id, SenderNotice update) {
        return selectSenderNoticeById(id)
                .map(notice -> {
                    int indexOfNoticeToUpdate = senderNotices.indexOf(notice);
                    if (indexOfNoticeToUpdate >= 0) {
                        try {
                            senderNotices.set(indexOfNoticeToUpdate, new SenderNotice(update.getDeviceID(), id, update.getFrom(), update.getTo(), update.getPhone(), update.getPrice(),
                                    update.getDeliveryDate(),update.getQuantity(), update.getCapacity()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            DB.writeToSFile(senderNotices);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }

    @Override
    public int updateTransporterNoticeById(UUID id, TransporterNotice update) {
        return selectTransporterNoticeById(id)
                .map(notice -> {
                    int indexOfNoticeToUpdate = transporterNotices.indexOf(notice);
                    if (indexOfNoticeToUpdate >= 0) {
                        try {
                            transporterNotices.set(indexOfNoticeToUpdate, new TransporterNotice(update.getDeviceID(), id, update.getFrom(), update.getTo(), update.getPhone(), update.getPrice(),
                                    update.getDeliveryDate(), update.getMeansoftransport(), update.getCapacity()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            DB.writeToTFile(transporterNotices);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }

    /**
     * clear the noticelists to debug
     * @return 1 if proceeded
     */
    @Override
    public int clearAllNotices() {
        try {
            senderNotices.clear();
            transporterNotices.clear();
            DB.writeToSFile(senderNotices);
            DB.writeToTFile(transporterNotices);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
