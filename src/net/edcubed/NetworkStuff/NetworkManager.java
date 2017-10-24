package net.edcubed.NetworkStuff;

import net.edcubed.TextMode.Constants;
import net.edcubed.TextMode.Main;
import net.edcubed.TextModeCommons.Packets.LoginPacket;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkManager {
    private String serverIP = "localhost";
    private int udpPort = 26656;
    private int tcpPort = 26655;

    //tcp
    public Socket tcpSocket;
    private String tcpIPAddress;
    public ObjectInputStream objectInput;
    public ObjectOutputStream objectOutput;

    //udp
    private InetAddress udpIPAddress;
    private DatagramSocket udpSocket;

    //listeners
    private ArrayList<NetworkListener> listeners = new ArrayList<>();

    public NetworkManager(String serverIP, int udpPort, int tcpPort) {
        this.serverIP = serverIP;
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        login();
    }

    public void addListener(NetworkListener listener) {
        listeners.add(listener);
    }

    private void update() {
        //udp stuff
        new Thread(new Runnable() {
            public void run() {
                System.out.println("Starting UDP networking utils");
                while (true) {
                    byte[] data = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    Object message = "uninitialized";
                    try {
                        udpSocket.receive(packet);
                        ByteArrayInputStream in = new ByteArrayInputStream(data);
                        ObjectInputStream inputStream = new ObjectInputStream(in);
                        message = inputStream.readObject();
                        for (NetworkListener nl : listeners) {
                            nl.receivedMessage(message);
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                        closeSocket(tcpSocket);
                    }catch(ClassNotFoundException e){
                        e.printStackTrace();
                        closeSocket(tcpSocket);
                    }
                }
            }
        }).start();

        //tcp stuff
        new Thread(new Runnable() {
            public void run() {
                System.out.println("Starting TCP networking utils");
                Object message = "uninitialized";
                while (true) {
                    try {
                        message = objectInput.readObject();
                        for (NetworkListener nl : listeners) {
                            nl.receivedMessage(message);
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                        closeSocket(tcpSocket);
                        return;
                    }catch(ClassNotFoundException e){
                        e.printStackTrace();
                        closeSocket(tcpSocket);
                        return;
                    }
                }
            }
        }).start();

        //keep alive thread thread
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(15000);
                        sendTCPData("i'm still here please don't leave me");
                        if (Constants.DEBUG) {
                            System.out.println("Sending keep alive");
                        }
                    }catch(InterruptedException e){
                        e.printStackTrace();
                        closeSocket(tcpSocket);
                        return;
                    }
                }
            }
        }).start();
    }

    private void closeSocket(Socket skt) {
        try {
            skt.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendTCPData(Object message) {
        try {
            this.objectOutput.writeObject(message);
            System.out.println("my port is " + tcpSocket.getLocalPort() + " Sending tcp data of type" + message.getClass() + " to " + tcpSocket.getInetAddress() + " on port " + tcpSocket.getPort());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendUDPData(Object message){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(message);
            os.flush();
            byte[] data = outputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, udpIPAddress, 26656);
            udpSocket.send(packet);
            os.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            tcpSocket.close();
            System.out.println("CLOSING SOCKET USING DEBUG MODE");
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void login(){
        try {
            //tcp stuff
            tcpSocket = new Socket(serverIP,tcpPort);
            this.objectOutput = new ObjectOutputStream(tcpSocket.getOutputStream());
            this.objectInput = new ObjectInputStream(tcpSocket.getInputStream());

            //CHANGE THIS!!!
            //sendTCPData(new PlayerMP(PlayerMP.Status.LOGIN, Main.me, Constants.VERSION));
            //temporarily using player from main file
            sendTCPData(new LoginPacket(Constants.VERSION, Main.me.getPlayerName(), "password"));

            //udp stuff
            udpSocket = new DatagramSocket();
            udpIPAddress = InetAddress.getByName(serverIP);

            //updates
            update();
        }catch(IOException e){
            e.printStackTrace();
            closeSocket(tcpSocket);
            return;
        }
    }
}