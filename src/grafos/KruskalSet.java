package grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KruskalSet<TIPO> {
	
	Map<String, String> parent = new HashMap<>();
	 
    public void montaMap(Set<String> verticesUnicos)
    {
        // cria `n` conjuntos disjuntos (um para cada vértice)
       
        verticesUnicos.stream().forEach(v -> {
			parent.put(v, v);
		});
    }
    
 // Encontra a raiz do conjunto ao qual pertence o elemento `k`
    private String buscaK(String k)
    {
        // se `k` for root
        if (parent.get(k) == k) {
            return k;
        }
 
        //recorre para o pai até encontrarmos a raiz
        return buscaK(parent.get(k));
    }
    
    // Realiza união de dois subconjuntos
    private void uniao(String a, String b)
    {
        // encontra a raiz dos conjuntos que os elementos `x` e `y` pertencem
        String x = buscaK(a);
        String y = buscaK(b);
 
        parent.put(x, y);
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
 
        // MST contém exatamente arestas `V-1`
        while (arestasNaMST.size() != verticesUnicos.size() - 1)
        {
            // considera a próxima aresta com peso mínimo do gráfico
            Aresta next_aresta = arestas.get(index++);
 
            // encontra a raiz dos conjuntos para os quais dois endpoints
            // os vértices da próxima aresta pertencem
            String x = ks.buscaK(next_aresta.getInicio().getDadosString());
            String y = ks.buscaK(next_aresta.getFim().getDadosString());
 
            // se ambos os endpoints tiverem pais diferentes, eles pertencem a
            // diferentes componentes conectados e podem ser incluídos no MST
            if (x != y)
            {
            	arestasNaMST.add(next_aresta);
                ks.uniao(x, y);
            }
        }
 
        return arestasNaMST;
    }
}
