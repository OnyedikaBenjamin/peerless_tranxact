//package org.example.peerless_transaction.dto;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//
//public class ResponseWrapper<T> {
//    private String status;
//    private String message;
//    private T data;
//
//    public ResponseWrapper(String status, String message, T data) {
//        this.status = status;
//        this.message = message;
//        this.data = data;
//    }
//
//    // Getters and setters
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//}
//
package org.example.peerless_transaction.dto;

public class ResponseWrapper<T> {

    private boolean success;
    private T data;
    private String message;

    // Constructor for successful response
    public ResponseWrapper(T data) {
        this.success = true;
        this.data = data;
        this.message = "Operation was successful";
    }

    // Constructor for failed response
    public ResponseWrapper(String message) {
        this.success = false;
        this.data = null;
        this.message = message;
    }

    // Getter and Setter methods
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
