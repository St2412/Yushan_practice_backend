package com.example.demo.common.security;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import java.util.Arrays;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonXssEscapeConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer xssEscapingCustomizer() {
        return builder -> builder.postConfigurer(objectMapper -> objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes()));
    }

    static class HtmlCharacterEscapes extends CharacterEscapes {

        private final int[] asciiEscapes;

        HtmlCharacterEscapes() {
            asciiEscapes = Arrays.copyOf(CharacterEscapes.standardAsciiEscapesForJSON(), 128);
            asciiEscapes['<'] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['>'] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['&'] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['\''] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['('] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes[')'] = CharacterEscapes.ESCAPE_STANDARD;
        }

        @Override
        public int[] getEscapeCodesForAscii() {
            return asciiEscapes;
        }

        @Override
        public SerializableString getEscapeSequence(int ch) {
            return new SerializedString(String.format("\\u%04x", ch));
        }
    }
}
