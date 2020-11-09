package fit.fm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@ComponentScan(basePackages = { "fit.fm.server"})
public class FmServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmServerApplication.class, args);
	}

}
