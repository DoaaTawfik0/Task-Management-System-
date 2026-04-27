package com.taskmanagement.task_management_system.Mapper;

public interface Mapper <A , B>{
    // a => entity , b => dto
    B mapTo(A a);
    A mapFrom(B b);
}
