package com.lwy.student.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject; // 版本用json存的，项目需要引入fastjson
import com.lwy.student.run.Main;

public class Upgrader {
    public static float currentversion = 1.0f;//当前版本号
    public static float newversion =  1.0f; //最新版本号
    public static boolean downloaded = false;//下载完成与否
    public static boolean errored = false;//下载出错与否
    public static String versinurl = "http://47.93.122.77/file/version.json"; //版本存放地址
    public static String jarurl = "http://47.93.122.77/file/jar/student.jar"; // jar存放地址
    public static String description = "";//新版本更新信息

    /**
     * 静默下载最新版本
     */
    public static void dowload() {
        try {
            downLoadFromUrl(jarurl, "tmp");
            downloaded = true;
        } catch (Exception e) {
            downloaded = false;
            errored = true;
            e.printStackTrace();
        }
    }

    /**
     * 重启完成更新
     */
    public static void restart() {
        try {
            Runtime.getRuntime().exec("cmd /k start .\\update.bat");
            Main.close(); //关闭程序以便重启 如果你不需要，直接把 Main.close()换为 System.exit(0); 即可
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最新版本号
     */
    public static boolean cmpversion() {
        String json = sendGetRequest(versinurl);
        JSONObject ob =  JSONObject.parseObject(json);
        newversion = ob.getFloat("version");
        description = ob.getString("desc");
        try {
            JSONObject jsonObject =JSON.parseObject(new FileInputStream(new File("./version.json")), JSONObject.class);
            currentversion = jsonObject.getFloat("version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentversion<newversion?true:false;
    }

    /**
     * 启动后自动更新，这个方法要在你自己的启动流程中调用
     */
    public static void autoupgrade() {
        dowload();
        restart();
    }

    /**
     * 发get请求，获取文本
     * @param getUrl
     * @return 网页context
     */
    public static String sendGetRequest(String getUrl) {
        StringBuffer sb = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            URL url = new URL(getUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setAllowUserInteraction(false);
            isr = new InputStreamReader(url.openStream(),"UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr,  String savePath) throws IOException {
        URL url = new URL(urlStr);
        String[] templist=urlStr.split("/");
        String fileName=templist[templist.length-1];
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 获取自己数组
        byte[] getData = readInputStream(inputStream);

        // 文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}

