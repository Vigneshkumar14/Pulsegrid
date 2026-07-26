# Spring Boot For Quarkus Developers

This note is for developers who are comfortable with Quarkus 3.x and want a practical mental model for how Spring Boot feels in Pulsegrid.

## Short Answer First

No, Spring Boot does not support only the repository pattern.

Spring Boot most commonly uses the repository pattern through Spring Data JPA, but it does not force you into it.

You can choose from:

- repository pattern with Spring Data JPA
- plain JPA with `EntityManager`
- JDBC-based data access with `JdbcTemplate`
- custom DAO-style classes
- event-sourcing or query-model persistence designs

What Spring Boot does not provide as a first-class default is an Active Record style like Quarkus Panache entities where the entity itself exposes persistence methods such as `persist()` or `findById()`.

So the practical answer is:

- Spring Boot supports repository-style persistence very well
- Active Record is not the standard Spring model
- you can emulate Active Record yourself, but it is not the idiomatic Spring Boot approach

## Why Spring Usually Chooses Repository Pattern

Spring tends to keep concerns separated:

- entity class models data
- repository handles persistence
- service handles business logic
- controller handles HTTP

That separation is why Spring code often looks a little more layered than Quarkus Panache-style code.

In this repo, the processor follows that style:

- entity: [VehicleStatusEntity.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/persistence/VehicleStatusEntity.java)
- repository: [VehicleStatusRepository.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/persistence/VehicleStatusRepository.java)
- service: [VehicleStatusProjectionService.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/service/VehicleStatusProjectionService.java)

## Quarkus Panache Vs Spring Data JPA

### Quarkus Panache Style

This is the kind of shape Quarkus developers often know:

```java
@Entity
public class VehicleStatus extends PanacheEntity {
    public String vehicleId;
    public double speedKph;
}
```

And then:

```java
VehicleStatus status = new VehicleStatus();
status.vehicleId = "VH-1001";
status.speedKph = 80.0;
status.persist();
```

That is close to Active Record.

### Spring Data JPA Style

In Spring Boot, the usual equivalent is:

```java
@Entity
public class VehicleStatusEntity {
    @Id
    private String vehicleId;
    private double speedKph;
}
```

And then:

```java
vehicleStatusRepository.save(status);
```

That is repository pattern.

From this project:

```java
public interface VehicleStatusRepository extends JpaRepository<VehicleStatusEntity, String> {
}
```

The entity does not persist itself. The repository persists it.

## Does Spring Boot Forbid Active Record

No.

You can write code that behaves in an Active Record-ish way, but Spring will not guide you there.

For example, you could put persistence methods on the entity or create a base entity helper, but that usually becomes awkward because:

- entities should stay simple
- transaction boundaries belong in services
- repositories are easier to test and mock
- business logic becomes clearer when persistence is separate

So even though it is possible, it is usually not recommended in Spring applications.

## What Spring Boot Encourages Instead

Spring Boot encourages:

1. controller receives request
2. service runs business logic
3. repository loads or saves entities
4. response DTO goes back out

In Pulsegrid, the flow is:

1. [FleetStatusController.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/api/FleetStatusController.java) exposes the API
2. [VehicleStatusProjectionService.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/service/VehicleStatusProjectionService.java) updates read models
3. [VehicleStatusRepository.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/persistence/VehicleStatusRepository.java) persists data
4. [VehicleStatusEntity.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/persistence/VehicleStatusEntity.java) models the table row

## Similarities Between Quarkus And Spring Boot

If you already know Quarkus, these concepts transfer very well:

- Maven `pom.xml`
- config files
- dependency injection
- REST controllers
- validation annotations
- JPA and Hibernate
- Flyway migrations
- profile-based config

## Key Differences In This Repo

### 1. Starters Instead Of Extensions

Quarkus usually says:

- add an extension

Spring Boot usually says:

- add a starter

Examples from this repo:

- `spring-boot-starter-webmvc`
- `spring-boot-starter-kafka`
- `spring-boot-starter-data-jpa`

See:

- [apps/backend/telemetry-producer/pom.xml](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-producer/pom.xml)
- [apps/backend/telemetry-processor/pom.xml](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/pom.xml)

### 2. Explicit Application Bootstrap

Spring Boot applications usually start from a clear main class:

- [TelemetryProducerApplication.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-producer/src/main/java/com/pulsegrid/producer/TelemetryProducerApplication.java)
- [TelemetryProcessorApplication.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/TelemetryProcessorApplication.java)

```java
@SpringBootApplication
public class TelemetryProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelemetryProducerApplication.class, args);
    }
}
```

### 3. Typed Config With `@ConfigurationProperties`

Quarkus developers often use `@ConfigProperty` or `@ConfigMapping`.

In Spring Boot, a very common pattern is:

```java
@ConfigurationProperties(prefix = "pulsegrid.kafka.topics")
public record TelemetryTopicsProperties(String telemetry, String telemetryDlq) {
}
```

That is backed by:

- [application-local.yml](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-producer/src/main/resources/application-local.yml)
- [application-local.yml](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/resources/application-local.yml)

### 4. Constructor Injection Instead Of `@Inject`

Spring usually favors constructor injection:

```java
public TelemetryProducerController(TelemetryProducerService telemetryProducerService) {
    this.telemetryProducerService = telemetryProducerService;
}
```

Instead of field injection.

### 5. Kafka Integration Feels Different

In Quarkus, you may have used Reactive Messaging with `@Incoming` and `@Outgoing`.

In this project, Spring uses:

- `KafkaTemplate` for producing
- Kafka Streams beans for processing

Producer example:

- [TelemetryProducerService.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-producer/src/main/java/com/pulsegrid/producer/service/TelemetryProducerService.java)

Processor example:

- [TelemetryStreamsConfig.java](/C:/personal_projects/Pulsegrid/apps/backend/telemetry-processor/src/main/java/com/pulsegrid/processor/config/TelemetryStreamsConfig.java)

## Translation Cheat Sheet

| Quarkus concept | Spring Boot concept |
|---|---|
| extension | starter |
| `@Inject` | constructor injection |
| `@ApplicationScoped` | `@Service` or `@Component` |
| `@Path` | `@RequestMapping` |
| `@GET` / `@POST` | `@GetMapping` / `@PostMapping` |
| `@ConfigProperty` / `@ConfigMapping` | `@ConfigurationProperties` |
| Panache entity | JPA entity plus repository |
| Panache repository | Spring Data repository |
| reactive messaging channels | `KafkaTemplate`, `@KafkaListener`, or Kafka Streams |

## How To Read Pulsegrid As A Quarkus Developer

Use this order:

1. open the application bootstrap class
2. read `application.yml` and `application-local.yml`
3. read the controller
4. read the service
5. read the persistence layer
6. read the Kafka Streams configuration

That order usually makes the Spring structure click quickly.

## Final Guidance

If you are coming from Quarkus Panache, the biggest adaptation is this:

- in Quarkus, the entity can feel smart and persistence-aware
- in Spring, the entity is usually simpler and the repository/service layers do more of the work

That is not because Spring Boot cannot do Active Record. It is because Spring’s default design culture strongly prefers separation of concerns.
