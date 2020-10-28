package com.nitinol.todolistapi.persist;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Схема таблицы тасков
 */
@Entity
@Table(name = "tasks")
@Data
public class Task {
    @ApiModelProperty(value = "ID таска")
    @Id
    @GeneratedValue
    private UUID id;

    @ApiModelProperty(value = "Заголовок таска")
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

    @ApiModelProperty(value = "Завершен ли таск")
    @Column(nullable = false)
    private boolean complete;

    @ApiModelProperty(value = "Описание таска")
    @Column
    private String description;

    @ApiModelProperty(value = "Приоритет таска")
    @Column
    private int urgency;

    @ApiModelProperty(value = "Список")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "list_id")
    private List list;
}
