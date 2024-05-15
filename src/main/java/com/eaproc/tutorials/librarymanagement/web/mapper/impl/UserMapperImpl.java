package com.eaproc.tutorials.librarymanagement.web.mapper.impl;

import com.eaproc.tutorials.librarymanagement.domain.model.User;
import com.eaproc.tutorials.librarymanagement.web.dto.UserDto;
import com.eaproc.tutorials.librarymanagement.web.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<User, UserDto> {

    private final ModelMapper modelMapper;

    public UserMapperImpl( ) {
        this.modelMapper = new ModelMapper();
        // Configure the model mapper if needed
        this.modelMapper.typeMap(User.class, UserDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getRoleEntity().getId(), UserDto::setRoleId);
            mapper.map(src -> src.getRoleEntity().getName(), UserDto::setRole);
        });
    }

    @Override
    public UserDto mapTo(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
