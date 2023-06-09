package org.openmobster.core.cloud.console.client.ui.accounts;

import org.openmobster.core.cloud.console.client.common.Payload;
import org.openmobster.core.cloud.console.client.flow.FlowServiceRegistry;
import org.openmobster.core.cloud.console.client.rpc.DeviceManagementService;
import org.openmobster.core.cloud.console.client.rpc.DeviceManagementServiceAsync;
import org.openmobster.core.cloud.console.client.ui.Screen;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.Button;

/**
 *
 * @author openmobster@gmail.com
 */
public class ConfirmRemoteLockDialog implements Screen {

    private String account;

    public ConfirmRemoteLockDialog(String account) {
        this.account = account;
    }

    public String title() {
        return "confirmRemoteLockDialog";
    }

    public Canvas render() {
        Window winModal = new Window();
        String icon = "openmobster/lock.png";
        winModal.setWidth(435);
        winModal.setHeight(150);
        winModal.setTitle("Confirm Remote Lock");
        winModal.setShowMinimizeButton(false);
        winModal.setIsModal(true);
        winModal.setShowModalMask(true);
        winModal.centerInPage();
        winModal.setShowCloseButton(true);
        winModal.setHeaderIcon(icon);
        VLayout formLayout = new VLayout();
        DynamicForm form = new DynamicForm();
        form.setHeight100();
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);
        BlurbItem message = new BlurbItem();
        message.setValue("Are you sure you want to remotely lock the device?");
        form.setFields(message);
        formLayout.addChild(form);
        HLayout toolbar = new HLayout();
        toolbar.setAlign(Alignment.CENTER);
        Button yes = new Button();
        yes.setTitle("Yes");
        yes.addClickHandler(new YesClickHandler());
        Button no = new Button();
        no.setTitle("No");
        no.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                FlowServiceRegistry.getTransitionService().closeActiveWindow();
            }
        });
        toolbar.addMember(yes);
        toolbar.addMember(no);
        winModal.addItem(formLayout);
        winModal.addItem(toolbar);
        return winModal;
    }

    private class YesClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            SC.showPrompt("Remotely Locking the Device....");
            String account = ConfirmRemoteLockDialog.this.account;
            final DeviceManagementServiceAsync service = GWT.create(DeviceManagementService.class);
            String payload = Payload.encode(new String[] { "lock", account });
            service.invoke(payload, new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable t) {
                    SC.clearPrompt();
                    SC.say("System Error", "Unexpected Network Error. Please try again.", null);
                }

                @Override
                public void onSuccess(String result) {
                    SC.clearPrompt();
                    if (result.trim().equals("500")) {
                        SC.say("System Error", "Internal Server Error. Please try again.", null);
                    } else {
                        SC.say("Remote Lock", "The device must be successfully locked", new BooleanCallback() {

                            @Override
                            public void execute(Boolean value) {
                                FlowServiceRegistry.getTransitionService().closeActiveWindow();
                            }
                        });
                    }
                }
            });
        }
    }
}
