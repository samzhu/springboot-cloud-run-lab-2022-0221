package com.example.demo.book.interfaces.rest.impl;


import com.example.demo.book.interfaces.rest.BookApi;
import com.example.demo.book.interfaces.rest.dto.BookCreateDTO;
import com.example.demo.book.interfaces.rest.dto.BookViewDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController implements BookApi {



  @Override
  public ResponseEntity<BookViewDTO> createBook(@Valid BookCreateDTO bookCreateDTO) {
    // TODO Auto-generated method stub
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BookViewDTO(bookCreateDTO.id(), null, null, null, null));
  }

  @Override
  public ResponseEntity<List<BookViewDTO>> getBooks() {





    // TODO Auto-generated method stub
    return ResponseEntity.status(HttpStatus.OK)
        .body(Arrays.asList(new BookViewDTO(null, null, null, null, null)));
  }

  @Override
  public ResponseEntity<BookViewDTO> getBookByBookId(Integer bookId) {
    // TODO Auto-generated method stub
    return ResponseEntity.status(HttpStatus.OK)
        .body(new BookViewDTO(bookId, null, null, null, null));
  }
}
