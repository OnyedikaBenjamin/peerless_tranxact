package org.example.peerless_transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FundTransferResponse {

    private Long transferId;
    private Long senderAccountId;

    public FundTransferResponse(Long transferId, Long senderAccountId, Long recipientAccountId, BigDecimal transferAmount, LocalDateTime transferDate) {
        this.transferId = transferId;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.transferAmount = transferAmount;
        this.scheduledDate = transferDate;
    }

    private Long recipientAccountId;
    private BigDecimal transferAmount;
    private LocalDateTime scheduledDate;

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Long getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(Long recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

}
