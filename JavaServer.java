import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Signature;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.io.InputStreamReader
import java.util.logging.SimpleFormatter;;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class host
{
    public static Boolean comprueba_firma(Byte[] firma, String mensaje,String publica){
        /*
         * Funcion que se dedica a la comprobacion de la firma pasada por el cliente, recibe como parametros la firma, y el mensaje
         */
        Signature sg = Signature.getInstance("SHA256withRSA");
        sg.initVerify(publica);

        return sg.verify(firma.getBytes()); // si nos devuelve true CORRECTO, si nos devuelve false INCORRECTO

    }

    public static void guarda_log(String texto){
        
        fh = new FileHandler(""); // Archivo donde guardaremos los logs
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        
        logger.info(texto);  
    }

    public static void dbase_guardado(String mensaje){
        /*
         * Mensaje recibido sera dividido y guardado en la base de datos para queno haya ningun tipo de problema,
         * de la manera clave -> Empleado Valor -> Sabana si o no
         */
        Connection conn = null;
        Integer numero = Integer.parseInt(mensaje);
        Boolean boleano = Boolean.parseBoolean(mensaje);

        try{
            String url = "";
            conn = DriverManager.getConnection(url);
            System.out.println("Conexion establecida con exito!");

        }

        catch(SQLException ex){
            System.err.print(ex.getMessage());
        }

        String sql = "INSERT INTO datos(numero,boleano) VALUES (?,?)";
        try{
            Connection conn1=conn.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,numero);
            pstmt.setDouble(2,boleano);
            pstmt.executeUpdate();
            }catch(SQLException e){
            System.out.println(e.getMessage());
            }


        

    }
    public static void main(String srgs[])
    {   
        Logger logger = Logger.getLogger("Mylog");
        Filehandler fh;
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintStream printStream = null;
        
        try{
            // serverSocket = new ServerSocket("127.0.0.1", "7070");
            serverSocket = new ServerSocket(7070);
            System.out.println("Esperando a cliente(s)");            
                                
            socket = serverSocket.accept();
            System.out.println("Conexión de " + 
                socket.getInetAddress() + ":" + socket.getPort());
            
            InputStreamReader inputStreamReader = 
                new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            
            System.out.println("Mensaje recibido: ");

            String line;
            while((line=bufferedReader.readLine()) != null){
                System.out.println(line);
            }
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }
            }
            
            if(printStream!=null){
                printStream.close();
            }
            
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }
            }
        }
    }
}