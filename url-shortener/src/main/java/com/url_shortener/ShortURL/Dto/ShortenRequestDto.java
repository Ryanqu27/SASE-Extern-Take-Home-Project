package com.url_shortener.ShortURL.Dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ShortenRequestDto(
    @NotBlank(message = "URL can't be blank")
    @URL(message = "Must provide a valid URL")
    String url
) {}