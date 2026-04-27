package com.taskmanagement.task_management_system.Mapper;


import com.taskmanagement.task_management_system.Model.dto.RegisterRequest;
import com.taskmanagement.task_management_system.Model.dto.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    Users RegisterToUser(RegisterRequest request);


    @Mapping(target = "fullName",
            expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserInfo entityToDto(Users user);
}
