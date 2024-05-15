package com.eaproc.tutorials.librarymanagement.web.mapper.impl;

import com.eaproc.tutorials.librarymanagement.domain.model.UserEntity;
import com.eaproc.tutorials.librarymanagement.web.dto.UserDto;
import com.eaproc.tutorials.librarymanagement.web.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    private final ModelMapper modelMapper;

    public UserMapperImpl( ) {
        this.modelMapper = new ModelMapper();
        // Configure the model mapper if needed
        this.modelMapper.typeMap(UserEntity.class, UserDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getRoleEntity().getId(), UserDto::setRoleId);
            mapper.map(src -> src.getRoleEntity().getName(), UserDto::setRole);
        });
    }

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
