package com.soundive.user_service.mapper;

import com.soundive.common.mapper.BaseMapper;
import com.soundive.common.mapper.BaseMapperDecorator;
import com.soundive.user_service.dto.UserDTO;
import com.soundive.user_service.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Custom Decorator for UserMapper to handle @ExcludeFromDto logic.
 */
@Component
@Slf4j
public class UserMapperDecorator extends BaseMapperDecorator<User, UserDTO> implements UserMapper {

    private UserMapper delegate;

    @Autowired
    @Qualifier("delegate")
    public void UserMapperDecorator(UserMapper delegate) {
        this.delegate = delegate;
        super.setDelegate(delegate);
    }

    @Override
    public UserDTO toDto(User entity) {
        return super.toDto(entity);
    }

    @Override
    public List<UserDTO> toDtoList(List<User> entities) {
        return super.toDtoList(entities);
    }

    @Override
    public List<User> toEntityList(List<UserDTO> dtos) {
        return super.toEntityList(dtos);
    }
    @Override
    public User toEntity(UserDTO dto) {
        return super.toEntity(dto);
    }




}