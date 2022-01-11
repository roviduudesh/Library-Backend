package com.app.lms.dto;

import lombok.Data;

@Data
public class BookDTO {
    private int id;
    private String copyrightDate;
    private String biblioNumber;
    private String author;
    private String isbn;
    private String title;
    private String type;
    private String subject;
}
