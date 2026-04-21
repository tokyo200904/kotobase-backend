package kotobase_backend.modules.JlptLevel.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jlpt_levels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JlptLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private level level;
}

