package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.ConsultaService;

import java.util.List;

/**
 * Clase ConsultaServiceImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 27/05/13 08:17 AM
 */
@Service
public class ConsultaServiceImpl
		extends AbstractBaseService
		implements ConsultaService {

	@Override
	public List getEntidadList() {
		throw new UnsupportedOperationException("TODO");
	}
}
