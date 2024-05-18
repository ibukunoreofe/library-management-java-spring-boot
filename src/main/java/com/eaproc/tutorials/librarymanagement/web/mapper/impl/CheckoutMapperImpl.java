package com.eaproc.tutorials.librarymanagement.web.mapper.impl;

import com.eaproc.tutorials.librarymanagement.domain.model.CheckoutEntity;
import com.eaproc.tutorials.librarymanagement.web.dto.CheckoutDto;
import com.eaproc.tutorials.librarymanagement.web.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CheckoutMapperImpl implements Mapper<CheckoutEntity, CheckoutDto> {

    private final ModelMapper modelMapper;

    public CheckoutMapperImpl( ) {
        this.modelMapper = new ModelMapper();
        // Configure the model mapper if needed
//        this.modelMapper.typeMap(CheckoutEntity.class, CheckoutDto.class).addMappings(mapper -> {
//            mapper.map(src -> src.getRoleEntity().getId(), CheckoutDto::setRoleId);
//            mapper.map(src -> src.getRoleEntity().getName(), CheckoutDto::setRole);
//        });
    }

    @Override
    public CheckoutDto mapTo(CheckoutEntity checkoutEntity) {
        return modelMapper.map(checkoutEntity, CheckoutDto.class);
    }

    @Override
    public CheckoutEntity mapFrom(CheckoutDto checkoutDto) {
        return modelMapper.map(checkoutDto, CheckoutEntity.class);
    }
}
