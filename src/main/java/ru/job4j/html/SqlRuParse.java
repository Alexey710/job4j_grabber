package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

    public static void main(String[] args) throws Exception {
        new SqlRuParse();
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        Elements row2 = doc.getElementsByClass("altCol");
        List<LocalDateTime> dates = new LinkedList<>();
        int index = 0;
        for (Element td : row2) {
            if (index % 2 > 0) {
                dates.add(stringToDateTime(td.text()));
            }
            index++;
        }
        index = 0;
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(dates.get(index++));
            System.out.println(href.text());
            System.out.println(href.attr("href"));
            System.out.println("-------------");
        }
    }
}