package com.nursetrack;


import com.nursetrack.model.*;
import com.nursetrack.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class NurseTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(NurseTrackApplication.class, args);
	}

	/**
	 * Inicializa datos de ejemplo para desarrollo
	 */
	@Bean
	CommandLineRunner inicializarDatos(
			PasswordEncoder passwordEncoder,
			RoleRepository roleRepository,
			UserRepository userRepository,
			ServicioRepository servicioRepository,
			TurnoRepository turnoRepository,
			ConceptoAusenciaRepository conceptoAusenciaRepository,
			EnfermeraRepository enfermeraRepository,
			AusenciaRepository ausenciaRepository) {

		return args -> {
			// Crear roles si no existen
			if (roleRepository.count() == 0) {
				Role adminRole = new Role();
				adminRole.setNombre("ADMIN");
				adminRole.setDescripcion("Administrador del sistema");

				Role userRole = new Role();
				userRole.setNombre("USER");
				userRole.setDescripcion("Usuario regular");

				roleRepository.saveAll(Arrays.asList(adminRole, userRole));

				System.out.println("Roles creados exitosamente");
			}

			// Crear usuario administrador por defecto si no existe
			if (userRepository.count() == 0) {
				User adminUser = new User();
				adminUser.setUsername("admin");
				adminUser.setPassword(passwordEncoder.encode("admin123"));
				adminUser.setNombre("Administrador");
				adminUser.setApellidos("Sistema");
				adminUser.setEmail("admin@adosimbron.com");
				adminUser.setActivo(true);

				// Asignar rol admin
				Role adminRole = roleRepository.findByNombre("ADMIN").orElseThrow();
				adminUser.setRoles(new HashSet<>(Arrays.asList(adminRole)));

				User regularUser = new User();
				regularUser.setUsername("user");
				regularUser.setPassword(passwordEncoder.encode("user123"));
				regularUser.setNombre("Usuario");
				regularUser.setApellidos("Regular");
				regularUser.setEmail("user@adosimbron.com");
				regularUser.setActivo(true);

				// Asignar rol user
				Role userRole = roleRepository.findByNombre("USER").orElseThrow();
				regularUser.setRoles(new HashSet<>(Arrays.asList(userRole)));

				userRepository.saveAll(Arrays.asList(adminUser, regularUser));

				System.out.println("Usuarios creados exitosamente");
			}

			// Crear servicios si no existen
			if (servicioRepository.count() == 0) {
				String[] serviciosNombres = {"Urgencias", "Medicina Interna", "Cirugía",
						"Pediatría", "Ginecología", "UCI", "Consulta Externa","Tecnologías"};

				for (String nombre : serviciosNombres) {
					Servicio servicio = new Servicio();
					servicio.setNombre(nombre);
					servicio.setDescripcion("Servicio de " + nombre);
					servicio.setActivo(true);
					servicioRepository.save(servicio);
				}

				System.out.println("Servicios creados exitosamente");
			}

			// Crear turnos si no existen
			if (turnoRepository.count() == 0) {
				Turno matutino = new Turno();
				matutino.setNombre("Matutino");
				matutino.setDescripcion("Turno de mañana");
				matutino.setHoraInicio("07:00");
				matutino.setHoraFin("14:00");
				matutino.setActivo(true);

				Turno vespertino = new Turno();
				vespertino.setNombre("Vespertino");
				vespertino.setDescripcion("Turno de tarde");
				vespertino.setHoraInicio("14:00");
				vespertino.setHoraFin("21:00");
				vespertino.setActivo(true);

				Turno nocturno = new Turno();
				nocturno.setNombre("Nocturno");
				nocturno.setDescripcion("Turno de noche");
				nocturno.setHoraInicio("21:00");
				nocturno.setHoraFin("07:00");
				nocturno.setActivo(true);

				Turno jornada = new Turno();
				jornada.setNombre("Jornada Acumulada");
				jornada.setDescripcion("Fin de semana");
				jornada.setHoraInicio("07:00");
				jornada.setHoraFin("19:00");
				jornada.setActivo(true);

				turnoRepository.saveAll(Arrays.asList(matutino, vespertino, nocturno, jornada));

				System.out.println("Turnos creados exitosamente");
			}

			// Crear conceptos de ausencia si no existen
			if (conceptoAusenciaRepository.count() == 0) {
				ConceptoAusencia vacaciones = new ConceptoAusencia();
				vacaciones.setNombre("Vacaciones");
				vacaciones.setDescripcion("Periodo vacacional");
				vacaciones.setDiasMaximo(20);
				vacaciones.setActivo(true);

				ConceptoAusencia incapacidad = new ConceptoAusencia();
				incapacidad.setNombre("Incapacidad Médica");
				incapacidad.setDescripcion("Reposo por enfermedad");
				incapacidad.setDiasMaximo(15);
				incapacidad.setActivo(true);

				ConceptoAusencia licencia = new ConceptoAusencia();
				licencia.setNombre("Licencia");
				licencia.setDescripcion("Licencia con goce de sueldo");
				licencia.setDiasMaximo(5);
				licencia.setActivo(true);

				ConceptoAusencia permiso = new ConceptoAusencia();
				permiso.setNombre("Permiso");
				permiso.setDescripcion("Permiso temporal");
				permiso.setDiasMaximo(3);
				permiso.setActivo(true);

				ConceptoAusencia maternidad = new ConceptoAusencia();
				maternidad.setNombre("Maternidad");
				maternidad.setDescripcion("Licencia por maternidad");
				maternidad.setDiasMaximo(84);
				maternidad.setActivo(true);

				conceptoAusenciaRepository.saveAll(Arrays.asList(vacaciones, incapacidad, licencia, permiso, maternidad));

				System.out.println("Conceptos de ausencia creados exitosamente");
			}

			// Crear algunas enfermeras de ejemplo si no existen
			if (enfermeraRepository.count() == 0) {
				// Obtener servicios y turnos para asignar
				Servicio urgencias = servicioRepository.findByNombre("Urgencias");
				Servicio medicina = servicioRepository.findByNombre("Medicina Interna");
				Servicio cirugia = servicioRepository.findByNombre("Cirugía");

				Turno matutino = turnoRepository.findByNombre("Matutino");
				Turno vespertino = turnoRepository.findByNombre("Vespertino");
				Turno nocturno = turnoRepository.findByNombre("Nocturno");

				// Crear enfermeras
				Enfermera enfermera1 = new Enfermera();
				enfermera1.setNumeroEmpleado("E001");
				enfermera1.setNombre("María");
				enfermera1.setApellidoPaterno("García");
				enfermera1.setApellidoMaterno("López");
				enfermera1.setRfc("GALM800101ABC");
				enfermera1.setCurp("GALM800101MDFRRR01");
				enfermera1.setTelefono("5551234567");
				enfermera1.setCorreo("maria.garcia@hospital.com");
				enfermera1.setFechaIngreso(LocalDate.of(2018, 1, 15));
				enfermera1.setServicio(urgencias);
				enfermera1.setTurno(matutino);
				enfermera1.setFechaCreacion(LocalDate.now());
				enfermera1.setUsuarioCreacion("Ado Simbron");
				enfermera1.setActivo(true);

				Enfermera enfermera2 = new Enfermera();
				enfermera2.setNumeroEmpleado("E002");
				enfermera2.setNombre("Juan");
				enfermera2.setApellidoPaterno("Hernández");
				enfermera2.setApellidoMaterno("Martínez");
				enfermera2.setRfc("HEMJ850520DEF");
				enfermera2.setCurp("HEMJ850520HDFRNN02");
				enfermera2.setTelefono("5559876543");
				enfermera2.setCorreo("juan.hernandez@hospital.com");
				enfermera2.setFechaIngreso(LocalDate.of(2019, 3, 10));
				enfermera2.setServicio(medicina);
				enfermera2.setTurno(vespertino);
				enfermera2.setFechaCreacion(LocalDate.now());
				enfermera2.setUsuarioCreacion("Ado Simbron");
				enfermera2.setActivo(true);

				Enfermera enfermera3 = new Enfermera();
				enfermera3.setNumeroEmpleado("E003");
				enfermera3.setNombre("Laura");
				enfermera3.setApellidoPaterno("Rodríguez");
				enfermera3.setApellidoMaterno("Sánchez");
				enfermera3.setRfc("ROSL901215GHI");
				enfermera3.setCurp("ROSL901215MDFDRR03");
				enfermera3.setTelefono("5553456789");
				enfermera3.setCorreo("laura.rodriguez@hospital.com");
				enfermera3.setFechaIngreso(LocalDate.of(2020, 6, 5));
				enfermera3.setServicio(cirugia);
				enfermera3.setTurno(nocturno);
				enfermera3.setFechaCreacion(LocalDate.now());
				enfermera3.setUsuarioCreacion("Ado Simbron");
				enfermera3.setActivo(true);

				enfermeraRepository.saveAll(Arrays.asList(enfermera1, enfermera2, enfermera3));

				System.out.println("Enfermeras de ejemplo creadas exitosamente");

				// Crear algunas ausencias de ejemplo
				ConceptoAusencia vacaciones = conceptoAusenciaRepository.findByNombre("Vacaciones");
				ConceptoAusencia incapacidad = conceptoAusenciaRepository.findByNombre("Incapacidad Médica");

				LocalDate hoy = LocalDate.now();

				Ausencia ausencia1 = new Ausencia();
				ausencia1.setEnfermera(enfermera1);
				ausencia1.setConceptoAusencia(vacaciones);
				ausencia1.setFechaInicio(hoy.minusDays(5));
				ausencia1.setFechaFin(hoy.plusDays(5));
				ausencia1.setObservaciones("Vacaciones programadas");
				ausencia1.setFechaRegistro(hoy.minusDays(30));
				ausencia1.setUsuarioRegistro("Ado Simbron");

				Ausencia ausencia2 = new Ausencia();
				ausencia2.setEnfermera(enfermera2);
				ausencia2.setConceptoAusencia(incapacidad);
				ausencia2.setFechaInicio(hoy.minusDays(1));
				ausencia2.setFechaFin(hoy.plusDays(2));
				ausencia2.setObservaciones("Incapacidad por gripe");
				ausencia2.setFechaRegistro(hoy.minusDays(1));
				ausencia2.setUsuarioRegistro("Ado Simbron");

				ausenciaRepository.saveAll(Arrays.asList(ausencia1, ausencia2));

				System.out.println("Ausencias de ejemplo creadas exitosamente");
			}
		};
	}
}