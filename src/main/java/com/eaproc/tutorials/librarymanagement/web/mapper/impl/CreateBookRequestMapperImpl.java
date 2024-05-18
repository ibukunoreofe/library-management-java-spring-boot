package com.eaproc.tutorials.librarymanagement.web.mapper.impl;

import com.eaproc.tutorials.librarymanagement.domain.model.BookEntity;
import com.eaproc.tutorials.librarymanagement.web.mapper.Mapper;
import com.eaproc.tutorials.librarymanagement.web.request.book.BookRequest;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class CreateBookRequestMapperImpl
        implements Mapper<BookRequest, BookEntity>,
        Converter<BookRequest, BookEntity> {

    private final ModelMapper modelMapper;
    private static final Logger logger = Logger.getLogger(CreateBookRequestMapperImpl.class.getName());

    public CreateBookRequestMapperImpl() {
        this.modelMapper = new ModelMapper();

        // Add custom converter - Not used because auto map kicked in
        modelMapper.addConverter(this);
    }

    @Override
    public BookEntity mapTo(BookRequest createBookRequest) {
        return modelMapper.map(createBookRequest, BookEntity.class);
    }

    @Override
    public BookRequest mapFrom(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookRequest.class);
    }

    @Override
    public BookEntity convert(MappingContext<BookRequest, BookEntity> context) {
        BookRequest source = context.getSource();
        BookEntity destination = context.getDestination();

        // Perform the mapping
        destination.setTitle(source.getTitle());
        destination.setAuthor(source.getAuthor());
        destination.setIsbn(source.getIsbn());
        destination.setPublishedAt(source.getPublishedAtAsDate());
        destination.setCopies(source.getCopies());

        return destination;
    }
}
