package net.edcubed.Smitty;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import net.edcubed.SmittyCommons.*;

public class NetworkManager extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;

    public NetworkManager(String ip){
        login(ip);
    }

    public void run(){
        System.out.println("Starting UDP networking utils");
        while (true) {
            byte[] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            Object message = "uninitialized";
            try {
                socket.receive(packet);
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream inputStream = new ObjectInputStream(in);
                message = inputStream.readObject();
            }catch(IOException e){
                e.printStackTrace();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }

            if(message instanceof String){
                System.out.println((String)message);
                return;
            }

            if(message instanceof ArrayList<?>){
                if(((ArrayList<?>)message).get(0) instanceof Player)
                {
                    Main.extraUtils.setGamePlayers((ArrayList<Player>)message);
                    Main.DrawScreen();
                }
                if(((ArrayList<?>)message).get(0) instanceof Place)
                {
                    if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.NATURAL) {
                        Main.worldUtils.setGeneratedTerrain((ArrayList<Place>)message);
                        Main.DrawScreen();
                    }
                    if (((ArrayList<Place>)message).get(0).getKind() == Place.TerrainKind.ARTIFICIAL) {
                        Main.worldUtils.setArtificialTerrain((ArrayList<Place>)message);
                        Main.DrawScreen();
                    }
                }
                if(((ArrayList<?>)message).get(0) instanceof Integer)
                {
                    ArrayList<Integer> worldSize = (ArrayList<Integer>)message;
                    Main.worldSizeX = worldSize.get(0);
                    Main.worldSizeY = worldSize.get(1);
                }
            }
        }
    }

    public void login(String ip) {
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ip);
            sendData(new PlayerMP(PlayerMP.Status.LOGIN, Main.me, Main.versionCode));
        }catch (SocketException e){
            e.printStackTrace();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void sendData(Object inputData){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(inputData);
            os.flush();
            byte[] data = outputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 26656);
            socket.send(packet);
            os.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}