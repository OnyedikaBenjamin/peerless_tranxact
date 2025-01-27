package org.example.peerless_transaction.service;


import org.example.peerless_transaction.dto.FundTransferRequest;
import org.example.peerless_transaction.dto.FundTransferResponse;

import java.util.List;

public interface FundTransferService {

    FundTransferResponse scheduleTransfer(FundTransferRequest request);

    List<FundTransferResponse> getScheduledTransfers(Long accountId);
    void cancelTransfer(Long transferId);
}
