package br.com.cfl.movieproject.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
