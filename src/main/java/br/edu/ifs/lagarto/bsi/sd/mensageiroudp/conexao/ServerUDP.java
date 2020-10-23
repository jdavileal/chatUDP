/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao;

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
    //private long tempoValidacaoConexaoViva = 40000;
    private int validacao = 0;
    private DatagramPacket datagrama;
    private byte buffer[] = new byte[1024];

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

    /**
     *
     * @param conexaoAceitar Interface com o metodo de aceitar, que diz se a
     * conexão dever ser aceita
     * @return true se a conexão for aceita e false se a conexão for recursada
     * @throws IOException
     */
    public boolean aguardarConexao(IConexaoAceitaServer conexaoAceitar) throws IOException {
        while (true) {
            datagrama = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagrama);
            Mensagem msg = Mensagem.getMensagem(datagrama.getData());
            try {
                byte bufferSend[] = new byte[1024];
                if (msg.getType() == MensagemType.INICIAR_CONEXAO) {
//                    int op = JOptionPane.showConfirmDialog(null,
//                            datagrama.getAddress().getHostAddress()+ "Está solicitando Uma conexão",
//                            "Deseja Aceitar",
//                            JOptionPane.INFORMATION_MESSAGE);
                    if (conexaoAceitar.aceitar(datagrama.getAddress().getHostAddress(), msg.getUsername())) {
                        IpCliente = datagrama.getAddress();
                        portaCliente = datagrama.getPort();
                        bufferSend = Mensagem.getMensagemConfiguracao(MensagemType.ACEITAR_CONEXAO).toString().getBytes();
                        datagrama = new DatagramPacket(bufferSend, bufferSend.length, IpCliente, datagrama.getPort());
                        socket.send(datagrama);
                        System.out.println("Conexao Aceita");
                        return true;
                    } else {
                        bufferSend = Mensagem.getMensagemConfiguracao(MensagemType.REJEITAR_CONEXAO).toString().getBytes();
                        datagrama = new DatagramPacket(bufferSend, bufferSend.length, datagrama.getAddress(), datagrama.getPort());
                        socket.send(datagrama);
                        System.out.println("Conexao Rejeitada");
                        return false;
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Este método continua rodando até receber uma mensagem de Encerrar Conexao
     *
     * @param print
     * @throws IOException
     */
    public void receberMensagem(IPrint print) throws IOException {
        boolean controle = true;
        while (controle) {
            buffer = new byte[1024];
            datagrama = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagrama);
            Mensagem msg = Mensagem.getMensagem(datagrama.getData());
            if (!datagrama.getAddress().equals(IpCliente)) {
                continue;
            }
            if (null != msg.getType()) {
                switch (msg.getType()) {
                    case ACEITAR_CONEXAO:
                        portaCliente = datagrama.getPort();
                        IpCliente = datagrama.getAddress();
                        System.out.print("Conexão aceita: " + IpCliente.getHostAddress());
                        break;
                    case ESTAH_ATIVO_REQUEST:
                        buffer = Mensagem.getMensagemConfiguracao(MensagemType.ESTAH_ATIVO_REPLY).toString().getBytes();
                        datagrama = new DatagramPacket(buffer, buffer.length, IpCliente, datagrama.getPort());
                        socket.send(datagrama);
                        validacao = 0;
                        break;
                    case MENSAGEM:
                        print.print(msg);
                        break;
                    case ENCERRAR_CONEXAO:
                        System.out.println("Encerrando Conexão");
                        IpCliente = null;
                        portaCliente = 0;
                        datagrama = null;
                        msg.setMsg("A conexão foi Encerrada");
                        msg.setUsername("App");
                        print.print(msg);
                        //tempoValidacaoConexaoViva = 0;
                        //aguardarConexao(null);
                        controle = false;
                        break;
                    default:
                        break;
                }
                
            }
        }
    }

    /**
     * TODO: Falta implementação
     */
    public void conexaoAtiva() {
        if (validacao > 3) {
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

    public InetAddress getIpCliente() {
        return IpCliente;
    }

    public int getPortaCliente() {
        return portaCliente;
    }

    public void setIpCliente(InetAddress IpCliente) {
        this.IpCliente = IpCliente;
    }

    public void setPortaCliente(int portaCliente) {
        this.portaCliente = portaCliente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
