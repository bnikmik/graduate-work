package ru.skypro.homework.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter extends AbstractJackson2HttpMessageConverter {

    protected MessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }

}