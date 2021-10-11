package com.hanssecurity.uaa.domain.dto;

import com.hanssecurity.uaa.domain.MfaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author hans
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendTotpDto implements Serializable {

    @NotNull
    private String mfaId;
    @NotNull
    private MfaType mfaType;
}
