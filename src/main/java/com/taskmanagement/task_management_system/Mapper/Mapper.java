package com.taskmanagement.task_management_system.Mapper;

import java.util.List;

public interface Mapper <A , B>{
    // a => entity , b => dto
    B mapTo(A a);
    A mapFrom(B b);
    List<B> mapToList(List<A> a);
}
