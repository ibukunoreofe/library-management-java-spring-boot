package com.eaproc.tutorials.librarymanagement.web.mapper.impl;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.web.mapper.Mapper;
import com.eaproc.tutorials.librarymanagement.web.request.book.CreateBookRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class CreateBookRequestMapperImpl implements Mapper<BookEntity, CreateBookRequest> {

    private final ModelMapper modelMapper;
    private static final Logger logger = Logger.getLogger(CreateBookRequestMapperImpl.class.getName());

    public CreateBookRequestMapperImpl() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public CreateBookRequest mapTo(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, CreateBookRequest.class);
    }

    @Override
    public BookEntity mapFrom(CreateBookRequest createBookRequest) {
        return modelMapper.map(createBookRequest, BookEntity.class);
    }
}
