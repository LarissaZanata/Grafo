package grafos;

import java.util.ArrayList;
import java.util.List;

public class BellmanFord<TIPO> {
	
	List<Vertice<TIPO>> naoVisitados = new ArrayList<>();
	 // Função para executar o algoritmo Bellman–Ford de uma determinada fonte
    public List<Vertice<TIPO>> bellmanFord(List<Aresta<TIPO>> arestas, List<Vertice<TIPO>> verticesGrafo, Vertice<TIPO> vOrigem) {
        
        List<Vertice<TIPO>> verticesDoGrafo = verticesGrafo;

		// Colocando a distancias iniciais
		for (int i = 0; i < verticesDoGrafo.size(); i++) {

			// Vertice atual tem distancia zero, e todos os outros,
			// 9999("infinita")
				
			if (verticesDoGrafo.get(i).getDado()
					.equals(vOrigem.getDado())) {

				verticesDoGrafo.get(i).setDistancia(0);

			} else {

				verticesDoGrafo.get(i).setDistancia(9999);//distancia infinita

			}
			// Insere o vertice na lista de vertices nao visitados
			this.naoVisitados.add(verticesDoGrafo.get(i));
		}
 

 
        // passo de relaxamento (executar V-1 vezes)
        for (int i = 0; i < verticesDoGrafo.size() - 1; i++)
        {
            for (Aresta<TIPO> a: arestas)
            {
                // aresta de `u` para `v` tendo peso `w`
            	Vertice<TIPO> u = a.getInicio();
            	Vertice<TIPO> v = a.getFim();
                int peso = a.getPeso();
 
                // se a distância até o destino `v` puder ser
                // encurtado tomando edge (u, v)
                if (u.getDistancia() != Integer.MAX_VALUE && u.getDistancia() + peso < v.getDistancia())
                {
                    // atualiza a distância para o novo valor mais baixo
                    v.setDistancia(u.getDistancia() + peso); 
 
                    // define o pai de v como `u`
                    v.setPai(u);
                }
            }
        }
 
        // executa a etapa de relaxamento mais uma vez pela enésima vez para
        // verifica se há ciclos de peso negativo
        for (Aresta<TIPO> a: arestas)
        {
            // aresta de `u` para `v` tendo peso `w`
        	Vertice<TIPO> u = a.getInicio();
        	Vertice<TIPO> v = a.getFim();
            int peso = a.getPeso();
 
            // se a distância até o destino `u` puder ser
            // encurtado tomando edge (u, v)
            if (u.getDistancia() != Integer.MAX_VALUE && u.getDistancia() + peso < v.getDistancia())
            {
                System.out.println("Ciclo encontrado!");
                break;
            }
        }
        
        return verticesDoGrafo;
 
    }
}