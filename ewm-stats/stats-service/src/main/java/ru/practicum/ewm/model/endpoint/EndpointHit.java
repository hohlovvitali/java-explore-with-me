package ru.practicum.ewm.model.endpoint;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHit {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "app")
    private String app;
    @NotBlank
    @Column(name = "uri")
    private String uri;
    @NotBlank
    @Column(name = "ip")
    private String ip;
    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
