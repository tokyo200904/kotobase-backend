package kotobase_backend.modules.user.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RoleName role;

    @OneToMany(mappedBy = "roleName",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
}
