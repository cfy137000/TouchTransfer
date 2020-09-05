package com.maodou.touchtransferpc.runner;

import com.maodou.touchtransferpc.gui.TouchTray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @Description 启动IconRunner
 * @Author Wang Yucui
 * @Date 9/3/2020 11:38 AM
 */
@Component
@Slf4j
public class IconRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments var1) throws Exception {
        System.out.println("iconrunner----------");
        TouchTray.initGlobalFont(new Font("宋体", Font.PLAIN, 13));
        TouchTray.miniTray();

    }

}