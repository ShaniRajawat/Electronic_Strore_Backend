package com.sr.electronic.store.Electronic_Store.services;

import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.UserDto;
import com.sr.electronic.store.Electronic_Store.entities.Role;
import com.sr.electronic.store.Electronic_Store.entities.User;
import com.sr.electronic.store.Electronic_Store.repositories.RoleRepository;
import com.sr.electronic.store.Electronic_Store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    User user;
    Role role;
    String roleId;
    @Autowired
    private ModelMapper mapper;

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
        roleId="1";
    }

    @Test
    public void createUserTest(){

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));

        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));
        System.out.println(user1.getName());

        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Shani",user1.getName());

    }

    @Test
    public void updateUserTest(){
        String userId ="Hello";

        UserDto userDto = UserDto.builder()
                .name("Shani Rajawat")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("xyz")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto userDto1 = userService.updateUser(userDto, userId);
        System.out.println(userDto1.getName());
        System.out.println(userDto1.getImageName());
        Assertions.assertNotNull(userDto);

    }

    @Test
    public void deleteUserTest(){
        String userId ="Shani";

        Mockito.when(userRepository.findById("Shani")).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);

    }

    @Test
    public void getAllUsersTest(){
        User user1 = User.builder().name("Honey")
                .email("honey.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("honey.png")
                .password("abcd")
                .roles(Set.of(role))
                .build();

        User user2 = User.builder().name("Satyam")
                .email("satyam.parmar.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("satyam.png")
                .password("abcd")
                .roles(Set.of(role))
                .build();


        List<User> userList = Arrays.asList(user, user1, user2);
        Page<User> page = new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> allUser = userService.getAllUser(1,2,"name","asc");
        Assertions.assertEquals(3,allUser.getContent().size());
    }

    @Test
    public void getUserByIdTest(){

        String userId= "Hello";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //actual call of service method
        UserDto userdto = userService.getByUserId(userId);

        Assertions.assertNotNull(userdto);
        Assertions.assertEquals(user.getName(), userdto.getName()," Test Failed");

    }

    @Test
    public void getUserByEmailTest(){

        String emailId ="Rajawat.sunny512@gmail.com";
        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserByEmail(emailId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(), userDto.getEmail(), "Email not match");

    }

    @Test
    public void searchUserTest(){
        User user1 = User.builder().name("Shani singh")
                .email("shani.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .roles(Set.of(role))
                .build();
        User user2 = User.builder().name("Satyam singh")
                .email("shani.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .roles(Set.of(role))
                .build();
        User user3 = User.builder().name("SUllas singh")
                .email("shani.rajawat.31@gmail.com")
                .about("This is Testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .roles(Set.of(role))
                .build();

        String keyword ="singh";
        Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(Arrays.asList(user, user1, user2, user3));

        List<UserDto> dtoList = userService.searchUser(keyword);

        Assertions.assertEquals(4 ,dtoList.size(),"Size not matched");

    }

    @Test
    public void findUserByEmailOptionalTest(){
        String email = "shani.rajawat.31@gmail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> userByEmailOptional = userService.findUserByEmailOptional(email);

        User user1 = userByEmailOptional.get();

        Assertions.assertEquals(email, user1.getEmail());
    }

    // Write all cases for rest service class
}
