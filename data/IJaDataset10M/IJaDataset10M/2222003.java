package net.entropysoft.transmorph.injectors;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import net.entropysoft.transmorph.ConversionContext;
import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.converters.IdentityConverter;
import net.entropysoft.transmorph.converters.MultiConverter;
import net.entropysoft.transmorph.converters.WrapperToPrimitive;
import net.entropysoft.transmorph.converters.beans.BeanToBeanMapping;
import net.entropysoft.transmorph.converters.beans.IBeanPropertyTypeProvider;
import net.entropysoft.transmorph.converters.beans.utils.ClassPair;
import net.entropysoft.transmorph.type.TypeReference;
import net.entropysoft.transmorph.utils.BeanUtils;

/**
 * Copy properties from one bean to another bean
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class BeanToBeanInjector extends AbstractBeanInjector {

    private IBeanPropertyTypeProvider beanDestinationPropertyTypeProvider;

    private Map<ClassPair<?, ?>, BeanToBeanMapping> beanToBeanMappings = new HashMap<ClassPair<?, ?>, BeanToBeanMapping>();

    private boolean handleTargetClassSameAsSourceClass = true;

    private boolean handleTargetClassIsSuperClassOfSourceClass = true;

    public BeanToBeanInjector() {
        propertyValueConverter = new MultiConverter(new IdentityConverter(), new WrapperToPrimitive());
    }

    public IBeanPropertyTypeProvider getBeanDestinationPropertyTypeProvider() {
        return beanDestinationPropertyTypeProvider;
    }

    public void setBeanDestinationPropertyTypeProvider(IBeanPropertyTypeProvider beanDestinationPropertyTypeProvider) {
        this.beanDestinationPropertyTypeProvider = beanDestinationPropertyTypeProvider;
    }

    /**
	 * Add a mapping of properties between two beans
	 * 
	 * @param beanToBeanMapping
	 */
    public void addBeanToBeanMapping(BeanToBeanMapping beanToBeanMapping) {
        beanToBeanMappings.put(ClassPair.get(beanToBeanMapping.getSourceClass(), beanToBeanMapping.getDestinationClass()), beanToBeanMapping);
    }

    public boolean isHandleTargetClassSameAsSourceClass() {
        return handleTargetClassSameAsSourceClass;
    }

    public boolean isHandleTargetClassIsSuperClassOfSourceClass() {
        return handleTargetClassIsSuperClassOfSourceClass;
    }

    /**
	 * By default, BeanToBeanInjector can handle the case where target class is
	 * the same as source class. If you don't want it to handle this case, you
	 * can set this property to false
	 * 
	 * @param useIfTargetClassSameAsSourceClass
	 */
    public void setHandleTargetClassSameAsSourceClass(boolean useIfTargetClassSameAsSourceClass) {
        this.handleTargetClassSameAsSourceClass = useIfTargetClassSameAsSourceClass;
    }

    public boolean canHandle(Object sourceObject, TypeReference<?> targetType) {
        return canHandle(sourceObject.getClass(), targetType.getRawType());
    }

    public void inject(ConversionContext context, Object sourceObject, Object targetBean, TypeReference<?> targetType) throws ConverterException {
        Class<?> destinationClass = targetType.getRawType();
        if (!canHandle(sourceObject.getClass(), destinationClass)) {
            throw new ConverterException(MessageFormat.format("Could not get bean to bean mapping for ''{0}''=>''{1}''", sourceObject.getClass().getName(), destinationClass.getName()));
        }
        Map<String, Method> destinationSetters;
        destinationSetters = BeanUtils.getSetters(destinationClass);
        for (Map.Entry<String, Method> entry : destinationSetters.entrySet()) {
            String destinationPropertyName = entry.getKey();
            Method destinationMethod = entry.getValue();
            Method sourceMethod = getPropertySourceMethod(sourceObject, targetBean, destinationPropertyName);
            if (sourceMethod == null) {
                continue;
            }
            Object sourcePropertyValue;
            try {
                sourcePropertyValue = sourceMethod.invoke(sourceObject);
            } catch (Exception e) {
                throw new ConverterException("Could not get property for bean", e);
            }
            java.lang.reflect.Type parameterType = destinationMethod.getGenericParameterTypes()[0];
            TypeReference<?> originalType = TypeReference.get(parameterType);
            TypeReference<?> propertyDestinationType = getBeanPropertyType(destinationClass, destinationPropertyName, originalType);
            Object destinationPropertyValue = propertyValueConverter.convert(context, sourcePropertyValue, propertyDestinationType);
            try {
                destinationMethod.invoke(targetBean, destinationPropertyValue);
            } catch (Exception e) {
                throw new ConverterException("Could not set property for bean", e);
            }
        }
    }

    /**
	 * get the property source method corresponding to given destination
	 * property
	 * 
	 * @param sourceObject
	 * @param destinationObject
	 * @param destinationProperty
	 * @return
	 */
    private Method getPropertySourceMethod(Object sourceObject, Object destinationObject, String destinationProperty) {
        BeanToBeanMapping beanToBeanMapping = beanToBeanMappings.get(ClassPair.get(sourceObject.getClass(), destinationObject.getClass()));
        String sourceProperty = null;
        if (beanToBeanMapping != null) {
            sourceProperty = beanToBeanMapping.getSourceProperty(destinationProperty);
        }
        if (sourceProperty == null) {
            sourceProperty = destinationProperty;
        }
        return BeanUtils.getGetterPropertyMethod(sourceObject.getClass(), sourceProperty);
    }

    /**
	 * get the bean property type
	 * 
	 * @param clazz
	 * @param propertyName
	 * @param originalType
	 * @return
	 */
    protected TypeReference<?> getBeanPropertyType(Class<?> clazz, String propertyName, TypeReference<?> originalType) {
        TypeReference<?> propertyDestinationType = null;
        if (beanDestinationPropertyTypeProvider != null) {
            propertyDestinationType = beanDestinationPropertyTypeProvider.getPropertyType(clazz, propertyName, originalType);
        }
        if (propertyDestinationType == null) {
            propertyDestinationType = originalType;
        }
        return propertyDestinationType;
    }

    private boolean canHandle(Class<?> sourceObjectClass, Class<?> destinationClass) {
        if (handleTargetClassSameAsSourceClass && sourceObjectClass.equals(destinationClass)) {
            return true;
        }
        if (handleTargetClassIsSuperClassOfSourceClass && destinationClass.isAssignableFrom(sourceObjectClass)) {
            return true;
        }
        BeanToBeanMapping beanToBeanMapping = beanToBeanMappings.get(ClassPair.get(sourceObjectClass, destinationClass));
        return beanToBeanMapping != null;
    }
}
