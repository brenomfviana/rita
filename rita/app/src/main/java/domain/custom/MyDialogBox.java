package domain.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tb.rita.R;

/**
 * Created by thalesaguiar on 21/11/2017.
 */

public class MyDialogBox extends DialogFragment {

    private String dialogMessage;
    private AlertDialog.Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(builder == null)
            builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getDialogMessage());
        return builder.create();
    }

    public void setBuilder(AlertDialog.Builder builder) {
        this.builder = builder;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public String getDialogMessage() {
        return dialogMessage;
    }

    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

}
