package com.sr.electronic.store.Electronic_Store.helper;

import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.dtos.UserDto;
import com.sr.electronic.store.Electronic_Store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    //We get Page of type User amd we need to return Page of type DTO
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
        List<U> users = page.getContent();
        List<V> dtoList = users.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElement(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }
}
