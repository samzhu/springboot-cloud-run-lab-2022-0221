package com.example.demo.book.interfaces.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.*;
import javax.annotation.Generated;
import javax.validation.constraints.*;
import lombok.Getter;

/** 錯誤明細 */
@Schema(name = "ErrorMessageDetail", description = "錯誤明細")
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2022-04-08T14:59:30.957743+08:00[Asia/Taipei]")
public record ErrorMessageDetail(
    @JsonProperty("defaultMessage") @Schema(name = "defaultMessage", required = false)
        String defaultMessage,
    @JsonProperty("objectName") @Schema(name = "objectName", required = false) String objectName,
    @JsonProperty("field") @Schema(name = "field", required = false) String field,
    @JsonProperty("rejectedValue") @Schema(name = "rejectedValue", required = false)
        Integer rejectedValue,
    @JsonProperty("bindingFailure") @Schema(name = "bindingFailure", required = false)
        Boolean bindingFailure,
    @JsonProperty("code") @Schema(name = "code", required = false) String code) {}
