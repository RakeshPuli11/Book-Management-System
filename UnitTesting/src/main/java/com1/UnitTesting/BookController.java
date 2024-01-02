package com1.UnitTesting;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {
    @Autowired
    BookRepository br;

    @GetMapping("/all")
    public List<Book> getAllBooks(){
        return br.findAll();
    }
    @GetMapping("/{bkid}")
    public Optional<Book> getBookById(@PathVariable Long bkid){
        return br.findById(bkid);
    }
    @PostMapping("/add")
    public Book createbk(@RequestBody Book book){
        return br.save(book);
    }
//    @PutMapping("/update")
//    public Book BookupdateBk(@RequestBody Book bookUp) throws NotFoundException {
//        Book dbBook = br.findById(bookUp.getId()).get();
//
//        if (!Objects.nonNull(dbBook)) {
//            throw new NotFoundException();
//        }
//        dbBook.setName(bookUp.getName());
//        dbBook.setSummary(bookUp.getSummary());
//        dbBook.setRating(bookUp.getRating());
//        return br.save(dbBook);
//    }
    @PutMapping("/update")
    public Book updateBook(@RequestBody Book bookUp)throws Exception {// we are using br twotimes so remember when we are doing unit tests we use two
        Optional<Book> optionalDbBook = br.findById(bookUp.getId());

        if (optionalDbBook.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with ID: " + bookUp.getId());
        }

        Book dbBook = optionalDbBook.get();
        dbBook.setName(bookUp.getName());
        dbBook.setSummary(bookUp.getSummary());
        dbBook.setRating(bookUp.getRating());

        return br.save(dbBook);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBookById(@PathVariable Long id)throws ChangeSetPersister.NotFoundException {
        if(!br.findById(id).isPresent()){
            throw new ChangeSetPersister.NotFoundException();
        }
        br.deleteById(id);
    }
}
