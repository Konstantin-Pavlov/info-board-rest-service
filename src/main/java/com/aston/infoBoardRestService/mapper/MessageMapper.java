package com.aston.infoBoardRestService.mapper;

import com.aston.infoBoardRestService.dto.MessageDto;
import com.aston.infoBoardRestService.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDto toMessageDto(Message message);
    Message toMessage(MessageDto messageDTO);
    List<MessageDto> toMessageDtoList(List<Message> messages);
    List<Message> toMessageList(List<MessageDto> messageDtos);
}
