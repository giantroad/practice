package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class redditpic {
    public static void main(String[] args) throws IOException {
        String url= "https://www.reddit.com/r/clevercomebacks/comments/f3m7s0/bill_bur_is_a_legend/";
        Document document = Jsoup.connect(url).maxBodySize(0).get();
        document.toString().split("https://i.redd.i");
        Element right = document.getElementById("right");
    }
}
