package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        Elements row2 = doc.getElementsByClass("altCol");
        List<String> dates = new LinkedList<>();
        int index = 0;
        for (Element td : row2) {
            if (index % 2 > 0) {
                dates.add(td.text());
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