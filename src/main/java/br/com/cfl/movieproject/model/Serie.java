package br.com.cfl.movieproject.model;

import br.com.cfl.movieproject.service.traducao.TradutorMyMemory;

import java.util.OptionalDouble;

public class Serie {
    private String titulo;
    private Categoria genero;
    private String elenco;
    private Integer qtdTemporadas;
    private Double avaliacao;
    private String poster;
    private String sinopse;
    private String dataDeLancamento;


    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.elenco = dadosSerie.atores();
        this.qtdTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.poster = dadosSerie.poster();
        this.dataDeLancamento = dadosSerie.dataDeLancamento();
        this.sinopse = TradutorMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public Integer getQtdTemporadas() {
        return qtdTemporadas;
    }

    public void setQtdTemporadas(Integer qtdTemporadas) {
        this.qtdTemporadas = qtdTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getDataDeLancamento() {
        return dataDeLancamento;
    }

    public void setDataDeLancamento(String dataDeLancamento) {
        this.dataDeLancamento = dataDeLancamento;
    }

    @Override
    public String toString() {

        return
                "\nPoster: " + poster
                +"\nTitulo: " + titulo
                +"\nGenero: " + genero + " | Elenco: " + elenco
                +"\nTemporadas: " + qtdTemporadas + " | Avaliacao: " + avaliacao + " | Data de lancamento: " + dataDeLancamento
                +"\nSinopse: " + sinopse;
    }
}
