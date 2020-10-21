/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao;

import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.IConexaoAceitaServer;
import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.IPrint;
import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.Mensagem;
import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.MensagemType;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author zero
 */
public class ServerUDP {

    private int porta;
    private String username;
    private DatagramSocket socket;
    private InetAddress IpCliente;
    private int portaCliente;
    private long tempoValidacaoConexaoViva = 40000;
    private int validacao = 0;

    private DatagramPacket datagrama;
    private byte buffer[] = new byte[1024];
    private byte bufferSend[] = new byte[1024];

    public ServerUDP(int porta, String username) {
        this.porta = porta;
        this.username = username;
        try {
            this.socket = new DatagramSocket(porta);
        } catch (SocketException ex) {
            System.err.println("Error ao abrir um socket: " + ex.getMessage());
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Tests");
    }

    public void aguardarConexao(IConexaoAceitaServer conexaoAceitar ) throws IOException {
        while (true) {
            datagrama = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagrama);
            Mensagem msg = Mensagem.getMensagem(datagrama.getData());
            try {
                if (msg.getType() == MensagemType.INICIAR_CONEXAO) {
                    int op = JOptionPane.showConfirmDialog(null,
                            datagrama.getAddress().getHostAddress() + "Está solicitando Uma conexão",
                            "Deseja Aceitar",
                            JOptionPane.INFORMATION_MESSAGE);
                    if (JOptionPane.OK_OPTION == op) {
                        IpCliente = datagrama.getAddress();
                        buffer = Mensagem.getMensagemConfiguracao(MensagemType.ACEITAR_CONEXAO).toString().getBytes();
                        datagrama = new DatagramPacket(buffer, buffer.length, IpCliente, datagrama.getPort());
                        portaCliente = datagrama.getPort();
                        socket.send(datagrama);
                        conexaoAtiva();
                        receberMensagem( new IPrint() {
                            @Override
                            public void print(String msg, String username) {
                               System.out.println(msg+": "+ username);
                            }

                            @Override
                            public void print(Mensagem mensagem) {
                                System.out.println(mensagem);
                            }
                        });
                        return;
                    } else {
                        buffer = Mensagem.getMensagemConfiguracao(MensagemType.REJEITAR_CONEXAO).toString().getBytes();
                        datagrama = new DatagramPacket(buffer, buffer.length, datagrama.getAddress(), datagrama.getPort());
                        socket.send(datagrama);
                        System.out.println("Conexao Rejeitada");
                    }
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public void receberMensagem(IPrint print) throws IOException {
        while (true) {
            datagrama = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagrama);
            Mensagem msg = Mensagem.getMensagem(datagrama.getData());
            
            if(!datagrama.getAddress().equals(IpCliente))
                continue;
            if(msg.getType() == MensagemType.ACEITAR_CONEXAO){
                portaCliente = datagrama.getPort();
                IpCliente = datagrama.getAddress();
                System.out.print("Conexão aceita: "+IpCliente.getHostAddress());
            }
            else if (msg.getType() == MensagemType.ESTAH_ATIVO_REQUEST) {
                bufferSend = Mensagem.getMensagemConfiguracao(MensagemType.ESTAH_ATIVO_REPLY).toString().getBytes();
                datagrama = new DatagramPacket(bufferSend, bufferSend.length, IpCliente, datagrama.getPort());
                socket.send(datagrama);
                validacao = 0;
            } else if (msg.getType() == MensagemType.MENSAGEM) {
                print.print(msg);
                JOptionPane.showMessageDialog(null, msg);
            }else if (msg.getType() == MensagemType.ENCERRAR_CONEXAO){
                IpCliente = null;
                datagrama = null;
                tempoValidacaoConexaoViva = 0;
                aguardarConexao(null);
                break;
            }
        }
    }
    
//    public void enviarMensagem(String msg, String username){;
//        buffer = Mensagem.getMensagem(msg, username).toString().getBytes();
//        DatagramPacket send = new DatagramPacket(buffer, buffer.length, IpCliente, portaCliente);
//        try {
//            socket.send(send);
//        } catch (IOException ex) {
//            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public void conexaoAtiva(){
        if(validacao > 3){
            JOptionPane.showMessageDialog(null, "A conexão foi encerrada!!!");
            return;
        }
        buffer = Mensagem.getMensagemConfiguracao(MensagemType.ESTAH_ATIVO_REQUEST).toString().getBytes();
        DatagramPacket send = new DatagramPacket(buffer, buffer.length, IpCliente, portaCliente);
        try {
            socket.send(send);
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }
    
    

}
