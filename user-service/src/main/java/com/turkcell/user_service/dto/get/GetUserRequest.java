import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;


//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class GetUserRequest {
    private UUID id;

    public GetUserRequest(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }  

    public void setId(UUID id) {
        this.id = id;
    }   
}
