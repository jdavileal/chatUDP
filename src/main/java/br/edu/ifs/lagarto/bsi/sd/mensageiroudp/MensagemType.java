/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp;

/**
 *
 * @author zero
 */
public enum MensagemType {
    
    INICIAR_CONEXAO(1),
    ACEITAR_CONEXAO(2),
    REJEITAR_CONEXAO(3),
    MENSAGEM(4),
    ESTAH_ATIVO_REQUEST(5),
    ESTAH_ATIVO_REPLY(6),
    ENCERRAR_CONEXAO(7);
    
    private int num;
    private MensagemType(int num){
        this.num = num;
    }
    
    public int getNum(){return num;}
    
    public static  MensagemType getType(int num){
        for(MensagemType t : MensagemType.values()){
            if(t.num == num)
                return t;
        }
        return null;
    }
    
}
