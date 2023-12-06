package com.gatdsen.animation.action;

import com.gatdsen.animation.entity.Toggleable;

/**
 * Action for Changing Objects implementing {@link Toggleable}.
 * Either by {@link Toggleable#toggle()} or {@link Toggleable#setEnabled(boolean)}
 */
public class ToggleElementAction extends Action{
	Toggleable target;
	boolean isEnabled;
	boolean toggle;

	public ToggleElementAction(float start,Toggleable target,boolean toggle, boolean isEnabled){
		super(start);
		this.target = target;
		this.isEnabled = isEnabled;
		this.toggle = toggle;
	}

	/**
	 * ToggleAction to change values of {@link Toggleable} via {@link Toggleable#setEnabled(boolean isEnabled)}
	 * @param start
	 * @param target Toggleable to change
	 * @param isEnabled value to change to
	 */
	public ToggleElementAction(float start, Toggleable target,boolean isEnabled){
		this(start,target,isEnabled,false);
	}

	/**
	 * ToggleAction to change values of {@link Toggleable} via {@link Toggleable#toggle()}
	 * @param start
	 * @param target
	 */
	public ToggleElementAction(float start, Toggleable target){
		this(start,target,true,false);
	}

	@Override
	protected void runAction(float oldTime, float current) {
		if(toggle){
			target.toggle();
		}
		else{
			target.setEnabled(isEnabled);
		}
		endAction(oldTime);
	}
}
