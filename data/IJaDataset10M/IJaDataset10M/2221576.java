package org.crud.android.command;

import org.openmobster.android.api.sync.MobileBean;
import org.openmobster.core.mobileCloud.api.ui.framework.Services;
import org.openmobster.core.mobileCloud.api.ui.framework.command.AppException;
import org.openmobster.core.mobileCloud.api.ui.framework.command.CommandContext;
import org.openmobster.core.mobileCloud.api.ui.framework.command.AsyncCommand;
import org.openmobster.core.mobileCloud.api.ui.framework.navigation.NavigationContext;
import org.openmobster.core.mobileCloud.android.errors.ErrorHandler;
import org.openmobster.core.mobileCloud.android.service.Registry;
import org.openmobster.core.mobileCloud.android_native.framework.ViewHelper;
import android.app.Activity;
import android.widget.Toast;

/**
 * @author openmobster@gmail.com
 *
 */
public final class CreateTicket implements AsyncCommand {

    public void doViewBefore(CommandContext commandContext) {
        Activity activity = (Activity) commandContext.getAppContext();
        Toast.makeText(activity, "Saving......", Toast.LENGTH_LONG).show();
    }

    public void doAction(CommandContext commandContext) {
        try {
            MobileBean ticketToAdd = (MobileBean) commandContext.getAttribute("ticket");
            ticketToAdd.save();
        } catch (Exception e) {
            AppException appe = new AppException();
            appe.setMessage(e.getMessage());
            ErrorHandler.getInstance().handle(appe);
            throw appe;
        }
    }

    public void doViewAfter(CommandContext commandContext) {
        NavigationContext.getInstance().back();
    }

    public void doViewError(CommandContext commandContext) {
        Activity currentActivity = Services.getInstance().getCurrentActivity();
        ViewHelper.getOkModal(currentActivity, "App Error", this.getClass().getName() + " had an error!!\n\n" + commandContext.getAppException().getMessage()).show();
    }
}
