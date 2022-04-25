package br.com.dbc.devser.colabore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColaboreApplication {
    /*
        TODO:
            - Testes unitários;
            - Testar rotas de segurança (Consertar);
            - Refatorar e deixar perfomático;

        IDEIAS
            - Serviço de email para criador da fundraiser e doador;
            - Testes automatizados;
     */
    public static void main(String[] args) {
        SpringApplication.run(ColaboreApplication.class, args);
    }

}
