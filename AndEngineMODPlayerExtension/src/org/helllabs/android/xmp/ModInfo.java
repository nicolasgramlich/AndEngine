package org.helllabs.android.xmp;

class ModInfo {
	String name;
	String filename;
	String type;
	int chn;
	int pat;
	int ins;
	int trk;
	int smp;
	int len;
	int bpm;
	int tpo;
	int time;
	
	public ModInfo(String _name, String _type, String _filename,
			int _chn, int _pat, int _ins, int _trk, int _smp, int _len,
			int _bpm, int _tpo, int _time)
	{
		name = _name;
		type = _type;
		filename = _filename;
		chn = _chn;
		pat = _pat;
		ins = _ins;
		trk = _trk;
		smp = _smp;
		len = _len;
		bpm = _bpm;
		tpo = _tpo;
		time = _time;
	}
}
