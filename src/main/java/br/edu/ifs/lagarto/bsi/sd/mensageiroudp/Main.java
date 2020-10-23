///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.edu.ifs.lagarto.bsi.sd.mensageiroudp;
//
//import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao.ClienteUDP;
//import br.edu.ifs.lagarto.bsi.sd.mensageiroudp.conexao.ServerUDP;
//import com.sun.security.ntlm.Client;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author zero
// */
//public class Main {
//    
////    public static void main(String[] args) {
////        ServerUDP server1 = new ServerUDP(32000,"user1");
////        ServerUDP server2 = new ServerUDP(32001,"user2");
////        
////        
////        Thread t1;
////        t1 = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    server1.aguardarConexao(null);
////                    server1.receberMensagem(new IPrint() {
////                        @Override
////                        public void print(String msg, String username) {
////                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////                        }
////
////                        @Override
////                        public void print(Mensagem mensagem) {
////                            ClienteUDP client2 = new ClienteUDP(server1);
////                            System.out.println(mensagem); //To change body of generated methods, choose Tools | Templates.
////                            client2.enviarMensagem("Retorno", "test");
////                        }
////                    });
////                } catch (IOException ex) {
////                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
////                }
////            }
////        });
////        t1.start();
////        
////        ClienteUDP client1 = new ClienteUDP(server2);
////        boolean r = client1.IniciarConexao("127.0.0.1", 32000);
////        System.out.println(r);
////      
////        Thread t2;
////        t2 = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    
////                    client1.getServer().receberMensagem(new IPrint() {
////                        @Override
////                        public void print(String msg, String username) {
////                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////                        }
////
////                        @Override
////                        public void print(Mensagem mensagem) {
////                            
////                            System.out.println(mensagem); //To change body of generated methods, choose Tools | Templates.
////                            
////                        }
////                    });
////                } catch (IOException ex) {
////                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
////                }
////            }
////        });
////        t2.start();
////        
////        
////        client1.enviarMensagem("Test", "user1");
////        client1.enviarMensagem("Test", "user1");
////        client1.enviarMensagem("Test", "user1");
////        
////        
////    }
//    
//    public static void User1(){
//        ClienteUDP cliente = new ClienteUDP(32000, System.getProperty("user.name"));
//        try {
//            cliente.getServer().aguardarConexao(null);
//            cliente.getServer().receberMensagem(new IPrint() {
//                @Override
//                public void print(String msg, String username) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//                
//                @Override
//                public void print(Mensagem mensagem) {
//                    System.out.print(mensagem.getMsg());
//                }
//            });
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("Conexão Fechada");
//    }
//    
//    public static void User2(){
//        ClienteUDP cliente = new ClienteUDP(32001, System.getProperty("user.name"));
//        cliente.IniciarConexao("127.0.0.1", 32000);
//        System.out.println("AKIII");
//        //cliente.enviarMensagem("oi", "oi");
//        cliente.enviarMensagem("MENSSS", "sjjsjsjjsjs");
//        cliente.enviarMensagem("MENSSS", "sjjsjsjjsjs");
//        cliente.enviarMensagem("MENSSS", "sjjsjsjjsjs");
//        cliente.enviarMensagem("MENSSS", "sjjsjsjjsjs");
//        cliente.enviarMensagem("MENSSS", "sjjsjsjjsjs");
//        cliente.EncerrarConexao();
//        //            cliente.getServer().receberMensagem(new IPrint() {
////                @Override
////                public void print(String msg, String username) {
////                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////                }
////
////                @Override
////                public void print(Mensagem mensagem) {
////                    System.out.print(mensagem.getMsg());
////                }
////            });;
//System.out.println("Conexão2 Fechada");
//    }
//    
//    public static void main(String[] args) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                User1();
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                User2();
//            }
//        }).start();
//        ;
//        
////        ServerUDP server = new ServerUDP(32000, System.getProperty("user.name"));
////        
////        
////        ClienteUDP client = new ClienteUDP(32001, "zero");
////        client.IniciarConexao("127.0.0.1", 32000);
////        client.enviarMensagem("123----", client.getServer().getUsername());
////        
////        client.EncerrarConexao();
//        //l.interrupt();
//       
//        //l.destroy();
//        
//       
//        
//    }
//    
//    public static void leitura(){
//    Thread t2;
//        t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {ClienteUDP client = new ClienteUDP(32000, "zero2");
//                
//                    if(!client.getServer().aguardarConexao(null))
//                        return;
//                    client.getServer().receberMensagem(new IPrint() {
//                        @Override
//                        public void print(String msg, String username) {
//                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                        }
//
//                        @Override
//                        public void print(Mensagem mensagem) {
//                            System.out.println(mensagem); //To change body of generated methods, choose Tools | Templates.
//                            client.enviarMensagem("Retorno", client.getServer().getUsername());
//                        }
//                    });
//                    System.out.print("Saiu");
//                } catch (IOException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        t2.start();}
//    
//    
//    
//}
