package com.taskmanagement.task_management_system.Mapper;


import com.taskmanagement.task_management_system.Model.dto.RegisterRequest;
import com.taskmanagement.task_management_system.Model.dto.UpdateUserRequest;
import com.taskmanagement.task_management_system.Model.dto.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.Users;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {
    Users RegisterToUser(RegisterRequest request);


    @Mapping(target = "fullName",
            expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserInfo entityToDto(Users user);

    // Update: Ignore null fields to preserve existing values
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserRequest dto, @MappingTarget Users entity);

}
