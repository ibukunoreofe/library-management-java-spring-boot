package com.eaproc.tutorials.librarymanagement.web.mapper.impl;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.web.dto.BookDto;
import com.eaproc.tutorials.librarymanagement.web.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDto> {

    private final ModelMapper modelMapper;

    public BookMapperImpl( ) {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public BookDto mapTo(BookEntity userEntity) {
        return modelMapper.map(userEntity, BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto userDto) {
        return modelMapper.map(userDto, BookEntity.class);
    }
}
