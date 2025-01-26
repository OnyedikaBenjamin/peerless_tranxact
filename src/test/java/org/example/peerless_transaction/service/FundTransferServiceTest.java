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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FundTransferServiceTest {

    @Mock
    private FundTransferRepository fundTransferRepository;

    @InjectMocks
    private FundTransferServiceImpl fundTransferService;

    private FundTransferRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validRequest = new FundTransferRequest();
        validRequest.setSenderAccountId(Long.valueOf("1234567890"));
        validRequest.setRecipientAccountId(Long.valueOf("0987654321"));
        validRequest.setTransferAmount(new BigDecimal("5000.00"));
        validRequest.setTransferDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testScheduleTransfer_success() {
        FundTransfer fundTransfer = new FundTransfer();
        fundTransfer.setTransferId(1L);
        fundTransfer.setSenderAccountId(Long.valueOf("1234567890"));
        fundTransfer.setRecipientAccountId(Long.valueOf("9087654321"));
        fundTransfer.setTransferAmount(new BigDecimal("5000.00"));
        fundTransfer.setScheduledDate(validRequest.getTransferDate());
        fundTransfer.setStatus(TransactionStatus.PENDING);

        when(fundTransferRepository.save(any(FundTransfer.class))).thenReturn(fundTransfer);
        FundTransferResponse response = fundTransferService.scheduleTransfer(validRequest);

        assertNotNull(response);
        assertEquals(1L, response.getTransferId());
        assertEquals("1234567890", String.valueOf(response.getSenderAccountId()));
        assertEquals("9087654321", String.valueOf(response.getRecipientAccountId()));
        assertEquals(new BigDecimal("5000.00"), response.getTransferAmount());
        assertEquals(validRequest.getTransferDate(), response.getScheduledDate());
        verify(fundTransferRepository, times(1)).save(any(FundTransfer.class));
    }

    @Test
    void testScheduleTransfer_invalidTransferDate() {
        validRequest.setTransferDate(LocalDateTime.now().minusDays(1)); // Transfer date in the past
        assertThrows(InvalidTransferDateException.class, () -> fundTransferService.scheduleTransfer(validRequest));
    }

    @Test
    void testScheduleTransfer_invalidTransferAmount() {
        validRequest.setTransferAmount(new BigDecimal("-100.00"));
        assertThrows(InvalidTransferAmountException.class, () -> fundTransferService.scheduleTransfer(validRequest));
    }

    @Test
    void testGetScheduledTransfers_success() {
        FundTransfer transfer1 = new FundTransfer();
        transfer1.setTransferId(1L);
        transfer1.setSenderAccountId(Long.valueOf("1234567890"));
        transfer1.setRecipientAccountId(Long.valueOf("0987654321"));
        transfer1.setTransferAmount(new BigDecimal("5000.00"));
        transfer1.setScheduledDate(validRequest.getTransferDate());
        transfer1.setStatus(TransactionStatus.PENDING);

        when(fundTransferRepository.findBySenderAccountIdAndStatus(Long.valueOf(anyLong()), eq("PENDING")))
                .thenReturn(Arrays.asList(transfer1));
        var transfers = fundTransferService.getScheduledTransfers(Long.valueOf("1234567890"));
        assertNotNull(transfers);
        assertEquals(1, transfers.size());
        assertEquals(1L, transfers.get(0).getTransferId());
    }

    @Test
    void testGetScheduledTransfers_emptyList() {
        when(fundTransferRepository.findBySenderAccountIdAndStatus(Long.valueOf(anyLong()), eq("PENDING")))
                .thenReturn(Arrays.asList());
        var transfers = fundTransferService.getScheduledTransfers(Long.valueOf("1234567890"));
        assertNotNull(transfers);
        assertTrue(transfers.isEmpty());
    }

    @Test
    void testCancelTransfer_success() {
        FundTransfer transfer = new FundTransfer();
        transfer.setTransferId(1L);
        transfer.setSenderAccountId(Long.valueOf("1234567890"));
        transfer.setRecipientAccountId(Long.valueOf("0987654321"));
        transfer.setTransferAmount(new BigDecimal("5000.00"));
        transfer.setScheduledDate(validRequest.getTransferDate());
        transfer.setStatus(TransactionStatus.PENDING);

        when(fundTransferRepository.findById(1L)).thenReturn(Optional.of(transfer));
        when(fundTransferRepository.save(any(FundTransfer.class))).thenReturn(transfer);
        fundTransferService.cancelTransfer(1L);
        assertEquals(TransactionStatus.CANCELED, transfer.getStatus());
        verify(fundTransferRepository, times(1)).save(any(FundTransfer.class));
    }


    @Test
    void testCancelTransfer_transferNotFound() {
        when(fundTransferRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TransferNotFoundException.class, () -> fundTransferService.cancelTransfer(1L));
    }

    @Test
    void testCancelTransfer_invalidStatus() {
        FundTransfer transfer = new FundTransfer();
        transfer.setTransferId(1L);
        transfer.setStatus(TransactionStatus.COMPLETED);

        when(fundTransferRepository.findById(1L)).thenReturn(Optional.of(transfer));
        assertThrows(InvalidOperationException.class, () -> fundTransferService.cancelTransfer(1L));
    }

    @Test
    void testCancelTransfer_transferInPast() {
        FundTransfer transfer = new FundTransfer();
        transfer.setTransferId(1L);
        transfer.setScheduledDate(LocalDateTime.now().minusDays(1));
        transfer.setStatus(TransactionStatus.PENDING);

        when(fundTransferRepository.findById(1L)).thenReturn(Optional.of(transfer));
        assertThrows(InvalidOperationException.class, () -> fundTransferService.cancelTransfer(1L));
    }
}