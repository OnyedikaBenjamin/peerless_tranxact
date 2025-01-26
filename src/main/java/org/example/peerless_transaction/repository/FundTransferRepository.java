package org.example.peerless_transaction.repository;
import org.example.peerless_transaction.entity.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {

    List<FundTransfer> findBySenderAccountIdAndStatus(Long senderAccountId, String status);
    List<FundTransfer> findByScheduledDateBeforeAndStatus(LocalDateTime now, String status);
}

