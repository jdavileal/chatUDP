/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp;

import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao.ClienteUDP;
import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao.ServerUDP;
import com.sun.security.ntlm.Client;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zero
 */
public class Main {
    
    public static void main(String[] args) {
        ServerUDP server1 = new ServerUDP(32000,"user1");
        ServerUDP server2 = new ServerUDP(32001,"user2");
        
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server1.aguardarConexao(null);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t1.start();
        
        ClienteUDP client1 = new ClienteUDP(server2);
        boolean r = client1.IniciarConexao("127.0.0.1", 32000);
        System.out.println(r);
        client1.enviarMensagem("Test", "user1");
        client1.enviarMensagem("Test", "user1");
        client1.enviarMensagem("Test", "user1");
        
    }
}
