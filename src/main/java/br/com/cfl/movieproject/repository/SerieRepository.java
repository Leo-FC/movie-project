package br.com.cfl.movieproject.repository;

import br.com.cfl.movieproject.model.Categoria;
import br.com.cfl.movieproject.model.Episodio;
import br.com.cfl.movieproject.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByElencoContainingIgnoreCase(String nomeAtor);

    //GreaterThanEqual: >=
    List<Serie> findByElencoContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    //Pesquisando série com um máximo de temporada e avaliacao >= um valor escolhido
    List<Serie> findByQtdTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer qtdTemporadas, Double avaliacao);

    @Query("select s from Serie s WHERE s.qtdTemporadas <= :qtdTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer qtdTemporadas, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria genero);

    // ILIKE = ignora o case sensitive
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo ILIKE :nomeSerie ORDER BY e.avaliacao DESC LIMIT :limite")
    List<Episodio> buscarTopEpisodios(String nomeSerie, Integer limite);
}
