package com.develhope.UnitTest;

import com.develhope.UnitTest.controllers.UserController;
import com.develhope.UnitTest.entities.User;
import com.develhope.UnitTest.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.get;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTestController {
    @Autowired
    private UserController studentController;

    @Autowired
    private UserService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        assertThat(studentController).isNotNull();
    }

    @Test
    @Order(1)
    void createUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("ivan");
        user.setLastName("piccioni");
        user.setActive(true);

        String studentJSON = objectMapper.writeValueAsString(user);

        MvcResult resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON)).andDo(print())
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    @Order(4)
    void getAllUser() throws Exception {
        createUser();
        MvcResult result = this.mockMvc.perform((RequestBuilder) get("/v1/getall"))
                .andDo(print()).andReturn();

        List<User> userFromResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(userFromResponseList.size()).isNotZero();


    }

    @Test
    @Order(3)
    void getUser() throws Exception {
        Long studentId = 1L;
        createUser();

        MvcResult resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/v1/getsingle/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(studentId)).andReturn();

    }

    @Test
    @Order(2)
    void updateUserById() throws Exception {
        Long studentId = 1L;
        createUser();
        User updatedUser = new User(studentId, "Updated", "Name", false);
        String studentJSON = objectMapper.writeValueAsString(updatedUser);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/v1/putuser/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON).content(studentJSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertNotNull(content);
    }

    @Test
    @Order(6)
    void deleteUser() throws Exception {
        Long studentId = 1L;
        createUser();

        MvcResult result = mockMvc.perform(delete("/v1/deletesingle/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();


    }

    @Test
    @Order(5)
    void userActivation() throws Exception {
        Long id = 1L;
        createUser();
        boolean isWorking = false;

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/active/{id}", id)
                        .param("isActive", String.valueOf(isWorking)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

