/******************************************************************************
 *
 *  Copyright 2014 Paphus Solutions Inc.
 *
 *  Licensed under the Eclipse Public License, Version 1.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package org.botlibre.sdk.activity;

import org.botlibre.sdk.activity.actions.HttpAction;
import org.botlibre.sdk.activity.actions.HttpGetImageAction;
import org.botlibre.sdk.activity.actions.HttpSaveResponseAction;
import org.botlibre.sdk.config.ResponseConfig;
import org.botlibre.sdk.util.TextStream;

import org.botlibre.sdk.R;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity for editing an response.
 */
public class ResponseActivity extends LibreActivity {
	
	protected ResponseConfig instance;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_response);

		this.instance = MainActivity.response;

		String title = "Edit Response";
		if (this.instance.type != null && this.instance.type.equals("default")) {
			findViewById(R.id.questionText).setVisibility(View.GONE);
			findViewById(R.id.keywordsText).setVisibility(View.GONE);
			findViewById(R.id.requiredText).setVisibility(View.GONE);
			if (this.instance.responseId == null || this.instance.responseId.isEmpty()) {
				title = "New Default Response";
			} else {
				title = "Edit Default Response";
			}
		} else if (this.instance.type != null && this.instance.type.equals("greeting")) {
			findViewById(R.id.questionText).setVisibility(View.GONE);
			findViewById(R.id.keywordsText).setVisibility(View.GONE);
			findViewById(R.id.requiredText).setVisibility(View.GONE);
			findViewById(R.id.repeatText).setVisibility(View.GONE);
			findViewById(R.id.labelText).setVisibility(View.GONE);
			findViewById(R.id.repeatCheckBox).setVisibility(View.GONE);
			findViewById(R.id.previousText).setVisibility(View.GONE);
			findViewById(R.id.previousCheckBox).setVisibility(View.GONE);
			if (this.instance.responseId == null || this.instance.responseId.isEmpty()) {
				title = "New Greeting";
			} else {
				title = "Edit Greeting";
			}
			((EditText)findViewById(R.id.responseText)).setHint("Greeting");
		} else if (this.instance.responseId == null || this.instance.responseId.isEmpty()) {
			title = "New Response";
		}
		setTitle(title);
		((TextView) findViewById(R.id.title)).setText(title);
        HttpGetImageAction.fetchImage(this, MainActivity.instance.avatar, findViewById(R.id.icon));

		((TextView) findViewById(R.id.questionText)).setText(this.instance.question);
		((TextView) findViewById(R.id.responseText)).setText(this.instance.response);
		((TextView) findViewById(R.id.topicText)).setText(this.instance.topic);
		((TextView) findViewById(R.id.labelText)).setText(this.instance.label);
		((TextView) findViewById(R.id.keywordsText)).setText(this.instance.keywords);
		((TextView) findViewById(R.id.requiredText)).setText(this.instance.required);
		((TextView) findViewById(R.id.emotionsText)).setText(this.instance.emotions);
		((TextView) findViewById(R.id.actionsText)).setText(this.instance.actions);
		((TextView) findViewById(R.id.posesText)).setText(this.instance.poses);
		((TextView) findViewById(R.id.previousText)).setText(this.instance.previous);
		((TextView) findViewById(R.id.repeatText)).setText(this.instance.onRepeat);
		
		((CheckBox) findViewById(R.id.repeatCheckBox)).setChecked(this.instance.noRepeat);
		((CheckBox) findViewById(R.id.previousCheckBox)).setChecked(this.instance.requirePrevious);

        boolean properties = false;
        if (this.instance.topic != null && !this.instance.topic.isEmpty()
        		|| (this.instance.label != null && !this.instance.label.isEmpty())
                || (this.instance.keywords != null && !this.instance.keywords.isEmpty())
                || (this.instance.required != null && !this.instance.required.isEmpty())
                || (this.instance.emotions != null && !this.instance.emotions.isEmpty())
                || (this.instance.actions != null && !this.instance.actions.isEmpty())
                || (this.instance.poses != null && !this.instance.poses.isEmpty())
                || (this.instance.previous != null && !this.instance.previous.isEmpty())
                || (this.instance.noRepeat)
                || (this.instance.requirePrevious)) {
        	properties = true;
        }
		if (!properties) {
			findViewById(R.id.propertiesView).setVisibility(View.GONE);
		}
		
		if (this.instance.keywords != null && !this.instance.keywords.isEmpty()) {
			final AutoCompleteTextView keywordsText = (AutoCompleteTextView)findViewById(R.id.keywordsText);
	        ArrayAdapter adapter = new ArrayAdapter(this,
	                android.R.layout.select_dialog_item, new TextStream(this.instance.question).allWords());
	        keywordsText.setThreshold(0);
	        keywordsText.setAdapter(adapter);
	        keywordsText.setOnTouchListener(new View.OnTouchListener() {
		    	   @Override
		    	   public boolean onTouch(View v, MotionEvent event) {
		    		   keywordsText.showDropDown();
		    		   return false;
		    	   }
		    	});
		}
		
		if (this.instance.required != null && !this.instance.required.isEmpty()) {
			final AutoCompleteTextView requiredText = (AutoCompleteTextView)findViewById(R.id.requiredText);
	        ArrayAdapter adapter = new ArrayAdapter(this,
	                android.R.layout.select_dialog_item, new TextStream(this.instance.question).allWords());
	        requiredText.setThreshold(0);
	        requiredText.setAdapter(adapter);
	        requiredText.setOnTouchListener(new View.OnTouchListener() {
		    	   @Override
		    	   public boolean onTouch(View v, MotionEvent event) {
		    		   requiredText.showDropDown();
		    		   return false;
		    	   }
		    	});
		}
		
		String[] values = new String[EmotionalState.values().length];
		for (int index = 0; index < EmotionalState.values().length; index++) {
			values[index] = EmotionalState.values()[index].name().toLowerCase();
		}
		final AutoCompleteTextView emotionsText = (AutoCompleteTextView)findViewById(R.id.emotionsText);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.select_dialog_item, values);
        emotionsText.setThreshold(0);
        emotionsText.setAdapter(adapter);
        emotionsText.setOnTouchListener(new View.OnTouchListener() {
	    	   @Override
	    	   public boolean onTouch(View v, MotionEvent event) {
	    		   emotionsText.showDropDown();
	    		   return false;
	    	   }
	    	});
        
		final AutoCompleteTextView actionsText = (AutoCompleteTextView)findViewById(R.id.actionsText);
        adapter = new ArrayAdapter(this,
                android.R.layout.select_dialog_item, new String[]{
                	"smile",
                	"frown",
                	"laugh",
                	"scream",
                	"sit",
                	"jump",
                	"bow",
                	"nod",
                	"shake-head",
                	"slap",
        			"kiss",
        			"burp",
        			"fart"
			    
        });
        actionsText.setThreshold(0);
        actionsText.setAdapter(adapter);
        actionsText.setOnTouchListener(new View.OnTouchListener() {
	    	   @Override
	    	   public boolean onTouch(View v, MotionEvent event) {
	    		   actionsText.showDropDown();
	    		   return false;
	    	   }
	    	});
		
		final AutoCompleteTextView posesText = (AutoCompleteTextView)findViewById(R.id.posesText);
        adapter = new ArrayAdapter(this,
                android.R.layout.select_dialog_item, new String[]{
                	"sitting",
                	"lying",
                	"walking",
                	"running",
                	"jumping",
                	"fighting",
                	"sleeping",
                	"dancing"
			    
        });
        posesText.setThreshold(0);
        posesText.setAdapter(adapter);
        posesText.setOnTouchListener(new View.OnTouchListener() {
	    	   @Override
	    	   public boolean onTouch(View v, MotionEvent event) {
	    		   posesText.showDropDown();
	    		   return false;
	    	   }
	    	});
	}
	
	public void showProperties(View view) {
		View properties = findViewById(R.id.propertiesView);
		if (properties.getVisibility() == View.GONE) {
			properties.setVisibility(View.VISIBLE);
		} else {
			properties.setVisibility(View.GONE);
		}
	}
    
    public void save(View view) {
        ResponseConfig config = new ResponseConfig();
        config.instance = MainActivity.instance.id;
        config.responseId = this.instance.responseId;
        config.questionId = this.instance.questionId;
        config.type = this.instance.type;
        config.correctness = this.instance.correctness;
        config.flagged = this.instance.flagged;
        
    	EditText text = (EditText) findViewById(R.id.questionText);
    	config.question = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.responseText);
    	config.response = text.getText().toString().trim();
    	if (config.response.isEmpty()) {
    		MainActivity.showMessage("Please enter a response", this);
    		return;
    	}
    	text = (EditText) findViewById(R.id.topicText);
    	config.topic = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.labelText);
    	config.label = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.keywordsText);
    	config.keywords = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.requiredText);
    	config.required = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.emotionsText);
    	config.emotions = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.actionsText);
    	config.actions = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.posesText);
    	config.poses = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.previousText);
    	config.previous = text.getText().toString().trim();
    	text = (EditText) findViewById(R.id.repeatText);
    	config.onRepeat = text.getText().toString().trim();
    	
    	CheckBox check = ((CheckBox) findViewById(R.id.repeatCheckBox));
    	config.noRepeat = check.isChecked();
    	check = ((CheckBox) findViewById(R.id.previousCheckBox));
    	config.requirePrevious = check.isChecked();
        
        HttpAction action = new HttpSaveResponseAction(ResponseActivity.this, config);
    	action.execute();
    }
}
