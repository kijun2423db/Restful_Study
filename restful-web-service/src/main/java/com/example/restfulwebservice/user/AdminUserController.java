package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){
        List<User> users = service.findAll();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate","password");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);
        return mapping;
    }
//    public User retrieveAllUsers(){
//        return service.findAll();
//    }

    @GetMapping("/v1/users/{id}")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id){ // string to int
        User user = service.findOne(id);

        if(user == null){ // user not found
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate","password", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }
    @GetMapping("/v2/users/{id}")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id){ // string to int
        User user = service.findOne(id);

        if(user == null){ // user not found
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        // User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2); // user 값을 userV2로 복사
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate","grade");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }
}