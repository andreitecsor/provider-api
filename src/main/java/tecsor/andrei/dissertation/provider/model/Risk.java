package tecsor.andrei.dissertation.provider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Risk {
    private String pid;
    private String fid;
    private int score;
}
