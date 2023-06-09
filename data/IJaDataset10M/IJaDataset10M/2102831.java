package edu.ncsu.csc.itrust.validate;

import org.apache.commons.validator.UrlValidator;
import edu.ncsu.csc.itrust.beans.PatientInstructionsBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 *  Validates a patient instructions bean.
 */
public class PatientInstructionsBeanValidator extends BeanValidator<PatientInstructionsBean> {

    @Override
    public void validate(PatientInstructionsBean bean) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        errorList.addIfNotNull(checkFormat("Name", bean.getName(), ValidationFormat.PATIENT_INSTRUCTIONS_NAME, false));
        errorList.addIfNotNull(checkFormat("Comments", bean.getComment(), ValidationFormat.PATIENT_INSTRUCTIONS_COMMENTS, false));
        errorList.addIfNotNull(checkFormat("URL", bean.getUrl(), ValidationFormat.PATIENT_INSTRUCTIONS_URL, false));
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(bean.getUrl())) {
            errorList.addIfNotNull("URL: A valid URL is required.");
        }
        if (errorList.hasErrors()) throw new FormValidationException(errorList);
    }
}
