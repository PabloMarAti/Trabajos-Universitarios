/**
 * 
 */
package Modelodedise�o.Negocio.Empleado.SAEmpleado.Imp;

import Modelodedise�o.Negocio.Cliente.TCliente;
import Modelodedise�o.Negocio.Departamento.Departamento;
import Modelodedise�o.Negocio.Empleado.SAEmpleado.SAEmpleado;
import Modelodedise�o.Negocio.Empleado.Empleado;
import Modelodedise�o.Negocio.Empleado.EmpleadoComercial;
import Modelodedise�o.Negocio.Empleado.EmpleadoRepartidor;
import Modelodedise�o.Negocio.Evento.EmpleadosPorEvento;
import Modelodedise�o.Negocio.Evento.Evento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author usuario_local
 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
 */
public class SAEmpleadoImp implements SAEmpleado {
	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#alta(Empleado empleado)
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	public int alta(Empleado empleado) {
		int resultado = -1;
		Empleado emp;
		EmpleadoRepartidor empRepartidor=null;
		EmpleadoComercial empComercial=null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		TypedQuery<Empleado> query = em.createNamedQuery("Empleado.findBydni", Empleado.class);
		query.setParameter("dni", empleado.getDni());
		emp=null;
		if(query.getResultList().size() != 0){
			emp =  query.getSingleResult();
		}
		if (emp != null){
			if (!empleado.getActivo()){
					emp.setDni(empleado.getDni());
					emp.setNombre(empleado.getNombre());

					emp.setActivo(true);
					emp.setTelefono(empleado.getTelefono());
					//emp.setDepartamento(empleado.getDepartamento());
					emp.setSueldo(empleado.getSueldo());
				
				if(empleado.getClass().equals(empRepartidor)){
					((EmpleadoRepartidor) emp).setKm(((EmpleadoRepartidor) empleado).getKm());
				}else if(empleado.getClass().equals(empComercial)){
					((EmpleadoComercial) emp).setVentas(((EmpleadoComercial) empleado).getVentas());
					}
					
					
					transaction.commit();
					resultado = emp.getId(); //alta correcta
			}
			else
			{
				resultado = -1; //empleado ya est� de alta
				em.getTransaction().rollback();
			}
		}
		else
		{
			em.persist(empleado);
			transaction.commit();
			resultado = empleado.getId(); //alta correcta
		}
		
		em.close();
		emf.close();
		
		return resultado;
		// end-user-code
	}

	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#baja(int id)
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	public int baja(int id) {
		// begin-user-code
		// TODO Ap�ndice de m�todo generado autom�ticamente
		int resultado = 0;
		EntityManagerFactory EMF = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = EMF.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		
		TypedQuery<Empleado> query = em.createNamedQuery("Empleado.findByid", Empleado.class);
		query.setParameter("id", id);
		if(query.getResultList().size() != 0){
			Empleado emp = query.getSingleResult();
			if(emp.getActivo()){
				emp.setActivo(false);
				transaction.commit(); //baja correcta
			}
			else{
				resultado = 1; //empleado ya est� dado de baja
				transaction.rollback();
			}
		}
		else{
			resultado = 2; //empleado no existe
			transaction.rollback();
		}
		em.close();EMF.close();
		return resultado;
		// end-user-code
		// end-user-code
	}

	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#modificar(Empleado empleado)
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	public int modificar(Empleado empleado) {
		
		int resultado = 0;
		Empleado emp;
		EmpleadoRepartidor empRepartidor = new EmpleadoRepartidor();
		EmpleadoComercial empComercial= new EmpleadoComercial();
		EntityManagerFactory EMF = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = EMF.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		
		emp= em.find(Empleado.class, empleado.getId(),LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		if(emp != null) {
			if(emp.getClass().equals(empleado.getClass())){
				if (emp.getActivo()){
					if(emp.getDni().equals(empleado.getDni())){
	
						emp.setNombre(empleado.getNombre());
	
						emp.setActivo(empleado.getActivo());
						emp.setTelefono(empleado.getTelefono());
						//emp.setDepartamento(empleado.getDepartamento());
						emp.setSueldo(empleado.getSueldo());
						emp.setId(empleado.getId());
						
						if(empleado.getClass().equals(empRepartidor.getClass())){
							((EmpleadoRepartidor) emp).setKm(((EmpleadoRepartidor) empleado).getKm());
						}else if(empleado.getClass().equals(empComercial.getClass())){
							((EmpleadoComercial) emp).setVentas(((EmpleadoComercial) empleado).getVentas());
							}
						
						transaction.commit();
						resultado = 0;
					}
					else{
							TypedQuery<Empleado> query = em.createNamedQuery("Empleado.findBydni", Empleado.class);
							query.setParameter("dni", empleado.getDni());
							if(query.getResultList().size() == 0){
								emp.setDni(empleado.getDni());
	
								emp.setNombre(empleado.getNombre());
								emp.setActivo(empleado.getActivo());
								emp.setTelefono(empleado.getTelefono());
								emp.setDepartamento(empleado.getDepartamento());
								emp.setSueldo(empleado.getSueldo());
								emp.setId(empleado.getId());
								
								if(empleado.getClass().equals(empRepartidor.getClass())){
									((EmpleadoRepartidor) emp).setKm(((EmpleadoRepartidor) empleado).getKm());
								}else if(empleado.getClass().equals(empComercial.getClass())){
									((EmpleadoComercial) emp).setVentas(((EmpleadoComercial) empleado).getVentas());
									}
								
								transaction.commit();
								resultado = 0;
							}
							else{
								transaction.rollback();
								resultado = 1; //Dni repetido
							}
						}
				}
				else{
					transaction.rollback();
					resultado = 2; //No est� activo
				}
			}
			else{
				transaction.rollback();
				resultado = 4; //Las clases no se corresponden
			}
		
		}
		else{
			transaction.rollback();
			resultado = 3; //No est� en el sistema			
		}
		em.close(); EMF.close();
		return resultado;
	}

	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#mostrar(int id)
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	public Empleado mostrar(int id) {
		Empleado emp;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		emp = em.find(Empleado.class, id);
		if(emp == null){
			transaction.rollback();
		}else{
			transaction.commit();
		}
		em.close();
		emf.close();
		
		return emp;
		// end-user-code
	}

	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#listar()
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	
	public Collection<Empleado> listar() {// Usar collection??? 
		
		EntityManagerFactory EMF = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = EMF.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		TypedQuery<Empleado> query = em.createNamedQuery("Empleado.findBytodo", Empleado.class);
		//query.setParameter("activo", true);
		Collection<Empleado> empleados = query.getResultList();
		transaction.commit();
		em.close();EMF.close();
		return empleados;
		// end-user-code
	}
	
	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#listar()
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	
	@Override
	public int asignarDepartamento(int idEmpleado, int idDepartamento) {
		int resultado = 0;
		Empleado emp;
		Departamento dep;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		emp = em.find(Empleado.class, idEmpleado);
		dep= em.find(Departamento.class, idDepartamento, LockModeType.OPTIMISTIC);
		if(emp == null || dep==null){
			transaction.rollback(); 
			resultado = 1; //ambos no existen
		}else{
			if(emp.getActivo() && dep.getActivo()){
				emp.setDepartamento(dep);
				transaction.commit();
				resultado = 0; //correcto
			}else{
				resultado = 2;
				transaction.rollback(); //alguno no est� de alta
			}
		}
		em.close();
		emf.close();
		
		return resultado;
	}

	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#listar()
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	
	@Override
	public int desasignarDepartamento(int idEmpleado) {
		int resultado = 0;
		Empleado emp;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		emp = em.find(Empleado.class, idEmpleado);
		if(emp == null){
			transaction.rollback();
		}else{
			if(emp.getActivo()){
				if(emp.getDepartamento() == null){
					resultado = 1; //no hay departamento asignado
					transaction.rollback();
				}
				else{
					emp.setDepartamento(null);
					transaction.commit();
					resultado = 0; //correcto
				}
			}else{
				resultado = 2; //empleado no est� activo
				transaction.rollback();
			}
		}
		em.close();
		emf.close();
		
		return resultado;
		// end-user-code
	}

	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#listar()
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	
	@Override
	public Collection<EmpleadosPorEvento> eventosPorEmpleado(int idEmpleado) 
	{
		// TODO Auto-generated method stub
		Collection<EmpleadosPorEvento> eventos = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		Empleado emp = em.find(Empleado.class, idEmpleado);
		if (emp == null)
			transaction.rollback();
		else
			if (emp.getActivo())
			{
				eventos = emp.getEmpleados();
				transaction.commit();
			}
			else
				transaction.rollback();

		em.close();
		emf.close();
		
		return eventos;
	}
	
	/** 
	 * (sin Javadoc)
	 * @see SAEmpleado#listar()
	 * @generated "UML a JPA (com.ibm.xtools.transform.uml2.ejb3.java.jpa.internal.UML2JPATransform)"
	 */
	@Override
	public Empleado MejorEmpleado() {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MCateringCod");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		TypedQuery<EmpleadosPorEvento> query = em.createNamedQuery("EmpleadosPorEvento.findAll", EmpleadosPorEvento.class);
		Collection<EmpleadosPorEvento> lista = query.getResultList();
		 HashMap<Integer, Integer>  map = new HashMap<Integer, Integer>();
		 
		 for(EmpleadosPorEvento emp: lista){
			 em.lock(emp, LockModeType.OPTIMISTIC);
			 int idEmp = emp.getEmpleado().getId();
			 if (map.containsKey(idEmp)){
				 map.put(idEmp, map.get(idEmp) + emp.getHoras());
			 }
			 else{
				 map.put(idEmp, emp.getHoras());
			 }
		 }
		 int minimo = 0;
		 int idEmp = -1;
		 for(Integer key: map.keySet()){
			 int valor = map.get(key);
			 if(valor > minimo){
				 minimo = valor;
				 idEmp = key;
			 }
		 }
		Empleado emp = em.find(Empleado.class, idEmp);
		em.close();emf.close();
		return emp;
	}
}