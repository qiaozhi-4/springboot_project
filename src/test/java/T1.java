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
        String ip = ""; //?????????ip??????
        String chinaz = "https://ip.chinaz.com"; //ip???????????????

        StringBuilder inputLine = new StringBuilder(); //??????????????????????????????
        String read = ""; //?????????????????????
        URL url = null; //?????????URL
        HttpURLConnection urlConnection = null; //Java?????????http??????
        BufferedReader in = null; //???????????????????????????
        try {
            url = new URL(chinaz); //????????????????????????????????????URL??????http???????????????
            urlConnection = (HttpURLConnection) url.openConnection(); //??????http???????????????????????????
            //???????????????????????????
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n"); //??????????????????????????????StringBuilder
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
        //??????????????????
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        //???????????????
        Matcher m = p.matcher(inputLine.toString());
        //????????????????????????
        if (m.find()) {
            //????????????????????????2???
            String ipstr = m.group(1);
            ip = ipstr;
            //System.out.println(ipstr);
        }
        return ip;
    }


}
