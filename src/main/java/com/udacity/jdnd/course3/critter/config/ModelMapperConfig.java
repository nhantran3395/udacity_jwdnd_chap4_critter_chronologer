package com.udacity.jdnd.course3.critter.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper;
    }
}
