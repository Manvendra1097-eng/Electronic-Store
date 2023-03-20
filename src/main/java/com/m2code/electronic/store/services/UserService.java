package com.m2code.electronic.store.services;

import com.m2code.electronic.store.dtos.PageableResponse;
import com.m2code.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserService {
    //    create
    UserDto createUser(UserDto userDto);

    //    update
    UserDto updateUser(UserDto userDto, String userId);

    //    delete
    void deleteUser(String userId);

    //    get all user
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //    get single user by id
    UserDto getUserById(String userId);

    //    get single user by email
    UserDto getUserByEmail(String email);

    //    search user
    List<UserDto> searchUser(String keyword);
}
