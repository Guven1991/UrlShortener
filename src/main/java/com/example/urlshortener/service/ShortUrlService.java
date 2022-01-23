package com.example.urlshortener.service;

import com.example.urlshortener.exception.CodeAlreadyExists;
import com.example.urlshortener.exception.ShortUrlNotFoundException;
import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.util.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final RandomStringGenerator randomStringGenerator;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, RandomStringGenerator randomStringGenerator) {
        this.shortUrlRepository = shortUrlRepository;
        this.randomStringGenerator = randomStringGenerator;
    }

    public List<ShortUrl> getAllShortUrl() {
        return shortUrlRepository.findAll();
    }

    public ShortUrl getUrlByCode(String code) {

        return shortUrlRepository.findAllByCode(code).orElseThrow(
                () -> new ShortUrlNotFoundException("url not found!")
        );
    }

    public ShortUrl create(ShortUrl shortUrl) {
        if(shortUrl.getCode() == null || shortUrl.getCode().isEmpty()){
            shortUrl.setCode(generateCode());
        }
        else if(shortUrlRepository.findAllByCode(shortUrl.getCode()).isPresent()){
            throw new CodeAlreadyExists("code already exists!");
        }
        shortUrl.setCode(shortUrl.getCode().toUpperCase());
            return shortUrlRepository.save(shortUrl);
    }

    private String generateCode(){
        String code;

        do{
            code = randomStringGenerator.generateRandomString();
        }while (shortUrlRepository.findAllByCode(code).isPresent());

        return code;
    }
}
