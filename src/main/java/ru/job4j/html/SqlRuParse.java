package ru.job4j.html;

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

public class SqlRuParse {
    private static Map<String, String> map = new HashMap<>();

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

    private static LocalDateTime stringToDateTime(String input) throws ParseException {
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

    public static void parseOnePage(String url, String query, String query2)
            throws ParseException, IOException {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(query);
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.text());
            getContentPostAndDate(href.attr("href"));
            System.out.println(href.attr("href"));
            System.out.println("-------------");
        }

    }

    public static void parsePages(String url, String query, String query2, int number)
            throws IOException, ParseException {
        for (int i = 1; i <= number; i++) {
            String urlPage = String.format("%s%s%s", url, "/", i);
            parseOnePage(urlPage, query, query2);
        }
    }

    public static void getContentPostAndDate(String url) throws IOException, ParseException {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.getElementsByClass("msgBody");
        Elements row2 = doc.getElementsByClass("msgFooter");
        int index = 0;
        for (Element td : row) {
            if (index == 1) {
                System.out.println(td.text());
                break;
            }
            index++;
        }
        for (Element td : row2) {
            String[] arr = td.text().split(" \\[");
                System.out.println(stringToDateTime(arr[0]));
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        new SqlRuParse();
        parsePages("https://www.sql.ru/forum/job-offers", ".postslisttopic", "altCol", 5);

    }
}