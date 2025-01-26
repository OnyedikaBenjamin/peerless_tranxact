package org.example.peerless_transaction.service;

import org.example.peerless_transaction.entity.FundTransfer;
import org.example.peerless_transaction.enums.TransactionStatus;
import org.example.peerless_transaction.repository.FundTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class TransferProcessor {

    @Autowired
    private FundTransferRepository transferRepository;

    @Scheduled(fixedRate = 10000)
    public void processScheduledTransfers() {
        LocalDateTime now = LocalDateTime.now();
        List<FundTransfer> pendingTransfers = transferRepository.findByScheduledDateBeforeAndStatus(now, "PENDING");

        for (FundTransfer transfer : pendingTransfers) {
            try {
                demoExecuteTransfer(transfer);

                transfer.setStatus(TransactionStatus.COMPLETED);
            } catch (Exception e) {
                transfer.setStatus(TransactionStatus.FAILED);
            }

            transfer.setUpdatedAt(LocalDateTime.now());
            transferRepository.save(transfer);
        }
    }

    public void demoExecuteTransfer(FundTransfer transfer) {
        System.out.println("Executing transfer: " + transfer);
    }
}