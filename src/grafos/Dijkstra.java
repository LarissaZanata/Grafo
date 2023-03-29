package grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dijkstra<TIPO> { //olhar

		// Lista que guarda os vertices pertencentes ao menor caminho encontrado
		List<Vertice<TIPO>> menorCaminho = new ArrayList<Vertice<TIPO>>();

		// Variavel que recebe os vertices pertencentes ao menor caminho
		Vertice<TIPO> verticeCaminho = new Vertice();

		// Variavel que guarda o vertice que esta sendo visitado
		Vertice<TIPO> atual = new Vertice();

		// Variavel que marca o vizinho do vertice atualmente visitado
		Vertice<TIPO> vizinho = new Vertice();

		// Lista dos vertices que ainda nao foram visitados
		List<Vertice<TIPO>> naoVisitados = new ArrayList<>();

		// Algoritmo de Dijkstra
		public List<Vertice<TIPO>> encontrarMenorCaminhoDijkstra(List<Vertice<TIPO>> verticesGrafo, Vertice<TIPO> vOrigem,
				Vertice<TIPO> vDestino) {

			// Adiciona a origem na lista do menor caminho
			menorCaminho.add(vOrigem);
			
			List<Vertice<TIPO>> verticesDoGrafo = verticesGrafo;

			// Colocando a distancias iniciais
			for (int i = 0; i < verticesDoGrafo.size(); i++) {

				// Vertice atual tem distancia zero, e todos os outros,
				// 9999("infinita")
				
				if (verticesDoGrafo.get(i).getDado()
						.equals(vOrigem.getDado())) {

					verticesDoGrafo.get(i).setDistancia(0);

				} else {

					verticesDoGrafo.get(i).setDistancia(9999);

				}
				// Insere o vertice na lista de vertices nao visitados
				this.naoVisitados.add(verticesDoGrafo.get(i));
			}

			//Collections.sort(naoVisitados);

			// O algoritmo continua ate que todos os vertices sejam visitados
			while (!this.naoVisitados.isEmpty()) {

				// Toma-se sempre o vertice com menor distancia, que eh o primeiro
				// da
				// lista

				atual = this.naoVisitados.get(0);

				/*
				 * Para cada vizinho (cada aresta), calcula-se a sua possivel
				 * distancia, somando a distancia do vertice atual com a da aresta
				 * correspondente. Se essa distancia for menor que a distancia do
				 * vizinho, esta eh atualizada.
				 */
				
				ArrayList<Aresta<TIPO>> arestasVerticeAtual = atual.getArestasSaida();
				
				for (int i = 0; i < arestasVerticeAtual.size(); i++) {

					vizinho = arestasVerticeAtual.get(i).getFim();
					if (!vizinho.isVisitado()) {

						// Comparando a distância do vizinho com a possível
						// distância
						if (vizinho.getDistancia() > (atual.getDistancia() + atual
								.getArestasSaida().get(i).getPeso())) {

							vizinho.setDistancia(atual.getDistancia()
									+ atual.getArestasSaida().get(i).getPeso());
							vizinho.setPai(atual);

							/*
							 * Se o vizinho eh o vertice procurado, e foi feita uma
							 * mudanca na distancia, a lista com o menor caminho
							 * anterior eh apagada, pois existe um caminho menor
							 * vertices pais, ateh o vertice origem.
							 */
							if (vizinho == vDestino) {
								menorCaminho.clear();
								verticeCaminho = vizinho;
								menorCaminho.add(vizinho);
								while (verticeCaminho.getPai() != null) {

									menorCaminho.add(verticeCaminho.getPai());
									verticeCaminho = verticeCaminho.getPai();

								}
								// Ordena a lista do menor caminho, para que ele
								// seja exibido da origem ao destino.
								//Collections.sort(menorCaminho);

							}
						}
					}

				}
				// Marca o vertice atual como visitado e o retira da lista de nao
				// visitados
				atual.setVisitado(true);
				this.naoVisitados.remove(atual);

			}

			return menorCaminho;
		}

}
