import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.*;
import java.util.*;
public class servermcs
 {
   public static void main(String[] args) throws Exception {
       //Create a database of userinfo using Hashmap
       Map<String, String> userDB = new HashMap<String, String>();
   
       String fileName,ack;
       ServerSocket welcomeSocket = new ServerSocket(4040);
       while(true){
           System.out.println("Server waiting for new request.");
           Socket connectionSocket = welcomeSocket.accept();
          
            BufferedReader inFromClient = new BufferedReader(new
                                         InputStreamReader(connectionSocket.getInputStream()));
            OutputStream outToClient = connectionSocket.getOutputStream();
          
            String userInfo = inFromClient.readLine();
            try{
                if(userDB.get(userInfo.split(":")[0])==null || !userDB.get(userInfo.split(":")[0]).equals(userInfo.split(":")[1])){
                   outToClient.write("failed\n".getBytes());
                   ack = inFromClient.readLine();
                }else{
                   Path currentRelativePath = Paths.get("");
                   String s = currentRelativePath.toAbsolutePath().toString();
                   outToClient.write((s+"\n").getBytes());
                   while(true){
                       String cmd = inFromClient.readLine();
                       if(cmd.equals("1")){//Upload files
                           fileName = inFromClient.readLine();
                           saveFile(connectionSocket, fileName);
                       }else if(cmd.equals("2")){//Change directory
                           String newPath = inFromClient.readLine();
                           System.setProperty("user.dir", newPath);
                          
                           outToClient.write((Paths.get(newPath).toAbsolutePath().toString()+"\n").getBytes());
                           //File file = new File("data.csv").getAbsoluteFile();
                           //System.out.println(file.getPath());
                       }else if(cmd.equals("3")){//List files
                           File directory = new File(".");
                            File[] fList = directory.listFiles();
                            for (File file : fList){
                               if(file.isFile())
                                   outToClient.write((file.getName()+":").getBytes());
                            }
                            outToClient.write("\n".getBytes());
                       }else if(cmd.equals("4")){//Logout
                           connectionSocket.close();
                           break;
                       }
                   }
                }
            }catch (Exception e) {
               e.printStackTrace();
            }
       }
   }
  
   private static void saveFile(Socket clientSock, String fileName) throws IOException {
       DataInputStream dis = new DataInputStream(clientSock.getInputStream());
       File file = new File(fileName).getAbsoluteFile();
       FileOutputStream fos = new FileOutputStream(file);
       byte[] buffer = new byte[4096];
      
       int filesize = 15123;
       int read = 0;
       int totalRead = 0;
       int remaining = filesize;
       while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
           totalRead += read;
           remaining -= read;
           fos.write(buffer, 0, read);
       }
      
       fos.close();
   }
}