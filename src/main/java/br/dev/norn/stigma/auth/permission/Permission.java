package br.dev.norn.stigma.auth.permission;

import com.github.slugify.Slugify;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @PrePersist
    @PreUpdate
    public void generateSlug() {
        if (this.name != null && !this.name.isEmpty()) {
            final Slugify slg = Slugify.builder().build();
            this.slug = slg.slugify(this.name);
        }
    }

}
