package kotobase_backend.modules.kanji.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kanji_readings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanjiReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String reading;
    private String readingType;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "kanji_id")
    private Kanji kanji;
}
