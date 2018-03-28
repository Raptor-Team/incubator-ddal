package studio.raptor.ddal.common.helper;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JMX助手
 *
 * @author Charley
 * @since 1.0
 */
public class JMXHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(JMXHelper.class);

  /**
   * Register the given mbean with the platform mbean server, unregistering any mbean that was there before. Note,
   * this method will not throw an exception if the registration fails (since there is nothing you can do and it isn't
   * fatal), instead it just returns false indicating the registration failed.
   *
   * @param mbean
   *            The object to register as an mbean
   * @param name
   *            The name to register this mbean with
   * @returns true if the registration succeeded
   */
  public static boolean registerMBean(Object mbean, String name) {
    try {
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      synchronized (mbs) {
        ObjectName objName = new ObjectName(name);
        if (mbs.isRegistered(objName)) {
          mbs.unregisterMBean(objName);
        }
        mbs.registerMBean(mbean, objName);
        LOGGER.info("register a MBean：{}", name);
      }
      return true;
    }
    catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("register the MBean of {} error", name, e);
    }
    return false;
  }

  /**
   * Unregister the mbean with the given name, if there is one registered
   *
   * @param name
   *            The mbean name to unregister
   * @see #registerMBean(Object, String)
   */
  public static void unregisterMBean(String name) {
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    try {
      synchronized (mbs) {
        ObjectName objName = new ObjectName(name);
        if (mbs.isRegistered(objName)) {
          mbs.unregisterMBean(objName);
          LOGGER.info("unregister MBean：{}", name);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      LOGGER.info("unregister MBean：{} error", name);
    }
  }
}
