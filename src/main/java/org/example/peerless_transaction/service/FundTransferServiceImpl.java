package org.example.peerless_transaction.service;

import org.example.peerless_transaction.dto.FundTransferRequest;
import org.example.peerless_transaction.dto.FundTransferResponse;
import org.example.peerless_transaction.entity.FundTransfer;
import org.example.peerless_transaction.enums.TransactionStatus;
import org.example.peerless_transaction.exception.InvalidOperationException;
import org.example.peerless_transaction.exception.InvalidTransferAmountException;
import org.example.peerless_transaction.exception.InvalidTransferDateException;
import org.example.peerless_transaction.exception.TransferNotFoundException;
import org.example.peerless_transaction.repository.FundTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FundTransferServiceImpl implements FundTransferService {

    private final FundTransferRepository fundTransferRepository;

    @Autowired
    public FundTransferServiceImpl(FundTransferRepository fundTransferRepository) {
        this.fundTransferRepository = fundTransferRepository;
    }

    @Override
    public FundTransferResponse scheduleTransfer(FundTransferRequest request) {
        // Validate the transfer date
        if (request.getTransferDate().isBefore(java.time.LocalDateTime.now())) {
            throw new InvalidTransferDateException("Transfer date must be in the future.");
        }

        if (request.getTransferAmount() == null || request.getTransferAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferAmountException("Transfer amount must be positive.");
        }

        FundTransfer fundTransfer = new FundTransfer();
        fundTransfer.setSenderAccountId(request.getSenderAccountId());
        fundTransfer.setRecipientAccountId(request.getRecipientAccountId());
        fundTransfer.setTransferAmount(request.getTransferAmount());
        fundTransfer.setScheduledDate(request.getTransferDate());
        fundTransfer.setStatus(TransactionStatus.PENDING);
        fundTransfer.setCreatedAt(java.time.LocalDateTime.now());
        fundTransfer = fundTransferRepository.save(fundTransfer);

        return new FundTransferResponse(
                fundTransfer.getTransferId(),
                fundTransfer.getSenderAccountId(),
                fundTransfer.getRecipientAccountId(),
                fundTransfer.getTransferAmount(),
                fundTransfer.getScheduledDate()
        );
    }


    @Override
    public List<FundTransferResponse> getScheduledTransfers(Long accountId) {
        List<FundTransfer> transfers = fundTransferRepository.findBySenderAccountIdAndStatus(accountId, "PENDING");
        return transfers.stream()
                .map(transfer -> new FundTransferResponse(
                        transfer.getTransferId(),
                        transfer.getSenderAccountId(),
                        transfer.getRecipientAccountId(),
                        transfer.getTransferAmount(),
                        transfer.getScheduledDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void cancelTransfer(Long transferId) {
            FundTransfer transfer = fundTransferRepository.findById(transferId)
                    .orElseThrow(() -> new TransferNotFoundException("Scheduled transfer not found with ID: " + transferId));

            if (!transfer.getStatus().equals(TransactionStatus.PENDING)) {
                throw new InvalidOperationException("Only PENDING transfers can be canceled.");
            }

            if (transfer.getScheduledDate().isBefore(LocalDateTime.now())) {
                throw new InvalidOperationException("Cannot cancel a transfer scheduled in the past.");
            }

            transfer.setStatus(TransactionStatus.CANCELED);
            transfer.setUpdatedAt(LocalDateTime.now());
            fundTransferRepository.save(transfer);
        }

}