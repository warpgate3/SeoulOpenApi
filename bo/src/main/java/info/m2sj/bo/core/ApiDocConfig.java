package info.m2sj.bo.core;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.Optional;

@Configuration
@EnableSwagger2WebFlux
@Import(BeanValidatorPluginsConfiguration.class)
public class ApiDocConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Project K")
                        .description("서울시 공용 주차장 조회 서비스")
                        .contact(new Contact("무명소졸", "http://warpgate3.tistory.com", "warpgate3@gmail.com"))
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("info.m2sj.bo.domain.parkinfo.controller"))
                .build()
                .genericModelSubstitutes(Optional.class, Flux.class, Mono.class);

    }
}
