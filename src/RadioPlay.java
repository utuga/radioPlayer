public class RadioPlay /*implements Comparable<RadioPlay> */{

	private String nome;
	private String url;

	public RadioPlay(String nome, String url) {
		super();
		this.nome = nome;
		this.url = url;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return nome;
	}

	/*@Override
	public int compareTo(RadioPlay o) {
		
		return o.getNome().compareTo(this.nome);
	}*/

}