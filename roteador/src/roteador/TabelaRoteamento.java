package roteador;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TabelaRoteamento {
    /*
     * Implemente uma estrutura de dados para manter a tabela de roteamento.
     * A tabela deve possuir: IPs Destino, Métrica e IP de Saída.
     */
    List<String> ips_destino;
    List<Integer> metricas;
    List<String> ips_saida;

    public TabelaRoteamento() {
        this.ips_destino = new ArrayList<String>();
        this.metricas = new ArrayList<Integer>();
        this.ips_saida = new ArrayList<String>();
    }

    /* Atualize a tabela de rotamento a partir da string recebida. */
    // System.out.println( IPAddress.getHostAddress() + ": " + tabela_s);
    public void update_tabela(String tabela_s, InetAddress IPAddress) {        
        tabela_s = tabela_s.trim();

        if(tabela_s.equals("!")) {
            System.out.println("Tabela vazia");
            return;
        }

        String[] tabela = tabela_s.split("\\*");

        // Remove o primeiro elemento do array, pois ele é vazio
        String[] tabela_aux = new String[tabela.length - 1];

        for (int i = 1; i < tabela.length; i++) {
            tabela_aux[i - 1] = tabela[i];
        }

        for (String ipAndMetric : tabela_aux) {
            String[] ipAndMetricArray = ipAndMetric.split(";");
            String ip = ipAndMetricArray[0];
            Integer metric = Integer.parseInt(ipAndMetricArray[1]);

            if (ips_destino.contains(ip)) {
                int index = ips_destino.indexOf(ip);
                if (metricas.get(index) <= metric) {
                    ips_destino.set(index, ip);
                    metricas.set(index, metric);
                    ips_saida.set(index, IPAddress.getHostAddress());
                }
            } else {
                ips_destino.add(ip);
                metricas.add(metric);
                ips_saida.add(IPAddress.getHostAddress());
            }
        }

        
        // se houver um destino no ip_destino que nao estiver na tabela_s, remove
        for (int i = 0; i < ips_destino.size(); i++) {
            if (!tabela_s.contains(ips_destino.get(i))) {
                ips_destino.remove(i);
                metricas.remove(i);
                ips_saida.remove(i);
            }
        }
        
    }

    public String get_tabela_string() {
        String tabela_string = ""; /* Tabela de roteamento vazia conforme especificado no protocolo */

        if (ips_destino.size() == 0) {
            tabela_string = "!";
        } else {
            /*
             * Converta a tabela de rotamento para string, conforme formato definido no
             * protocolo .
             */
            
            for (int i = 0; i < ips_destino.size(); i++) {
                tabela_string += "*" + ips_destino.get(i) + ";" + metricas.get(i);
            }
        }

        return tabela_string;
    }

}
