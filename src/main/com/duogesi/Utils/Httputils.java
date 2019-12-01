package com.duogesi.Utils;
import javax.net.ssl.*;
import javax.servlet.http.HttpUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yinx on 2017/9/19 0019.
 */
public class Httputils {
    public static final String UTF8 = "utf-8";
    public static final String GBK = "gbk";
    public static final String GB2312 = "gb2312";
    public static final String ISO88591 = "ISO-8859-1";
    public static int READ_TIMEOUT = 30000;
    public static int CONNECT_TIMEOUT = 30000;

    /**
     * post请求数据
     *
     * @param connectURL
     * @param param
     * @param charset
     * @return
     */
    public static String doPost(String connectURL, String param, String charset) {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOut = null;
        URL url = null;
        HttpURLConnection httpPost = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            url = new URL(connectURL);
            httpPost = (HttpURLConnection) url.openConnection();
            httpPost.setRequestMethod("POST");
            httpPost.setDoInput(true);
            httpPost.setDoOutput(true);
            httpPost.setUseCaches(false);
            httpPost.setConnectTimeout(CONNECT_TIMEOUT);
            httpPost.setReadTimeout(READ_TIMEOUT);
            httpPost.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpPost.connect();
            out = httpPost.getOutputStream();
            out.write(param.getBytes(charset));
            out.flush();
            in = httpPost.getInputStream();
            byteArrayOut = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int l = 0;
            while ((l = in.read(buf)) != -1) {
                byteArrayOut.write(buf, 0, l);
            }
            bytes = byteArrayOut.toByteArray();
            return new String(bytes, charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(byteArrayOut);
            close(out);
            close(in);
            close(httpPost);
        }
        return null;
    }

    public static String doPostSSL(String connectURL, Map<String, String> params, String charset) throws MalformedURLException, IOException, UnsupportedEncodingException {
        _ignoreSSL();
        return doPost(connectURL, params, charset);
    }

    /**
     * post请求数据
     *
     * @param connectURL
     * @param params
     * @param charset
     * @return
     */
    public static String doPost(String connectURL, Map<String, String> params, String charset) {
        String param = "";
        if (params != null && !params.isEmpty()) {
            StringBuffer paramBuf = new StringBuffer();
            for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                String value = params.get(key);
                paramBuf.append("&").append(key).append("=").append(value);
            }
            param = paramBuf.substring(1);
        }
        System.out.println("post url:" + connectURL);
        System.out.println("post data:" + param);
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOut = null;
        URL url = null;
        HttpURLConnection httpPost = null;
        OutputStream out = null;
        BufferedInputStream in = null;
        try {
            url = new URL(connectURL);
            httpPost = (HttpURLConnection) url.openConnection();
            httpPost.setRequestMethod("POST");
            httpPost.setDoInput(true);
            httpPost.setDoOutput(true);
            httpPost.setUseCaches(false);
            httpPost.setConnectTimeout(CONNECT_TIMEOUT);
            httpPost.setReadTimeout(READ_TIMEOUT);
            httpPost.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpPost.connect();
            out = httpPost.getOutputStream();
            out.write(param.getBytes(charset));
            out.flush();

            try {
                in = new BufferedInputStream(httpPost.getInputStream());
            } catch (IOException e) {
                in = new BufferedInputStream(httpPost.getErrorStream());
            }

            byteArrayOut = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int l = 0;
            while ((l = in.read(buf)) != -1) {
                byteArrayOut.write(buf, 0, l);
            }
            bytes = byteArrayOut.toByteArray();
            return new String(bytes, charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(byteArrayOut);
            close(in);
            close(out);
            close(httpPost);
        }
        return null;
    }

    /**
     * Get请求数据
     *
     * @param connectURL
     * @param charset
     * @return
     */
    public static String doGet(String connectURL, String charset) {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOut = null;
        URL url = null;
        HttpURLConnection httpGet = null;
        InputStream in = null;
        try {
            url = new URL(connectURL);
            httpGet = (HttpURLConnection) url.openConnection();
            httpGet.setConnectTimeout(CONNECT_TIMEOUT);
            httpGet.setReadTimeout(READ_TIMEOUT);
            httpGet.connect();
            in = httpGet.getInputStream();
            byteArrayOut = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int l = 0;
            while ((l = in.read(buf)) != -1) {
                byteArrayOut.write(buf, 0, l);
            }
            bytes = byteArrayOut.toByteArray();
            return bytes != null ? new String(bytes, charset) : null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(byteArrayOut);
            close(in);
            close(httpGet);
        }
        return null;
    }

    private static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(HttpURLConnection httpConn){
        if(httpConn != null){
            httpConn.disconnect();
        }
    }

    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslsession) {
            return true;
        }
    };

    /**
     * 忽略SSL
     */
    private static void _ignoreSSL() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } };

            // Install the all-trusting trust manager

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        } catch (KeyManagementException ex) {
            Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String doPost(String actionURL, HashMap<String, String> parameters){
        String response = "";
        try{
            URL url = new URL(actionURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");;
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求数据内容
            String requestContent = "";
            Set<String> keys = parameters.keySet();
            for(String key : keys){
                requestContent = requestContent + key + "=" + parameters.get(key) + "&";
            }
            requestContent = requestContent.substring(0, requestContent.lastIndexOf("&"));
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            //使用write(requestContent.getBytes())是为了防止中文出现乱码
            ds.write(requestContent.getBytes());
            ds.flush();
            try{
                //获取URL的响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String s = "";
                String temp = "";
                while((temp = reader.readLine()) != null){
                    s += temp;
                }
                response = s;
                reader.close();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("No response get!!!");
            }
            ds.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Request failed!");
        }
        return response;
    }

    /**
     * @author Johnson
     * @method singleFileUploadWithParameters
     * @description 集上传单个文件与传递参数于一体的方法
     * @param actionURL 上传文件的URL地址包括URL
     * @param fileType 文件类型(枚举类型)
     * @param uploadFile 上传文件的路径字符串
     * @param parameters 跟文件一起传输的参数(HashMap)
     * @return String("" if no response get)
     * @attention 上传文件name为file(服务器解析)
     * */
    /*public String singleFileUploadWithParameters(String actionURL, String uploadFile, MIME_FileType fileType, HashMap<String, String> parameters){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "---------------------------7e0dd540448";
        String response = "";
        try{
            URL url = new URL(actionURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //设置请求参数
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            //获取请求内容输出流
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            String fileName = uploadFile.substring(uploadFile.lastIndexOf(this.PathSeparator) + 1);
            //开始写表单格式内容
            //写参数
            Set<String> keys = parameters.keySet();
            for(String key : keys){
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; name=\"");
                ds.write(key.getBytes());
                ds.writeBytes("\"" + end);
                ds.writeBytes(end);
                ds.write(parameters.get(key).getBytes());
                ds.writeBytes(end);
            }
            //写文件
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\"; " + "filename=\"");
            //防止中文乱码
            ds.write(fileName.getBytes());
            ds.writeBytes("\"" + end);
            ds.writeBytes("Content-Type: " + fileType.getValue() + end);
            ds.writeBytes(end);
            //根据路径读取文件
            FileInputStream fis = new FileInputStream(uploadFile);
            byte[] buffer = new byte[1024];
            int length = -1;
            while((length = fis.read(buffer)) != -1){
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            fis.close();
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.writeBytes(end);
            ds.flush();
            try{
                //获取URL的响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String s = "";
                String temp = "";
                while((temp = reader.readLine()) != null){
                    s += temp;
                }
                response = s;
                reader.close();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("No response get!!!");
            }
            ds.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Request failed!");
        }
        return response;
    }*/
}
