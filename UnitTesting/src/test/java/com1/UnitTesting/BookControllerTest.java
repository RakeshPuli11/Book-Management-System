package com1.UnitTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;
    ObjectMapper  objm = new ObjectMapper();// we can't convert a pojo object directly into json without springboot help
    ObjectWriter  objw = objm.writer();// but we can use object mapper and writer

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    Book record1 = new Book(1L,"Berserk","teaches you to survive",9);
    Book record2 = new Book(2L,"VinlandSaga","i have no enimies",9);

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    // get
    @Test
    public void getAllBooks() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(record1, record2));
        Mockito.when(bookRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].summary", Matchers.is("teaches you to survive")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Berserk")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("VinlandSaga")));
        ;//         $[1] -- index 2
    }

    // get by id
    @Test
    public  void getBookById()throws Exception{
        Mockito.when(bookRepository.findById(record1.getId())).thenReturn(java.util.Optional.of(record1));
        mockMvc.perform(MockMvcRequestBuilders.get("/book/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", notNullValue()));
    }

    // post
    @Test
    public void createRecord()throws Exception{
        Book r = Book.builder()//  this is possible because @Bulider annotation from lombok in entity
                .id(4L)
                .name("vagabond")
                .summary("know peace")
                .rating(9)
                .build();
        Mockito.when(bookRepository.save(r)).thenReturn(r);
    String x = objw.writeValueAsString(r);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book/add")
                .contentType(MediaType.APPLICATION_JSON)//returning content type of book in json format
                .accept(MediaType.APPLICATION_JSON)// accepting request by json type
                .content(x);

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", notNullValue()));
    }

    // put
    @Test
    public void updateBookRecord()throws Exception{
        Book updtr = Book.builder()
                .id(1l)
                .name("updated name")
                .summary("ipdated summary")
                .rating(1).build();

        Mockito.when(bookRepository.findById(record1.getId())).thenReturn(java.util.Optional.of(record1));
        Mockito.when(bookRepository.save(updtr)).thenReturn(updtr);

        String xupdate = objw.writeValueAsString(updtr);
        MockHttpServletRequestBuilder mockReq = MockMvcRequestBuilders.put("/book/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(xupdate);
        mockMvc.perform(mockReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",Matchers.is("updated name")))
        ;

    }

    // TDD  delete
    @Test
    public void del()throws Exception{
        Mockito.when(bookRepository.findById(record2.getId())).thenReturn(Optional.of(record2));
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/delete/2")
                .contentType((MediaType.APPLICATION_JSON))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }
}
