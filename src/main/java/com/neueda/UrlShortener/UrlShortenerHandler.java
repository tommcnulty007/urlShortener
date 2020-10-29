package com.neueda.UrlShortener;

import com.google.common.hash.Hashing;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


@RequestMapping("/")
@RestController
public class UrlShortenerHandler {

    String urlBase = "http://localhost:8080/";

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * The forwardUrl Method is used to retrieve the full URL from the database
     * based on the ID provided which is taken from after the trailing / in the shortened
     * URL created.
     * @param id The ID of the longer URL in the key/value store database.
     * @param resp Object for returning information to the client. Used to redirect from
     *             the localhost URL to the full URL obtained from the database.
     * @throws Exception if no URL can be found corresponding to ID provided.
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("{id}")
    public void forwardUrl(@PathVariable String id, HttpServletResponse resp) throws Exception{
        String url = redisTemplate.opsForValue().get(id);
        System.out.println("URL Retrieved: " + url);
        if(url == null){
            throw new RuntimeException("No URL found for ID : " + id);
        }

        resp.sendRedirect(url);
    }

    /**
     * The createShortUrl is used with a POST request to retrieve a shortened URL.
     * @param url The entire URL to be hashed and stored as an ID in the database.
     *            The ID generated will be used to create the localhost URL (Shortened URL).
     * @return Returns the localhost URL base with the ID appended creating a usable URL.
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public String createShortUrl(@RequestBody String url){

        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );

        if(urlValidator.isValid(url)){
            String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            System.out.println("Short URL Generated : " +  urlBase + id);
            redisTemplate.opsForValue().set(id, url);
            return urlBase + id;
        }
        throw new RuntimeException("URL Invalid: " + url);
    }
}




