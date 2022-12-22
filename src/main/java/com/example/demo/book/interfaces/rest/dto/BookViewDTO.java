package com.example.demo.book.interfaces.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.*;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/** 書籍檢視資料 */
@Schema(name = "BookViewDTO", description = "書籍檢視資料")
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2022-04-08T14:59:30.957743+08:00[Asia/Taipei]")
public record BookViewDTO(
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
        String description,
    @JsonProperty("created_by")
        @NotNull
        @Schema(name = "created_by", description = "建檔人員", required = true)
        String createdBy,
    @JsonProperty("created_date")
        @Valid
        @Schema(name = "created_date", description = "建立時間", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime createdDate) {}
