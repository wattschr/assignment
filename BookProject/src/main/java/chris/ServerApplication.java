package chris;

import chris.data.JpaConfig;
import chris.grpc.BookJsonServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication extends JpaConfig {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookJsonServer server) throws IOException, InterruptedException {
        return args -> {
            server.start();
            server.blockUntilShutdown();
        };
    }
}
