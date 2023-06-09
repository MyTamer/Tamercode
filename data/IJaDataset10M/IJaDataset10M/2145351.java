package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException.MissingBindingType;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationValidatingBindingException;

/**
 * Small utility class that validates pipes.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 3:43:30 PM
 */
final class PipeValidator {

    private PipeValidator() {
    }

    /**
     * Validates that read and write pipes for dto to entity are non null.
     *
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param dtoField dto field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     * @param entityField entity field
     * @throws AnnotationMissingBindingException if any of pipe is null (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validatePipeNonNull(final DataReader dtoRead, final DataWriter dtoWrite, final String dtoField, final DataReader entityRead, final DataWriter entityWrite, final String entityField) throws AnnotationMissingBindingException {
        validateReadPipeNonNull(dtoWrite, dtoField, entityRead, entityField);
        validateWritePipeNonNull(dtoRead, dtoField, entityWrite, entityField);
    }

    /**
     * Validates that read and write pipes for dto to entity are non null.
     *
     * @param dtoWrite method for writting data to DTO field
     * @param dtoWriteField dto field
     * @param entityRead method for reading data from Entity field
     * @param entityReadField entity field
     * @throws AnnotationMissingBindingException if any of pipe is null (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateReadPipeNonNull(final DataWriter dtoWrite, final String dtoWriteField, final DataReader entityRead, final String entityReadField) throws AnnotationMissingBindingException {
        validatePipeNonNull(entityRead, MissingBindingType.ENTITY_READ, entityReadField);
        validatePipeNonNull(dtoWrite, MissingBindingType.DTO_WRITE, dtoWriteField);
    }

    /**
     * Validates that read and write pipes for dto to entity match types.
     *
     * @param dtoRead method for reading data from DTO field
     * @param dtoReadField dto field
     * @param entityWrite method for writting data to Entity field
     * @param entityWriteField entity field
     * @throws AnnotationMissingBindingException if any of pipe is null (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateWritePipeNonNull(final DataReader dtoRead, final String dtoReadField, final DataWriter entityWrite, final String entityWriteField) throws AnnotationMissingBindingException {
        validatePipeNonNull(dtoRead, MissingBindingType.DTO_READ, dtoReadField);
        validatePipeNonNull(entityWrite, MissingBindingType.ENTITY_WRITE, entityWriteField);
    }

    /**
     * Validate that method is not null.
     * 
     * @param meth method to check
     * @param type type of reder/writer
     * @param fieldName field name
     * @throws AnnotationMissingBindingException if reader/writer is missing
     */
    static void validatePipeNonNull(final Object meth, final MissingBindingType type, final String fieldName) throws AnnotationMissingBindingException {
        if (meth == null) {
            throw new AnnotationMissingBindingException(type, fieldName);
        }
    }

    /**
     * Validates thate read and write pipes for dto to entity match types.
     *
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param dtoField dto field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     * @param entityField entity field
     * @throws AnnotationValidatingBindingException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validatePipeTypes(final DataReader dtoRead, final DataWriter dtoWrite, final String dtoField, final DataReader entityRead, final DataWriter entityWrite, final String entityField) throws AnnotationValidatingBindingException {
        validateReadPipeTypes(dtoWrite, dtoField, entityRead, entityField);
        validateWritePipeTypes(dtoRead, dtoField, entityWrite, entityField);
    }

    private static boolean sameDataType(final Class<?> data1, final Class<?> data2) {
        return data1.equals(data2) || (data1.isPrimitive() && !data2.isPrimitive() && samePrimitiveDataType(data2, data1)) || (!data1.isPrimitive() && data2.isPrimitive() && samePrimitiveDataType(data1, data2));
    }

    private static boolean samePrimitiveDataType(final Class<?> wrapper, final Class<?> primitive) {
        try {
            return wrapper.getDeclaredField("TYPE").get(null).equals(primitive);
        } catch (Throwable thr) {
            return false;
        }
    }

    /**
     * Validates that read and write pipes for dto to entity match types.
     *
     * @param dtoWrite method for writting data to DTO field
     * @param dtoField dto field
     * @param entityRead method for reading data from Entity field
     * @param entityField entity field
     * @throws AnnotationValidatingBindingException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateReadPipeTypes(final DataWriter dtoWrite, final String dtoField, final DataReader entityRead, final String entityField) throws AnnotationValidatingBindingException {
        final Class<?> dtoWriteClass = dtoWrite.getParameterType();
        final Class<?> entityReadClass = entityRead.getReturnType();
        if (!dtoWriteClass.isInterface() && dtoWriteClass.getAnnotation(Dto.class) == null && !entityReadClass.equals(Object.class) && !dtoWriteClass.equals(Object.class) && !sameDataType(dtoWriteClass, entityReadClass)) {
            throw new AnnotationValidatingBindingException(dtoField, dtoWrite.getClass().getCanonicalName(), dtoWriteClass.getSimpleName(), entityField, entityRead.getClass().getCanonicalName(), entityReadClass.getSimpleName(), false);
        }
    }

    /**
     * Validates that read and write pipes for dto to entity match types.
     *
	 * @param dtoRead method for reading data from DTO field
	 * @param dtoField dto field
     * @param entityWrite method for writting data to Entity field
     * @param entityField entity field
     *
     * @throws AnnotationValidatingBindingException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateWritePipeTypes(final DataReader dtoRead, final String dtoField, final DataWriter entityWrite, final String entityField) throws AnnotationValidatingBindingException {
        final Class<?> dtoReadClass = dtoRead.getReturnType();
        final Class<?> entityWriteClass = entityWrite.getParameterType();
        if (!dtoReadClass.isInterface() && dtoReadClass.getAnnotation(Dto.class) == null && !entityWriteClass.equals(Object.class) && !dtoReadClass.equals(Object.class) && !sameDataType(dtoReadClass, entityWriteClass)) {
            throw new AnnotationValidatingBindingException(dtoField, dtoRead.getClass().getCanonicalName(), dtoReadClass.getSimpleName(), entityField, entityWrite.getClass().getCanonicalName(), entityWriteClass.getSimpleName(), true);
        }
    }
}
