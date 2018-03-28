package studio.raptor.ddal.dashboard.service.impl;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import studio.raptor.ddal.dashboard.service.interfaces.MessageByLocaleService;

/**
 * @author Sam
 * @since 3.1.0
 */
@Service
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

  private MessageSource messageSource;

  @Autowired
  public MessageByLocaleServiceImpl(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String getMessage(String id) {
    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(id, null, locale);
  }

  @Override
  public String getMessage(String id, Object[] objects) {
    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(id, objects, locale);
  }
}
