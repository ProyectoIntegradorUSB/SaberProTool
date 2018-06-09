package com.saberpro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.saberpro.modelo.Facultad;
import com.saberpro.modelo.Programa;
import com.saberpro.modelo.ProgramaUsuario;
import com.saberpro.modelo.TipoUsuario;
import com.saberpro.modelo.Usuario;
import com.saberpro.modelo.dto.UsuarioDTO;
import com.saberpro.presentation.businessDelegate.IBusinessDelegatorView;
import com.saberpro.utilities.Constantes;
import com.saberpro.utilities.FacesUtils;
import com.saberpro.utilities.VariablesSession;;




/**
 * @author Zathura Code Generator http://code.google.com/p/zathura/
 *         www.zathuracode.org
 *
 */
@Scope("singleton")
@Component("zathuraCodeAuthenticationProvider")
public class ZathuraCodeAuthenticationProvider implements AuthenticationProvider {
	/**
	 * Security Implementation
	 */
	
	@Autowired
	IBusinessDelegatorView businessDelegatorView;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		
		
		long codigo = Long.parseLong(authentication.getName());
		String password = authentication.getCredentials().toString();

		try {
			
			User user = businessDelegatorView.loadByCodigoUsuario(codigo);
			
			if (user != null) {
				
				if (passwordEncoder.matches(password, user.getPassword())) {
					
					inicializar(businessDelegatorView.findByCodigoUsuario(codigo).getIdUsuario());	
					
					
					return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),user.getAuthorities());

				} else {
					
					throw new Exception("Error en codigo o contraseña");
				}
			} else {
				
				throw new Exception("Error en codigo o contraseña");
			}
		} catch (Exception e) {			
			
			e.printStackTrace();
			throw new BadCredentialsException("Error en codigo o contraseña");
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
    /**
    *
    * Metodo encargado de almacenar los datos de session
    *
    * @param idUsuario long id del usuario que se esta logeando 
    */ 
	public void inicializar(long idUsuario) {
		
		 
		try {
			Usuario usuario = businessDelegatorView.getUsuario(idUsuario);
			TipoUsuario tipoUsuario = businessDelegatorView.getTipoUsuario(usuario.getTipoUsuario().getIdTipoUsuario());
			
			Object[] variableProgramaUsuario = {"usuario.idUsuario",true,usuario.getIdUsuario(),"="};
			ProgramaUsuario programaUsuario = businessDelegatorView.findByCriteriaInProgramaUsuario(variableProgramaUsuario,null,null).get(0);	
			
			Programa programa = businessDelegatorView.getPrograma(programaUsuario.getPrograma().getIdPrograma());
			Facultad facultad = businessDelegatorView.getFacultad(programa.getFacultad().getIdFacultad());
			
			
			VariablesSession.usuario = usuario;
			VariablesSession.tipoUsuario = tipoUsuario;
			VariablesSession.programaUsuario = programaUsuario;
			VariablesSession.programa = programa;
			VariablesSession.facultad = facultad;
			
			
			FacesUtils.putinSession("usuario",usuario); 
			FacesUtils.putinSession("tipoUsuario",tipoUsuario); 
			FacesUtils.putinSession("programaUsuario",programaUsuario); 
			FacesUtils.putinSession("programa",programa); 
			FacesUtils.putinSession("facultad",facultad); 			
			
			
			FacesUtils.putinSession("usuarioDTO",businessDelegatorView.findDataByCodigoUsuario(usuario.getCodigo()));  
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		
		
	}
}
