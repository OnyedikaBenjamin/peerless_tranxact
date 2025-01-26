package org.example.peerless_transaction.exception;

public class InvalidTransferAmountException extends RuntimeException {

    public InvalidTransferAmountException(String message) {
        super(message);
    }
}
