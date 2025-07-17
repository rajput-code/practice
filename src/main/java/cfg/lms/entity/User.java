package cfg.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor@Table(schema="VehicleManagement",name="Vuser")
public class User {
    @Id @GeneratedValue
    private Long userId;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
}