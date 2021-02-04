package ru.job4j.html;

import java.time.LocalDateTime;

public class Post {
    private String url;
    private String content;
    private LocalDateTime date;

    public Post(String url, String content, LocalDateTime date) {
        this.url = url;
        this.content = content;
        this.date = date;
    }
}
