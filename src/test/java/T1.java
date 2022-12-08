import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class T1 {
    @Test
    public void t1() throws IOException {
        //System.out.println(getInternetIP());
        demo();
    }

    public static void demo() {
        int[] arr1 = new int[18];
        int[] arr2 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] arr3 = new int[11];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (int) (Math.random() * 100);
            for (int j = 0; j < arr2.length; j++) {
                int nub = arr1[i] / 10;
                if (nub == arr2[j]) {
                    arr3[j]++;
                }
            }
        }
        int length = 0;
        for (int i = 0; i < arr3.length; i++) {
            if (arr3[i] > 0) {
                length++;
            }
        }
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr3));
        System.out.println(length);
        for (int i = 0; i < arr1.length - 1; i++) {
            for (int j = i + 1; j < arr1.length; j++) {
                if (arr1[i] > arr1[j]) {
                    int temp = arr1[i];
                    arr1[i] = arr1[j];
                    arr1[j] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr1));


        String[][] arr4 = new String[length][];
        int index = 0;
        int index1 = 0;
        for (int i = 0; i < arr4.length; i++) {
            if (arr3[index] > 0) {
                arr4[i] = new String[arr3[index]];
                for (int j = 0; j < arr3[index]; j++) {
                    arr4[i][j] = "\"" + arr1[index1] + "\"";
                    index1++;
                }
            } else {

                i--;
            }
            index++;
        }

        for (String[] strings : arr4) {

            System.out.println(Arrays.toString(strings));
        }


    }


    public static String getInternetIP() {
        String ip = ""; //返回的ip地址
        String chinaz = "https://ip.chinaz.com"; //ip查询的网址

        StringBuilder inputLine = new StringBuilder(); //装载获取到的网页内容
        String read = ""; //每次读取的内容
        URL url = null; //请求的URL
        HttpURLConnection urlConnection = null; //Java自带的http连接
        BufferedReader in = null; //读取请求到网页内容
        try {
            url = new URL(chinaz); //参数为要请求的网址，创建URL作为http请求参数，
            urlConnection = (HttpURLConnection) url.openConnection(); //发送http请求，获取网页内容
            //包装字节流为字符流
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n"); //每次读取一行并追加到StringBuilder
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //创建匹配规则
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        //使用匹配器
        Matcher m = p.matcher(inputLine.toString());
        //判读是否有匹配的
        if (m.find()) {
            //获取匹配成功的第2个
            String ipstr = m.group(1);
            ip = ipstr;
            //System.out.println(ipstr);
        }
        return ip;
    }


}
