package net.edcubed.Smitty;

import net.edcubed.SmittyCommons.Place;
import net.edcubed.SmittyCommons.PlayerMP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.net.*;

public class TCPNetworkManager extends Thread{
    public Socket socket;
    private String ipAddress;
    private int port;
    public ObjectInputStream objectInput;
    public ObjectOutputStream objectOutput;

    public TCPNetworkManager(String ip, int tcpPort) {
        this.ipAddress = ip;
        this.port = tcpPort;
        login(ipAddress, port);
    }

    public void run(){
        System.out.println("Starting TCP networking utils");
        Object message = "uninitialized";
        while (true) {
            try {
                message = objectInput.readObject();
                //JOptionPane.showMessageDialog(null, socket.isConnected(), "InfoBox: TCP Connection", JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e){
                e.printStackTrace();
                closeSocket(this.socket);
                return;
            }catch(ClassNotFoundException e){
                e.printStackTrace();
                closeSocket(this.socket);
                return;
            }
            //i should honestly use a switch statement but im a lazy & terrible programmer so whatever
            if(message instanceof ArrayList<?>)
            {
                if(((ArrayList<?>)message).get(0) instanceof Place)
                {
                    if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.NATURAL) {
                        Main.worldUtils.setGeneratedTerrain((ArrayList<Place>)message);
                    }
                    if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.ARTIFICIAL) {
                        Main.worldUtils.setArtificialTerrain((ArrayList<Place>)message);
                    }
                }
                if(((ArrayList<?>)message).get(0) instanceof Integer)
                {
                    ArrayList<Integer> worldSize = (ArrayList<Integer>)message;
                    Main.worldSizeX = worldSize.get(0);
                    Main.worldSizeY = worldSize.get(1);
                }
                continue;
            }
            if (message instanceof String) {
                System.out.println((String) message);
                if (message == "i'll always be here for you :)") {
                    System.out.println(":)");
                    continue;
                }
            }
        }
    }

    public void sendData(Object message) {
        try {
            this.objectOutput.writeObject(message);
            System.out.println("Sending tcp data to " + this.socket.getInetAddress() + " on port " + this.socket.getPort());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void closeSocket(Socket skt) {
        try {
            skt.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void login(String ip, int tcpPort) {
        try {
            this.socket = new Socket(ip,tcpPort);
            try {
                this.objectOutput = new ObjectOutputStream(this.socket.getOutputStream());
                this.objectInput = new ObjectInputStream(this.socket.getInputStream());
            }catch(IOException e){
                e.printStackTrace();
                closeSocket(this.socket);
                return;
            }
            sendData(new PlayerMP(PlayerMP.Status.LOGIN, Main.me, Main.versionCode));
        }catch(IOException e){
            e.printStackTrace();
            closeSocket(this.socket);
        }
    }
}