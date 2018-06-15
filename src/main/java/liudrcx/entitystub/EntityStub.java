package liudrcx.entitystub;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.testng.Assert;

/**
 * 
 * @author Allen Liu
 * A tool to create and assert java entities for testing purpose
 */
public class EntityStub {
  
  public static <T> void assertEntity(T bean, Entity entity) {
    entity.initType(bean.getClass());
    entity.assertEquals(bean);
  }
  
  public static <T> void assertEntities(List<T> beans, Entities entities) {
    assertFalse(beans.isEmpty());
    entities.initType(beans.get(0).getClass());
    entities.assertEquals(beans);
  }
  
  @SuppressWarnings("unchecked")
  public static <T> List<T> createEntities(Class<T> klass, Entities entities) {
    entities.initType(klass);
    return (List<T>)entities.create();
  }
  
  @SuppressWarnings("unchecked")
  public static <T> T createEntity(Class<T> klass, Entity entity) {
    entity.initType(klass);
    return (T)entity.create();
  }
  
  public static Entities entities(Entity... entities) {
    return new Entities(entities);
  }
  
  public static Entity entity(Field... fields) {
    return new Entity(fields);
  }
  
  public static Field field(String name, Object value) {
    return new Field(name, value);
  }
  
  public static abstract class Element {
    public abstract void initType(Class<?> klass);
    public abstract Object create();
    public abstract void assertEquals(Object bean);
  }
  
  public static class Entities extends Element {
    private Entity[] entities;
    
    public Entities(Entity[] entities) {
      this.entities = entities;
    }

    @Override
    public void initType(Class<?> klass) {
      for (Entity entity : entities) {
        entity.initType(klass);
      }
    }

    @Override
    public Object create() {
      List<Object> result = new ArrayList<>();
      for (Entity entity : entities) {
        result.add(entity.create());
      }
      return result;
    }

    @Override
    public void assertEquals(Object bean) {
      List<?> items = (List<?>) bean;
      Assert.assertEquals(items.size(), entities.length);
      for (int i = 0; i < entities.length; i ++) {
        entities[i].assertEquals(items.get(i));
      }
    }
    
  }
   
  public static class Entity extends Element {
    private Class<?> klass;
    private Field[] fields;
    
    public Entity(Field[] fields) {
      this.fields = fields;
    }

    @Override
    public void initType(Class<?> klass) {
      this.klass = klass;
      for (Field field : fields) {
        field.initType(klass);
      }
    }

    @Override
    public Object create() {
      Object result = null;
      try {
        result = klass.newInstance();
        for (Field field : fields) {
          BeanUtils.setProperty(
            result, 
            field.name, 
            field.create());
        }
      } catch (InstantiationException e) {
        e.printStackTrace();
        fail();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        fail();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        fail();
      } catch (SecurityException e) {
        e.printStackTrace();
        fail();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
        fail();
      }
      return result;
    }

    @Override
    public void assertEquals(Object bean) {
      for (Field field : fields) {
        field.assertEquals(bean);
      }
    }
  }
  
  public static class Field extends Element {
    private String name;
    private Object value;
    
    public Field(String name, Object value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public void initType(Class<?> klass) {
      if (value instanceof Element) {
        try {
          Class<?> target = null;
          java.lang.reflect.Field field = klass.getDeclaredField(name);
          if (field.getType() == List.class) {
            ParameterizedType genericType = (ParameterizedType)field.getGenericType();
            target = (Class<?>)genericType.getActualTypeArguments()[0];
          } else {
            target = field.getType();
          }
          ((Element) value).initType(target);
        } catch (NoSuchFieldException e) {
          e.printStackTrace();
          fail();
        } catch (SecurityException e) {
          e.printStackTrace();
          fail();
        }
      } 
    }

    @Override
    public Object create() {
      if (value instanceof Element) {
        return ((Element) value).create();
      } else {
        return value;
      }
    }

    @Override
    public void assertEquals(Object bean) {
      try {
        if (value instanceof Element) {
            ((Element) value)
              .assertEquals(PropertyUtils.getProperty(bean, name));
          
        } else {
          Assert
            .assertEquals(
              PropertyUtils.getProperty(bean, name), 
              value);
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        fail();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
        fail();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
        fail();
      }
    }
  }
}
