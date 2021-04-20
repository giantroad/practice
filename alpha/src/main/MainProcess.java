package main;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainProcess {
    public static Statement stmt;
    public static Statement stmt2;
    public static Scanner sc = new Scanner(System.in);
    public static Boolean VoiceOn = false;
    public static Boolean jpdk = false;
    //数据库备份使用自带的mysqldump.exe在新环境可能需要更新
    /*随新环境更新的参数*/
    public static String dbuser = "root";
    public static String dbpassword = "haineng";
    public static String dbname = "haineng";
    /*随新环境更新的参数*/

    public static  Connection con;
    static {
        try {
            con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&characterEncoding=UTF-8&autoReconnect=true", dbuser, dbpassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, IOException, URISyntaxException {

        stmt = con.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        stmt2 = con.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        sc.useDelimiter("\n");
//        try {
//            fresh();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        Integer k;
        dbb();
        one();

    }

    public static void one() throws IOException, URISyntaxException, SQLException {

        while (true) {
            if (VoiceOn)
            s("1->slient mode");
            else
            s("1->general mode");
            s("2->all");
            s("3->review");
            s("4->review all");
            s("5->manual input mode");
            if (jpdk)
            s("6->ALl lang");
            else
            s("6->jp dake");
            s("7->db backup");
            s("8->db update");
            s("9->novel");
            s("10->delete word");
            s("11->update word");
            String w = sc.next();
            if ("1".equals(w)) VoiceOn ^= true;
            else if ("2".equals(w)) showAll();
            else if ("3".equals(w)) review(false);
            else if ("4".equals(w)) review(true);
            else if ("5".equals(w)) {
                s("plz input q p m v");
                s("word :");
                String q = sc.next();
                s("phonetic :");
                String p = sc.next();
                s("meaning :");
                String m = sc.next();
//                s("voice URL :");
//                String v = sc.next();
//                s("example sequence :");
//                String e = sc.next();
                insert(q, m, p, null, null);
            }
            else if ("6".equals(w)) jpdk ^= true;
            else if ("7".equals(w)) dbb();
            else if ("8".equals(w)) dbu();
            else if ("9".equals(w)) novel();
            else if ("10".equals(w)) deleteWord();
            else if ("11".equals(w)) updateWord();
            else t(w);
        }
    }

    public static void updateWord(){
        s("which word to update?");
        String w = sc.next();
        s("atarashi meaning plz");
        String m = sc.next();
        try {
            stmt.execute("update "+dbname+".word  set meaning = '"+m+"' where word = '" + w + "';");
        } catch (SQLException e) {
            e.printStackTrace();
            s("update fail");
            return;
        }
        s("update " + w + " meaing : " + m  +" successfully");
    }
    public static void deleteWord(){
        s("which word to del?");
        String w = sc.next();
        try {
            stmt.execute("delete from "+dbname+".word where word = '" + w + "';");
        } catch (SQLException e) {
            e.printStackTrace();
            s("delete fail");
            return;
        }
        s("delete " + w + " successfully");
    }
    public static void novel(){
        try {
            ResultSet rs = stmt.executeQuery("select meaning from "+dbname+".word where word = \"novel\"");
            rs.next();
            String url= rs.getString("meaning");
            Document document = Jsoup.connect(url).maxBodySize(0).get();
            Element right = document.getElementById("right");
            s(right.text());


        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public String getHtml(String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
        String html = "";
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();   // 使用默认的HttpClient
        response = httpclient.execute(httpget);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {     // 返回 200 表示成功
            html = EntityUtils.toString(response.getEntity(), "utf-8");     // 获取服务器响应实体的内容
        }
        s(html);
        return html;
    }
    public static void dbb() {
        File directory = new File("");
        String target = "mysqldump -u"+dbuser+" -p"+dbpassword+" "+dbname+" word -r"+  directory.getAbsolutePath()+"\\word.sql";
        try {
            Process process = Runtime.getRuntime().exec(target);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            while (content != null) {
                System.out.println(content);
                content = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();

//        File directory = new File("");
//        String target = "mysqldump "+ "-u"+dbuser+" -p"+dbpassword+" "+dbname+" word -r"+  directory.getAbsolutePath()+"\\word.sql";
//        try {
//            Process p = Runtime.getRuntime().exec("cmd.exe C: & "+"cd "+dump+" & "+target);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        }
    }

    public static void dbu(){
//        Runtime runtime = Runtime.getRuntime();
//        Process process = null;
        File sqlfile = new File(new File("").getAbsolutePath()+"\\word.sql");
        if (!sqlfile.exists()){
            s("sql file doesn't exsit!");
            return;
        }
        InputStream is = null;
        StringBuilder buffer = new StringBuilder();
        try {
            is = new FileInputStream(sqlfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line =""; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
        while (line != null) { // 如果 line 为空说明读完了
            try {
                line = reader.readLine(); // 读取下一行4
                if (line != null && !line.startsWith("--"))
                buffer.append(line); // 将读到的内容添加到 buffer 中
                if (line != null && line.endsWith(";") ) {
                    stmt.execute(buffer.toString());
                    buffer = new StringBuilder();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                s(line);
                buffer = new StringBuilder();
                e.printStackTrace();
            }
        }
        reader.close();
        is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            process = runtime.exec("mysql "+ " -uroot -ppassword -Ddic<"+ sqlfile.getAbsolutePath() );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static void play(String url) throws IOException {
        if (url == null) return;
        BufferedInputStream buffer;
        try {
            buffer = new BufferedInputStream(new URL(url).openConnection().getInputStream());
            Player player = new Player(buffer);
            player.play();
        } catch (IOException e) {
            buffer = new BufferedInputStream(HttpClients.createDefault().execute(new HttpGet(url)).getEntity().getContent());
            try {
                Player player = new Player(buffer);
                player.play();
            } catch (JavaLayerException e1) {
                return ;
            }
        } catch (JavaLayerException e) {
            try {
                stmt = con.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e1) {
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void review(Boolean a) throws IOException {
        ResultSet rs = null;
        String sql ;
        if (a) sql = "SELECT * FROM word  order by (miss+hit) ASC , date desc , miss desc";
        else sql =   "SELECT * FROM word WHERE review = 0 order by date desc ,(miss+hit) desc ,  miss desc";
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        s("q->quit,3->miss,enter->hit");
        try {
            //
        while(rs.next()){
            String word ;
            word = rs.getString("word");
            if (jpdk && word.matches("^[a-zA-Z]+$"))  continue;
            String meaning = rs.getString("meaning");
            String phonetic = rs.getString("phonetic");
            String voice = rs.getString("voice_us");

            String example = rs.getString("example");
            int miss = rs.getInt("miss");
            int hit = rs.getInt("hit");
            Date d = rs.getDate("date");
            //Long period = (new Date().getTime() - d.getTime()) / (24*60*60*1000);
            s(word);
            int k = ce(word);
            if (k == 1)  hit++;  else if (k == 0) break; else miss++;
            rs.updateInt("hit", hit);
            rs.updateInt("miss", miss);
//            java.sql.Date date =  new java.sql.Date(System.currentTimeMillis());
//            rs.updateDate("date",date);
            if (hit + miss > 2 && hit > miss + 1) rs.updateInt("review", 1);
            try {
                rs.updateRow();
            }catch (com.mysql.cj.jdbc.exceptions.CommunicationsException e){
                review(a);
            }
            s( "\nmeaning : " + meaning + "\nphonetic : " + phonetic + "\nexample : \n" + example );
           // s( "\nmeaning : " + meaning + "\nphonetic : " + phonetic  );
            if (VoiceOn) play(voice);
            s("");
        }
        } catch (SQLException e) {
            review(a);
        }

    }


    public static int ce(String word){
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        do {
            try {
                str = bf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if ("q".equals(str)) return 0;
            if ("3".equals(str)) return -1;
            if ((str.length() != 0)) {
                try {
                    t(str);
                    s(word);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        while (str.length() != 0);

        return 1;
    }

    public static void fresh() throws SQLException {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(
                    "SELECT * FROM word");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (rs.next()) {
            Date d = rs.getDate("date");
            Long period = (new Date().getTime() - d.getTime()) / (24 * 60 * 60 * 1000);
            if (period > 30) rs.updateInt("review", 0);
            rs.updateRow();
        }

    }


    public static void showAll() throws SQLException {
        String sql = "select * from word";
        ResultSet rs = stmt.executeQuery(sql);
        int a = 1 ;
        List<Cell> header =  new ArrayList<Cell>(){{
            add(new Cell(Align.LEFT,"word"));
            add(new Cell(Align.LEFT,"meaning"));
            add(new Cell(Align.LEFT,"phonetic"));
            add(new Cell(Align.LEFT,"date"));
            add(new Cell(Align.LEFT,"miss"));
            add(new Cell(Align.LEFT,"hit"));
            add(new Cell(Align.LEFT,"review"));
            add(new Cell(Align.LEFT,"voice_us"));
        }};
        List<List<Cell>> body = new ArrayList<>();
        while(rs.next()) {
            List<Cell> row = new ArrayList<Cell>() {
                {
                    add(new Cell(Align.LEFT, rs.getString(1)));
                    add(new Cell(Align.LEFT, rs.getString(2)));
                    add(new Cell(Align.LEFT, rs.getString(3)));
                    add(new Cell(Align.LEFT, rs.getString(4)));
                    add(new Cell(Align.LEFT, rs.getString(5)));
                    add(new Cell(Align.LEFT, rs.getString(6)));
                    add(new Cell(Align.LEFT, rs.getString(7)));
                    add(new Cell(Align.LEFT, rs.getString(8)));
                }

            };
            body.add(row);
        }
        cmdTable(header ,body);
        ss();
    }

    public static void cmdTable(List<Cell> header ,List<List<Cell>> body ){
        new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).build().print();
    }

    public static String ten(String q) throws IOException {
        Document document = Jsoup.connect("https://dictionary.cambridge.org/ja/dictionary/english-chinese-simplified/"+q).get();
        Elements meaings = document.getElementsByClass("trans dtrans dtrans-se  break-cj");
        Elements uk = document.getElementsByClass("uk dpron-i ");
        Elements us = document.getElementsByClass("us dpron-i ");
        String m  = "";
        String p = "";
        for (Element e : meaings){
            m = m + sh(e.toString());
        }
        if (uk.size() != 0 && us.size() != 0) {
            List<String> examples = document.getElementsByClass("examp dexamp").stream().map(e -> {
                String s = sh(e.toString()).substring(3);
                return s;
            }).collect(Collectors.toList());
            p = "uk :" + sh(uk.get(0).getElementsByClass("ipa dipa lpr-2 lpl-1").toString()) + ", us :" + sh(us.get(0).getElementsByClass("ipa dipa lpr-2 lpl-1").toString());
            String e = examples.toString();
            e = e.replace('\"',' ').replace('\'',' ');
            insert(q, m, p, "https://dictionary.cambridge.org"+uk.get(0).getElementsByTag("source").get(0).attributes().get("src"),e);
            if (VoiceOn) play("https://dictionary.cambridge.org"+uk.get(0).getElementsByTag("source").get(0).attributes().get("src"));

        }
        else {
            document = Jsoup.connect("https://dictionary.cambridge.org/ja/dictionary/english/"+q).get();
            meaings = document.getElementsByClass("trans dtrans dtrans-se ");
            uk = document.getElementsByClass("uk dpron-i ");
            us = document.getElementsByClass("us dpron-i ");
            p = "";
            if (uk.size() != 0 && us.size() != 0) {
                List<String> examples = document.getElementsByClass("examp dexamp").stream().map(e -> {
                    String s = sh(e.toString()).substring(3);
                    return s;
                }).collect(Collectors.toList());
                p = "uk :" + sh(uk.get(0).getElementsByClass("ipa dipa lpr-2 lpl-1").toString()) + ", us :" + sh(us.get(0).getElementsByClass("ipa dipa lpr-2 lpl-1").toString());
                s("plz input m");
                m = sc.next();
                insert(q, m, p, "https://dictionary.cambridge.org"+uk.get(0).getElementsByTag("source").get(0).attributes().get("src"),examples.toString());
                if (VoiceOn) play("https://dictionary.cambridge.org"+uk.get(0).getElementsByTag("source").get(0).attributes().get("src"));
            }
            else {
                s("manual input plz");
            }
        }



        return null;

    }

    public static String sh(String h){
        return h.replaceAll("<.*?>","");
    }
    public static String t(String q) throws IOException, URISyntaxException{
        q = q.trim();
        String er = "^[a-zA-Z]+$";
        String from = "ja";
        if (q.matches(er)) {
            return ten(q);
        }
        String to = "zh-CN";
        String url = "http://translate.google.cn/translate_a/single";
        String tk = token(q);
        URIBuilder uri = new URIBuilder(url);
        uri.addParameter("client", "webapp");
        uri.addParameter("sl", from);
        uri.addParameter("tl", to);
        uri.addParameter("hl", "ja");
        uri.addParameter("dt", "at");
        uri.addParameter("dt", "bd");
        uri.addParameter("dt", "ex");
        uri.addParameter("dt", "ld");
        uri.addParameter("dt", "md");
        uri.addParameter("dt", "qca");
        uri.addParameter("dt", "rw");
        uri.addParameter("dt", "rm");
        uri.addParameter("dt", "ss");
        uri.addParameter("dt", "t");
        //    uri.addParameter("ie", "UTF-8");
        uri.addParameter("oe", "UTF-8");
        //   uri.addParameter("source", "tws_spell");
        uri.addParameter("ssel", "3");
        uri.addParameter("otf", "1");
        uri.addParameter("pc", "1");
        uri.addParameter("tsel", "0");
        uri.addParameter("kc", "2");
        uri.addParameter("tk", tk);
        uri.addParameter("q", q);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest request = new HttpGet(uri.toString());
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        //     s(result);
        ss();
        int leftcount = 2;
        int index = 3;
        while(leftcount > 0 ){
            if (result.charAt(index) == ']') leftcount--;
            if (result.charAt(index++) == '[') leftcount++;
        }
        result = result.substring(2,index);
        result = result.chars().filter(i -> IntStream.of('[', ']','\"','\n').allMatch(k -> k != i)).collect(
                StringBuilder::new,
                (sb, i) -> sb.append((char)(i)),StringBuilder::append
        ).toString();
        String m = result.substring(0,result.indexOf(','));
        String p = result.substring(result.lastIndexOf(',')+1);
        String v = "https://translate.google.cn/translate_tts?ie=UTF-8&q="+q+"&tl=ja&tk="+tk+"&client=webapp&prev=input";
        Document document = Jsoup.connect("https://cjjc.weblio.jp/content/"+q).get();
        Elements sequences = document.getElementsByClass("qotC");
        String s = "" ;
        for (Element e : sequences){
            String l = StringUtils.normalizeSpace(sh(e.child(0).toString()));
            String c = StringUtils.normalizeSpace(sh(e.child(1).toString()));
            c = c.substring(0,c.indexOf("&nbsp"));
            s = s + l + "\n" + c + "\n";
        }
        s = s.replace('\"',' ').replace('\'',' ');
        insert(q,m,p,v,s);
        EntityUtils.consume(entity);
        response.getEntity().getContent().close();
        response.close();
        if (VoiceOn) play(v);
        return result;
    }

    private static String token(String value) {
        String result = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            FileReader reader = new FileReader(System.getProperty("user.dir")+ File.separator + "alpha" + File.separator + "src" + File.separator + "main" + File.separator + "Google.js");
            engine.eval(reader);

            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                result = String.valueOf(invoke.invokeFunction("token", value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void w() {
        s("wrong!wrong!wrong!wrong!wrong!");
    }

    public static int insert(String word, String meaning, String phonetic ,String voiceURL ,String example)  {
        s("word :"+ word + "\nmeaning : " + meaning + "\nphonetic : " + phonetic + "\nvoiceURL : " + voiceURL + "\nexample sequence : " + example);
        String sql = "insert into word(word,meaning,date,phonetic,voice_us,example) values ('" + word.trim() + "','" + meaning.trim() + "'," + new SimpleDateFormat("yyyyMMdd").format(new Date()) +",\"" + phonetic+ "\",'"+voiceURL+"',"+"\""+example+"\n\")";
        ss();
       // s("execute sql : " + sql);
        s("execute sql");
        ss();
        int rs ;
        try {
            rs = stmt2.executeUpdate(sql);
            s("insert success");
        } catch (SQLException e) {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&characterEncoding=UTF-8&autoReconnect=true", dbuser, dbpassword);
                stmt2 = con.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                rs = stmt2.executeUpdate(sql);
                s("insert success");
            }catch (Exception ee){
                s("insert fail");
            }
            return -1;
        }
        return rs;
    }

    public static void ss(){
        s("***********************************");
    }

    public static void s(Object o) {
        System.out.println(o);
    }
}
