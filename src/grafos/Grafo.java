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
	 * Insers�o de aresta
	 * */
	public void adicionarAresta(TIPO dadoOrigem, TIPO dadoDesttino, int peso) {
		Vertice<TIPO> origem = this.getVertice(dadoOrigem);
		Vertice<TIPO> destino = this.getVertice(dadoDesttino);
		Aresta<TIPO> aresta = new Aresta<TIPO>(origem, destino, peso);
		origem.adicionarArestaSaida(aresta);  // j� add a aresta criada como uma das arestas de destino no vertice de origem
		destino.adicionarArestaEntrada(aresta); // j� add a aresta criada como uma das arestas de origem no vertice de destino
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
	 * Remo��o de aresta
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
	 * Remo��o de V�tices
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
		
		//remove arestas de saidas do v�tice e trata suas origens(vertices origens)
		ArrayList<Aresta<TIPO>> arestasSaidaVertice = verticeRemover.getArestasSaida();
		for (Aresta<TIPO> aresta : arestasSaidaVertice) {
			Vertice<TIPO> verticesDestinoAresta = aresta.getFim();
			ArrayList<Aresta<TIPO>> arestasEntrada = verticesDestinoAresta.getArestasEntrada();
			arestasEntrada.remove(aresta);
			//this.arestasRemovidas.add(aresta);
			verticesDestinoAresta.setArestasEntrada(arestasEntrada);
		}
		
		if(this.vertices.contains(verticeRemover)) {  //verifica se o grafo contem o vertice que ser� removido
			this.vertices.remove(verticeRemover);
			//this.verticesRemovidos.add(verticeRemover);
			System.out.println("V�tice e arestas incidentes/adjacentes removidas!");
		} else {
			System.out.println("V�tice inexistente nesse grafo!");
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
	 * 4. Desenvolver uma fun��o que devolve a representa��o do grafo em matriz de adjac�ncia.
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
	 * 5. Desenvolver uma fun��o para verificar se uma aresta pertence ao grafo.
	 * */
	public boolean verificaArestaPertenceGrafo(TIPO dadoOrigem, TIPO dadoDesttino) {
		Vertice<TIPO> verticeOrigem = this.getVertice(dadoOrigem);
		Vertice<TIPO> verticeDestino = this.getVertice(dadoDesttino);
		boolean arestaPertenceGrafo = false;
		
		if(Objects.nonNull(verticeOrigem) && Objects.nonNull(verticeDestino)) {  //verifica se vertices n�o s�o nulos
			for (Aresta<TIPO> aresta : arestas) { //percorre a lista de arestas do grafo
				if(aresta.getInicio().equals(verticeOrigem) && aresta.getFim().equals(verticeDestino)){
					arestaPertenceGrafo = true; //para cada aresta verifica se existe uma com vertice origem e destino igual a q o usuario passou
					}
			}
		}
		return arestaPertenceGrafo;
	}
	
	/*
	 * 6. Desenvolver uma fun��o que devolve todos os v�rtices adjacentes a um v�rtice.
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
	 * 7. Desenvolver uma fun��o que devolve todos os v�rtices que s�o incidentes em um v�rtice.
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
	 * 8. Desenvolver uma fun��o que devolve o grafo complemento.
	 * */
	public void mostrarGrafoComplemento() { 
		arestasRemovidas.stream().forEach(ar -> {
			System.out.println(ar.getInicio().getDado() + ", " + ar.getFim().getDado());
		});
	}
	
	/*
	 * 9. Desenvolver uma fun��o que devolve o grafo transposto (grafo formado pela invers�o de todas as arestas).
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
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Parte 2 do Trabalho: <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 */
	
	
	/*
	 * Algoritmo Prim
	 */
	public void algoritmoPrim(Grafo grafo, String vertice) {
		Prim prim = new Prim();
		Set<Aresta<TIPO>> arvoreDeArestasMinimas =  prim.prim(grafo, vertice); 
		System.out.println("ARESTAS DO CAMINHO M�NIMO: ");
		System.out.println(" ");
		//imprimir �rvore
		arvoreDeArestasMinimas.forEach(a -> {
			System.out.println(a.getInicio().getDado() + " - " + a.getFim().getDado() + "    peso: " + a.getPeso());
		});
	}
	
	/*
	 * Algoritmo Kruskal
	 */
	public void algoritmoKruskal(Set<String> verticesUnicos){
		KruskalSet kruskalSet = new KruskalSet();
		List<Aresta<TIPO>> retorno = kruskalSet.kruskal(arestas, verticesUnicos);
		
		System.out.println("ARESTAS DO CAMINHO M�NIMO: ");
		System.out.println(" ");
		
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
		
		List<Vertice<TIPO>> retorno = dijkstra.dijkstra(vertices, vOrigem, vDestino);
		
		retorno.forEach(v -> {
			System.out.println(v.getDado() + " com distancia " + v.getDistancia());
		});
	}
	
	/*
	 * Algoritmo Bellman-Ford
	 */
	
	public void algoritmoBellmanFord(String verticeOrigem) {
		BellmanFord bellmanFord = new BellmanFord<TIPO>();
		
		Vertice<TIPO> vOrigem = getVerticeString(verticeOrigem);
		
		List<Vertice<TIPO>> retorno = bellmanFord.bellmanFord(arestas, vertices, vOrigem);
		retorno.forEach(v -> {
			System.out.println(v.getDado() + " com distancia " + v.getDistancia());
		});
	}
	
	/*
	 * Algoritmo Floyd-Warshal
	 */
	public void algoritmoFloydWarshal() {
		System.out.println("O algoritmo FloydWarsha ainda n�o foi implementado.");
		System.out.println(" ");
	}

}
