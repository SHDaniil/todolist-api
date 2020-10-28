package com.nitinol.todolistapi.persist;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Схема таблицы списков
 */
@Entity
@Table(name = "lists")
@Data
public class List {
    @ApiModelProperty(value = "ID списка")
    @Id
    @GeneratedValue
    private UUID id;

    @ApiModelProperty(value = "Заголовок списка")
    @Column(nullable = false)
    private String title;

    @ApiModelProperty(value = "Дата создания", example = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @ApiModelProperty(value = "Дата изменения", example = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changeDate;
}
