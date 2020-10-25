/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao;

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

    public void enviarMensagem(String msg) {
        buffer = Mensagem.getMensagem(msg, server.getUsername()).getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, server.getIpCliente(), server.getPortaCliente());
        try {
            server.getSocket().send(send);
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean IniciarConexao(String ip, int porta) {
        try {
            Mensagem mensagem = Mensagem.getMensagemConfiguracao(MensagemType.INICIAR_CONEXAO);
            mensagem.setUsername(this.server.getUsername());
            buffer = mensagem.getBytes();
            InetAddress ipA = InetAddress.getByName(ip);
            DatagramPacket send = new DatagramPacket(buffer, buffer.length, ipA, porta);
            server.getSocket().send(send);
            System.out.println("Iniciando Conexao");
            buffer = new byte[1024];
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

    public void EncerrarConexao() {
        buffer = Mensagem.getMensagemConfiguracao(MensagemType.ENCERRAR_CONEXAO).getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, server.getIpCliente(), server.getPortaCliente());
        try {
            server.getSocket().send(send);
            
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean receberConexao(IConexaoAceitaServer aceitarConexao) throws IOException {
        return this.server.aguardarConexao(aceitarConexao);
    }
    
    public void receberMensagem(IPrint print) throws IOException {
        this.server.receberMensagem(print);
    }
}
