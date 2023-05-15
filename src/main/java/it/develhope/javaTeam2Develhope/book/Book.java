package it.develhope.javaTeam2Develhope.book;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank (message = "mandatory")
    private String author;
    @NotBlank (message = "mandatory")
    private String title;
    @NotBlank (message = "mandatory")
    private String topic;
    @NotBlank (message = "mandatory")
    private String publisher;
    @NotBlank (message = "mandatory")
    @Column(unique = true)
    private String ISBN;
    @NotNull
    private Integer year;
    @NotNull
    private Float price;
    @NotNull
    private Integer numberOfPages;
    @NotNull
    private Boolean isInStock;
    private String imageCoverFilePath;
    @NotNull
    private Boolean isEbook;
}