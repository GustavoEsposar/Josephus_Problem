import ListaDupla.*;

/**
 * Classe que implementa o algoritmo solucao para o Problema de Josephus
 * 
 * @author Gustavo Barbieri Esposar
 *         Caio de nasi Sclavi
 * @version v1.0 2022 06 10
 */
public class RegrasJosephus {
    public static String[] ResolverJoesephus(ListaDuplamenteLigadaCircular lista, int num) {
        int n = lista.getQtdNos();
        String partes = "";
        if (n > 1) {
            // No ant;
            No p = lista.getInicio();
            int c = 1;
            do {
                // ant = p;
                p = p.getProximo();
                c++;

                if (c == num) {
                    c = 1;

                    // ant = p.getAnterior();
                    No prox = p.getProximo();
                    partes += p.getConteudo().toString() + "-";
                    lista.remover(p.getId());

                    p = prox;

                    n--;
                }
            } while (n != 1);
        }
        String[] valores = partes.split("-");
        for (int i = 0; i < valores.length; i++)
            System.out.println(valores[i]);
        return valores;
    }
}