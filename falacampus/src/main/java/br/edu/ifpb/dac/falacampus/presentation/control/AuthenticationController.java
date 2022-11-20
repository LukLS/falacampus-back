package br.edu.ifpb.dac.falacampus.presentation.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import br.edu.ifpb.dac.falacampus.business.service.AuthenticationService;
import br.edu.ifpb.dac.falacampus.business.service.LoginService;
import br.edu.ifpb.dac.falacampus.business.service.TokenService;
import br.edu.ifpb.dac.falacampus.business.service.UserConverterService;
import br.edu.ifpb.dac.falacampus.business.service.UserService;
import br.edu.ifpb.dac.falacampus.business.service.impl.AuthenticationServiceImpl;
import br.edu.ifpb.dac.falacampus.model.entity.User;
import br.edu.ifpb.dac.falacampus.presentation.dto.LoginDto;
import br.edu.ifpb.dac.falacampus.presentation.dto.TokenDto;
import br.edu.ifpb.dac.falacampus.presentation.dto.UserDto;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api")
@Scope(value = WebApplicationContext.SCOPE_SESSION)

//@RequestMapping("/api")
//@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class AuthenticationController {
	
	@Autowired
	private LoginService loginService;

	@Autowired
	private AuthenticationServiceImpl authenticationService;
	@Autowired
	private UserConverterService userConverterService; // private ConverterSystemUser converterSystemUser;
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginDto dto) {
		try {
			String token = authenticationService.suapLogin(dto.getUsername(), dto.getPassword());
			User entity = userService.findByRegistration(dto.getUsername());
			UserDto systemUserDTO = userConverterService.userToDTO(entity);
			
			TokenDto tokenDTO = new TokenDto(token, systemUserDTO);
			
			return new ResponseEntity(tokenDTO, HttpStatus.OK);
		}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/isValidToken")
	public ResponseEntity isTokenValid(@RequestBody TokenDto token) {

		System.out.println("teste: " + token);
		try {
			boolean isTokenValid = tokenService.isValid(token.getToken());

			return new ResponseEntity(true, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/*
	 * @PostMapping("/isTokenValid") public ResponseEntity isTokenValid(@RequestBody
	 * TokenDto dto) { try { boolean isTokenValid =
	 * tokenService.isValid(dto.getToken());
	 * 
	 * return new ResponseEntity(isTokenValid, HttpStatus.OK);
	 * 
	 * } catch (Exception e) { return
	 * ResponseEntity.badRequest().body(e.getMessage()); } }
	 * 
	 */

}
