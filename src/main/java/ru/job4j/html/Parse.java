package ru.job4j.html;

import java.text.ParseException;
import java.util.List;

public interface Parse {
    List<Post> list(String link);

    Post detail(String link);
}