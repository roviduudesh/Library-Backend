package com.app.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class holds the details of a book.
 */
@Data
public class BookDTO {
    private String copyrightDate;
    private String biblioNumber;
    private String author;
    private String isbn;
    private String title;
    private String type;
    private String subject;
}
