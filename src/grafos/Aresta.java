package grafos;

public class Aresta<TIPO> {
	private Vertice<TIPO> inicio;
	private Vertice<TIPO> fim;
	private int peso;
	
	public Aresta() {
		
	}
	
	public Aresta(Vertice inicio, Vertice fim, int peso) {
		this.inicio = inicio;
		this.fim = fim;
		this.peso = peso;
	}
	
	public Vertice<TIPO> getInicio() {
		return inicio;
	}
	public void setInicio(Vertice<TIPO> inicio) {
		this.inicio = inicio;
	}
	public Vertice<TIPO> getFim() {
		return fim;
	}
	public void setFim(Vertice<TIPO> fim) {
		this.fim = fim;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
}
