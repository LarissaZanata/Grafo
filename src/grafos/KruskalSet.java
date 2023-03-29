package grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KruskalSet<TIPO> {
	
	Map<String, String> vertices = new HashMap<>();
	 
    public void montaMap(Set<String> verticesUnicos){
        // cria `n` conjuntos disjuntos (um para cada vértice)
        verticesUnicos.stream().forEach(v -> {
        	vertices.put(v, v);
		});
    }
    
 // Encontra a raiz do conjunto ao qual pertence o vertice
    private String buscaK(String vertice){
        // se o vertice for a raiz
        if (vertices.get(vertice) == vertice) {
            return vertice;
        }
 
        //recorre para o pai até encontrarmos a raiz
        return buscaK(vertices.get(vertice));
    }
    
    // Realiza união de dois subconjuntos
    private void uniao(String verticeOrigem, String verticeDestino){
        // encontra a raiz dos conjuntos que o verticeOrigem e verticeDestino pertencem
        String v_origem = buscaK(verticeOrigem);
        String v_destino = buscaK(verticeDestino);
 
        vertices.put(v_origem, v_destino);
    }

	public List<Aresta<TIPO>> runKruskalAlgorithm(List<Aresta<TIPO>> arestas, Set<String> verticesUnicos){
        // armazena as arestas presentes na MST
        List<Aresta<TIPO>> arestasNaMST = new ArrayList<>();
 
        // cria um conjunto singleton para cada elemento do universo.
        KruskalSet ks = new KruskalSet();
        ks.montaMap(verticesUnicos);
 
        int index = 0;
 
        // ordena as arestas pelo peso, do menor ao maior
        Collections.sort(arestas, Comparator.comparingInt(a -> a.getPeso()));
 
      
        while (arestasNaMST.size() != verticesUnicos.size() - 1)
        {
            // considera a próxima aresta com peso mínimo do gráfico
            Aresta next_aresta = arestas.get(index++);
 
            // encontra a raiz dos conjuntos para os quais dois endpoints
            // os vértices da próxima aresta pertencem
            String verticeOrigem = ks.buscaK(next_aresta.getInicio().getDadosString());
            String verticeDestino = ks.buscaK(next_aresta.getFim().getDadosString());
 
            // se ambos os endpoints tiverem pais diferentes, eles pertencem a
            // diferentes componentes conectados e podem ser incluídos no MST
            if (verticeOrigem != verticeDestino)
            {
            	arestasNaMST.add(next_aresta);
                ks.uniao(verticeOrigem, verticeDestino);
            }
        }
 
        return arestasNaMST;
    }
}
