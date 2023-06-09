package fm.last.citrine.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import fm.last.citrine.model.Task;
import fm.last.citrine.service.TaskManager;

public class TaskFormControllerTest {

    private TaskFormController taskFormController = new TaskFormController();

    @Mock
    private TaskManager mockTaskManager;

    private MockHttpServletRequest mockRequest = new MockHttpServletRequest();

    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        taskFormController.setTaskManager(mockTaskManager);
    }

    @Test
    public void testDelete() {
        Task task = new Task();
        task.setId(345);
        TaskDTO dto = new TaskDTO(task);
        when(mockTaskManager.get(task.getId())).thenReturn(task);
        BindException bindException = new BindException(dto, "bla");
        mockRequest.addParameter(Constants.PARAM_DELETE, "true");
        ModelAndView modelAndView = taskFormController.onSubmit(mockRequest, mockResponse, dto, bindException);
        RedirectView view = (RedirectView) modelAndView.getView();
        assertEquals("tasks.do?selectedGroupName=" + Constants.GROUP_NAME_ALL, view.getUrl());
        verify(mockTaskManager).delete(task);
        assertTrue(modelAndView.getModel().isEmpty());
    }
}
