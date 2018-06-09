package com.saberpro.presentation.backingBeans;

import com.saberpro.exceptions.*;

import com.saberpro.modelo.*;
import com.saberpro.modelo.dto.PreguntaDTO;
import com.saberpro.modelo.dto.TipoModuloDTO;
import com.saberpro.presentation.businessDelegate.*;

import com.saberpro.utilities.*;

import org.primefaces.component.calendar.*;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputnumber.InputNumber;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.extensions.component.ckeditor.CKEditor;
import org.primefaces.model.UploadedFile;
import org.primefaces.event.RowEditEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import java.sql.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;


/**
 * @author Zathura Code Generator http://zathuracode.org/
 * www.zathuracode.org
 *
 */
@ManagedBean
@ViewScoped
public class PreguntaView implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(PreguntaView.class);
    
    private boolean cargo = true;
    
    private String content;
   
    
    private InputNumber porcentajeAciertoRespuestaA;
    private InputNumber porcentajeAciertoRespuestaB;
    private InputNumber porcentajeAciertoRespuestaC;
    private InputNumber porcentajeAciertoRespuestaD;
    
    private String contenidoRespuestaA;
    private String contenidoRespuestaB;
    private String contenidoRespuestaC;
    private String contenidoRespuestaD;
    
    private SelectOneMenu somTipoModulo;
	private SelectOneMenu somModulo;
    
    private InputTextarea txtRetroalimentacion;
    
    private UploadedFile seleccionarImportFile;
    private UploadedFile seleccionarPregunta;
    
    private UploadedFile seleccionarRespuestaA;
    private UploadedFile seleccionarRespuestaB;
    private UploadedFile seleccionarRespuestaC;
    private UploadedFile seleccionarRespuestaD;
    
    private List<SelectItem> lasTipoModuloSelectItem;
    private List<SelectItem> lasModuloSelectItem;
    private List<SelectItem> lasModuloSelectItemFilter;
    
    private CommandButton importFile;
    private CommandButton subirPregunta;
    
    private CommandButton subirRespuestaA;
    private CommandButton subirRespuestaB;
    private CommandButton subirRespuestaC;
    private CommandButton subirRespuestaD;
    
    private CommandButton crear;
    private CommandButton actualizar;
    private CommandButton cargar;
    
    
    
    private List<Pregunta> data;
    
    private PreguntaDTO selectedPregunta;
    
    private Pregunta entity;
    
    private List<Respuesta> entityRespuestas;
    
    private boolean showDialog;
    
    @ManagedProperty(value = "#{BusinessDelegatorView}")
    private IBusinessDelegatorView businessDelegatorView;

    public PreguntaView() {
        super();      
     }
    
    public void listener_txtId() {
    	try {
    		if(cargo) {
    			FacesContext facesContext = FacesContext.getCurrentInstance();
        		Map params = facesContext.getExternalContext().getRequestParameterMap();
        		Long id= new Long((String) params.get("id"));
        		
        		if(id!=null) {
        			Object[] variable = {"pregunta",true,id,"=","activo",true,Constantes.ESTADO_ACTIVO,"="};
            		
            		entity = businessDelegatorView.getPregunta(id);	
            		
            		Modulo modulo = businessDelegatorView.getModulo(entity.getModulo().getIdModulo());
            		TipoModulo tipoModulo = businessDelegatorView.getTipoModulo(modulo.getTipoModulo().getIdTipoModulo());   		
        			
        			entityRespuestas = businessDelegatorView.findByCriteriaInRespuesta(variable,null,null);
        			
        			somTipoModulo.setValue(tipoModulo.getIdTipoModulo());
        			somModulo.setValue(modulo.getIdModulo());
        			
        			txtRetroalimentacion.setValue(entity.getRetroalimentacion());
        			content = entity.getDescripcionPregunta();
        			
        			contenidoRespuestaA = entityRespuestas.get(0).getDescripcionRespuesta();
        			contenidoRespuestaB = entityRespuestas.get(1).getDescripcionRespuesta();
        			contenidoRespuestaC = entityRespuestas.get(2).getDescripcionRespuesta();
        			contenidoRespuestaD = entityRespuestas.get(3).getDescripcionRespuesta();
        			
        			porcentajeAciertoRespuestaA.setValue(entityRespuestas.get(0).getPorcentajeAcierto());
        			porcentajeAciertoRespuestaB.setValue(entityRespuestas.get(1).getPorcentajeAcierto());
        			porcentajeAciertoRespuestaC.setValue(entityRespuestas.get(2).getPorcentajeAcierto());
        			porcentajeAciertoRespuestaD.setValue(entityRespuestas.get(3).getPorcentajeAcierto());
        			
        			crear.setDisabled(true);
        			actualizar.setDisabled(false);
        		}
    		
    			cargo = false;
    			
    		}
    		
					
		} catch (Exception e) {
			log.debug("Error "+e.getMessage());
		}
    }
    
    public String action_create() {
		try {
			Usuario usuario = (Usuario) FacesUtils.getfromSession("usuario");

			if (usuario != null && FacesUtils.checkInteger(porcentajeAciertoRespuestaA)!=null && FacesUtils.checkInteger(porcentajeAciertoRespuestaB)!=null
					&& FacesUtils.checkInteger(porcentajeAciertoRespuestaC)!=null && FacesUtils.checkInteger(porcentajeAciertoRespuestaD)!=null && !contenidoRespuestaB.isEmpty()
					&& !contenidoRespuestaB.isEmpty() && !contenidoRespuestaC.isEmpty() && !contenidoRespuestaD.isEmpty()) {				
				
				entity = new Pregunta();

				entity.setActivo(Constantes.ESTADO_ACTIVO);
				entity.setDescripcionPregunta(content);
				entity.setFechaCreacion(new Date());
				entity.setModulo(businessDelegatorView.getModulo(FacesUtils.checkLong(somModulo)));
				entity.setTipoPregunta(businessDelegatorView.getTipoPregunta(Constantes.PREGUNTA_TYPE_MULTIPLE));
				entity.setRetroalimentacion(FacesUtils.checkString(txtRetroalimentacion));
				entity.setUsuCreador(usuario.getIdUsuario());
				
				
				
				List<Respuesta> listRespuesta = new ArrayList<>();

				Respuesta respuesta = new Respuesta();
				
				respuesta.setActivo(Constantes.ESTADO_ACTIVO);
				respuesta.setDescripcionRespuesta(contenidoRespuestaA);
				respuesta.setFechaCreacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaA));				
				respuesta.setUsuCreador(usuario.getIdUsuario());
				
				listRespuesta.add(respuesta);
				
				respuesta = new Respuesta();
				
				respuesta.setActivo(Constantes.ESTADO_ACTIVO);
				respuesta.setDescripcionRespuesta(contenidoRespuestaB);
				respuesta.setFechaCreacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaB));				
				respuesta.setUsuCreador(usuario.getIdUsuario());
				
				listRespuesta.add(respuesta);
				
				respuesta = new Respuesta();
				
				respuesta.setActivo(Constantes.ESTADO_ACTIVO);
				respuesta.setDescripcionRespuesta(contenidoRespuestaC);
				respuesta.setFechaCreacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaB));			
				respuesta.setUsuCreador(usuario.getIdUsuario());
				
				listRespuesta.add(respuesta);
				
				respuesta = new Respuesta();
				
				respuesta.setActivo(Constantes.ESTADO_ACTIVO);
				respuesta.setDescripcionRespuesta(contenidoRespuestaD);
				respuesta.setFechaCreacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaD));				
				respuesta.setUsuCreador(usuario.getIdUsuario());
				
				listRespuesta.add(respuesta);
				
				businessDelegatorView.savePregunta(entity, listRespuesta);

				FacesUtils.addInfoMessage("Pregunta creada correctamente");
				
				action_clear();
				

			}
			else {
				FacesUtils.addErrorMessage("No se pudo crear la pregunta, por favor verifique los datos");
			}

		} catch (Exception e) {
			entity = null;
			FacesUtils.addErrorMessage(e.getMessage());
		}

		return "";

	}
    
    public String action_clear() {
		entity = null;
		selectedPregunta = null;

		txtRetroalimentacion.resetValue();
		
		somModulo.resetValue();
		somTipoModulo.resetValue();
		
		content = "";
		contenidoRespuestaA = "";
		contenidoRespuestaB = "";
		contenidoRespuestaC = "";
		contenidoRespuestaD = "";
		
		porcentajeAciertoRespuestaA.resetValue();
		porcentajeAciertoRespuestaB.resetValue();
		porcentajeAciertoRespuestaC.resetValue();
		porcentajeAciertoRespuestaD.resetValue();
		

		return "";
	}
    
    public String action_modify() {
		try {
			Usuario usuario = (Usuario) FacesUtils.getfromSession("usuario");			

			if (usuario != null && FacesUtils.checkInteger(porcentajeAciertoRespuestaA)!=null && FacesUtils.checkInteger(porcentajeAciertoRespuestaB)!=null
					&& FacesUtils.checkInteger(porcentajeAciertoRespuestaC)!=null && FacesUtils.checkInteger(porcentajeAciertoRespuestaD)!=null && !contenidoRespuestaB.isEmpty()
					&& !contenidoRespuestaB.isEmpty() && !contenidoRespuestaC.isEmpty() && !contenidoRespuestaD.isEmpty()) {			
				
				entity.setActivo(Constantes.ESTADO_ACTIVO);
				entity.setDescripcionPregunta(content);
				entity.setFechaModificacion(new Date());
				entity.setModulo(businessDelegatorView.getModulo(FacesUtils.checkLong(somModulo)));
				entity.setTipoPregunta(businessDelegatorView.getTipoPregunta(Constantes.PREGUNTA_TYPE_MULTIPLE));
				entity.setRetroalimentacion(FacesUtils.checkString(txtRetroalimentacion));
				entity.setUsuModificador(usuario.getIdUsuario());
				
				businessDelegatorView.updatePregunta(entity);			

				Respuesta respuesta = entityRespuestas.get(0);				
				
				respuesta.setDescripcionRespuesta(contenidoRespuestaA);
				respuesta.setFechaModificacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaA));				
				respuesta.setUsuModificador(usuario.getIdUsuario());
				
				businessDelegatorView.updateRespuesta(respuesta);
				
				respuesta = entityRespuestas.get(1);				
				
				respuesta.setDescripcionRespuesta(contenidoRespuestaB);
				respuesta.setFechaModificacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaB));				
				respuesta.setUsuModificador(usuario.getIdUsuario());
				
				businessDelegatorView.updateRespuesta(respuesta);
				
				respuesta = entityRespuestas.get(2);
				
				
				respuesta.setDescripcionRespuesta(contenidoRespuestaC);
				respuesta.setFechaModificacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaC));				
				respuesta.setUsuModificador(usuario.getIdUsuario());
				
				businessDelegatorView.updateRespuesta(respuesta);
				
				respuesta = entityRespuestas.get(3);
				
				
				respuesta.setDescripcionRespuesta(contenidoRespuestaD);
				respuesta.setFechaModificacion(new Date());
				respuesta.setPorcentajeAcierto(FacesUtils.checkInteger(porcentajeAciertoRespuestaD));				
				respuesta.setUsuModificador(usuario.getIdUsuario());
				
				businessDelegatorView.updateRespuesta(respuesta);

				FacesUtils.addInfoMessage("Pregunta actualizada correctamente");
				
				FacesContext.getCurrentInstance().getExternalContext().redirect("/saber-pro-app/XHTML/Pregunta/verPregunta.xhtml");
				
			}
			else {
				FacesUtils.addErrorMessage("No se pudo actualizar la pregunta, por favor verifique los datos");
			}

		} catch (Exception e) {
			entity = null;
			FacesUtils.addErrorMessage(e.getMessage());
		}

		return "";
	}
    public String getModulo(long id) {
    	try {
			return businessDelegatorView.getModulo(id).getNombre();
		} catch (Exception e) {
			return e.getMessage();
		}
    }
    
    public void changeTipoModulo() {
    	lasModuloSelectItem = null;
    }
    
    public void importFilePregunta() {
    	try {
			if(seleccionarImportFile!=null) {
				
				Usuario usuario = (Usuario)FacesUtils.getfromSession("usuario");
				
				String ext = seleccionarImportFile.getFileName().substring(seleccionarImportFile.getFileName().lastIndexOf("."));
				businessDelegatorView.importFilePregunta(seleccionarImportFile.getInputstream(),usuario.getIdUsuario(),ext);
				
				data = null;
				
				FacesUtils.addInfoMessage("Preguntas importadas correctamente");
			}
			
			
		} catch (Exception e) {
			FacesUtils.addErrorMessage(e.getMessage());
			log.error("Error importando el archivo "+e.getMessage(),e);
		}
    }
    
    public void subirImagenPregunta() {
    	try {
    		if(seleccionarPregunta!=null) {
    			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    			
    			String ruta= (String) servletContext.getRealPath("/")+"pregunta/"+seleccionarPregunta.getFileName();
    			String httpRuta = businessDelegatorView.getParametro(Constantes.PARAMETRO_WEB_PREGUNTA).getValor()+seleccionarPregunta.getFileName();
    			
    			businessDelegatorView.subirFilePregunta(seleccionarPregunta.getInputstream(),ruta);
    			
    			   			
    			
    			content = content +"<p><img width=\"300px\"   alt=\"\" src=\""+httpRuta+"\"/></p>"; 
    			
    			FacesUtils.addInfoMessage("Se subió el archivo correctamente");
    		}
			
		} catch (Exception e) {
			FacesUtils.addErrorMessage(e.getMessage());
		}
    }
    
    public void subirImagenRespuestaA() {
    	try {
    		if(seleccionarRespuestaA!=null) {
    			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    			
    			String ruta= (String) servletContext.getRealPath("/")+"respuesta/"+seleccionarRespuestaA.getFileName();
    			String httpRuta = businessDelegatorView.getParametro(Constantes.PARAMETRO_WEB_RESPUESTA).getValor()+seleccionarRespuestaA.getFileName();
    			
    			businessDelegatorView.subirFilePregunta(seleccionarRespuestaA.getInputstream(),ruta);
    			
    						
    			
    			contenidoRespuestaA = contenidoRespuestaA +"<p><img width=\"300px\" alt=\"\" src=\""+httpRuta+"\"/></p>"; 
    			
    			FacesUtils.addInfoMessage("Se subió el archivo correctamente");
    		}
			
		} catch (Exception e) {
			FacesUtils.addErrorMessage(e.getMessage());
		}
    }
    
    public void subirImagenRespuestaB() {
    	try {
    		if(seleccionarRespuestaB!=null) {
    			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    			
    			String ruta= (String) servletContext.getRealPath("/")+"respuesta/"+seleccionarRespuestaB.getFileName();
    			String httpRuta = businessDelegatorView.getParametro(Constantes.PARAMETRO_WEB_RESPUESTA).getValor()+seleccionarRespuestaB.getFileName();
    			
    			businessDelegatorView.subirFilePregunta(seleccionarRespuestaB.getInputstream(),ruta);
    			
    			  			
    			
    			contenidoRespuestaB = contenidoRespuestaB +"<p><img width=\"300px\" alt=\"\" src=\""+httpRuta+"\"/></p>"; 
    			
    			FacesUtils.addInfoMessage("Se subió el archivo correctamente");
    		}
			
		} catch (Exception e) {
			FacesUtils.addErrorMessage(e.getMessage());
		}
    }
    
    public void subirImagenRespuestaC() {
    	try {
    		if(seleccionarRespuestaC!=null) {
    			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    			
    			String ruta= (String) servletContext.getRealPath("/")+"respuesta/"+seleccionarRespuestaC.getFileName();
    			String httpRuta = businessDelegatorView.getParametro(Constantes.PARAMETRO_WEB_RESPUESTA).getValor()+seleccionarRespuestaC.getFileName();
    			
    			businessDelegatorView.subirFilePregunta(seleccionarRespuestaC.getInputstream(),ruta);
    			
    		
    			contenidoRespuestaC = contenidoRespuestaC +"<p><img  width=\"300px\" alt=\"\" src=\""+httpRuta+"\"/></p>"; 
    			
    			FacesUtils.addInfoMessage("Se subió el archivo correctamente");
    		}
			
		} catch (Exception e) {
			FacesUtils.addErrorMessage(e.getMessage());
		}
    }
    
    public void subirImagenRespuestaD() {
    	try {
    		if(seleccionarRespuestaD!=null) {
    			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    			
    			String ruta= (String) servletContext.getRealPath("/")+"respuesta/"+seleccionarRespuestaD.getFileName();
    			String httpRuta = businessDelegatorView.getParametro(Constantes.PARAMETRO_WEB_RESPUESTA).getValor()+seleccionarRespuestaD.getFileName();
    			
    			businessDelegatorView.subirFilePregunta(seleccionarRespuestaD.getInputstream(),ruta);
    			
    						
    			
    			contenidoRespuestaD = contenidoRespuestaD +"<p><img width=\"300px\" alt=\"\" src=\""+httpRuta+"\"/></p>"; 
    			
    			FacesUtils.addInfoMessage("Se subió el archivo correctamente");
    		}
			
		} catch (Exception e) {
			FacesUtils.addErrorMessage(e.getMessage());
		}
    }

    public List<Pregunta> getData() {
        try {
            if (data == null) {
            	if(VariablesSession.tipoUsuario.getIdTipoUsuario()==Constantes.USER_TYPE_DIRECTOR || VariablesSession.tipoUsuario.getIdTipoUsuario()==Constantes.USER_TYPE_ADMIN )
                data = businessDelegatorView.getPregunta();
            }
            else if(VariablesSession.tipoUsuario.getIdTipoUsuario()==Constantes.USER_TYPE_DOCENTE) {
            	Object[] variable = {"usuCreador",true,VariablesSession.usuario.getIdUsuario(),"="};
            	data = businessDelegatorView.findByCriteriaInPregunta(variable, null, null);
            }
        } catch (Exception e) {
        	log.error("Error de "+e.getMessage(),e);
            e.printStackTrace();
        }

        return data;
    }
    
public List<SelectItem> getLasModuloSelectItem() {
		
		if(lasModuloSelectItem==null && FacesUtils.checkInteger(somTipoModulo)!=null) {				
				Object[] variable = {"tipoModulo",true,FacesUtils.checkInteger(somTipoModulo),"=","activo",true,Constantes.ESTADO_ACTIVO,"="};
				lasModuloSelectItem = new ArrayList<>();				
				try {
					log.info("El tipo de usuario es "+VariablesSession.tipoUsuario.getIdTipoUsuario());
					List<Modulo> list = null;
					if(VariablesSession.tipoUsuario.getIdTipoUsuario()==Constantes.USER_TYPE_DOCENTE) {
						list = businessDelegatorView.findByProgramaModulo(VariablesSession.programa.getIdPrograma());
					}
					else if(VariablesSession.tipoUsuario.getIdTipoUsuario()==Constantes.USER_TYPE_DIRECTOR || VariablesSession.tipoUsuario.getIdTipoUsuario()==Constantes.USER_TYPE_ADMIN ) {
						list = businessDelegatorView.findByCriteriaInModulo(variable,null,null);
					}
					
					
					for (Modulo modulo : list) {
						lasModuloSelectItem.add(new SelectItem(modulo.getIdModulo(),modulo.getNombre()));
					}
				} catch (Exception e) {
					
					log.debug("Error" + e.getMessage());
				}
			}			
		
		return lasModuloSelectItem;
	}

    public void setData(List<Pregunta> pregunta) {
        this.data = pregunta;
    }

    public PreguntaDTO getSelectedPregunta() {
        return selectedPregunta;
    }

    public void setSelectedPregunta(PreguntaDTO pregunta) {
        this.selectedPregunta = pregunta;
    }

   public TimeZone getTimeZone() {
        return java.util.TimeZone.getDefault();
    }

    public IBusinessDelegatorView getBusinessDelegatorView() {
        return businessDelegatorView;
    }

    public void setBusinessDelegatorView(
        IBusinessDelegatorView businessDelegatorView) {
        this.businessDelegatorView = businessDelegatorView;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContenidoRespuestaA() {
		return contenidoRespuestaA;
	}

	public void setContenidoRespuestaA(String contenidoRespuestaA) {
		this.contenidoRespuestaA = contenidoRespuestaA;
	}

	public String getContenidoRespuestaB() {
		return contenidoRespuestaB;
	}

	public void setContenidoRespuestaB(String contenidoRespuestaB) {
		this.contenidoRespuestaB = contenidoRespuestaB;
	}

	public String getContenidoRespuestaC() {
		return contenidoRespuestaC;
	}

	public void setContenidoRespuestaC(String contenidoRespuestaC) {
		this.contenidoRespuestaC = contenidoRespuestaC;
	}

	public String getContenidoRespuestaD() {
		return contenidoRespuestaD;
	}

	public void setContenidoRespuestaD(String contenidoRespuestaD) {
		this.contenidoRespuestaD = contenidoRespuestaD;
	}

	public UploadedFile getChoosePregunta() {
		return seleccionarPregunta;
	}

	public void setSeleccionarPregunta(UploadedFile seleccionarPregunta) {
		this.seleccionarPregunta = seleccionarPregunta;
	}

	public CommandButton getSubirPregunta() {
		return subirPregunta;
	}

	public void setSubirPregunta(CommandButton subirPregunta) {
		this.subirPregunta = subirPregunta;
	}

	

	public InputTextarea getTxtRetroalimentacion() {
		return txtRetroalimentacion;
	}

	public void setTxtRetroalimentacion(InputTextarea txtRetroalimentacion) {
		this.txtRetroalimentacion = txtRetroalimentacion;
	}

	public CommandButton getImportFile() {
		return importFile;
	}

	public void setImportFile(CommandButton importFile) {
		this.importFile = importFile;
	}

	public SelectOneMenu getSomTipoModulo() {
		return somTipoModulo;
	}

	public void setSomTipoModulo(SelectOneMenu somTipoModulo) {
		this.somTipoModulo = somTipoModulo;
	}

	public SelectOneMenu getSomModulo() {
		return somModulo;
	}

	public void setSomModulo(SelectOneMenu somModulo) {
		this.somModulo = somModulo;
	}

	public List<SelectItem> getLasTipoModuloSelectItem() {
		if(lasTipoModuloSelectItem==null) {
			Object[] variable = {"activo",true,Constantes.ESTADO_ACTIVO,"="};
			lasTipoModuloSelectItem = new ArrayList<>();
			try {
				List<TipoModulo> list = businessDelegatorView.findByCriteriaInTipoModulo(variable,null,null);
				for (TipoModulo tipoModulo : list) {
					lasTipoModuloSelectItem.add(new SelectItem(tipoModulo.getIdTipoModulo(), tipoModulo.getNombre()));
				}
			} catch (Exception e) {
				log.debug("Error" + e.getMessage());
			}
		}
		return lasTipoModuloSelectItem;
	}
	
	public void setLasModuloSelectItem(List<SelectItem> lasModuloSelectItem) {
		this.lasModuloSelectItem = lasModuloSelectItem;
	}

	public void setLasTipoModuloSelectItem(List<SelectItem> lasTipoModuloSelectItem) {
		this.lasTipoModuloSelectItem = lasTipoModuloSelectItem;
	}

	
	public InputNumber getPorcentajeAciertoRespuestaA() {
		return porcentajeAciertoRespuestaA;
	}

	public void setPorcentajeAciertoRespuestaA(InputNumber porcentajeAciertoRespuestaA) {
		this.porcentajeAciertoRespuestaA = porcentajeAciertoRespuestaA;
	}

	public InputNumber getPorcentajeAciertoRespuestaB() {
		return porcentajeAciertoRespuestaB;
	}

	public void setPorcentajeAciertoRespuestaB(InputNumber porcentajeAciertoRespuestaB) {
		this.porcentajeAciertoRespuestaB = porcentajeAciertoRespuestaB;
	}

	public InputNumber getPorcentajeAciertoRespuestaC() {
		return porcentajeAciertoRespuestaC;
	}

	public void setPorcentajeAciertoRespuestaC(InputNumber porcentajeAciertoRespuestaC) {
		this.porcentajeAciertoRespuestaC = porcentajeAciertoRespuestaC;
	}

	public InputNumber getPorcentajeAciertoRespuestaD() {
		return porcentajeAciertoRespuestaD;
	}

	public void setPorcentajeAciertoRespuestaD(InputNumber porcentajeAciertoRespuestaD) {
		this.porcentajeAciertoRespuestaD = porcentajeAciertoRespuestaD;
	}

	public UploadedFile getSeleccionarImportFile() {
		return seleccionarImportFile;
	}

	public void setSeleccionarImportFile(UploadedFile seleccionarImportFile) {
		this.seleccionarImportFile = seleccionarImportFile;
	}

	public UploadedFile getSeleccionarRespuestaA() {
		return seleccionarRespuestaA;
	}

	public void setSeleccionarRespuestaA(UploadedFile seleccionarRespuestaA) {
		this.seleccionarRespuestaA = seleccionarRespuestaA;
	}

	public UploadedFile getSeleccionarRespuestaB() {
		return seleccionarRespuestaB;
	}

	public void setSeleccionarRespuestaB(UploadedFile seleccionarRespuestaB) {
		this.seleccionarRespuestaB = seleccionarRespuestaB;
	}

	public UploadedFile getSeleccionarRespuestaC() {
		return seleccionarRespuestaC;
	}

	public void setSeleccionarRespuestaC(UploadedFile seleccionarRespuestaC) {
		this.seleccionarRespuestaC = seleccionarRespuestaC;
	}

	public UploadedFile getSeleccionarRespuestaD() {
		return seleccionarRespuestaD;
	}

	public void setSeleccionarRespuestaD(UploadedFile seleccionarRespuestaD) {
		this.seleccionarRespuestaD = seleccionarRespuestaD;
	}

	public CommandButton getSubirRespuestaA() {
		return subirRespuestaA;
	}

	public void setSubirRespuestaA(CommandButton subirRespuestaA) {
		this.subirRespuestaA = subirRespuestaA;
	}

	public CommandButton getSubirRespuestaB() {
		return subirRespuestaB;
	}

	public void setSubirRespuestaB(CommandButton subirRespuestaB) {
		this.subirRespuestaB = subirRespuestaB;
	}

	public CommandButton getSubirRespuestaC() {
		return subirRespuestaC;
	}

	public void setSubirRespuestaC(CommandButton subirRespuestaC) {
		this.subirRespuestaC = subirRespuestaC;
	}

	public CommandButton getSubirRespuestaD() {
		return subirRespuestaD;
	}

	public void setSubirRespuestaD(CommandButton subirRespuestaD) {
		this.subirRespuestaD = subirRespuestaD;
	}

	public UploadedFile getSeleccionarPregunta() {
		return seleccionarPregunta;
	}

	public CommandButton getCrear() {
		return crear;
	}

	public void setCrear(CommandButton crear) {
		this.crear = crear;
	}

	public CommandButton getActualizar() {
		return actualizar;
	}

	public void setActualizar(CommandButton actualizar) {
		this.actualizar = actualizar;
	}

	public CommandButton getCargar() {
		listener_txtId();
		return cargar;
	}

	public void setCargar(CommandButton cargar) {
		this.cargar = cargar;
	}
	
	public boolean getCargo() {
		return cargo;
	}
	
	public void setCargo(boolean cargo) {
		this.cargo = cargo;
	}

	public List<SelectItem> getLasModuloSelectItemFilter() {
		if(lasModuloSelectItemFilter==null) {				
			Object[] variable = {"activo",true,Constantes.ESTADO_ACTIVO,"="};
			lasModuloSelectItemFilter = new ArrayList<>();				
			try {
				List<Modulo> list = businessDelegatorView.findByCriteriaInModulo(variable,null,null);
				for (Modulo modulo : list) {
					lasModuloSelectItemFilter.add(new SelectItem(modulo.getIdModulo(),modulo.getNombre()));
				}
			} catch (Exception e) {
				
				log.debug("Error" + e.getMessage());
			}
		}		
		return lasModuloSelectItemFilter;
	}

	public void setLasModuloSelectItemFilter(List<SelectItem> lasModuloSelectItemFilter) {
		this.lasModuloSelectItemFilter = lasModuloSelectItemFilter;
	}

	
}
