package com.url_shortener.ShortURL;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.url_shortener.ShortURL.Dto.ShortenResponseDto;

@Service
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final CodeGenerator codeGenerator;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UrlService(ShortUrlRepository shortUrlRepository, CodeGenerator codeGenerator) {
        this.shortUrlRepository = shortUrlRepository;
        this.codeGenerator = codeGenerator;
    }

    @Transactional
    public ShortenResponseDto createShortUrl(String originalUrl) {
        for (int attempt = 0; attempt < 5; attempt++) {
            String code = codeGenerator.generateCode();

            // Check for collision
            if (shortUrlRepository.findByShortCode(code).isPresent()) {
                continue;
            }

            ShortUrl shortUrl = new ShortUrl();
            shortUrl.setShortCode(code);
            shortUrl.setOriginalUrl(originalUrl);
            shortUrl.setClickCount(0);

            ShortUrl saved = shortUrlRepository.save(shortUrl);
            String fullShortUrl = String.format("%s/%s", baseUrl, saved.getShortCode());
            return new ShortenResponseDto(saved.getShortCode(), fullShortUrl, saved.getOriginalUrl());
        }

        throw new IllegalStateException("Failed to generate unique short code. Please try again.");
    }

    
    @Transactional
    public String getUrl(String code) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found for code: " + code));
        shortUrlRepository.incrementClickCount(code);

        return shortUrl.getOriginalUrl();
    }

    @Transactional(readOnly = true)
    public long getStats(String code) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found for code: " + code));

        return shortUrl.getClickCount();
    }
}