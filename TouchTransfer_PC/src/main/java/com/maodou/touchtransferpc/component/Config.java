package com.maodou.touchtransferpc.component;

import com.maodou.touchtransferpc.util.ConfigUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class Config {
    private static final AtomicReference<Config> INSTANCE = new AtomicReference<>();
    public Config(){}

    public static Config getINSTANCE() {
        for (; ; ) {
            Config current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = ConfigUtil.readConfig();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private String storePath = "";
    private String uniqueCode = "";

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        if (storePath.length() > 0 &&!this.storePath.equals(storePath)) {
            ConfigUtil.writeConfig(this);
        }
        this.storePath = storePath;
        File storeDir = new File(storePath);
        if (!storeDir.exists()){
            storeDir.mkdirs();
        }
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        if (this.uniqueCode.length() > 0 && !this.uniqueCode.equals(uniqueCode)) {
            ConfigUtil.writeConfig(this);
        }
        this.uniqueCode = uniqueCode;
    }
}
