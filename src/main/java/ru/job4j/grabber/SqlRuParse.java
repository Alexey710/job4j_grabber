package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SqlRuParse implements Parse {
    private int number;
    private Map<String, String> map = new HashMap<>();

    {
        map.put("янв", "01");
        map.put("фев", "02");
        map.put("мар", "03");
        map.put("апр", "04");
        map.put("май", "05");
        map.put("июн", "06");
        map.put("июл", "07");
        map.put("авг", "08");
        map.put("сен", "09");
        map.put("окт", "10");
        map.put("ноя", "11");
        map.put("дек", "12");
    }

    public SqlRuParse(int number) {
        this.number = number;
    }

    private LocalDateTime stringToDateTime(String input) throws ParseException {
        String[] arr = input.split(" ");
        if (arr[0].equals("сегодня,")) {
            LocalDate now = LocalDate.now();
            String date = now.format(DateTimeFormatter.ofPattern("d MM yy"));
            String rsl = String.format("%s%s%s", date, ", ", arr[1]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MM yy, HH:mm");
            return LocalDateTime.parse(rsl, formatter);
        }
        if (arr[0].equals("вчера,")) {
            LocalDate yesterday =  LocalDate.now().minusDays(1);
            String date = yesterday.format(DateTimeFormatter.ofPattern("d MM yy"));
            String rsl = String.format("%s%s%s", date, ", ", arr[1]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MM yy, HH:mm");
            return LocalDateTime.parse(rsl, formatter);
        }
        String s = arr[1];
        String placeholder = map.get(s);
        String dateEng = input.replace(arr[1], placeholder);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MM yy, HH:mm");
        return LocalDateTime.parse(dateEng, formatter);
    }

    public List<Post> parseOnePage(String url, String query, String query2)
            throws ParseException, IOException {
        List<Post> postsOnePage = new LinkedList<>();
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(query);
        for (Element td : row) {
            Element href = td.child(0);
            Post post = detail(href.attr("href"));
            post.setTitle(href.text());
            postsOnePage.add(post);
        }
        return postsOnePage;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new LinkedList<>();
        for (int i = 1; i <= number; i++) {
            String urlPage = String.format("%s%s%s", link, "/", i);
            try {
                posts.addAll(parseOnePage(urlPage, ".postslisttopic", "altCol"));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        LocalDateTime date = null;
        String content = null;
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.getElementsByClass("msgBody");
            Elements row2 = doc.getElementsByClass("msgFooter");
            int index = 0;
            for (Element td : row) {
                if (index == 1) {
                    content = td.text();
                    break;
                }
                index++;
            }
            for (Element td : row2) {
                String[] arr = td.text().split(" \\[");
                try {
                    date = stringToDateTime(arr[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Post(link, content, date);
    }

    public static void main(String[] args) {
        SqlRuParse sqlRuParse = new SqlRuParse(5);
        List<Post> rsl = sqlRuParse.list("https://www.sql.ru/forum/job-offers");
        int d = 0;
        for (Post elem : rsl) {
            System.out.println(elem);
            System.out.println(d++);
        }
    }
}