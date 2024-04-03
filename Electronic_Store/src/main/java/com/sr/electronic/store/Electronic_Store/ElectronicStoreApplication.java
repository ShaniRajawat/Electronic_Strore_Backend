package com.sr.electronic.store.Electronic_Store;

import com.sr.electronic.store.Electronic_Store.entities.Role;
import com.sr.electronic.store.Electronic_Store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Value("${admin.role.id}")
	private String role_admin_id;
	@Value("${normal.role.id}")
	private String role_normal_id;


	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("abcd"));

		try {
			Role role_Admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role role_Normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
			roleRepository.save(role_Admin);
			roleRepository.save(role_Normal);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
