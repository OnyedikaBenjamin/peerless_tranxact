package org.example.peerless_transaction.controller;

import org.example.peerless_transaction.dto.FundTransferRequest;
import org.example.peerless_transaction.dto.FundTransferResponse;
import org.example.peerless_transaction.service.FundTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class FundTransferController {

    private final FundTransferService fundTransferService;

    @Autowired
    public FundTransferController(FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
    }

    @PostMapping("/schedule")
    @ResponseStatus(HttpStatus.CREATED)
    public FundTransferResponse scheduleTransfer(@RequestBody FundTransferRequest request) {
        return fundTransferService.scheduleTransfer(request);
    }

    @GetMapping("/account/{accountId}")
    public List<FundTransferResponse> getScheduledTransfers(@PathVariable("accountId") Long accountId) {
        return fundTransferService.getScheduledTransfers(accountId);
    }

    @DeleteMapping("/cancel/{transferId}")
    public ResponseEntity<Void> cancelTransfer(@PathVariable Long transferId) {
        fundTransferService.cancelTransfer(transferId);
        return ResponseEntity.noContent().build();
    }
}