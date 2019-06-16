package com.example.demo.dao;

import com.example.demo.model.SenderNotice;
import com.example.demo.model.TransporterNotice;
import com.example.demo.model.Utility;
import com.example.demo.model.Utility.MeansOfTransport;
import java.util.Random;
import org.junit.Test;
import org.junit.Before;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class FakeNoticeDataAccessServiceTest {

    private FakeNoticeDataAccessService underTest;

    @Before
    public void setUp() {
        underTest = new FakeNoticeDataAccessService();
        underTest.clearAllNotices();
    }

    /**
     * testing senderNotice methods
     */
    @Test
    public void canPerformCrudSenderNotices() {
        // Given senderNotice1 A to B
        UUID idOne = UUID.randomUUID();
        SenderNotice senderNotice1 = new SenderNotice("AndroidID1", idOne, "A", "B", "733999999", 20.0f, "2019-06-02", 1, "3");

        // ..And senderNotice2 C to D
        UUID idTwo = UUID.randomUUID();
        SenderNotice senderNotice2 = new SenderNotice("AndroidID2", idTwo, "C", "D", "733888888", 30.0f, "2019-06-15", 2, "4");

        // When senderNotice1 and senderNotice2 added to db
        underTest.insertSenderNotice(idOne, senderNotice1);
        underTest.insertSenderNotice(idTwo, senderNotice2);

        // Then we can retrieve senderNotice1 by id
        assertThat(underTest.selectSenderNoticeById(idOne))
                .isPresent()
                .hasValueSatisfying(noticeFromDB -> assertThat(noticeFromDB).isEqualToComparingFieldByField(senderNotice1));

        // And also senderNotice2 by id
        assertThat(underTest.selectSenderNoticeById(idTwo))
                .isPresent()
                .hasValueSatisfying(noticeFromDB2 -> assertThat(noticeFromDB2).isEqualToComparingFieldByField(senderNotice2));

        // When get all SenderNotices
        List<SenderNotice> allSenderNotices = underTest.selectAllSenderNotices();

        // ...List should have size 2 and should have both senderNotice1 and senderNotice2
        assertThat(allSenderNotices)
                .hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(senderNotice1, senderNotice2);

        // ... An update request (E to B instead of A to B, another telefonnumber and price)
        SenderNotice senderNoticeUpdate = new SenderNotice("AndroidID1", idOne, "E", "B", "733111111", 33.0f, "2019-06-02", 1, "3");

        // When Update
        assertThat(underTest.updateSenderNoticeById(idOne, senderNoticeUpdate)).isEqualTo(1);

        // Then when get senderNotice1 with idOne then should have from E to B and different phone and price
        assertThat(underTest.selectSenderNoticeById(idOne))
                .isPresent()
                .hasValueSatisfying(senderNoticeFromDb -> assertThat(senderNoticeFromDb).isEqualToComparingFieldByField(senderNoticeUpdate));

        // When Delete senderNotice1
        assertThat(underTest.deleteSenderNoticeById(idOne)).isEqualTo(1);

        // When get senderNotice1 should be empty
        assertThat(underTest.selectSenderNoticeById(idOne)).isEmpty();

        // Finally DB should only contain only senderNotice2
        assertThat(underTest.selectAllSenderNotices())
                .hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(senderNotice2);
    }

    @Test
    public void willReturn0IfNoSenderNoticeFoundToDelete() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        int deleteResult = underTest.deleteSenderNoticeById(id);

        // Then
        assertThat(deleteResult).isEqualTo(0);
    }

    @Test
    public void willReturn0IfNoSenderNoticeFoundToUpdate() {
        // Given
        UUID id = UUID.randomUUID();
        SenderNotice senderNotice = new SenderNotice("AndroidID1", id, "Y", "Z", "722222222", 44.0f, "2019-10-02", 1, "3");

        // When
        int deleteResult = underTest.updateSenderNoticeById(id, senderNotice);

        // Then
        assertThat(deleteResult).isEqualTo(0);
    }



    /**
     * testing transporterNotice methods
     */
    @Test
    public void canPerformCrudTransporterNotices() {
        // Given transporterNotice1 A to B
        UUID idOne = UUID.randomUUID();
        TransporterNotice transporterNotice1 = new TransporterNotice("AndroidID1", idOne, "A", "B", "733999999", 20.0f, "2019-06-02", Utility.MeansOfTransport.CAR, "3");

        // ..And transporterNotice2 C to D
        UUID idTwo = UUID.randomUUID();
        TransporterNotice transporterNotice2 = new TransporterNotice("AndroidID2",idTwo, "C", "D", "733888888", 30.0f, "2019-06-15", Utility.MeansOfTransport.CAR, "4");

        // When transporterNotice1 and transporterNotice2 added to db
        underTest.insertTransporterNotice(idOne, transporterNotice1);
        underTest.insertTransporterNotice(idTwo, transporterNotice2);

        // Then we can retrieve transporterNotice1 by id
        assertThat(underTest.selectTransporterNoticeById(idOne))
                .isPresent()
                .hasValueSatisfying(noticeFromDB -> assertThat(noticeFromDB).isEqualToComparingFieldByField(transporterNotice1));

        // And also transporterNotice2 by id
        assertThat(underTest.selectTransporterNoticeById(idTwo))
                .isPresent()
                .hasValueSatisfying(noticeFromDB2 -> assertThat(noticeFromDB2).isEqualToComparingFieldByField(transporterNotice2));

        // When get all TransporterNotices
        List<TransporterNotice> allTransporterNotices = underTest.selectAllTransporterNotices();

        // ...List should have size 2 and should have both transporterNotice1 and transporterNotice2
        assertThat(allTransporterNotices)
                .hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(transporterNotice1, transporterNotice2);

        // ... An update request (E to B instead of A to B, another telefonnumber and price)
        TransporterNotice transporterNoticeUpdate = new TransporterNotice("AndroidID1", idOne, "E", "B", "733111111", 33.0f, "2019-06-02", Utility.MeansOfTransport.CAR, "3");

        // When Update
        assertThat(underTest.updateTransporterNoticeById(idOne, transporterNoticeUpdate)).isEqualTo(1);

        // Then when get transporterNotice1 with idOne then should have from E to B and different phone and price
        assertThat(underTest.selectTransporterNoticeById(idOne))
                .isPresent()
                .hasValueSatisfying(transporterNoticeFromDb -> assertThat(transporterNoticeFromDb).isEqualToComparingFieldByField(transporterNoticeUpdate));

        // When Delete transporterNotice1
        assertThat(underTest.deleteTransporterNoticeById(idOne)).isEqualTo(1);

        // When get transporterNotice1 should be empty
        assertThat(underTest.selectTransporterNoticeById(idOne)).isEmpty();

        // Finally DB should only contain only transporterNotice2
        assertThat(underTest.selectAllTransporterNotices())
                .hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(transporterNotice2);
    }

    @Test
    public void willReturn0IfNoTransporterNoticeFoundToDelete() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        int deleteResult = underTest.deleteTransporterNoticeById(id);

        // Then
        assertThat(deleteResult).isEqualTo(0);
    }

    @Test
    public void willReturn0IfNoTransporterNoticeFoundToUpdate() {
        // Given
        UUID id = UUID.randomUUID();
        TransporterNotice transporterNotice = new TransporterNotice("AndroidID1", id, "Y", "Z", "722222222", 44.0f, "2019-10-02", Utility.MeansOfTransport.CAR, "3");

        // When
        int deleteResult = underTest.updateTransporterNoticeById(id, transporterNotice);

        // Then
        assertThat(deleteResult).isEqualTo(0);
    }

    @Test
    public void deletesAllNotices() {
        Random rng = new Random();
        for (int i = 0; i < 20; i++) {
            UUID id = UUID.randomUUID();
            if (rng.nextBoolean()) {
                SenderNotice senderNotice = new SenderNotice("AndroidID1", id, "A", "B", "703432334", 3f, "2019-10-03", 1, "3");
                underTest.insertSenderNotice(id, senderNotice);
            } else {
                TransporterNotice transporterNotice = new TransporterNotice("AndroidID1", id, "A", "B", "703432334", 3f, "2019-10-03", MeansOfTransport.CAR, "3");
                underTest.insertTransporterNotice(id, transporterNotice);
            }
        }
        underTest.clearAllNotices();
        assertThat(underTest.selectAllSenderNotices().isEmpty()).isTrue();
        assertThat(underTest.selectAllTransporterNotices().isEmpty()).isTrue();
    }
}
