package demo.spring.boot.getting.started;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Step 1 - comment all dependencies in POM.XML - with exception of spring-boot-starter-data-jpa and h2


// Step 2 - comment about @SpringBootApplication annotation


// Step 3 - Create a @Entity class Reservation


// Step 4 - declare the interface ReservationRepository without annotations: @RepositoryRestResource
// 4.1. The interface JpaRepository (Logical Repository) provide database persistence methods such as findAll, findAllById, etc
// 4.2. Insert the class (Reservation) and id type (Long)
// 4.3. Show JpaRepository methods (open declaration)
// 4.4. Show PagingAndSortingRepository (open declaration)
// 4.5. Show CrudRepository (open declaration)
// 4.6. Declare findByReservationName method without @RestResource and @Param


// Step 5 - Create a @Bean "runner" to inject ReservationRepository
// For each element the array will be created a reservation object
// Check the results in console - called methods findAll() and findByReservationName()


// Step 6 - Create a REST service
// 6.1. uncomment dependencies spring-boot-starter-web and spring-boot-starter-data-rest
// 6.2. uncomment dependencies jackson-annotations and jackson-databind - just to adjust the JSON format
// 6.3. create a microservice ReservationRestController
// 6.4. uncomment interface ReservationRepository - Step 6
// 6.5. comment interface ReservationRepository - Step 4


// Step 7 - Create a new controller ReservationMvcController
// 7.1. uncomment dependency spring-boot-starter-thymeleaf
// 7.2. create html file - reservations.html
// 7.3. create ReservationMvcController

// Step 8 - Enable metrics and security
// 8.1. uncomment dependencies spring-boot-starter-actuator and spring-boot-starter-security
// 8.2. fill application.properties with security and management keys
// 8.3. show http://localhost:9000/admin/metrics
// 8.4. show http://localhost:9000/admin/beans
// 8.5. show http://localhost:9000/admin/mappings

// Step 9 - Embedded web servers
// 9.1. Tomcat by default (is not necessary add the dependency)
// 9.2. uncomment dependency spring-boot-starter-jetty and run the application
// 9.3. uncomment dependency spring-boot-starter-undertow and run the application
//      undertow is sponsored by JBoss

// Step 10 - Create a custom indicator
// 10.1. Create a @Bean hybrisIndicator
// 10.2. show http://localhost:9000/admin/health
// 10.3. show http://localhost:9000/admin/beans



// Step 2
@SpringBootApplication
public class JoshLongDemoSpringBootGettingStartedApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoshLongDemoSpringBootGettingStartedApplication.class, args);
	}
	
	// Step 5
	@Bean
	CommandLineRunner runner (ReservationRepository rr) {
		return args -> {
			Arrays.asList("Les,Josh,Phil,Sasha,Peter".split(",")).forEach(n -> rr.save(new Reservation(n)));
			
			rr.findAll().forEach( System.out::println );
			rr.findByReservationName("Les").forEach(System.out::println);
		};
	}
	
	// Step 10
	@Bean
	HealthIndicator hybrisIndicator () {
		return ()-> Health.status("I <3 SFJUG!").build();
	}
}

//Step 3 
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

// Step 4 
//interface ReservationRepository extends JpaRepository<Reservation, Long> {
//	
//	// select * from reservations where reservation_name = :rn
//	Collection<Reservation> findByReservationName (String rn);
//}

// Step 6
@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	// select * from reservations where reservation_name = :rn
	@RestResource(path="by-name")
	Collection<Reservation> findByReservationName ( @Param("rn") String rn);
}

//Step 6 
@RestController
class ReservationRestController {

	@RequestMapping("/reservations")
	Collection<Reservation> reservations() {
		return this.reservationRepository.findAll();
	}
	
	@Autowired
	private ReservationRepository reservationRepository;
}

//Step 7
@Controller
class ReservationMvcController {
	
	@RequestMapping("/reservations-2")
	String reservations (Model model) {
		model.addAttribute("reservationsxxx", this.reservationRepository.findAll());
		
		return "reservations";
	}

	@Autowired
	private ReservationRepository reservationRepository;
}

//Is not necessary
//@Component
//class ReservationResourceProcessor implements ResourceProcessor<org.springframework.hateoas.Resource<Reservation>> {
//	
//	public ReservationResourceProcessor() {
//		//super();
//		
//		System.out.println(":: Component => ReservationResourceProcessor ::");
//	}
//	
//	@Override
//	public org.springframework.hateoas.Resource<Reservation> process(org.springframework.hateoas.Resource<Reservation> reservationResource) {
//		
//		reservationResource.add(new org.springframework.hateoas.Link("http://s3.com/imgs/" + reservationResource.getContent().getId() + ".jpg", "profile-photo"));
//	
//		return reservationResource;
//	}
//}