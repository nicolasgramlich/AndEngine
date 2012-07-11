package org.anddev.andengine.util.path;

@SuppressWarnings("serial")
public class NegativeStepCostException extends Exception {
	public NegativeStepCostException()
	{
		super("Can not handle negative step cost!");
	}
}