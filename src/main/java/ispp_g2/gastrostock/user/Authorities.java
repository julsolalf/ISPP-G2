package ispp_g2.gastrostock.user;

import ispp_g2.gastrostock.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Authorities extends BaseEntity{

    @Column(length = 20)
	String authority;
}
