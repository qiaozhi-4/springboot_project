package com.zking.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Ffmpeg {
    public static void test() throws IOException {
        String path = "D:\\springboot\\videolook\\";        //要遍历的路径
        File file = new File(path);        //获取其file对象
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                    //遍历File[]数组
            if (!f.isDirectory())        //若非目录(即文件)，则打印
                cope(f.getName());
        }
    }

    public static String cope(String filename) throws IOException {
        // 在服务端安装ffmpeg，路径配置到环境变量或者yml配置文件中
        String ffmpeg = "D:\\springboot\\老师的工具\\ffmpeg-5.0.1-essentials_build\\bin/ffmpeg.exe";
        // 一些参数
        int start = 0; // 从0开始截取
        String input = "D:\\springboot\\videolook\\" + filename;//视频所在路径

        int duration = 5; // 截取60秒
        String output = getOutputPath(input, duration);
        // 配置命令
        List<String> commands = new ArrayList<>(10);
        commands.add(ffmpeg);
        // 详细命令：ffmpeg -ss [start] -i [input] -t [duration] -c copy [output]
        commands.add("-ss");
        commands.add(String.valueOf(start));
        commands.add("-i");
        commands.add(input);
        commands.add("-t");
        commands.add(String.valueOf(duration));
        commands.add("-c");
        commands.add("copy");
        commands.add(output);
        String[] command = commands.toArray(new String[]{});
        // 处理命令
        log.info("[ffmpeg] 视频处理：{} => {}", input, output);
        processVideo(command);
        return output.substring(13);
        // 其他业务逻辑……
        // 返回路径到前端：output
    }

    private static void processVideo(String[] command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = processBuilder.command(command).start();

        try (InputStream inputStream = process.getInputStream();
             OutputStream outputStream = process.getOutputStream()) {
            byte[] bytes = new byte[10240];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                log.info("[ffmpeg] 剪切视频中：{}", new String(bytes, 0, len));
            }
        }

        process.destroy();
        log.info("[ffmpeg] 视频处理完毕，请保存路径！");
    }

    private static String getOutputPath(String input, int duration) {
        File file = new File(input);
        String name = "videolookshort\\" + file.getName();
        int index = name.lastIndexOf('.');
        if (index == -1) {
            name = name + "-" + duration + ".mp4";
        } else {
            name = name.substring(0, index) + "-" + duration + name.substring(index);
        }
        file = new File(file.getParentFile(), name);
        return file.getAbsolutePath();
    }
}
