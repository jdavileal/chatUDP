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
public interface IPrint {
    
    public void print(String msg, String username);
    public void print(Mensagem mensagem);
}
