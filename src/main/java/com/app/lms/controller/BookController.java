package com.app.lms.controller;

import com.app.lms.service.BookService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@Data
@RequestMapping(path = "/api/v1/book")
public class BookController {

    private final BookService bookService;

    /**
     * This API will return the book details.
     * @return responseDto
     */
    @GetMapping(value = "/details")
    public ResponseEntity<?> getBookList(){
        ResponseEntity<?> responseDto =  bookService.getData();
        return responseDto;
    }
}
