import java.security.*;
import java.security.spec.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.charset.*;
import java.security.*;
import java.net.*;
import java.math.*;
import java.security.*;

public class clientmcs
{
public static void main(String[] args) throws Exception {
  
Scanner in=new Scanner (System.in);
  
//System.out.println("Enter your password: ");
String id="XXXX";
String pwd="Xaaa@123"; //=in.next();
  
String id1,pwd1;
int c;

Socket clientSocket = new Socket("localhost",4040);
       OutputStream outToServer = clientSocket.getOutputStream();
       BufferedReader inFromServer = new BufferedReader(
                                                new InputStreamReader(clientSocket.getInputStream()));
       outToServer.write((id+":"+pwd+"\n").getBytes());
       String ret = inFromServer.readLine();

		
do
{
System.out.println(" Welcome to MCS Log In");	
System.out.println("\n1. Please Log in" +
"\n2. Create an Account" +
"\n3. Change Password"+
"\n4. Exit"+
"\n\nEnter your choice: ");
  
c=in.nextInt();
  
switch(c){
case 1:
int a=0;
while(a<3){
if(login(in,id,pwd)==0){
System.out.println("invalid password or Id");
a++;
}
else{
System.out.println("Logged in successfully\n");
break;
}
}
if(a==3)
System.out.println("3 attempts.. Account locked\n");
  
WriteToFile(" "+id," "+getMd5(pwd));
break;
  
case 2:
System.out.println("\nCreate Username:");
id=in.next();
System.out.println("\nCreate Password:");
pwd=in.next();
getMd5(pwd);
if(Passwordcheck(pwd)==1)
System.out.println("Account created\n");
  
WriteToFile(id,"account created");
break;
  
case 3:
a=0;
while(a<3){
if(login(in,id,pwd)==0){
System.out.println("invalid password or Id");
a++;
}
else
{
System.out.println("\nenter new Password:");
pwd1=in.next();
if(Passwordcheck(pwd1)==1)
System.out.println("Password updated\n");
  
}
}
if(a==3)
System.out.println("3 attempts.. Account locked\n");
  
WriteToFile(id,"password change");
  
break;
  
case 4:
System.out.println("Thank you\n");
System.exit(1);

}
  
}while(true);
}
  
public static int Passwordcheck(String pwd) {
int n=checkPassword(pwd); //call check fuction
  
if(n==0){
System.out.println("Password is valid");
return 1;
}
  
if(n==1)
System.out.println("Wrong size\n");
if(n==2)
System.out.println("Does not consists of only letters, digits and special characters\n");
  
if(n==3)
System.out.println("does not contains atleast one upper case and one lower case character\n");
  
if(n==4)
System.out.println("does not contains atleast two digits an invalid character\n");
  
if(n==5)
System.out.println("does not contains any of @, #, $ or %\n");
  

return 0;
}
//check all conditions
private static int checkPassword(String pwd) {
if(hasValidSize(pwd)==0)
return 1;
if(hasValidCharacters(pwd)==0)
return 2;
if(hasUpperLowerCase(pwd)==0)
return 3;
if(hasTwoDigits(pwd)==0)
return 4;
if(hasSpecialCharacters(pwd)==0)
return 5;
  
return 0;
  
}
//check for valid size
private static int hasValidSize(String pwd) {
if(pwd.length()>7 && pwd.length()<33)
return 1;
else
return 0;
}
//check for valid characters
private static int hasValidCharacters(String pwd) {
int flag=1,flag1=0,flag2=0;
  
  
for(int i=0;i<pwd.length();i++)
{
flag1=flag2=0;
if(!(Character.isLetterOrDigit(pwd.charAt(i))))
flag1=1;
  
if(!((pwd.charAt(i))=='@' ||(pwd.charAt(i))=='#'||(pwd.charAt(i))=='$'||(pwd.charAt(i))=='%'))
flag2=1;
  
if(flag1==1 &&flag2==1)
return 0;
  

}
return 1;
}
//check for upper case
private static int hasUpperLowerCase(String pwd) {
int upper=0,lower=0;
  
for(int i=0;i<pwd.length();i++){
if(Character.isUpperCase(pwd.charAt(i)))
upper++;
if(Character.isLowerCase(pwd.charAt(i)))
lower++;
}
if(upper>0 && lower>0)
return 1;
else
return 0;
}
//check for 2 digits
private static int hasTwoDigits(String pwd) {
int digit=0;
  
for(int i=0;i<pwd.length();i++){
if(Character.isDigit(pwd.charAt(i)))
digit++;
}
if(digit>1)
return 1;
else
return 0;
}
//check for special character
private static int hasSpecialCharacters(String pwd) {
  
int s=0;

  
for(int i=0;i<pwd.length();i++)
{
  
if((pwd.charAt(i))=='@' ||(pwd.charAt(i))=='#'||(pwd.charAt(i))=='$'||(pwd.charAt(i))=='%')
s++;

}
if(s>0)
return 1;
else
return 0;
}

  private static String getMd5(String input)
    {
        try {
  
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
  
            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
  
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

private static int login(Scanner in,String id, String pwd) {
  
String id1,pwd1;
System.out.println("\nLogin name:");
id1=in.next();
  
System.out.println("\n Password:");
pwd1=in.next();
  
  
if(id.equals(id1))
{
if(pwd.equals(pwd1))
return 1;
}
return 0;
  
}
  
//write to file
public static void WriteToFile(String id, String pwd) throws FileNotFoundException
{
File file=new File("accountdata.txt");
PrintWriter pw=new PrintWriter(file);
pw.println(pwd);
pw.flush();
  
pw.write(id+"\t"+pwd);
}
 public static void sendFile(String file, Socket s) throws IOException {
       DataOutputStream dos = new DataOutputStream(s.getOutputStream());
       FileInputStream fis = new FileInputStream(file);
       byte[] buffer = new byte[4096];
      
       while (fis.read(buffer) > 0) {
           dos.write(buffer);
       }
      
       fis.close();
  
}}