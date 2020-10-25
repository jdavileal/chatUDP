/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zero
 */
public class Mensagem {
    
    private MensagemType type;
    private String msg;
    private String username;
    
    private  static String sinal = ";-;";
    
    private Mensagem(){}
    
    public static Mensagem getMensagemConfiguracao(MensagemType type){
        Mensagem msg = new Mensagem();
        msg.type = type;
        return msg;
    }
    
    public static Mensagem getMensagem(String mensagem, String username){
        Mensagem msg = new Mensagem();
        msg.type = MensagemType.MENSAGEM;
        msg.msg = mensagem;
        msg.username = username;
        return msg;
    }
    
    public static Mensagem getMensagem(byte[] bytes){
        String[] ms;
        try {
            ms = new String(bytes, 0, bytes.length,"UTF-8").split(sinal);
        } catch (UnsupportedEncodingException ex) {
            ms = new String(bytes, 0, bytes.length).split(sinal);
            Logger.getLogger(Mensagem.class.getName()).log(Level.SEVERE, null, ex);
        }
        Mensagem msg = new Mensagem();
        
        msg.type = MensagemType.getType(Integer.parseInt(ms[0]));
        msg.username = ms[1];
        msg.msg = ms[2];
        return msg;
        
    }
    public static Mensagem getMensagem(String decode){
        String[] ms = decode.split(sinal);
        Mensagem msg = new Mensagem();
        
        msg.type = MensagemType.getType(Integer.parseInt(ms[0]));
        msg.username = ms[1];
        msg.msg = ms[2];
        return msg;
        
    }
    
    public String toString(){
        return this.type.getNum()+sinal+this.username+sinal+this.msg;
    }
    
    public byte[] getBytes(){
        try {
            return this.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Mensagem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.toString().getBytes(); 
    }

    public MensagemType getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public String getUsername() {
        return username;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
    
    
    
    
}
