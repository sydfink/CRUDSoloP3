package com.galvanize.SoloP3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Calendar;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    TodoRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    @Transactional
    @Rollback
    public void createNewToDo() throws Exception{

        this.mvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"take out the trash\", \"dueDate\": \"2022-04-06\", \"priority\": \"medium\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.dueDate", is("2022-04-06")))
                .andExpect(jsonPath("$.priority", is("medium")))
                .andExpect(jsonPath("$.description", is("take out the trash")));
    }

    @Test
    @Transactional
    @Rollback
    public void getOneToDo() throws Exception{
        Calendar dog = Calendar.getInstance();
        dog.set(2022, Calendar.APRIL, 06);
        Todo test = new Todo("walk the dog", "high", dog.getTime());
        Todo record = repository.save(test);

        this.mvc.perform(get("/todo/" + record.getId()))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description", is("walk the dog")))
                .andExpect(jsonPath("$.priority", is("high")))
                .andExpect(jsonPath("$.dueDate", is("2022-04-06")));

    }

    @Test
    @Transactional
    @Rollback
    public void getAllToDo() throws Exception{
        Calendar dog = Calendar.getInstance();
        dog.set(2022, Calendar.APRIL, 06);
        Todo test = new Todo("walk the dog", "medium", dog.getTime());
        Todo record = repository.save(test);

        Calendar dogFood = Calendar.getInstance();
        dogFood.set(2022, Calendar.APRIL, 07);
        Todo test2 = new Todo("buy dog food", "high", dogFood.getTime());
        Todo record2 = repository.save(test2);

        Calendar dogToy = Calendar.getInstance();
        dogToy.set(2022, Calendar.APRIL, 06);
        Todo test3 = new Todo("put away dog toys", "low", dogToy.getTime());
        Todo record3 = repository.save(test3);

        this.mvc.perform(get("/todo"))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].description", is("walk the dog")))
                .andExpect(jsonPath("$[0].priority", is("medium")))
                .andExpect(jsonPath("$[0].dueDate", is("2022-04-06")))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].description", is("buy dog food")))
                .andExpect(jsonPath("$[1].priority", is("high")))
                .andExpect(jsonPath("$[1].dueDate", is("2022-04-07")))
                .andExpect(jsonPath("$[2].id").isNumber())
                .andExpect(jsonPath("$[2].description", is("put away dog toys")))
                .andExpect(jsonPath("$[2].priority", is("low")))
                .andExpect(jsonPath("$[2].dueDate", is("2022-04-06")));
    }

    @Test
    @Transactional
    @Rollback
    public void updateToDo() throws Exception{
        Calendar dog = Calendar.getInstance();
        dog.set(2022, Calendar.APRIL, 06);
        Todo test = new Todo("walk the dog", "high", dog.getTime());
        Todo record = repository.save(test);

        this.mvc.perform(patch("/todo/" + record.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"walk the dog\", \"dueDate\": \"2022-04-06\", \"priority\": \"medium\"}"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description", is("walk the dog")))
                .andExpect(jsonPath("$.priority", is("medium")))
                .andExpect(jsonPath("$.dueDate", is("2022-04-06")));
    }

    @Test
    @Transactional
    @Rollback
    public void deleteToDo() throws Exception{
        Calendar dog = Calendar.getInstance();
        dog.set(2022, Calendar.APRIL, 06);
        Todo test = new Todo("walk the dog", "high", dog.getTime());
        Todo record = repository.save(test);

        Calendar dog2 = Calendar.getInstance();
        dog2.set(2022, Calendar.APRIL, 06);
        Todo test2 = new Todo("walk the dog", "high", dog2.getTime());
        Todo record2 = repository.save(test2);


        this.mvc.perform(delete("/todo/" + record2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"walk the dog\", \"dueDate\": \"2022-04-06\", \"priority\": \"medium\"}"))
                .andExpect(jsonPath("$[1].id").doesNotHaveJsonPath());
    }

}
