package br.dev.norn.stigma.auth.role;

import br.dev.norn.stigma.auth.permission.Permission;
import com.github.slugify.Slugify;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void generateSlug() {
        if (this.name != null && !this.name.isEmpty()) {
            final Slugify slg = Slugify.builder().build();
            this.slug = slg.slugify(this.name);
        }
    }
}
