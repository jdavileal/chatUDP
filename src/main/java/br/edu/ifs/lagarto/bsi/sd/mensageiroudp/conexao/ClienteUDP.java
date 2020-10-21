/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao;

import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.Mensagem;
import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.MensagemType;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zero
 */
public class ClienteUDP {

    private ServerUDP server;
    private String username;
    private InetAddress IpServer;
    private byte buffer[] = new byte[1024];
    private int portaServer;

    public ClienteUDP(ServerUDP server) {
        this.server = server;
    }

    public void enviarMensagem(String msg, String username) {
        buffer = Mensagem.getMensagem(msg, username).toString().getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, IpServer, portaServer);
        try {
            server.getSocket().send(send);
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean IniciarConexao(String ip, int porta) {
        buffer = Mensagem.getMensagemConfiguracao(MensagemType.INICIAR_CONEXAO).toString().getBytes();
        try {
            IpServer = InetAddress.getByName(ip);
            portaServer = porta;
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClienteUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, IpServer, portaServer);
        try {
            server.getSocket().send(send);
            while (true) {
                DatagramPacket datagrama = new DatagramPacket(buffer, buffer.length);
                server.getSocket().receive(datagrama);
                Mensagem msg = Mensagem.getMensagem(datagrama.getData());

                if (!datagrama.getAddress().equals(IpServer)) {
                    continue;
                }
                if (msg.getType() == MensagemType.ACEITAR_CONEXAO) {
                    System.out.println("Conexão aceita: " + IpServer.getHostAddress());
                    return true;
                } else {
                    System.out.println("Conexão Rejeitada: " + IpServer.getHostAddress());
                    return false;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
