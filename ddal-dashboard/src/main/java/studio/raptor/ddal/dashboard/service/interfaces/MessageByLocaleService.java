package studio.raptor.ddal.dashboard.service.interfaces;

/**
 * @author Sam
 * @since 3.1.0
 */
public interface MessageByLocaleService {

  String getMessage(String id);

  String getMessage(String id, Object[] objects);

}
