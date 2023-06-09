package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.questionAndAnswer.data.xmlBindingSubclasses;

import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewElementLink;
import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewElementLinkTarget;
import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewElementsManager;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.questionAndAnswer.data.xmlBinding.ViewElementLink;

public class FLGQuestionAndAnswerElementLink extends ViewElementLink implements FSLLearningUnitViewElementLink {

    public FSLLearningUnitViewElementLinkTarget addNewLearningUnitViewElementLinkTarget(String learningUnitId, String learningUnitViewManagerId, String learningUnitViewElementId) {
        FLGQuestionAndAnswerElementLinkTarget learningUnitViewElementLinkTarget = new FLGQuestionAndAnswerElementLinkTarget();
        return FSLLearningUnitViewElementsManager.addLearningUnitViewElementLinkTarget(this, learningUnitViewElementLinkTarget, learningUnitId, learningUnitViewManagerId, learningUnitViewElementId);
    }
}
