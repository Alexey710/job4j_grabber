package ru.job4j.grabber;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private String title;
    private String url;
    private String content;
    private LocalDateTime date;

    public Post(String title, String url, String content, LocalDateTime date) {
        this.title = title;
        this.url = url;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{"
                + "title='"
                + title + '\''
                + ", url='" + url + '\''
                + ", content='" + content + '\''
                + ", date=" + date
                + '}';
    }
}

