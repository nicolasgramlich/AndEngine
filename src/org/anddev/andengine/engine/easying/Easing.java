package org.anddev.andengine.engine.easying;

public enum Easing {
	
	////////////
	// LINEAR //
	////////////
	LINEAR  		{ public float calc(float t, float b, float c, float d) { 
		return c*t/d + b;
	} },
	
	//////////
	// BACK //
	//////////
	BACK_IN  		{ public float calc(float t, float b, float c, float d) { 
		return c*(t/=d)*t*((1.70158f+1)*t - 1.70158f) + b;
	} },
	
	BACK_OUT   	{ public float calc(float t, float b, float c, float d) {
		return c*((t=t/d-1)*t*((1.70158f+1)*t + 1.70158f) + 1) + b;
	} },
	
	BACK_IN_OUT 	{ public float calc(float t, float b, float c, float d) { 
		float s = 1.70158f;
		if ((t/=d*0.5f) < 1)
			return c*0.5f*(t*t*(((s*=(1.525f))+1)*t - s)) + b;
		
		return c/2*((t-=2)*t*(((s*=(1.525f))+1)*t + s) + 2) + b;
	} },
	
	//////////
	// CIRC //
	//////////
	CIRC_IN  		{ public float calc(float t, float b, float c, float d) { 
		return (float) (-c * (Math.sqrt(1 - (t/=d)*t) - 1.0f) + b);
	} },
	
	CIRC_OUT   	{ public float calc(float t, float b, float c, float d) {
		return (float) (c * Math.sqrt(1 - (t=t/d-1)*t) + b);
	} },
	
	CIRC_IN_OUT 	{ public float calc(float t, float b, float c, float d) { 
		if ((t/=d*0.5) < 1) 
			return (float) (-c*0.5 * (Math.sqrt(1 - t*t) - 1) + b);
		
		return (float) (c*0.5 * (Math.sqrt(1 - (t-=2)*t) + 1) + b);
	} },
	
	/////////////
	// ELASTIC //
	/////////////
	ELASTIC_IN  	{ public float calc(float t, float b, float c, float d) { 
		float s;
		float p = 0.0f;
		float a = 0.0f;
		if (t==0) return b;  if ((t/=d)==1) return b+c;  if (p==0) p=d*0.3f;
		if (a==0 || (c > 0 && a < c) || (c < 0 && a < -c)) { a=c; s = p/4; }
		else s = (float) (p/_2PI * Math.asin (c/a));
		return (float) (-(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*_2PI/p )) + b);
	} },
	
	ELASTIC_OUT   	{ public float calc(float t, float b, float c, float d) {
		float s;
		float p = 0.0f;
		float a = 0.0f;
		if (t==0) return b;  if ((t/=d)==1) return b+c;  if (p==0) p=d*0.3f;
		if (a==0 || (c > 0 && a < c) || (c < 0 && a < -c)) { a=c; s = p/4; }
		else s = (float) (p/_2PI * Math.asin (c/a));
		return (float) (a*Math.pow(2,-10*t) * Math.sin( (t*d-s)*_2PI/p ) + c + b);
	} },
	
	ELASTIC_IN_OUT 	{ public float calc(float t, float b, float c, float d) { 
		float s;
		float p = 0.0f;
		float a = 0.0f;
		if (t==0) return b;  if ((t/=d*0.5)==2) return b+c;  if (p==0) p=d*(0.3f*1.5f);
		if (a==0 || (c > 0 && a < c) || (c < 0 && a < -c)) { a=c; s = p/4; }
		else s = (float) (p/_2PI * Math.asin (c/a));
		if (t < 1) return (float) (-0.5*(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*_2PI/p )) + b);
		return (float) (a*Math.pow(2,-10*(t-=1)) * Math.sin( (t*d-s)*_2PI/p )*.5 + c + b);
	} },
	
	
	//////////
	// EXPO //
	//////////
	EXPO_IN  	{ public float calc(float t, float b, float c, float d) { 
		return (float) ((t==0) ? b : c * Math.pow(2, 10 * (t/d - 1)) + b - c * 0.001f);
	} },
	
	EXPO_OUT   	{ public float calc(float t, float b, float c, float d) {
		return (float) ((t==d) ? b+c : c * (-Math.pow(2, -10 * t/d) + 1) + b);
	} },
	
	EXPO_IN_OUT { public float calc(float t, float b, float c, float d) { 
		if (t==0) return b;
		if (t==d) return b+c;
		if ((t/=d*0.5f) < 1) return (float) (c*0.5f * Math.pow(2, 10 * (t - 1)) + b);
		return (float) (c*0.5f * (-Math.pow(2, -10 * --t) + 2) + b);
	} },
	
	//////////
	// QUAD //
	//////////
	QUAD_IN  	{ public float calc(float t, float b, float c, float d) { 
		return c*(t/=d)*t + b;
	} },
	
	QUAD_OUT   	{ public float calc(float t, float b, float c, float d) {
		return -c *(t/=d)*(t-2) + b;
	} },
	
	QUAD_IN_OUT { public float calc(float t, float b, float c, float d) { 
		if ((t/=d*0.5f) < 1) return c*0.5f*t*t + b;
		return -c*0.5f * ((--t)*(t-2) - 1) + b;
	} },
	
	///////////
	// QUART //
	///////////
	QUART_IN  	{ public float calc(float t, float b, float c, float d) { 
		return c*(t/=d)*t*t*t + b;
	} },
	
	QUART_OUT   { public float calc(float t, float b, float c, float d) {
		return -c * ((t=t/d-1)*t*t*t - 1) + b;
	} },
	
	QUART_IN_OUT { public float calc(float t, float b, float c, float d) { 
		if ((t/=d*0.5f) < 1) return c*0.5f*t*t*t*t + b;
		return -c*0.5f * ((t-=2)*t*t*t - 2) + b;
	} },
	
	///////////
	// QUINT //
	///////////
	QUINT_IN  	{ public float calc(float t, float b, float c, float d) { 
		return c*(t/=d)*t*t*t*t + b;
	} },
	
	QUINT_OUT   { public float calc(float t, float b, float c, float d) {
		return c*((t=t/d-1)*t*t*t*t + 1) + b;
	} },
	
	QUINT_IN_OUT { public float calc(float t, float b, float c, float d) { 
		if ((t/=d*0.5f) < 1) return c*0.5f*t*t*t*t*t + b;
		return c*0.5f*((t-=2)*t*t*t*t + 2) + b;
	} },
	
	//////////
	// SINE //
	//////////
	SINE_IN  	{ public float calc(float t, float b, float c, float d) { 
		return (float) (-c * Math.cos(t/d * _HALF_PI) + c + b);
	} },
	
	SINE_OUT   { public float calc(float t, float b, float c, float d) {
		return (float) (c * Math.sin(t/d * _HALF_PI) + b);
	} },
	
	SINE_IN_OUT { public float calc(float t, float b, float c, float d) { 
		return (float) (-c*0.5f * (Math.cos(Math.PI*t/d) - 1) + b);
	} },
	
	////////////
	// STRONG //
	////////////
	STRONG_IN  	{ public float calc(float t, float b, float c, float d) { 
		return c*(t/=d)*t*t*t*t + b;
	} },
	
	STRONG_OUT   { public float calc(float t, float b, float c, float d) {
		return c*((t=t/d-1)*t*t*t*t + 1) + b;
	} },
	
	STRONG_IN_OUT { public float calc(float t, float b, float c, float d) { 
		if ((t/=d*0.5f) < 1) return c*0.5f*t*t*t*t*t + b;
		return c*0.5f*((t-=2)*t*t*t*t + 2) + b;
	} },
	
	///////////
	// CUBIC //
	///////////
	CUBIC_IN  		{ public float calc(float t, float b, float c, float d) { 
		return c*(t/=d)*t*t + b;
	} },
	
	CUBIC_OUT   	{ public float calc(float t, float b, float c, float d) {
		return c*((t=t/d-1)*t*t + 1) + b;
	} },
	
	CUBIC_IN_OUT 	{ public float calc(float t, float b, float c, float d) { 
		if ((t/=d*0.5f) < 1) return c*0.5f*t*t*t + b;
		return c*0.5f*((t-=2)*t*t + 2) + b;
	} },
	
	////////////
	// BOUNCE //
	////////////
	BOUNCE_IN  		{ public float calc(float t, float b, float c, float d) { 
		return c - BOUNCE_OUT.calc(d-t, 0, c, d) + b;
	} },
	
	BOUNCE_OUT   	{ public float calc(float t, float b, float c, float d) {
		if ((t/=d) < (1/2.75)) {
			return c*(7.5625f*t*t) + b;
		} else if (t < (2/2.75)) {
			return c*(7.5625f*(t-=(1.5f/2.75f))*t + 0.75f) + b;
		} else if (t < (2.5/2.75)) {
			return c*(7.5625f*(t-=(2.25f/2.75f))*t + 0.9375f) + b;
		} else {
			return c*(7.5625f*(t-=(2.625f/2.75f))*t + 0.984375f) + b;
		}
	} },
	
	BOUNCE_IN_OUT 	{ public float calc(float t, float b, float c, float d) { 
		if (t < d*0.5) 
			return BOUNCE_IN.calc(t*2, 0, c, d) * 0.5f + b;
		else 
			return BOUNCE_OUT.calc(t*2-d, 0, c, d) * 0.5f + c*0.5f + b;
	} };
	
	private static float _2PI 		= (float) (Math.PI * 2);
	private static float _HALF_PI 	= (float) (Math.PI * 0.5);

	public abstract float calc(float t, float b, float c, float d);
}

