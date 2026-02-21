package io.github.marquinhos_c.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Classe de configuração responsável por definir
 * como a aplicação se conecta ao banco de dados.
 *
 * Aqui é criado o DataSource que será utilizado pelo Spring
 * e pelo JPA para obter conexões com o banco.
 */
@Configuration
public class DatabaseConfiguration {

    /**
     * URL de conexão com o banco de dados.
     * Ex: jdbc:postgresql://localhost:5432/library
     */
    @Value("${spring.datasource.url}")
    String url;
    /**
     * Usuário do banco de dados.
     */
    @Value("${spring.datasource.username}")
    String username;
    /**
     * Senha do banco de dados.
     */
    @Value("${spring.datasource.password}")
    String password;
    /**
     * Driver JDBC utilizado para a conexão.
     */
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    /**
     * Cria e configura um DataSource com pool de conexões
     * utilizando o HikariCP.
     *
     * Este é o DataSource principal da aplicação.
     * Ele reutiliza conexões, melhora a performance
     * e permite controle total sobre o acesso ao banco.
     */
    @Bean
    public DataSource hikariDataSource() {

        // Objeto de configuração do HikariCP
        HikariConfig config = new HikariConfig();

        // Configura dados básicos de conexão
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        // Define o número máximo de conexões no pool
        config.setMaximumPoolSize(10);

        // Define o número mínimo de conexões ociosas
        config.setMinimumIdle(1);

        // Nome do pool (útil para logs e monitoramento)
        config.setPoolName("library-db-pool");

        // Tempo máximo de vida de uma conexão (10 minutos)
        config.setMaxLifetime(600000); // 600 mil ms (10 minutos)

        // Tempo máximo de espera para obter uma conexão do pool
        config.setConnectionTimeout(100000); // timeout para conseguir uma conexão

        // Query usada para validar se a conexão está ativa
        config.setConnectionTestQuery("select 1");

        // Cria o DataSource com base na configuração definida
        return new HikariDataSource(config);
    }
}

