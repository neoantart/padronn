package pe.gob.reniec.padronn.logic.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.PrecotejoDao;
import pe.gob.reniec.padronn.logic.model.Precotejo;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistroDetalle;
import pe.gob.reniec.padronn.logic.model.Usuario;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.PrecotejoFiltroTipoRegistro;
import pe.gob.reniec.padronn.logic.service.PrecotejoService;
import pe.gob.reniec.padronn.logic.web.validator.checks.MinimalPrecotejoRegistroPriorInsertChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.PrecotejoRegistroPriorInsertChecks;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.ConstraintViolation;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Clase PrecotejoServiceImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:33 PM
 */
@Service
public class PrecotejoServiceImpl
		extends AbstractBaseService
		implements PrecotejoService {

	@Autowired
	PrecotejoDao precotejoDao;

	// todo sería mejor ? http://www.mkyong.com/spring/spring-and-java-thread-example/
	ExecutorService executorService;
	Future future;

	@Autowired
	Usuario usuario;

	@PostConstruct
	public void start() {
		log.debug("Iniciando executorService...");
		executorService = Executors.newSingleThreadExecutor();

	}

	@PreDestroy
	public void finalize() {
		if (executorService != null && !executorService.isShutdown()) {
//			log.debug("Deteniendo executorService...");
			executorService.shutdown();
		}
	}



	@Override
	public List getAll() {
		try {
			return precotejoDao.getAll(null, null, null, null, null);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAll(String coEntidad, String esEnvio) {
		try {
			return precotejoDao.getAll(coEntidad, esEnvio, null, null, null);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}


	@Override
	public List getAll(String nuEnvio, String feInicio, String feFin) {
		try {
			return precotejoDao.getAll(null, null, nuEnvio, feInicio, feFin);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAll(String coEntidad, String nuEnvio, String feInicio, String feFin) {
		try {

			if (feInicio != null && !feInicio.isEmpty()) {
				try {
					Date feInicioDate = validatorUtils.getSimpleDateFormat().parse(feInicio);
					feInicio = validatorUtils.getSimpleDateFormat().format(feInicioDate);
				} catch (ParseException e) {
					log.error(e.getMessage());
					//e.printStackTrace();
					//feInicio = "";
					return null;
				}
			}

			if (feFin != null && !feFin.isEmpty()) {
				try {
					Date feFinDate = validatorUtils.getSimpleDateFormat().parse(feFin);
					Calendar cal = new GregorianCalendar();
					cal.setTime(feFinDate);
					cal.add(Calendar.DAY_OF_YEAR, 1);
					feFinDate = cal.getTime();
					feFin = validatorUtils.getSimpleDateFormat().format(feFinDate);
				} catch (ParseException e) {
					log.error(e.getMessage());
					//e.printStackTrace();
					//feFin = "";
					return null;
				}
			}

			return precotejoDao.getAll(coEntidad, null, nuEnvio, feInicio, feFin);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin, int nuRegistroInicial, int nuRegistroFinal) {
		try {

			if (feInicio != null && !feInicio.isEmpty()) {
				try {
					Date feInicioDate = validatorUtils.getSimpleDateFormat().parse(feInicio);
					feInicio = validatorUtils.getSimpleDateFormat().format(feInicioDate);
				} catch (ParseException e) {
					log.error(e.getMessage());
					//e.printStackTrace();
					//feInicio = "";
					return null;
				}
			}

			if (feFin != null && !feFin.isEmpty()) {
				try {
					Date feFinDate = validatorUtils.getSimpleDateFormat().parse(feFin);
					Calendar cal = new GregorianCalendar();
					cal.setTime(feFinDate);
					cal.add(Calendar.DAY_OF_YEAR, 1);
					feFinDate = cal.getTime();
					feFin = validatorUtils.getSimpleDateFormat().format(feFinDate);
				} catch (ParseException e) {
					log.error(e.getMessage());
					//e.printStackTrace();
					//feFin = "";
					return null;
				}
			}

			return precotejoDao.getAll(coEntidad, esEnvio, nuEnvio, feInicio, feFin, nuRegistroInicial, nuRegistroFinal);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public int countAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin) {
		try {

			if (feInicio != null && !feInicio.isEmpty()) {
				try {
					Date feInicioDate = validatorUtils.getSimpleDateFormat().parse(feInicio);
					feInicio = validatorUtils.getSimpleDateFormat().format(feInicioDate);
				} catch (ParseException e) {
					log.error(e.getMessage());
					//e.printStackTrace();
					//feInicio = "";
					return 0;
				}
			}

			if (feFin != null && !feFin.isEmpty()) {
				try {
					Date feFinDate = validatorUtils.getSimpleDateFormat().parse(feFin);
					Calendar cal = new GregorianCalendar();
					cal.setTime(feFinDate);
					cal.add(Calendar.DAY_OF_YEAR, 1);
					feFinDate = cal.getTime();
					feFin = validatorUtils.getSimpleDateFormat().format(feFinDate);
				} catch (ParseException e) {
					log.error(e.getMessage());
					//e.printStackTrace();
					//feFin = "";
					return 0;
				}
			}

			return precotejoDao.countAll(coEntidad, esEnvio, nuEnvio, feInicio, feFin);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return 0;
		}
	}

	@Override
	public List getAllFromPrecotejo(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getAllFromPrecotejo(coEntidad, nuEnvio);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAllFromPrecotejoObs(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getAllFromPrecotejoObs(coEntidad, nuEnvio);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAllObs(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getAllObs(coEntidad, nuEnvio);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAllFromCotejo(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getAllFromCotejo(coEntidad, nuEnvio);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAllFromCotejoObs(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getAllFromCotejoObs(coEntidad, nuEnvio);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List getAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, int numeroRegistroInicial, int numeroRegistroFinal, PrecotejoFiltroTipoRegistro filtroTipoRegistro) {
		try {
			List list;
			switch (filtroTipoRegistro) {
				case TODO:
					list = precotejoDao.getAllFromPrecotejoCotejo(
							coEntidad, nuEnvio, numeroRegistroInicial, numeroRegistroFinal,
							padronProperties.ESREGISTRO_REG_OK + "," + padronProperties.ESREGISTRO_COT_ACTUALIZADO + "," + padronProperties.ESREGISTRO_COT_INSERTADO + "," + padronProperties.ESREGISTRO_COT_ERROR,
							padronProperties.ESREGISTRO_REG_ERROR);
					break;

				case SOLO_OK:
					list = precotejoDao.getAllFromPrecotejoCotejo(
							coEntidad, nuEnvio, numeroRegistroInicial, numeroRegistroFinal,
							padronProperties.ESREGISTRO_REG_OK + "," + padronProperties.ESREGISTRO_COT_ACTUALIZADO + "," + padronProperties.ESREGISTRO_COT_INSERTADO,
							"0"); // nada
					break;

				case SOLO_OBSERVACIONES:
					list = precotejoDao.getAllFromPrecotejoCotejo(
							coEntidad, nuEnvio, numeroRegistroInicial, numeroRegistroFinal,
							"0", // nada
							padronProperties.ESREGISTRO_REG_ERROR);
					break;

				case SOLO_ERRORES:
					list = precotejoDao.getAllFromPrecotejoCotejo(
							coEntidad, nuEnvio, numeroRegistroInicial, numeroRegistroFinal,
							padronProperties.ESREGISTRO_COT_ERROR,
							"0"); // nada
					break;

				default:
					throw new IllegalStateException("PrecotejoFiltroTipoRegistro no válido");
			}
			return list;

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public int countAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, PrecotejoFiltroTipoRegistro filtroTipoRegistro) {
		try {
			switch (filtroTipoRegistro) {
				case TODO:
					return precotejoDao.countAllFromRegistroObs(coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_ERROR)
							+ precotejoDao.countAllFromRegistro(coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_OK + "," + padronProperties.ESREGISTRO_COT_ACTUALIZADO + "," + padronProperties.ESREGISTRO_COT_INSERTADO + "," + padronProperties.ESREGISTRO_COT_ERROR);

				case SOLO_OK:
					return precotejoDao.countAllFromRegistro(coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_OK + "," + padronProperties.ESREGISTRO_COT_ACTUALIZADO + "," + padronProperties.ESREGISTRO_COT_INSERTADO);

				case SOLO_OBSERVACIONES:
					return precotejoDao.countAllFromRegistroObs(coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_ERROR);

				case SOLO_ERRORES:
					return precotejoDao.countAllFromRegistro(coEntidad, nuEnvio, padronProperties.ESREGISTRO_COT_ERROR);

				default:
					throw new IllegalStateException("PrecotejoFiltroTipoRegistro no válido");
			}

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return 0;
		}
	}

	/*
	@Override
	public int countAllFromPrecotejo(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.countAllFromRegistroObs(coEntidad, nuEnvio);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return 0;
		}
	}

	@Override
	public int countAllFromCotejo(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.countAllFromRegistro(coEntidad, nuEnvio);

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return 0;
		}
	}
	*/

	@Override
	public List<PrecotejoRegistro> getAllFromCotejoObsDetails(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getAllObsDetails(coEntidad, nuEnvio, padronProperties.ESREGISTRO_COT_ERROR); //4x

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public List<PrecotejoRegistro> getAllFromPrecotejoObsDetails(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getAllObsDetails(coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_ERROR); //12

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

	@Override
	public Precotejo getPrecotejoDetails(String coEntidad, String nuEnvio) {
		try {
			return precotejoDao.getPrecotejoDetails(coEntidad, nuEnvio);

		} catch (EmptyResultDataAccessException erdae) {
			log.error(erdae.getMessage());
			//erdae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.erdae"));
			return new Precotejo();

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}

    @Override
    public void setEstadoPrecotejo(String nuEnvio, String coEntidad) {
           precotejoDao.setEstadoPrecotejo(nuEnvio,coEntidad);
    }

	@Override
	public PrecotejoRegistroDetalle getPrecotejoRegistroDetalle(String coEntidad, String nuEnvio, String nuRegistro) {
		try {
			PrecotejoRegistroDetalle precotejoRegistroDetalle = precotejoDao.getPrecotejoRegistroDetalle(coEntidad, nuEnvio, nuRegistro);
			Type collectionType = new TypeToken<List<HashMap<String, Object>>>() {
			}.getType();
			Gson gson = new Gson();
			List<HashMap<String, Object>> hashMapList = gson.fromJson(precotejoRegistroDetalle.getDeObservacionCotejo(), collectionType);
			HashMap<String, Object> result = new HashMap<String, Object>();


			for (HashMap<String, Object> error : hashMapList) {
				Iterator it = error.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry) it.next();
					result.put((String) e.getKey(), e.getValue());
				}
			}

			Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
//			log.debug(gson2.toJson(result));
			precotejoRegistroDetalle.setCamposErrores(result);
			return precotejoRegistroDetalle;
		} catch (EmptyResultDataAccessException erdae) {
			log.error(erdae.getMessage());
			//erdae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.erdae"));
			return new PrecotejoRegistroDetalle();

		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//throw new RuntimeException(new Throwable("err.dae"));
			return null;
		}
	}



	// Este método no debe ser transaccional, porque el nuenvio debe quedar registrado antes de que se registren los registros, inclusive.
	@Override
	public void insert(Precotejo precotejo, List<PrecotejoRegistro> precotejoRegistroList, boolean minimalConstraintsCheck) {
//		log.debug("Iniciando inserción de : " + precotejoRegistroList.size() + " item(s).");

		Number nuEnvio = precotejoDao.getNextNuEnvio(precotejo.getCoEntidad().toString());
		precotejo.setNuEnvio(nuEnvio);
		precotejo.setEsEnvio(padronProperties.ESENVIO_REG_PROCESO);
		precotejoDao.insert(precotejo);

		// iniciamos thread de registro, considerar que la referencia a precotejo cambia, por eso se crea un nuevo registro
		future = executorService.submit(new InsertListThread(new Precotejo(precotejo), precotejoRegistroList, minimalConstraintsCheck));
	}



	// ver http://static.springsource.org/spring/docs/3.0.x/reference/transaction.html
	//@Transactional //de todas formas no funcionaría
	private int[] insertList(Precotejo precotejo, List<PrecotejoRegistro> precotejoRegistroList, boolean minimalConstraintsCheck) {

		int nuRegistro = 0;
		int nuRegistroObs = 0;
		for (PrecotejoRegistro precotejoRegistro : precotejoRegistroList) {
			nuRegistro++;

			precotejoRegistro.setNo(String.valueOf(nuRegistro));
			precotejoRegistro.setNuEnvio(precotejo.getNuEnvio());
			precotejoRegistro.setCoEntidad(precotejo.getCoEntidad().toString());
			precotejoRegistro.setUsCreaRegistro(precotejo.getUsCreaAudi());
			precotejoRegistro.setUsModiRegistro(precotejo.getUsModiAudi());

			// podríamos haber agregado estas validaciones en el set del bean pero aquí es más legible
			precotejoRegistro.cleanBeforeInsert();

			//log.debug("Bean a validar: "+precotejoRegistro);
			Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;
			if (minimalConstraintsCheck) {
				constraintViolations = beanValidator.getValidator()
						.validate(precotejoRegistro, MinimalPrecotejoRegistroPriorInsertChecks.class);

                /*Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations2 = beanValidator.getValidator()
                        .validate(precotejoRegistro);*/


			} else {
                // fixne: VALIDADOR ANTIGUO, VERSION ANTERIOR
				/*constraintViolations = beanValidator.getValidator()
						.validate(precotejoRegistro, PrecotejoRegistroPriorInsertChecks.class);*/
                constraintViolations = beanValidator.getValidator()
                        .validate(precotejoRegistro, MinimalPrecotejoRegistroPriorInsertChecks.class);
			}

			if (constraintViolations.isEmpty()) {
				//log.debug("DEBUG INSERTAR: "+precotejoRegistro);
				//try {
//                log.debug("fe_nac_menor:: " + precotejoRegistro.getFeNacMenor());
				precotejoDao.insert(precotejoRegistro);
				//} catch (DataAccessException dae) {
				//	log.error(dae.getMessage());
				//	dae.printStackTrace();
				//	precotejoRegistro.setDeObservacion(dae.getMessage());
				//	precotejoDao.insertObs(precotejoRegistro);
				//}

			} else {
				//precotejoRegistro.setDeObservacion(validatorUtils.getMessage(constraintViolations));
				//precotejoRegistro.setDeObservacion(validatorUtils.getHtmlMessage(constraintViolations));
				//precotejoRegistro.setDeObservacion(validatorUtils.getTextMessage(constraintViolations));
				//precotejoRegistro.setDeObservacion(validatorUtils.getJsonMessage(constraintViolations));
				precotejoRegistro.setDeObservacion(validatorUtils.getSpecialMessage(constraintViolations));

				//log.debug("DEBUG INSERTAR ERROR: "+precotejoRegistro);
				precotejoDao.insertObs(precotejoRegistro);
				nuRegistroObs++;
			}
		}
		return new int[]{nuRegistro, nuRegistroObs};
	}



	@Override
	public void waitTask() {
		if (future != null) {
			try {
				future.get();
			} catch (InterruptedException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} catch (ExecutionException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}



	// un thread no tendrá acceso a los beans de Spring!
	class InsertListThread
			extends Thread {

		List<PrecotejoRegistro> precotejoRegistroList;
		Precotejo precotejo;
		boolean minimalConstraintsCheck;

		public InsertListThread(Precotejo precotejo, List<PrecotejoRegistro> precotejoRegistroList, boolean minimalConstraintsCheck) {
			this.precotejoRegistroList = precotejoRegistroList;
			this.precotejo = precotejo;
			this.minimalConstraintsCheck = minimalConstraintsCheck;
		}

		public void run() {
//			log.debug("Iniciando thread #" + this.getId() + " para inserción de lista: " + precotejoRegistroList.size() + " ítem(s).");
			//
			boolean successfully = false;
			int[]nuRegistrosArray = {0, 0};
			if (precotejoRegistroList.size() > 0) {
				try {
					nuRegistrosArray = insertList(precotejo, precotejoRegistroList, minimalConstraintsCheck);
					successfully = true;
				} catch (DataAccessException dae) {
					log.error(dae.getMessage());
					dae.printStackTrace();
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			} else {
				successfully = true;
			}

			if (successfully) {
				// finalizado exitosamente
				precotejo.setEsEnvio(padronProperties.ESENVIO_REG_OK);
				precotejo.setNuRegistros(String.valueOf(nuRegistrosArray[0]));
				precotejo.setNuRegistrosObs(String.valueOf(nuRegistrosArray[1]));
				precotejoDao.update(precotejo);

                // callback...

				precotejoDao.cotejarPrecarga(precotejo.getCoEntidad(), precotejo.getNuEnvio());
			} else {
				// finalizado con errores ?!
				precotejo.setEsEnvio(padronProperties.ESENVIO_REG_ERROR);
				precotejo.setNuRegistros(String.valueOf(nuRegistrosArray[0]));
				precotejo.setNuRegistrosObs(String.valueOf(nuRegistrosArray[1]));
				precotejoDao.update(precotejo);
			}

			// <<
			precotejoDao.callCotejo();
			// >>

			//
//			log.debug("Finalizando thread #" + this.getId() + ".");
		}
	}
}


