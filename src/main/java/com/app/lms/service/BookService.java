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

    public ResponseEntity<?> getData() {
        ResponseDTO responseDto = new ResponseDTO();

        try {
            String url = "https://prosentient.intersearch.com.au/cgi-bin/koha/svc/report?id=1&annotated=1";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            List<BookDTO> bookDTOList = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(data -> parse(data))
                    .join();

            String message = bookDTOList.size() > 0 ? "Data Fetched Successfully" : "Book Details Not Found";
            int value = bookDTOList.size() > 0 ? HttpStatus.OK.value() : HttpStatus.NO_CONTENT.value();

            responseDto.setStatus(value);
            responseDto.setData(bookDTOList);
            responseDto.setMessage(message);
        }catch(Exception ex){
            ex.printStackTrace();
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public List parse(String responseBody){
        List<BookDTO> bookDTOList = new ArrayList<>();
        try {
            JSONArray books = new JSONArray(responseBody);

            BookDTO bookDTO;
            for (int i = 0; i < books.length(); i++) {

                JSONObject jsonObject = books.getJSONObject(i);
                bookDTO = new BookDTO();

                String copyrightDate = jsonObject.getString("copyrightdate");
                String biblioNumber = jsonObject.getString("biblionumber");
                String author = jsonObject.getString("author");
                String isbn = jsonObject.getString("isbn");
                String title = jsonObject.getString("title");
                String type = jsonObject.getString("type");
                String subject = jsonObject.getString("Subjects");

                bookDTO.setId(i);
                bookDTO.setCopyrightDate(copyrightDate);
                bookDTO.setBiblioNumber(biblioNumber);
                bookDTO.setAuthor(author);
                bookDTO.setIsbn(isbn.equalsIgnoreCase("null") ? "-" : isbn);
                bookDTO.setTitle(title);
                bookDTO.setType(type);
                bookDTO.setSubject(subject);

                bookDTOList.add(bookDTO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookDTOList;
    }
}
