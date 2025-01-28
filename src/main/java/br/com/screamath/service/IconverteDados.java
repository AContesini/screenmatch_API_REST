package br.com.screamath.service;

public interface IconverteDados {
    <T> T  obetemDados(String json,Class<T> classe);
}
