package com.example.demo.book.interfaces.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/** 錯誤訊息 */
@Schema(name = "ErrorMessage", description = "錯誤訊息")
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2022-04-08T14:59:30.957743+08:00[Asia/Taipei]")
public record ErrorMessage(
    @JsonProperty("timestamp")
        @Valid
        @Schema(name = "timestamp", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime timestamp,
    @JsonProperty("status") @Schema(name = "status", required = false) Integer status,
    @JsonProperty("error") @Schema(name = "error", required = false) String error,
    @JsonProperty("message") @Schema(name = "message", required = false) String message,
    @JsonProperty("path") @Schema(name = "path", required = false) String path,
    @JsonProperty("errors") @Valid @Schema(name = "errors", required = false)
        List<ErrorMessageDetail> errors) {}
