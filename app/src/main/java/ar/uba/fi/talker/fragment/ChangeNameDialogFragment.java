package ar.uba.fi.talker.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dataSource.TalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;

public class ChangeNameDialogFragment extends ParentDialogFragment implements DialogInterface.OnClickListener {

	private TalkerDTO scenarioView;
	private BaseAdapter adapter;
	private EditText input;
	private TalkerDataSource dao;

	public void init(TalkerDTO scenarioView, BaseAdapter adapter, TalkerDataSource dao) {
		this.scenarioView = scenarioView;
		this.adapter = adapter; 
		this.dao = dao;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		input = new EditText(getActivity());
		input.setId(R.id.insert_text_input);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		input.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE && v.getText().length() != 0) {
					ChangeNameDialogFragment.this.onClick(getDialog(), AlertDialog.BUTTON_POSITIVE);
					return true;
				}
				return false;
			}
		});
		builder.setView(input)
				.setTitle(R.string.change_conversation_title)
				.setPositiveButton(R.string.delete_conversation_accept, this)
				.setNegativeButton(R.string.insert_text_cancel, this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == AlertDialog.BUTTON_POSITIVE && input.getText().length() != 0) {
			String text = input.getText().toString();
			scenarioView.setName(text);
			new NameChangerTask(dao).execute(scenarioView);				
			adapter.notifyDataSetChanged();
		}
		dialog.dismiss();
	}

	private class NameChangerTask extends AsyncTask<TalkerDTO, ProgressBar, Boolean> {

		private TalkerDataSource dao;

		public NameChangerTask(TalkerDataSource dao) {
			this.dao = dao;
		}

		@Override
		protected Boolean doInBackground(TalkerDTO... params) {
			TalkerDTO scenarioView = params[0];
			this.dao.update(scenarioView);
			return true;
		}
		
	}
}
