package kernel.tech.systemgab.utils.contract;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private String status;
    private String message;
    private T data  ;
    private List<T> datas;
    private String token ;

    // Getters, setters et constructeurs
}
