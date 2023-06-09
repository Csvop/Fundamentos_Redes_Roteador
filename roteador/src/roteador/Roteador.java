package roteador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Roteador {

    public static void main(String[] args) throws IOException {
        /* Lista de endereço IPs dos vizinhos */
        ArrayList<String> ip_list = new ArrayList<>();

        /* Le arquivo de entrada com lista de IPs dos roteadores vizinhos. */
        try ( BufferedReader inputFile = new BufferedReader(new FileReader("IPVizinhos.txt"))) {
            String ip;
            
            while( (ip = inputFile.readLine()) != null){
                ip_list.add(ip);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        /* Cria instâncias da tabela de roteamento e das threads de envio e recebimento de mensagens. */
        TabelaRoteamento tabela = new TabelaRoteamento();

        for (String ipVizinho : ip_list) { // adiciona os vizinhos na tabela
            tabela.ips_destino.add(ipVizinho);
            tabela.metricas.add(1);
            tabela.ips_saida.add(ipVizinho);
        }

        Thread sender = new Thread(new MessageReceiver(tabela));
        Thread receiver = new Thread(new MessageSender(tabela, ip_list));
        
        sender.start();
        receiver.start();
        
        
        while(true){ // a cada 15 segundos imprime a tabela de roteamento
            try {
                System.out.println("\nMinha Tabela:");
                System.out.println("IP Destino     |     Métrica     |      IP Saída");
                for(int i = 0; i < tabela.ips_destino.size(); i++){
                    
                    System.out.println(tabela.ips_destino.get(i) + "           " + tabela.metricas.get(i) + "           " + tabela.ips_saida.get(i));
                    
                }
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
}
