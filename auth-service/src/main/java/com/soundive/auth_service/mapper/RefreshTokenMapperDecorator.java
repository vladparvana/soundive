package com.soundive.auth_service.mapper;

import com.soundive.auth_service.dto.RefreshTokenDTO;
import com.soundive.auth_service.entity.RefreshToken;
import com.soundive.common.mapper.BaseMapperDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RefreshTokenMapperDecorator extends BaseMapperDecorator<RefreshToken, RefreshTokenDTO> implements RefreshTokenMapper {
    private RefreshTokenMapper delegate;

    @Autowired
    @Qualifier("delegate")
    public void RefreshTokenMapperDecorator(RefreshTokenMapper delegate) {
        this.delegate = delegate;
        super.setDelegate(delegate);
    }

    @Override
    public RefreshTokenDTO toDto(RefreshToken entity) {
        return super.toDto(entity);
    }

    @Override
    public List<RefreshTokenDTO> toDtoList(List<RefreshToken> entities) {
        return super.toDtoList(entities);
    }

    @Override
    public List<RefreshToken> toEntityList(List<RefreshTokenDTO> dtos) {
        return super.toEntityList(dtos);
    }
    @Override
    public RefreshToken toEntity(RefreshTokenDTO dto) {
        return super.toEntity(dto);
    }
}
