package blog;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/6 20:45
 * <p>
 * 复习一下 组合模式
 */

public class DealFile {

    /**
     * @author Firefly
     * 根目录
     */
    PostFile root = new PostFile("post", true);
    private String classPath;//类类路径是项目的根目录，这里是jerryrat
    // filesindex    fileName  filecontent文件所有内容
    private Map<Integer, List<Pair<String, String>>> dataBasa = new HashMap<>();
    BlogLogger logger = new BlogLogger();

    public DealFile() {
        try {
            this.search();
        } catch (IOException e) {
            logger.log(e.toString());
        }
    }

    public void search() throws IOException {
        classPath = DealFile.class.getClassLoader().getResource("").getPath();
        File file = new File(classPath + "blog/post");
        if (file.isDirectory()) {
            File[] types = file.listFiles();
            List<PostFile> temp = new ArrayList<>();
            // 每一分类
            for (File type : types ) {
                if (type.isDirectory() && !type.getName().contentEquals(".git")) {
                    PostFile temp_type = new PostFile(type.getName(), true);
                    File[] contents = new File(type.getPath()).listFiles();
                    // 每一篇文章
                    List<PostFile> temp_contents = new ArrayList<>();
                    for (File content : contents) {
                        if (content.getName().endsWith(".html")) {  //想要的
                            PostFile con = new PostFile(content.getName(), false);
                            BufferedReader br = new BufferedReader(new FileReader(content.getPath()));
                            String line;
                            String temp_info = "";
                            while ((line = br.readLine()) != null) {
                                temp_info += (line + "\n");
                            }
                            br.close();
                            con.setInfo(temp_info);
                            temp_contents.add(con);
                        }
                    }
                    temp_type.setChild(temp_contents);
                    temp.add(temp_type);
                }
            }
            root.setChild(temp);
        }

    }

    public static void main(String[] args) {
        DealFile file = new DealFile();
        new GenHtml();
        System.out.print("fileNum:" + file.getFileNum());
        for (String s : file.getTypes()) System.out.print("\t" + s);
        System.out.println();
        System.out.println("*********************start del the posted file*************************");
        file.delPostedFiles();
        System.out.println("***********************start deal the file!*******************");
        file.deaLFiles();
        System.out.println("*********************make the index.html*********************");
        file.makeIndexHtml();
        System.out.println("*********************make the allblogs.html*********************");
        file.makeAllBlogs();
        System.out.println("*********************deal over!*********************");
    }


    private List<String> getTypes() {
        List<String> ret = new ArrayList<>();
        List<PostFile> types = root.getChild();
        for (PostFile p : types) {
            ret.add(p.getFileName());
        }
        return ret;
    }

    private int getFileNum() {
        List<PostFile> types = root.getChild();
        int cnt = 0;
        for (PostFile p : types)
            for (PostFile pp : p.getChild())
                cnt++;
        return cnt;
    }

    private void makeDir(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdir = file.mkdirs();
            logger.log("make dir post " + mkdir + " " + file.getPath());
        } else logger.log("post dir is exited " + file.getPath());
    }

    private void deaLFiles() {
        List<PostFile> types = root.getChild();
        makeDir(classPath + "WebContent/post");
        int cntp = 1;
        for (PostFile p : types) {
            makeDir(classPath + "WebContent/post/dir" + cntp);
            makeDir(classPath + "WebContent/post/dir" + cntp + p.getFileName());
            int cntpp = 1;
            for (PostFile pp : p.getChild()) {
                add2Database(cntp, cntpp + "-" + pp.getFileName(), pp.getInfo());
                logger.log("deal: " + pp.getFileName());
                dealFile("WebContent/post/dir" + cntp + "/file" + cntpp++ + ".html", pp.getInfo());
            }
            cntp++;
        }
    }

    private void delPostedFiles() {
        // 小心啊， 别删错了a， 用一个方法保护一下！
        delFiles(classPath + "WebContent/post");
    }

    private void delFiles(String path) {
        File file = new File(path);
        if (!file.exists()) return;
        logger.log(path);
        if (file.isDirectory()) {
            if (file.listFiles() != null)
                for (File f : file.listFiles()) {
                    delFiles(f.getPath());
                }
            System.gc();
            // 文件正在使用或者删除不掉， 哪里用了啊？？？？
            boolean delete = file.delete();
            if (delete) logger.log("del " + file.getName());
            else logger.log("please del the by hand: " + file.getName());

        } else logger.log("del " + file.getName() + " " + file.delete());
    }

    private void add2Database(int fileIndex, String fname, String info) {
        /**
         * @author Firefly
         * 把 span 里的数据 扣出来
         */
        Matcher matcher = Pattern.compile("<span>.*</span>").matcher(info);
        String dealInfo = "";
        while (matcher.find()) {
            dealInfo += info.substring(matcher.start() + 6, matcher.end() - 7).replace("\n", " ").replace("\r\n", " ") + "\t";
        }

//        Matcher m1 = Pattern.compile("[\\u4E00-\\u9FA5]+").matcher(dealInfo);//匹配中文
//        String dealInfo1 = "";
//        while (m1.find()) {
//            if (m1.end() < dealInfo.length())
//                dealInfo1 += dealInfo.substring(m1.start(), m1.end()) + "\t";
//        }
//
//        dealInfo = dealInfo1;

//        matcher = Pattern.compile("<.{0,10}?>").matcher(dealInfo);
//        while (matcher.find()) {
//            if (matcher.end() < dealInfo.length())
//                dealInfo = dealInfo.replace(dealInfo.substring(matcher.start(), matcher.end()), " ");
//        }

        while (true) {
            matcher = Pattern.compile("<.{0,200}?>").matcher(dealInfo);
            if (matcher.find() && matcher.end() < dealInfo.length()) {
                dealInfo = dealInfo.replace(dealInfo.substring(matcher.start(), matcher.end()), "");
            } else {
                break;
            }
        }
//        matcher = Pattern.compile("<.{0,10}?>").matcher(dealInfo);
//        while (matcher.find()) {
//            if (matcher.end() < dealInfo.length())
//                dealInfo = dealInfo.replace(dealInfo.substring(matcher.start(), matcher.end()), " ");
//        }
        dealInfo = dealInfo.replace("<", "_");
        dealInfo = dealInfo.replace("\"", " ");
        // logger.log(dealInfo);
        //logger.log(dealInfo);
        System.out.println(dealInfo);
        Pair<String, String> temp = new Pair<>(fname, dealInfo);
        if (this.dataBasa.get(fileIndex) == null) {
            List<Pair<String, String>> data = new ArrayList<>();
            data.add(temp);
            this.dataBasa.put(fileIndex, data);
        } else
            this.dataBasa.get(fileIndex).add(temp);
    }

    public String devBlog() {

        new GenHtml();
        logger.log("fileNum:" + getFileNum());
        for (String s : getTypes()) logger.log("\t" + s);
        logger.log(" ");
        logger.log("*********************start del the posted file*************************");
        delPostedFiles();
        logger.log("\n\n\n***********************start deal the file!*******************");
        deaLFiles();
        logger.log("\n\n\n*********************make the index.html*********************");
        makeIndexHtml();
        logger.log("\n\n\n*********************deal over!*********************");
        makeAllBlogs();
        logger.log("*********************make the allblogs.html*********************");
        return logger.getInfo();
    }

    private void dealFile(String path, String html) {
        String temp = "";
        //添加 head 后面jquery 头文件
        temp += "<!doctype html>\n" + "<html>" + GenHtml.getHead1();


        Matcher matcher = Pattern.compile("<body.*?>").matcher(html);
        if (!matcher.find()) {
            logger.log("not value file");
            return;
        }
        temp += html.substring(matcher.start());

        //某一个页有 导航(侧边栏)
//        Matcher matcher = Pattern.compile("<body").matcher(html);
//        boolean b1 = matcher.find();
//        if (!b1) {
//            logger.log(path + " deal error!!  <body dont find");
//            return;
//        }
//        int st2, en2;
//        int en1 = matcher.start();
//        st2 = en2 = matcher.end();
//        temp += html.substring(en1, en2);
//        temp += " id=\"changedWidth\" ";
//        while (!html.substring(en2, en2 + 1).contentEquals(">")) en2++;
//        temp += html.substring(st2, en2 + 1);
//        temp += "<script>\n" + "let types = [";
//        for (String s : this.getTypes()) temp += "'" + s + "',";
//        temp += "];\n" + GenHtml.getLeftjs();
//        temp += "</script>" + html.substring(en2 + 1, html.length());
        /**
         * @author Firefly
         *
        注意 当加入js时不要这样做， js 可以不写分号！！！！！！！！！！！！！！！！！！！！！！！！！！！
         */
        temp = temp.replace("\n", "").replace("\r\n", "");
        temp = temp.replace("\t", "");
        temp = temp.trim();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(classPath + path));
            bw.write(temp);
            bw.close();
        } catch (IOException e) {
            logger.log(e.toString());
        }
    }

    public void makeIndexHtml() {
        String indexHtml = "";
        indexHtml += "<!doctype html>\n" + "<html>" + GenHtml.getHead();
        // 开始 body
        indexHtml += "<body id=\"changedWidth\" class='typora-export os-windows'>";
        // 生成分类   <script> 开始
        indexHtml += "<script>\n" + "let types = [";
        for (String s : this.getTypes()) indexHtml += "'" + s + "',";
        indexHtml += "];\n" + "var addTypes=\"\";\n" + GenHtml.getLeftjs();// addType是存储左边的js
        //生成跳转首页页面的 js
        indexHtml += "function forIndex(flag) {\n" + "  console.log(\"dianle\");";
        int cntp = 1;
        for (String i : this.getTypes()) {
            indexHtml += cntp == 1 ? "if (flag === 0) {" : "else if(flag === " + (cntp - 1) + "){";
            indexHtml += "document.getElementById(\"changedWidth\").innerHTML = \"<div style=\\\"width: 1px; border: dashed lightgray 0.1px;height: 100%;position: fixed; left: 15%;\\\"></div>\\n\";\n" +
                    "document.getElementById(\"changedWidth\").innerHTML += \"<div id='write' class='is-node'><h2>" +
                    "<a name=\\\"" + i + "\\\" class=\\\"md-header-anchor\\\"></a><span>" + i + "</span></h2>\\n\" +\n" +
                    "\"<p>&nbsp;</p>\\n\" +\n" + "\"<blockquote><p><span><span id='jinrishici-sentence'>思考中。。。</span></span></p></blockquote>\\n\" +\n" +
                    "\"<p>&nbsp;</p>\\n\" +\n" + "\" <p>&nbsp;</p>\\n\"  + ";
            if (dataBasa.get(cntp) != null) {
                int cntpp = 1;
                for (Pair<String, String> d : dataBasa.get(cntp)) {
                    indexHtml += " \n// 文章\" \n ";
                    indexHtml += "     \"<p onclick='window.open(\\\"post/dir" + (cntp + "/file" + cntpp++ + ".html") + "\\\")'><strong><span >" + d.getKey() + "</span></strong></p>" + "  <hr/>" + "  <p><span>";
                    indexHtml += d.getValue().length() > 200 ? d.getValue().substring(0, 200) : d.getValue();
                    indexHtml += "+ </span>" + "</p>" + "<p>&nbsp;</p> \" + ";
                }
            }
            //生成页尾
            indexHtml += "\"<p>&nbsp;</p>\" +" + "\"<p>&nbsp;</p>\" +" + " \"<p>&nbsp;</p>\" +" + "\"<hr/>\" +" +
                    "\"    <p><span>@Author: Firefly</span></p>\" +" + "\"    </div> \" ;" +
                    "document.getElementById(\"changedWidth\").innerHTML += addTypes;" + " }";
            cntp++;

        }
        indexHtml += "    }" + "</script>" ;

        String indexBody =  GenHtml.getIndexBody();
        Matcher matcher = Pattern.compile("</body>").matcher(indexBody);
        if (matcher.find()){
            indexHtml += indexBody.substring(0,matcher.start());
        }
        indexHtml += "<div id='write' class='is-node'><p onclick='window.open(\"allblogs.html\")'><strong><span >文章列表</span></strong></p></div>";
        indexHtml += indexBody.substring(matcher.start(),matcher.end());
        indexHtml = indexHtml.trim();
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(classPath + "WebContent/index.html"));
            br.write(indexHtml);
            br.flush();
            logger.log("Gen index.html success!!");
            br.close();
        } catch (Exception e) {
            logger.log(e.toString());
        }
    }


    private void makeAllBlogs() {
        String indexHtml = "";
        indexHtml += "<!doctype html>\n" + "<html>" + GenHtml.getHead();
        // 开始 body
        indexHtml += "<body class='typora-export os-windows'>";
        indexHtml += "<div  id='write'  class = 'is-node'>";

        int cntp = 1;
        int cnt = 0;
        for (String i : this.getTypes()) {
            if (dataBasa.get(cntp) != null) {
                int cntpp = 1;
                for (Pair<String, String> d : dataBasa.get(cntp)) {
                    indexHtml += "  <p onclick='window.open(\"post/dir" + (cntp + "/file" + cntpp++ + ".html") + "\")'><strong><span >" + cnt++ +" - "+  d.getKey().split("-")[1] + "</span></strong></p>" + "  <hr/>" + "  <p><span>";
                    indexHtml += d.getValue().length() > 200 ? d.getValue().substring(0, 200) : d.getValue();
                    indexHtml += "+ </span>" + "</p>" + "<p>&nbsp;</p> \n ";
                }
            }
            cntp++;

        }
        //生成页尾
        indexHtml += "   <p><span>@Author: Firefly</span></p>" + "    </div>  ;";
        indexHtml += "</body></html>";
        indexHtml = indexHtml.trim();
        indexHtml = indexHtml.replace("\n","");
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(classPath + "WebContent/allblogs.html"));
            br.write(indexHtml);
            br.flush();
            logger.log("Gen allblogs.html success!!");
            br.close();
        } catch (Exception e) {
            logger.log(e.toString());
        }

    }


}
