package fr.lauparr.apigenerator.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String label;

	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "profile_role")
	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "role")
	private List<Role> roles = new ArrayList<>();

	@Builder
	public Profile(String label, @Singular List<Role> roles) {
		this.label = label;
		this.roles = roles;
	}
}
