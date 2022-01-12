package com.app.lms.dto;

import lombok.Data;

/**
 * This class holds status, message & data (details of the books)
 */
@Data
public class ResponseDTO {
    private int status;
    private String message;
    private Object data;
}
