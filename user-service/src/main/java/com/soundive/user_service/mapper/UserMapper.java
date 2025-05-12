package com.soundive.user_service.mapper;

import com.soundive.common.mapper.BaseMapper;
import com.soundive.user_service.dto.UserDTO;
import com.soundive.user_service.entity.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper for User Entity and UserDTO.
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper extends BaseMapper<User, UserDTO> {
    @Override
    UserDTO toDto(User entity);

    @Mapping(target = "id", defaultExpression = "java(com.soundive.common.util.IdGeneratorFactory.getIdStrategy(\"UUID\").generateId().toString())")
    @Override
    User toEntity(UserDTO dto);

    @Override
    List<UserDTO> toDtoList(List<User> entities);

    @Override
    List<User> toEntityList(List<UserDTO> dtos);

}
