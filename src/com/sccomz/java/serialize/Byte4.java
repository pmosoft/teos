package com.sccomz.java.serialize;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Byte4 implements Serializable {
	public byte[] value = new byte[4];
	
	public Byte4(byte[] value) {
		this.value = value;
	}
}	
