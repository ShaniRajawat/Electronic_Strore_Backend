package com.sr.electronic.store.Electronic_Store.services;

import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.UserDto;
import com.sr.electronic.store.Electronic_Store.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId);

    //get by userId
    UserDto getByUserId(String userId);

    //get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single users by email
    UserDto getUserByEmail(String email);

    //Search user
    List<UserDto> searchUser(String keyword);

    //Other user specific details
    Optional<User> findUserByEmailOptional(String email);
}
