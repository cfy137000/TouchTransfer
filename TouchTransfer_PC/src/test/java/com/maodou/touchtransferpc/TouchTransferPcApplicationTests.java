package com.maodou.touchtransferpc;

import com.maodou.touchtransferpc.util.QRCodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.*;
import java.util.UUID;

@SpringBootTest
class TouchTransferPcApplicationTests {

    @Test
    void contextLoads() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("#########" + inetAddress);
        System.out.println("#########" + inetAddress.getHostName());
        System.out.println("#########" + inetAddress.getHostAddress());
    }

    //客户端
    @Test
    void send() throws Exception {
        // 创建广播
        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
        DatagramSocket socket = new DatagramSocket();
        // 发送数据--唯一识别码
        String uniqueIdentificationCode = UUID.randomUUID().toString();
        System.out.println("客户端发送的唯一识别码是: " + uniqueIdentificationCode);
        DatagramPacket sendPacket = new DatagramPacket(uniqueIdentificationCode.getBytes(),
                uniqueIdentificationCode.getBytes().length, broadcastAddress, 19999);
        socket.send(sendPacket);
        // 接收数据
        byte[] buffer = new byte[1024 * 1024];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(receivePacket);
        String ip = new String(receivePacket.getData()).trim();
        System.out.println("服务端ip是: " + ip);
        socket.close();

    }

    @Test
    public void testQRCoder() {
//        String uniqueCode = QRCodeUtil.zxingCodeAnalyze("QRCode.jpg").getText();
//        System.out.println(uniqueCode);

    }

}
