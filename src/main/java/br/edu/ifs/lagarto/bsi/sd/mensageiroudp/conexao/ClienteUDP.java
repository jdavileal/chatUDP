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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zero
 */
public class ClienteUDP {

    private ServerUDP server;
    private byte buffer[];
    
    public ClienteUDP(int porta, String username) {
        this.server = new ServerUDP(porta, username);
    }

    public ClienteUDP(ServerUDP server) {
        this.server = server;
    }

    public void enviarMensagem(String msg, String username) {       
        buffer = Mensagem.getMensagem(msg, username).toString().getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, server.getIpCliente(), server.getPortaCliente());
        try {
            server.getSocket().send(send);
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean IniciarConexao(String ip, int porta) {
        
        try {
            buffer = Mensagem.getMensagemConfiguracao(MensagemType.INICIAR_CONEXAO).toString().getBytes();
            InetAddress ipA = InetAddress.getByName(ip);
            DatagramPacket send = new DatagramPacket(buffer, buffer.length, ipA, porta);
            server.getSocket().send(send);  
            while (true) {
                DatagramPacket datagrama = new DatagramPacket(buffer, buffer.length);
                server.getSocket().receive(datagrama);
                Mensagem msg = Mensagem.getMensagem(datagrama.getData());

                if (!datagrama.getAddress().equals(ipA)) {
                    continue;
                }
                if (msg.getType() == MensagemType.ACEITAR_CONEXAO) {
                    System.out.println("Conexão aceita: " + ipA.getHostAddress());
                    server.setPortaCliente(porta);
                    server.setIpCliente(ipA);
                    return true;
                } else {
                    System.out.println("Conexão Rejeitada: " + ipA.getHostAddress());
                    return false;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public ServerUDP getServer() {
        return server;
    }
     
    public void EncerrarConexao(){
        buffer = Mensagem.getMensagemConfiguracao(MensagemType.ENCERRAR_CONEXAO).toString().getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, server.getIpCliente(), server.getPortaCliente());
        try {
            server.getSocket().send(send);
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
