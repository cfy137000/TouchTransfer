package com.maodou.touchtransferpc.util;

import com.google.gson.Gson;
import com.maodou.touchtransferpc.component.Config;

import java.io.*;
import java.util.UUID;

public class ConfigUtil {
    private static final String CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config";
    private static final String CONFIG_PATH = "config.json";
    // 读配置文件
    public static Config readConfig(){
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir,CONFIG_PATH);
        System.out.println("----------------");
        System.out.println(file.getAbsolutePath());
        Config config = null;
        if (!file.exists()) {
            config = createDefaultConfig();
        } else {
            FileReader reader = null;
            try {

                reader = new FileReader(file);
                Gson gson = new Gson();
                config = gson.fromJson(reader, Config.class);
                if (config.getStorePath() == null || config.getStorePath() == null){
                    config = createDefaultConfig();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return config;
    }

    public static void writeConfig(Config config){
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File configFile = new File(dir,CONFIG_PATH);
        configFile.delete();
        FileWriter writer= null;
        try {
            configFile.createNewFile();
            Gson gson = new Gson();
            String s = gson.toJson(config);
            writer = new FileWriter(configFile);
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Config createDefaultConfig(){
        Config config = new Config();
        // 初始化配置
        config.setStorePath(System.getProperty("user.dir") + File.separator + "images");
        config.setUniqueCode(UUID.randomUUID().toString());
        writeConfig(config);
        return config;

    }
}
