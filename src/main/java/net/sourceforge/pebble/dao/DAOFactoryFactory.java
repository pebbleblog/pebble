package net.sourceforge.pebble.dao;

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.dao.file.FileDAOFactory;
import net.sourceforge.pebble.dao.orient.OrientDAOFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.io.File;
import java.util.Locale;

/**
 * Factory for creating the right DAO factory
 */
public class DAOFactoryFactory implements FactoryBean<DAOFactory>, InitializingBean, DisposableBean {

  @Inject
  private Configuration configuration;
  private DAOFactory daoFactory;

  public void afterPropertiesSet() throws Exception {
    if (configuration.getStorageType().toLowerCase(Locale.ENGLISH).equals(FileDAOFactory.FILE_STORAGE_TYPE)) {
      daoFactory = new FileDAOFactory();
    } else if (configuration.getStorageType().toLowerCase(Locale.ENGLISH).equals(OrientDAOFactory.ORIENT_STORAGE_TYPE)) {
      daoFactory = new OrientDAOFactory(new File(configuration.getDataDirectory()));
    } else {
      throw new IllegalArgumentException("Unknown storage type: " + configuration.getStorageType());
    }
  }

  public void destroy() throws Exception {
    if (daoFactory != null) {
      daoFactory.shutdown();
    }
  }

  public DAOFactory getObject() throws Exception {
    return daoFactory;
  }

  public Class<?> getObjectType() {
    return DAOFactory.class;
  }

  public boolean isSingleton() {
    return true;
  }
}
