package ispp_g2.gastrostock.user;

import ispp_g2.gastrostock.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User extends BaseEntity{
    

    @Column(unique = true)
	String username;

    @NotBlank
	String password;

    @NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority_id")
	Authorities authority;

	public Boolean hasAuthority(String auth) {
		return authority.getAuthority().equals(auth);
	}

	public Boolean hasAnyAuthority(String... authorities) {
		Boolean cond = false;
		for (String auth : authorities) {
			if (auth.equals(authority.getAuthority()))
				cond = true;
		}
		return cond;
	}
}
