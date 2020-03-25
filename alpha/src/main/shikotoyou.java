package main;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import javafx.util.Pair;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class shikotoyou {
    public  static String format = "yyyy/MM/dd HH:mm:ss";
//    public static String textTohtml(String text ){
//  //          return "<p>"+ String.join("",Arrays.stream(text.replaceAll("\r\n","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>").split("<br>")).map(x->"<div>"+x+"</div><br>").toArray(String[]::new)).replaceAll("<div></div>","")+"</p>";
//    }
    public static void main(String[] args) throws IOException, ParseException {
//        String command = "cd.. cd \"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\"\n" +
//                "mysqldump -uroot -ppassword dic word>C://word.sql";
        String filePath ="C:\\backendApiJava\\Shell\\NAS\\work\\application\\backyard\\netstore_csv\\tmpCsv\\test.csv";
        ArrayList<String[]> csvList = new ArrayList<String[]>();
        CsvReader reader = new CsvReader(filePath,',', Charset.forName("SJIS"));
        reader.readHeaders();
        CsvWriter writer = new CsvWriter(filePath,',', Charset.forName("SJIS"));
        while(reader.readRecord()){
            csvList.add(reader.getValues());
        }
        reader.close();
        System.out.println("读取的行数："+csvList.size());

        for(int row=0;row<csvList.size();row++){
            System.out.println("-----------------");
            System.out.print(csvList.get(row)[0]+",");
            System.out.print(csvList.get(row)[1]+",");
            System.out.print(csvList.get(row)[2]+",");
            System.out.println(csvList.get(row)[3]+",");
         }
        }
}
