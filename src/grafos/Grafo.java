package grafos;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.transform.stax.StAXResult;

public class Grafo<TIPO> {
	private ArrayList<Vertice<TIPO>> vertices;
	private ArrayList<Aresta<TIPO>> arestas;

	private ArrayList<Vertice<TIPO>> verticesRemovidos;
	private ArrayList<Aresta<TIPO>> arestasRemovidas;
	
	public ArrayList<Vertice<TIPO>> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Vertice<TIPO>> vertices) {
		this.vertices = vertices;
	}

	public ArrayList<Aresta<TIPO>> getArestas() {
		return arestas;
	}

	public void setArestas(ArrayList<Aresta<TIPO>> arestas) {
		this.arestas = arestas;
	}

	public Grafo() {
		this.vertices = new ArrayList<Vertice<TIPO>>();
		this.arestas = new ArrayList<Aresta<TIPO>>();
	}

	public void adicionarVertice(TIPO dado) {
		Vertice<TIPO> novoVertice = new Vertice<TIPO>(dado);
		this.vertices.add(novoVertice);
	}

	/*
	 * Insersão de aresta
	 * */
	public void adicionarAresta(TIPO dadoOrigem, TIPO dadoDesttino, int peso) {
		Vertice<TIPO> origem = this.getVertice(dadoOrigem);
		Vertice<TIPO> destino = this.getVertice(dadoDesttino);
		Aresta<TIPO> aresta = new Aresta<TIPO>(origem, destino, peso);
		origem.adicionarArestaSaida(aresta);  // já add a aresta criada como uma das arestas de destino no vertice de origem
		destino.adicionarArestaEntrada(aresta); // já add a aresta criada como uma das arestas de origem no vertice de destino
		this.arestas.add(aresta); //add aresta criada a lista de arestas do grafo
	}

	public Vertice<TIPO> getVertice(TIPO dado) {
		Vertice<TIPO> vertice = null;
		for (int i = 0; i < this.vertices.size(); i++) {
			if (this.vertices.get(i).getDado().equals(dado)) {
				vertice = this.vertices.get(i);
				break;
			}
		}
		return vertice;
	}
	
	public Vertice<TIPO> getVerticeString(String dado) {
		Vertice<TIPO> vertice = null;
		for (int i = 0; i < this.vertices.size(); i++) {
			if (this.vertices.get(i).getDado().equals(dado)) {
				vertice = this.vertices.get(i);
				break;
			}
		}
		return vertice;
	}
	
	/*
	 * Remoção de aresta
	 * */
	public void removeAresta(TIPO dadoOrigem, TIPO dadoDesttino) {
		Vertice<TIPO> verticeOrigem = this.getVertice(dadoOrigem);
		Vertice<TIPO> verticeDestino = this.getVertice(dadoDesttino);
		
		ArrayList<Aresta<TIPO>> arestasRemove = new ArrayList<Aresta<TIPO>>(); 
		
		arestas.stream().forEach(a -> { 
			if(a.getInicio().equals(verticeOrigem) && a.getFim().equals(verticeDestino)) {
				arestasRemove.add(a);
			}
		});
		
		arestas.removeAll(arestasRemove);
	}
	
	/*
	 * Remoção de Vétices
	 * */
	
	public void removeVerticeESuasArestas(TIPO dadoRemover) {
		Vertice<TIPO> verticeRemover = this.getVertice(dadoRemover); //busca o vertice na lista de vertices do grafo
		
		//remove arestas de entrada do vertice e trata seus destinos(vertices destino)
		ArrayList<Aresta<TIPO>> arestasEntradaVertice = verticeRemover.getArestasEntrada(); //get arestas de entradas do vertice
		for (Aresta<TIPO> aresta : arestasEntradaVertice) { //para cada aresta de entrada do vertice:
			Vertice<TIPO> verticesInicioAresta = aresta.getInicio(); //pega as arestas de inicio do vertice
			ArrayList<Aresta<TIPO>> arestasSaida = verticesInicioAresta.getArestasSaida(); //get aresta de saida do vertice de inivio da aresta
			arestasSaida.remove(aresta); //remove a aresta de saida do vertice destino
			//this.arestasRemovidas.add(aresta); //add a lista geral de arestas removidas do grafo
			verticesInicioAresta.setArestasSaida(arestasSaida);  //atualiza as arestas removdas do verttice destino
		}
		
		//remove arestas de saidas do vétice e trata suas origens(vertices origens)
		ArrayList<Aresta<TIPO>> arestasSaidaVertice = verticeRemover.getArestasSaida();
		for (Aresta<TIPO> aresta : arestasSaidaVertice) {
			Vertice<TIPO> verticesDestinoAresta = aresta.getFim();
			ArrayList<Aresta<TIPO>> arestasEntrada = verticesDestinoAresta.getArestasEntrada();
			arestasEntrada.remove(aresta);
			//this.arestasRemovidas.add(aresta);
			verticesDestinoAresta.setArestasEntrada(arestasEntrada);
		}
		
		if(this.vertices.contains(verticeRemover)) {  //verifica se o grafo contem o vertice que será removido
			this.vertices.remove(verticeRemover);
			//this.verticesRemovidos.add(verticeRemover);
			System.out.println("Vétice e arestas incidentes/adjacentes removidas!");
		} else {
			System.out.println("Vétice inexistente nesse grafo!");
		}
	}
	
	/*
	 * 3. Desenvolver procedimento para mostrar o grafo.
	 * */
	public void mostrarGrafo() {
		arestas.stream().forEach(a -> {
			System.out.println(a.getInicio().getDado() + ", " + a.getFim().getDado());
		});
	}
	
	private Map<Vertice<TIPO>, List<Vertice<TIPO>>> montaMatrizAdjacencia(Set<String> vertices) {
		Map<Vertice<TIPO>, List<Vertice<TIPO>>> matrizAdjacencia = 
				new HashMap<Vertice<TIPO>, List<Vertice<TIPO>>>();
		
		for (String vertice : vertices) {
			List<Vertice<TIPO>> verticesAdjacentes = new ArrayList();
			Vertice<TIPO> verticeAtual = getVerticeString(vertice); //get o vertice na lista de vertices do grafo
			for(int i = 0; i < arestas.size(); i++) { //percorre a lista de arestas do grafo
				Aresta<TIPO>  aresta = arestas.get(i); //para cada aresta 
				if(aresta.getInicio().equals(verticeAtual)) { //se a aresta tiver o inicio igual ao vertice atual do Set
					verticesAdjacentes.add(aresta.getFim()); //add a lista de adjacentes o vertice final dessa aresta
				}
			}
			
			matrizAdjacencia.put(verticeAtual, verticesAdjacentes); //add ao map o vertice atual e a lista de adjacencia encontrada para esse vertice
		}	
		return matrizAdjacencia;
	}
	
	/*
	 * 4. Desenvolver uma função que devolve a representação do grafo em matriz de adjacência.
	 * */
	public void imprimeMatrizAdjacencia(Set<String> vertices) {
		Map<Vertice<TIPO>, List<Vertice<TIPO>>> matrizAdjacencia = this.montaMatrizAdjacencia(vertices);
		
		for (Vertice<TIPO> vertice : matrizAdjacencia.keySet()) {
			StringBuilder verticesAdj = new StringBuilder();
			List<Vertice<TIPO>> adjacentesVerticeAtual = matrizAdjacencia.get(vertice); //pega a lista de vertices adjacentes 
			adjacentesVerticeAtual.stream().forEach(adj -> verticesAdj.append(adj.getDado() + " ")); //percorre a lista dos adjacentes
			System.out.println(vertice.getDado()+ " -> " + verticesAdj); //imprimindo o dado de cada vertice adjacente
		}
	}
	
	/*
	 * 5. Desenvolver uma função para verificar se uma aresta pertence ao grafo.
	 * */
	public boolean verificaArestaPertenceGrafo(TIPO dadoOrigem, TIPO dadoDesttino) {
		Vertice<TIPO> verticeOrigem = this.getVertice(dadoOrigem);
		Vertice<TIPO> verticeDestino = this.getVertice(dadoDesttino);
		boolean arestaPertenceGrafo = false;
		
		if(Objects.nonNull(verticeOrigem) && Objects.nonNull(verticeDestino)) {  //verifica se vertices não são nulos
			for (Aresta<TIPO> aresta : arestas) { //percorre a lista de arestas do grafo
				if(aresta.getInicio().equals(verticeOrigem) && aresta.getFim().equals(verticeDestino)){
					arestaPertenceGrafo = true; //para cada aresta verifica se existe uma com vertice origem e destino igual a q o usuario passou
					}
			}
		}
		return arestaPertenceGrafo;
	}
	
	/*
	 * 6. Desenvolver uma função que devolve todos os vértices adjacentes a um vértice.
	 * */
	public void getVerticesAdjacentes(TIPO dado, Set<String> vertices) {
		Vertice<TIPO> vertice = this.getVertice(dado);
		Map<Vertice<TIPO>, List<Vertice<TIPO>>> matrizAdjacencia = this.montaMatrizAdjacencia(vertices);
		List<Vertice<TIPO>> adjacentesVerticeAtual = matrizAdjacencia.get(vertice); //busco pelo vertice informado a lista de adjacencia na matriz
		StringBuilder verticesAdj = new StringBuilder();
		adjacentesVerticeAtual.stream().forEach(adj -> verticesAdj.append(adj.getDado() + " ")); //preencho o stringBuilder com cada dado do vertice
		System.out.println(vertice.getDado()+ " = " + verticesAdj); 
	}
	
	/*
	 * 7. Desenvolver uma função que devolve todos os vértices que são incidentes em um vértice.
	 * */
	public void getVerticesIncidentes(TIPO dado) {
		Vertice<TIPO> vertice = this.getVertice(dado);
		ArrayList<Vertice<TIPO>> incidentesVerticeAtual = new ArrayList<Vertice<TIPO>>();
		List<Aresta<TIPO>> arestaEntradaVertice = vertice.getArestasEntrada(); //pego a lista de arestas de entrada ao vertice
		for (Aresta<TIPO> aresta : arestaEntradaVertice) {
			incidentesVerticeAtual.add(aresta.getInicio()); //add cada vertice origem da aresta a lista de incidentes
		}
		
		incidentesVerticeAtual.stream().forEach(i -> {
			System.out.println(i.getDado() + " "); //imprimo a lista de incidentes
		});
	}
	
	
	/*
	 * 8. Desenvolver uma função que devolve o grafo complemento.
	 * */
	public void mostrarGrafoComplemento() { 
		arestasRemovidas.stream().forEach(ar -> {
			System.out.println(ar.getInicio().getDado() + ", " + ar.getFim().getDado());
		});
	}
	
	/*
	 * 9. Desenvolver uma função que devolve o grafo transposto (grafo formado pela inversão de todas as arestas).
	 * */
	
	public void imprimeMatrizAdjacenciaTransposta(Set<String> vertices) {
		Map<Vertice<TIPO>, List<Vertice<TIPO>>> matrizAdjacenciaTransposta = this.getGrafoTransposto(vertices);
		
		for (Vertice<TIPO> vertice : matrizAdjacenciaTransposta.keySet()) {
			StringBuilder verticesAdj = new StringBuilder();
			List<Vertice<TIPO>> adjacentesVerticeAtual = matrizAdjacenciaTransposta.get(vertice);
			adjacentesVerticeAtual.stream().forEach(adj -> verticesAdj.append(adj.getDado() + " "));
			System.out.println(vertice.getDado()+ " = " + verticesAdj);
		}
	}
	
	private Map<Vertice<TIPO>, List<Vertice<TIPO>>> getGrafoTransposto(Set<String> vertices) {
		Map<Vertice<TIPO>, List<Vertice<TIPO>>> matrizAdjacencia = this.montaMatrizAdjacencia(vertices);
		Map<Vertice<TIPO>, List<Vertice<TIPO>>> matrizAdjacenciaTransposta = new HashMap<Vertice<TIPO>, List<Vertice<TIPO>>>();
		
		for (String vertice : vertices) {
			List<Vertice<TIPO>> listaAux = new ArrayList<Vertice<TIPO>>();
			Vertice<TIPO> verticeReal = this.getVerticeString(vertice);  //pego o vertice
			Vertice<TIPO> verticeAtualMap = null;
			for (Map.Entry<Vertice<TIPO>, List<Vertice<TIPO>>> entrada : matrizAdjacencia.entrySet()) { 
				verticeAtualMap = entrada.getKey(); //vertice atual da matriz de adjacencia normal
				List<Vertice<TIPO>> verticesAdjacentesAoAtual = entrada.getValue(); //lista dos vertices adjacentes do vertice atual
				if(verticesAdjacentesAoAtual.contains(verticeReal)) {
					listaAux.add(verticeAtualMap);
				}
			}
			matrizAdjacenciaTransposta.put(verticeReal, listaAux);
		}
		
		return matrizAdjacenciaTransposta;
	}
	
	
	/*
	 * Busca em Largura
	 */
	public void buscaEmLargura(Vertice<TIPO> origem, Vertice<TIPO> destino) {
		int peso = 0;
		ArrayList<Vertice<TIPO>> visitados = new ArrayList<Vertice<TIPO>>();
		ArrayList<Vertice<TIPO>> fila = new ArrayList<Vertice<TIPO>>();
		List<String> caminhos = new ArrayList<String>();
		List<Integer> pesos = new ArrayList<Integer>();
		Vertice<TIPO> objetivo = destino;
		Vertice<TIPO> atual = origem;
		visitados.add(atual);
		fila.add(atual);
		
		try {
			caminhos.add((String)atual.getDado());
		} catch (Exception e) {
			System.out.println("Origem informada errada ou inexistente! Erro: " + e.getMessage());
		}
		
		while(fila.size() > 0 ) {
			boolean encontrouDestino = false;
			Vertice<TIPO> visitado = fila.get(0);
			
			List<Vertice<TIPO>> cidades = new ArrayList<Vertice<TIPO>>();
			try {
				for(int i = 0; i < visitado.getArestasSaida().size(); i++) {
					Vertice<TIPO> proximo = visitado.getArestasSaida().get(i).getFim();
					if(!visitados.contains(proximo)) {
						visitados.add(proximo);
						fila.add(proximo);
						cidades.add(proximo);
					}
				
					if(proximo.getDado().equals(objetivo.getDado())) {
						encontrouDestino = true;
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Origem ou Destino informado errado ou inexistente! Erro: " + e.getMessage());
			}
			
			for(int i = 0; i < cidades.size(); i++) {
				for(int j = 0; j < cidades.get(i).getArestasEntrada().size(); j++) {
					if(this.verifica(caminhos, (String) cidades.get(i).getArestasEntrada().get(j).getInicio().getDado()) != null){
						caminhos.add(this.verifica(caminhos, (String) cidades.get(i).getArestasEntrada().get(j).getInicio().getDado())
								+ "->" + (String)cidades.get(i).getDado());
					}
				}
			}

			fila.remove(0);
			if(encontrouDestino) {
				System.out.println("Execução da Busca em Largura:" + "\n");
				System.out.println("Caminho: " + caminhos.get(caminhos.size()-1) + "\n");
				System.out.println("Peso: " + this.calculaPeso(caminhos.get(caminhos.size()-1)) + "\n");
				break;
			}
		}
	}
	
	
	private String verifica(List<String> caminhos, String cidade) {
		boolean contain = false;
		for(int i = 0; i < caminhos.size(); i++) {
			if(caminhos.get(i).contains(cidade)) {
				return caminhos.get(i);
			}
		}
		return null;
	}
	
	private int calculaPeso(String caminho) {
		String[] cidades = caminho.split("->");
		int pesoCalculado = 0;
		
		for(int i = 1; i < cidades.length; i++) {
			String c = cidades[i];
			List<Aresta<TIPO>> arestas = this.getVerticeString(c).getArestasEntrada();

			for (Aresta<TIPO> aresta : arestas) {
				if(aresta.getInicio().getDado().equals(cidades[i-1])) {
					pesoCalculado = pesoCalculado + aresta.getPeso();
				}
			}
		}
		return pesoCalculado;
	}

	/*
	 * Busca em Profundidade
	 */
	
	public void buscaEmProfundidade(Vertice<TIPO> origem, Vertice<TIPO> destino) {
		ArrayList<Vertice> visitados = new ArrayList<>();
	    ArrayList<String> caminhos = new ArrayList<>();
	    ArrayList<Integer> pesos = new ArrayList<>();
	    visitados.add(origem);
	    caminhos.add(origem.getDado().toString());
	    int peso = 0;
	    pesos.add(0);
	    this.buscaEmProfundidade(origem, destino, visitados, caminhos, peso, pesos);
	}
	
	private void buscaEmProfundidade(Vertice<TIPO> origem, Vertice<TIPO> destino, ArrayList<Vertice> visitados,  
			ArrayList<String> caminhos, int peso, ArrayList<Integer> pesos) {
		if(!visitados.contains(destino)) {
			for (int i = 0; i < origem.getArestasSaida().size(); i++) {
				if(visitados.contains(destino)) {
					break;
				}
				if (!visitados.contains(origem.getArestasSaida().get(i).getFim())) {
					visitados.add(origem.getArestasSaida().get(i).getFim());
					peso = peso + origem.getArestasSaida().get(i).getPeso();
					caminhos.add(caminhos.get(caminhos.size()-1) + "->" + origem.getArestasSaida().get(i).getFim().getDado());
					pesos.add(peso);
					buscaEmProfundidade(origem.getArestasSaida().get(i).getFim(), destino, visitados, caminhos, peso, pesos);
				}
			}
		} else {
			System.out.println("Busca em Profundidade:" + "\n");
			System.out.println("Caminho:" + caminhos.get(caminhos.size()-1) + "\n");
			System.out.println("Peso:" + pesos.get(pesos.size()-1) + "\n");
		}	
	}
	
	/*
	 * Busca pelo Custo Uniforme
	 * */
	public void buscaPeloCustoUniforme(Vertice<TIPO> origem, Vertice<TIPO> destino) {
		int peso = 0;
		ArrayList<Vertice<TIPO>> visitados = new ArrayList<Vertice<TIPO>>();
		ArrayList<Vertice<TIPO>> fila = new ArrayList<Vertice<TIPO>>();
		List<String> caminhos = new ArrayList<String>();
		List<Integer> pesos = new ArrayList<Integer>();
		Vertice<TIPO> objetivo = destino;
		Vertice<TIPO> atual = origem;
		visitados.add(atual);
		fila.add(atual);
		
		try {
			caminhos.add((String)atual.getDado());
		} catch (Exception e) {
			System.out.println("Origem informada errada ou inexistente! Erro: " + e.getMessage());
		}
		
		while(fila.size() > 0 ) {
			boolean encontrouDestino = false;
			Vertice<TIPO> visitado = fila.get(0);
			
			List<Vertice<TIPO>> cidades = new ArrayList<Vertice<TIPO>>();
			try {
				for(int i = 0; i < visitado.getArestasSaida().size(); i++) {
					Vertice<TIPO> proximo = visitado.getArestasSaida().get(i).getFim();
					if(!visitados.contains(proximo)) {
						visitados.add(proximo);
						fila.add(proximo);
						cidades.add(proximo);
					}
				
					if(proximo.getDado().equals(objetivo.getDado())) {
						encontrouDestino = true;
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Origem ou Destino informado errado ou inexistente! Erro: " + e.getMessage());
			}
			
			for(int i = 0; i < cidades.size(); i++) {
				for(int j = 0; j < cidades.get(i).getArestasEntrada().size(); j++) {
					if(this.verifica(caminhos, (String) cidades.get(i).getArestasEntrada().get(j).getInicio().getDado()) != null){
						caminhos.add(this.verifica(caminhos, (String) cidades.get(i).getArestasEntrada().get(j).getInicio().getDado())
								+ "->" + (String)cidades.get(i).getDado());
					}
				}
			}

			fila.remove(0);
			this.ordenaFronteiraPeloCusto(fila);;
			if(encontrouDestino) {
				System.out.println("Busca em Largura:" + "\n");
				System.out.println("Caminho: " + caminhos.get(caminhos.size()-1) + "\n");
				System.out.println("Peso: " + this.calculaPeso(caminhos.get(caminhos.size()-1)) + "\n");
				break;
			}
		}
	}
	
	private void ordenaFronteiraPeloCusto(ArrayList<Vertice<TIPO>> fila) {
		fila.stream().forEach(f -> {
			ArrayList<Aresta<TIPO>> arestasEntrada = f.getArestasEntrada();
			this.ordenaArestasPorCusto(arestasEntrada);
			f.setMenorCusto(arestasEntrada.get(0).getPeso());
		});
		fila.sort(Comparator.comparing(Vertice::getMenorCusto));
	}
	
	private void ordenaArestasPorCusto(ArrayList<Aresta<TIPO>> arestasEntrada) {
		arestasEntrada.sort(Comparator.comparing(Aresta::getPeso));
	}
	
	public Vertice selecionarAleatorio() {
		List<Vertice<TIPO>> list = new ArrayList<>(vertices);
		Collections.shuffle(list);
		return list.get(new Random().nextInt(vertices.size() - 1));
	}
	
	public List<Aresta<TIPO>> getAdjacentes(Vertice<TIPO> origem) {
		return arestas.stream().filter(aresta -> aresta.getInicio().getDadosString().equals(origem.getDadosString()))
				.collect(Collectors.toList());
	}
	
	
	/*
	 * Algoritmo Prim
	 */
	public void algoritmoPrim(Grafo grafo, String vertice) {
		Prim prim = new Prim();
		Set<Aresta<TIPO>> arvoreDeArestasMinimas =  prim.algoritmoPrim(grafo, vertice); 
		System.out.println("ARESTAS DO CAMINHO MÍNIMO: ");
		//imprimir árvore
		arvoreDeArestasMinimas.forEach(a -> {
			System.out.println(a.getInicio().getDado() + " - " + a.getFim().getDado() + "    peso: " + a.getPeso());
		});
	}
	
	/*
	 * Algoritmo Kruskal
	 */
	public void algoritmoKruskal(Set<String> verticesUnicos){
		KruskalSet kruskalSet = new KruskalSet();
		int n = verticesUnicos.size();
		List<Aresta<TIPO>> retorno = kruskalSet.runKruskalAlgorithm(arestas, verticesUnicos);
		
		System.out.println("ARESTAS DO CAMINHO MÍNIMO: ");
		
		retorno.forEach(a -> {
			System.out.println(a.getInicio().getDado() + " - " + a.getFim().getDado() + "    peso: " + a.getPeso());
		});
	}
	
	
	/*
	 * Algoritmo Dijkstra
	 */
	
	public void algoritmoDijkstra(String verticeOrigem, String verticeDestino) {
		Dijkstra dijkstra = new Dijkstra<TIPO>();
		
		Vertice<TIPO> vOrigem = getVerticeString(verticeOrigem);
		Vertice<TIPO> vDestino = getVerticeString(verticeDestino);
		
		List<Vertice<TIPO>> verticesMenorCaminho = dijkstra.encontrarMenorCaminhoDijkstra(vertices, vOrigem, vDestino);
		
		verticesMenorCaminho.forEach(v -> {
			System.out.println(v.getDado());
		});
	}
	
	/*
	 * Algoritmo Bellman-Ford
	 */
	
	/*
	 * Algoritmo Floyd-Warshal
	 */

}
