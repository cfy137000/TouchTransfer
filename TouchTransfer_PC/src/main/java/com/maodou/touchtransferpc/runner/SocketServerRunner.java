package com.maodou.touchtransferpc.runner;

import com.maodou.touchtransferpc.util.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @Description 启动socketServer
 * @Author Wang Yucui
 * @Date 9/3/2020 11:38 AM
 */
@Component
@Slf4j
public class SocketServerRunner implements ApplicationRunner {

    private int port = 19999;

    @Override
    public void run(ApplicationArguments var1) throws Exception {
        socketThreat();
    }

    /**
     * @Description socket服务端，将本机ip地址返回给客户端
     * @author Wang Yucui
     * @date 9/3/2020 11:42 AM
     */
    private void socketThreat() {
        new Thread(() -> {
            while (true) {
                try {
                    DatagramSocket socket = new DatagramSocket(port);
                    byte[] buffer = new byte[255];
                    DatagramPacket receivePacket = new DatagramPacket(buffer, 0, buffer.length);
                    log.info("准备接收数据");
                    socket.receive(receivePacket);
                    String clientUniqueCode = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    log.info("服务器接收到的唯一识别码是: " + clientUniqueCode);

                    String serverUniqueCode = QRCodeUtil.zxingCodeAnalyze("QRCode.jpg").getText();
                    if (serverUniqueCode.equals(clientUniqueCode)) {
                        InetAddress inetAddress = InetAddress.getLocalHost();
                        // 回复数据--本机ip地址
                        String hostAddress = inetAddress.getHostAddress();
                        DatagramPacket replypacket = new DatagramPacket(hostAddress.getBytes(),
                                hostAddress.getBytes().length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(replypacket);
                    }
                    socket.close();
                } catch (Exception e) {

                }

            }

        }).start();
    }
}