package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Archivo PrecotejoUploadService.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 03:58 PM
 */
public interface PrecotejoUploadService {
	List<PrecotejoRegistro> readFromStream(InputStream stream);
	List<PrecotejoRegistro> readFromExcelStream(InputStream stream);
	byte[] writeToBytes(List<HashMap<String, Object>> precotejoRegistroList);
	OutputStream writeToStream(List<HashMap<String, Object>> precotejoRegistroList);

	byte[] writeToCsvBytesFromPrecoteRegistroList(List<PrecotejoRegistro> precotejoRegistroList);
}
