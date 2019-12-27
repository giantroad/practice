package main;

import javafx.util.Pair;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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
        Runtime runtime = Runtime.getRuntime();
        File directory = new File("");
        Process process = runtime.exec("mysqld -rrrr ");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while(true){
            //读取响应信息
            String s = br.readLine();
            if(s==null){
                break;
            }
            System.out.println(s);
        }

        }

    private static boolean DisclosureDateErrCheck(List<Pair<String, String>> disclosureDate)  {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        List<Pair<String, String>> finalTest = disclosureDate;
        if (!IntStream.range(0,finalTest.size()).allMatch(i-> {
            try {
                if ((finalTest.get(i).getKey() != null || finalTest.get(i).getValue()  != null) && (finalTest.get(i).getKey() == null || finalTest.get(i).getValue() == null))
                    return false;
                if (finalTest.get(i).getKey()!= null && format.parse(finalTest.get(i).getKey()).compareTo(format.parse(finalTest.get(i).getValue())) > 0)
                    return false;
                return true;
            } catch (ParseException e) {
                return false;
            }
        })) return false;

        disclosureDate =  disclosureDate.stream().filter(a->a.getKey()!=null).sorted((o1, o2) -> {
            try {
                return format.parse(o1.getKey()).compareTo(format.parse(o2.getKey()));
            } catch (ParseException e) {
                return 0;
            }
        }).collect(Collectors.toList());
        for (int i = 0 ; i < disclosureDate.size()-1 ; i++){
            try {
                if (format.parse(disclosureDate.get(i).getValue()).compareTo(format.parse(disclosureDate.get(i+1).getValue())) > 0)
                    return false;
            } catch (ParseException e) {
                return false;
            }
        }

        return true;
    }
        public static String getUnix (String d) throws ParseException {
        if (d==null) return null;
        return  String.valueOf(new SimpleDateFormat(format).parse(d).getTime());
        }

        public static void s(Object a){
            System.out.println(a);
        }

}
