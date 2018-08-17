package demo.spring.boot.getting.started;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class JoshLongDemoSpringBootGettingStartedApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoshLongDemoSpringBootGettingStartedApplication.class, args);
	}
	
	@Bean
	CommandLineRunner runner (ReservationRepository rr) {
		return args -> {
			Arrays.asList("Les,Josh,Phil,Sasha,Peter".split(",")).forEach(n -> rr.save(new Reservation(n)));
			
			rr.findAll().forEach( System.out::println );
			rr.findByReservationName("Les").forEach(System.out::println);
		};
	}
}

@RestController
class ReservationRestController {

	@RequestMapping("/reservations")
	Collection<Reservation> reservations() {
		return this.reservationRepository.findAll();
	}
	
	@Autowired
	private ReservationRepository reservationRepository;
}

@Component
class ReservationResourceProcessor implements ResourceProcessor<org.springframework.hateoas.Resource<Reservation>> {
	
	public ReservationResourceProcessor() {
		super();
		
		System.out.println(":: Component => ReservationResourceProcessor ::");
	}
	
	@Override
	public org.springframework.hateoas.Resource<Reservation> process(org.springframework.hateoas.Resource<Reservation> reservationResource) {
		
		reservationResource.add(new org.springframework.hateoas.Link("http://s3.com/imgs/" + reservationResource.getContent().getId() + ".jpg", "profile-photo"));
	
		return reservationResource;
	}
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	// select * from reservations where reservation_name = :rn
	@RestResource(path="by-name")
	Collection<Reservation> findByReservationName ( @Param("rn") String rn);
}

@Entity
class Reservation {
	@Id
	@GeneratedValue
	private Long id;
	
	private String reservationName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	@Override
	public String toString() {
		return "Reservations [id=" + id + ", reservationName=" + reservationName + "]";
	}

	public Reservation(String reservationName) {
		this.reservationName = reservationName;
	}
	
	public Reservation() {

	}
}