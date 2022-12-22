package com.example.demo.book.interfaces.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.*;
import javax.annotation.Generated;
import javax.validation.constraints.*;
import lombok.Getter;

/** 書籍建立資料 */
@Schema(name = "BookCreateDTO", description = "書籍建立資料")
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2022-04-08T14:59:30.957743+08:00[Asia/Taipei]")
public record BookCreateDTO(
    @JsonProperty("id") @NotNull @Schema(name = "id", description = "編號", required = true)
        Integer id,
    @JsonProperty("title")
        @NotNull
        @Size(max = 20)
        @Schema(name = "title", description = "書名", required = true)
        String title,
    @JsonProperty("description")
        @NotNull
        @Size(max = 50)
        @Schema(name = "description", description = "說明", required = true)
        String description) {}
