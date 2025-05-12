package com.soundive.auth_service.mapper;

import com.soundive.auth_service.dto.RefreshTokenDTO;
import com.soundive.auth_service.entity.RefreshToken;
import com.soundive.common.mapper.BaseMapper;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

@DecoratedWith(RefreshTokenMapperDecorator.class)
public interface RefreshTokenMapper extends BaseMapper<RefreshToken, RefreshTokenDTO> {
    @Override
    RefreshTokenDTO toDto(RefreshToken entity);

    @Mapping(target = "id", defaultExpression = "java(com.soundive.common.util.IdGeneratorFactory.getIdStrategy(\"UUID\").generateId().toString())")
    @Override
    RefreshToken toEntity(RefreshTokenDTO dto);

    @Override
    List<RefreshTokenDTO> toDtoList(List<RefreshToken> entities);

    @Override
    List<RefreshToken> toEntityList(List<RefreshTokenDTO> dtos);
}
