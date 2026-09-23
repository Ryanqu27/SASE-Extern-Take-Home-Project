package com.url_shortener.ShortURL;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url_shortener.ShortURL.Dto.ShortenRequestDto;
import com.url_shortener.ShortURL.Dto.ShortenResponseDto;

import jakarta.validation.Valid;

@RestController 
@RequestMapping
public class UrlController {
    
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponseDto> shorten(@Valid @RequestBody ShortenRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(urlService.createShortUrl(request.url()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = urlService.getUrl(code);
        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, originalUrl).build();
    }

    @GetMapping("/{code}/stats")
    public long getStats(@PathVariable String code) {
        return urlService.getStats(code);
    }
}
