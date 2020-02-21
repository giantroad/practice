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
        }

    public static int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        int j;
        int i;
        Map<Character, Integer> map = new HashMap<>(); // current index of character
        // try to extend the range [i, j]
        for (j = 0, i = 0; j < n; j++) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(map.get(s.charAt(j)), i);
            }
            ans = Math.max(ans, j - i + 1);
            map.put(s.charAt(j), j + 1);
        }
        s(ans);
        j = 0;
        ans = 0;
        map = new HashMap<>();
        for (i = 0; i < s.length(); i++){
            if (map.containsKey(s.charAt(i))){
                j = i + 1;
            }
            ans = Math.max(i-j+1,ans);
            map.put(s.charAt(i),i);
        }

        s(ans);
        return ans;
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
        private static void fr(String url) throws IOException {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(url);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String str = null;
            try {
                bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int count = 1;
            int sc = 1;
            String su = "C:\\Users\\base\\Desktop\\csv\\"+count+".csv";
            File s = new File(su);
            if (!s.exists()){
                try {
                    s.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writer = new FileWriter(su);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write("\"sku_code\",\"[更新後]sales_start_timestamp\",\"[更新後]msrp\",\"Version\",\"[更新前]sales_start_timestamp\",\"[更新前]msrp\"\n");
            while(true)
            {
                try {
                    if (!((str = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sc++;
                bw.write(str+"\n");
                bw.flush();
                if (sc % 10000 == 0){
                    sc = 0;
                    count++;
                    su = "C:\\Users\\base\\Desktop\\csv\\"+count+".csv";
                    s = new File(su);
                    if (!s.exists()){
                        try {
                            s.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    writer = new FileWriter(su);
                    bw = new BufferedWriter(writer);
                    bw.write("\"sku_code\",\"[更新後]sales_start_timestamp\",\"[更新後]msrp\",\"Version\",\"[更新前]sales_start_timestamp\",\"[更新前]msrp\"\n");
                }
            }

            //close
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
}
