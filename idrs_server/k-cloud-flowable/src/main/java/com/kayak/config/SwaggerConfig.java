package com.kayak.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.kayak.config.properties.SwaggerProperties;
import com.kayak.utils.StringUtils;
import com.kayak.utils.spring.SpringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Swagger 文档配置
 *
 * @author yuanjinqiao
 */
@RequiredArgsConstructor
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;
    private final OpenApiExtensionResolver openApiExtensionResolver;
    //配置content type
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Arrays.asList("application/json"));

    /**
     * 创建API
     */
    @PostConstruct
    public void createRestApi() {
        for (SwaggerProperties.Groups group : swaggerProperties.getGroups()) {
            String basePackage = group.getBasePackage();
            Docket docket = new Docket(DocumentationType.OAS_30)
                    .enable(swaggerProperties.getEnabled())
                    // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                    .apiInfo(apiInfo())
                    .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                    .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                    // 设置哪些接口暴露给Swagger展示
                    .select()
                    // 扫描所有有注解的api，用这种方式更灵活
                    //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                    // 扫描指定包中的swagger注解
                    .apis(RequestHandlerSelectors.basePackage(basePackage))
                    // 扫描所有 .apis(RequestHandlerSelectors.any())
                    .paths(PathSelectors.any())
                    .build()
                    .groupName(group.getName())
                    // 设置安全模式，swagger可以设置访问token
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts())
                    .extensions(openApiExtensionResolver.buildExtensions(group.getName()))
                    .pathMapping(swaggerProperties.getPathMapping());
            String beanName = StringUtils.substringAfterLast(basePackage, ".") + "Docket";
            SpringUtils.registerBean(beanName, docket);
        }
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
/*    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        String header = saTokenConfig.getTokenName();
        apiKeyList.add(new ApiKey(header, header, In.HEADER.toValue()));
        return apiKeyList;
    }*/

    /**
     * 安全上下文
     */
//    private List<SecurityContext> securityContexts() {
//        List<SecurityContext> securityContexts = new ArrayList<>();
//        securityContexts.add(
//            SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
//                .build());
//        return securityContexts;
//    }

    /**
     * 默认的安全上引用
     */
/*    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(saTokenConfig.getTokenName(), authorizationScopes));
        return securityReferences;
    }*/

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        SwaggerProperties.Contact contact = swaggerProperties.getContact();
        return new ApiInfoBuilder()
                // 设置标题
                .title(swaggerProperties.getTitle())
                // 描述
                .description(swaggerProperties.getDescription())
                // 作者信息
                .contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()))
                // 版本
                .version(swaggerProperties.getVersion())
                .build();
    }
}
