package com.app.lms.service;

import com.app.lms.dto.BookDTO;
import com.app.lms.dto.ResponseDTO;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class BookService {
    /**
     * This method will get data from given url.
     * @return responseDto
     */
    public ResponseEntity<?> getData() {
        ResponseDTO responseDto = new ResponseDTO();
        List<BookDTO> bookDTOList = null;
        int value;
        String message;
        try {
            String url = "https://prosentient.intersearch.com.au/cgi-bin/koha/svc/report?id=1&annotated=1";
            HttpClient client = HttpClient.newHttpClient(); // Create http client
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build(); // Build http request using the given url
            bookDTOList = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()) // Get the response as string
                    .thenApply(HttpResponse::body) // Body method will be called once response received
                    .thenApply(data -> parse(data)) // Once body method is done, call parse method to format the received data
                    .join(); // Returns the final result;

            // Validate response value & message
            value = bookDTOList.size() > 0 ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value();
            message = bookDTOList.size() > 0 ? "Data Fetched Successfully" : "Book Details Not Found";
        }catch(Exception ex){
            ex.printStackTrace();
            value = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        // Set response values
        responseDto.setStatus(value);
        responseDto.setData(bookDTOList);
        responseDto.setMessage(message);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    /**
     * This method will extract data from responseBody & format accordingly.
     * @param responseBody
     * @return bookDTOList
     */
    public List parse(String responseBody){
        BookDTO bookDTO;
        List<BookDTO> bookDTOList = new ArrayList<>(); // This list will hold all the book details
        try {
            JSONArray books = new JSONArray(responseBody); // Extract book details to json array

            for (int i = 0; i < books.length(); i++) { // Iterating books
                JSONObject jsonObject = books.getJSONObject(i); // Get book object from books
                bookDTO = new BookDTO(); // Initialize bookDTO object

                // Retrieving book details from json object
                String copyrightDate = jsonObject.getString("copyrightdate");
                String biblioNumber = jsonObject.getString("biblionumber");
                String author = jsonObject.getString("author");
                String isbn = jsonObject.getString("isbn");
                String title = jsonObject.getString("title");
                String type = jsonObject.getString("type");
                String subject = jsonObject.getString("Subjects");

                // Validates null values by replacing "-"
                bookDTO.setCopyrightDate(copyrightDate.equalsIgnoreCase("null") ? "-" : copyrightDate);
                bookDTO.setBiblioNumber(biblioNumber.equalsIgnoreCase("null") ? "-" : biblioNumber);
                bookDTO.setAuthor(author.equalsIgnoreCase("null") ? "-" : author);
                bookDTO.setIsbn(isbn.equalsIgnoreCase("null") ? "-" : isbn);
                bookDTO.setTitle(title.equalsIgnoreCase("null") ? "-" : title);
                bookDTO.setType(type.equalsIgnoreCase("null") ? "-" : type);
                bookDTO.setSubject(subject.equalsIgnoreCase("null") ? "-" : subject);

                bookDTOList.add(bookDTO); // Add book object to book list
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookDTOList;
    }
}
