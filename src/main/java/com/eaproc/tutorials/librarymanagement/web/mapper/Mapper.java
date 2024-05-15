package com.eaproc.tutorials.librarymanagement.web.mapper;

public interface Mapper<A,B> {

    B mapTo(A a);

    A mapFrom(B b);

}
