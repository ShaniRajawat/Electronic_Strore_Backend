package com.sr.electronic.store.Electronic_Store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.UserDto;
import com.sr.electronic.store.Electronic_Store.entities.Role;
import com.sr.electronic.store.Electronic_Store.entities.User;
import com.sr.electronic.store.Electronic_Store.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    private Role role;

    private User user;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        role = Role.builder().roleId("1").roleName("NORMAL").build();

        user = User.builder().name("Shani singh")
                .email("shani.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .roles(Set.of(role))
                .build();
    }

    @Test
    public void createUserTest() throws Exception {
        // call /users +POST + user data as json
        //data as json + status created

        UserDto dto = mapper.map(user, UserDto.class);

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        //actual request for URL
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void updateUserTest() throws Exception {

        // /users/(userId) + PUT request + json
        String userId="123";
        UserDto dto = this.mapper.map(user, UserDto.class);

        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/"+userId)
                //PUT header first from postman login
//                        .header(HttpHeaders.AUTHORIZATION,"Bearer PUT HEADER HERE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToObjectToJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());

    }

    @Test
    public void getAllUsersTest() throws Exception {

        UserDto userDto1 = UserDto.builder().name("Shani singh")
                .email("shani.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .build();

        UserDto userDto2 = UserDto.builder().name("Honey singh")
                .email("shani.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .build();

        UserDto userDto3 = UserDto.builder().name("Rishi singh")
                .email("shani.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .build();

        PageableResponse<UserDto> pageableResponse =new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(userDto1,userDto2,userDto3));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setPageNumber(100);
        pageableResponse.setTotalElement(1000);
        Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);


        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String convertToObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
