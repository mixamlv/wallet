package mm.wallet;

import mm.wallet.api.AccountRestApiApi;
import mm.wallet.api.ClientRestApiApi;
import mm.wallet.invoker.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIntegrationTest {

    static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:16");
    static GenericContainer<?> ratesMockServer = new GenericContainer<>("mockserver/mockserver:latest")
            .withExposedPorts(1080)
            .withEnv("MOCKSERVER_INITIALIZATION_JSON_PATH", "/config/initializerJson.json")
            .withCopyFileToContainer(MountableFile.forClasspathResource("/ratesMockServer/initializerJson.json"),
                    "/config/initializerJson.json");

    static {
        postgreSQL.start();
        ratesMockServer.start();
    }

    protected AccountRestApiApi accountApi;
    protected ClientRestApiApi clientApi;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("server.port", () -> "8444");
        registry.add("spring.datasource.username", postgreSQL::getUsername);
        registry.add("spring.datasource.password", postgreSQL::getPassword);
        registry.add("spring.datasource.url", postgreSQL::getJdbcUrl);
        registry.add("infra.exchangerate.baseUrl", () -> "http://localhost:%d".formatted(ratesMockServer.getMappedPort(1080)));
    }

    @BeforeEach
    void init() {
        var webClient = WebClient.builder().build();
        ApiClient client = new ApiClient(webClient);
        client.setBasePath("http://localhost:%d".formatted(port));
        accountApi = new AccountRestApiApi(client);
        clientApi = new ClientRestApiApi(client);
    }

}
