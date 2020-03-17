package everis.bootcamp.bankAccountMicroservice.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public static final Contact DEFAULT_CONTACT = new Contact(
            "Jose Morales Torrejón", "https://github.com/Joselon1999/", "jmortorr@everis.com");

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "clientMicroservice","Microservice responsible for CRUD actions towards the BANK ACCOUNTS" +
            "WITH THE VERIFICATION OF EXISTANCE OF THE CLIENT",
            "1.0","urn:tos",
            DEFAULT_CONTACT.getName(),"Apache 2.0", "http://www.google.com.pe");

    private static Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<String>(Arrays.asList("application/json","application/xml"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
