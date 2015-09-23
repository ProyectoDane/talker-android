package ar.uba.fi.talker.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import ar.uba.fi.talker.CanvasActivity;
import ar.uba.fi.talker.HistoricalActivity;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.utils.GridItems;

public class OnClickStartActionDefault implements OnClickListener {

	
	private final Context context;
	protected GridItems gridItem = null;
	protected DialogFragment parent=null;
		
	public OnClickStartActionDefault(final Context context,GridItems gridItem,DialogFragment parent){
		this.context=context;
		this.gridItem=gridItem;
		this.parent = parent;
	}
	
	
	@Override
	public void onClick(View v) {
		
		Bundle extras = new Bundle();
		TalkerDTO scenarioView = gridItem.getElementGridView();
		if (scenarioView.getPath() != null && scenarioView.getPath().contains("/")){
			extras.putString("path", scenarioView.getPath());
		} else {
			int idCode = Integer.valueOf(scenarioView.getPath());
			extras.putInt("code", idCode);
		}

		Intent intent = new Intent(context, CanvasActivity.class);
		if (context instanceof HistoricalActivity) {
			extras.putBoolean("isHistory", true);
		}
		intent.putExtras(extras);
		parent.startActivity(intent);
		parent.dismiss();
	}

}