/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao;

/**
 *
 * @author zero
 */
public interface IConexaoAceitaServer {
    
    public boolean aceitar(String ip, String user);
}
