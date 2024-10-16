package taco.rbiz.domain.model;

import lombok.Data;
import taco.rbiz.domain.model.enums.OptionType;

import java.util.List;

/**
 * 상품의 선택 옵션을 나타낸다
 */
@Data
public class Option {

    private String name;
    private OptionType type; // RADIO or CHECKBOX
    private List<String> values;
}
