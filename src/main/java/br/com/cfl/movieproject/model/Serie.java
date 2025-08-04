package br.com.cfl.movieproject.model;

import br.com.cfl.movieproject.service.traducao.TradutorMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String elenco;
    private Integer qtdTemporadas;
    private Double avaliacao;
    private String poster;
    private String sinopse;
    private String dataDeLancamento;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Serie(){}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Serie)) return false;
        Serie e = (Serie) o;
        return id == e.id &&
               Objects.equals(titulo, e.titulo);
    }
}
