package com.url_shortener.ShortURL.Dto;

public record ShortenResponseDto(
    String shortCode,
    String shortUrl,
    String originalUrl
) {}