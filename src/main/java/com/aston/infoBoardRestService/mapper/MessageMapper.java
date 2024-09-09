package com.aston.infoBoardRestService.mapper;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDto toDTO(Message message);
    Message toEntity(MessageDto messageDTO);
    List<MessageDto> toDTOList(List<Message> messages);
    List<Message> toEntityList(List<MessageDto> messageDtos);
}
